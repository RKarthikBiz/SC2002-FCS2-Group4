package com.combatarena.control;

import com.combatarena.boundary.GameCLI;
import com.combatarena.domain.actions.Action;
import com.combatarena.domain.combatants.Combatant;
import com.combatarena.domain.combatants.Enemy;
import com.combatarena.domain.combatants.Player;
import com.combatarena.domain.statuseffects.StatusEffect;
import com.combatarena.domain.statuseffects.SmokeBombEffect;
import com.combatarena.domain.items.Item;
import com.combatarena.domain.level.Level;
import com.combatarena.util.BattleLogger;
import com.combatarena.util.GameConstants;
import com.combatarena.util.LootTable;
import java.util.ArrayList;
import java.util.List;

/**
 * Core controller that drives the battle loop.
 *
 * Responsibilities (SRP):
 *   - Manage the turn cycle (order, tick effects, process actions).
 *   - Check win/loss conditions.
 *   - Trigger backup enemy spawning.
 *   - Track combo counter and apply combo ATK bonus.
 *   - Check loot drops after enemy kills.
 *   - Delegate display to GameCLI and turn ordering to TurnOrderStrategy (DIP).
 *
 * BattleEngine depends on the TurnOrderStrategy interface, not on
 * SpeedBasedTurnOrder directly (DIP).
 */
public class BattleEngine {

    // -------------------------------------------------------------------------
    // Fields
    // -------------------------------------------------------------------------

    private final TurnOrderStrategy turnStrategy;
    private Level currentLevel;
    private final List<Enemy> activeEnemies;
    private final Player player;
    private final GameCLI gameCLI;

    /** Tracks whether the backup wave has already been spawned this level. */
    private boolean backupSpawned;

    /** Counts consecutive hits the player has landed without taking damage. */
    private int comboCounter;

    /** Tracks attack bonus currently applied by combo (so it can be reversed cleanly). */
    private int comboBonusApplied;

    /** Stores the base attack value for combo bonus reversal. */
    private int playerBaseAttack;

    /** Logs every action taken during the battle for end-of-battle summary. */
    private final BattleLogger battleLogger;

    // -------------------------------------------------------------------------
    // Constructor
    // -------------------------------------------------------------------------

    public BattleEngine(TurnOrderStrategy turnStrategy, Level level,
                        Player player, GameCLI gameCLI) {
        this.turnStrategy  = turnStrategy;
        this.currentLevel  = level;
        this.player        = player;
        this.gameCLI       = gameCLI;
        this.activeEnemies = new ArrayList<>(level.getInitialEnemies());
        this.backupSpawned = false;
        this.comboCounter  = 0;
        this.comboBonusApplied = 0;
        this.playerBaseAttack = player.getAttack();
        this.battleLogger  = new BattleLogger();
    }

    // -------------------------------------------------------------------------
    // Main battle loop
    // -------------------------------------------------------------------------

    /**
     * Runs the full battle until the player wins, loses, or flees.
     * Called by GameCLI after setup.
     */
    public void runBattle() {
        gameCLI.displayBattleState();

        while (!checkGameOver()) {
            battleLogger.incrementTurn();

            List<Combatant> participants = buildParticipantList();
            List<Combatant> turnOrder   = turnStrategy.determineTurnOrder(participants);

            for (Combatant combatant : turnOrder) {
                if (!combatant.isAlive()) {
                    continue;
                }

                processTurn(combatant);

                if (checkGameOver()) {
                    break;
                }
            }

            // Spawn backups at the end of the round (after all combatants have acted)
            spawnBackups();

            // Tick status effects once per round
            tickAllStatusEffects(participants);

            gameCLI.displayBattleState();
        }

        // Print full battle log before outcome
        battleLogger.printLog();

        if (player.isAlive()) {
            gameCLI.displayVictory();
        } else {
            gameCLI.displayDefeat();
        }
    }

    // -------------------------------------------------------------------------
    // Turn processing
    // -------------------------------------------------------------------------

    /**
     * Processes a single combatant's turn.
     */
    public void processTurn(Combatant combatant) {
        // Tick special skill cooldown at the start of each combatant's turn
        com.combatarena.domain.actions.SpecialSkill.tick(combatant);

        // Stunned combatants skip their turn
        if (isStunned(combatant)) {
            String entry = "Turn " + battleLogger.getTurnNumber()
                    + ": " + combatant.getName() + " is stunned and skips their turn!";
            System.out.println(entry);
            battleLogger.record(entry);
            return;
        }

        // ── Player turn ──────────────────────────────────────────────────────
        if (combatant instanceof Player) {
            Action action     = gameCLI.getPlayerAction();
            Combatant target  = resolveTarget(combatant);
            if (target == null || action == null) return;

            // Snapshot target HP before action to detect damage dealt and kills
            int targetHpBefore = target.getHp();

            action.execute(combatant, target);

            // Log the action
            String actionName = action.getClass().getSimpleName();
            battleLogger.record("Turn " + battleLogger.getTurnNumber()
                    + ": " + combatant.getName() + " used " + actionName
                    + " on " + target.getName());

            // Combo tracking — did the player deal damage this turn?
            boolean dealtDamage = target.getHp() < targetHpBefore;
            if (dealtDamage) {
                incrementCombo();
            }

            // Loot drop check — did the player kill the target?
            if (!target.isAlive()) {
                checkLootDrop((Enemy) target);
            }

        // ── Enemy turn ───────────────────────────────────────────────────────
        } else {
            Enemy enemy       = (Enemy) combatant;
            Action action     = enemy.decideAction();
            Combatant target  = resolveTarget(combatant);
            if (target == null) return;

            int playerHpBefore = player.getHp();

            action.execute(combatant, target);

            battleLogger.record("Turn " + battleLogger.getTurnNumber()
                    + ": " + combatant.getName() + " used " + action.getClass().getSimpleName()
                    + " on " + target.getName());

            // If player took damage, break the combo
            if (player.getHp() < playerHpBefore) {
                resetCombo();
            }
        }
    }

    // -------------------------------------------------------------------------
    // Combo counter
    // -------------------------------------------------------------------------

    /**
     * Increments the combo counter. At every COMBO_BONUS_THRESHOLD hits,
     * grants the player a temporary ATK bonus that is tracked separately
     * so it can be reversed cleanly without affecting other attack bonuses.
     */
    public void incrementCombo() {
        comboCounter++;
        System.out.println("  [COMBO x" + comboCounter + "]");

        if (comboCounter % GameConstants.COMBO_BONUS_THRESHOLD == 0) {
            int bonus = GameConstants.COMBO_ATK_BONUS;
            player.setAttack(player.getAttack() + bonus);
            comboBonusApplied += bonus;
            String msg = "  ★ COMBO BONUS! +" + bonus + " ATK this turn for "
                    + player.getName() + "!";
            System.out.println(msg);
            battleLogger.record(msg);
        }
    }

    /**
     * Resets the combo counter when the player takes damage.
     * Only reverses the combo-specific attack bonus, leaving other
     * permanent bonuses (e.g. Wizard's Arcane Blast kills) intact.
     */
    public void resetCombo() {
        // Reverse only the combo-applied bonus, not other attack changes
        if (comboBonusApplied > 0) {
            player.setAttack(Math.max(playerBaseAttack, player.getAttack() - comboBonusApplied));
            comboBonusApplied = 0;
        }

        if (comboCounter > 0) {
            System.out.println("  [Combo broken! Was x" + comboCounter + "]");
            comboCounter = 0;
        }
    }

    // -------------------------------------------------------------------------
    // Loot drop
    // -------------------------------------------------------------------------

    /**
     * Rolls for a loot drop from a defeated enemy.
     * If successful, the item is added directly to the player's inventory.
     */
    public void checkLootDrop(Enemy enemy) {
        if (LootTable.shouldDrop()) {
            Item dropped = LootTable.rollDrop();
            if (dropped != null) {
                player.addItem(dropped);
                String msg = "  ★ LOOT! " + enemy.getName() + " dropped a "
                        + dropped.getClass().getSimpleName()
                        + "! Added to your inventory.";
                System.out.println(msg);
                battleLogger.record(msg);
            }
        }
    }

    // -------------------------------------------------------------------------
    // Game-state helpers
    // -------------------------------------------------------------------------

    /**
     * Returns true if the battle is over (player dead or all enemies dead
     * with no backup remaining).
     */
    public boolean checkGameOver() {
        if (!player.isAlive()) {
            return true;
        }
        if (allEnemiesDead()) {
            if (!backupSpawned && currentLevel.hasBackup()) {
                return false;
            }
            return true;
        }
        return false;
    }

    private boolean allEnemiesDead() {
        for (Enemy e : activeEnemies) {
            if (e.isAlive()) return false;
        }
        return true;
    }

    /**
     * Spawns the backup wave when the initial wave is fully wiped.
     */
    public void spawnBackups() {
        if (!backupSpawned && allEnemiesDead() && currentLevel.hasBackup()) {
            List<Enemy> backups = currentLevel.spawnBackupEnemies();
            activeEnemies.addAll(backups);
            backupSpawned = true;
            currentLevel.markInitialCleared();
            System.out.println("=== Backup enemies have arrived! ===");
            for (Enemy e : backups) {
                String msg = "  + " + e.getName() + " joins the fight!";
                System.out.println(msg);
                battleLogger.record(msg);
            }
        }
    }

    // -------------------------------------------------------------------------
    // Utility helpers
    // -------------------------------------------------------------------------

    private List<Combatant> buildParticipantList() {
        List<Combatant> list = new ArrayList<>();
        list.add(player);
        list.addAll(activeEnemies);
        return list;
    }

    private void tickAllStatusEffects(List<Combatant> participants) {
        for (Combatant c : participants) {
            if (c.isAlive()) {
                c.applyStatusEffects();
            }
        }
    }

    private Combatant resolveTarget(Combatant combatant) {
        if (combatant instanceof Player) {
            for (Enemy e : activeEnemies) {
                if (e.isAlive()) return e;
            }
            return null;
        } else {
            return player.isAlive() ? player : null;
        }
    }

    private boolean isStunned(Combatant combatant) {
        for (StatusEffect effect : combatant.getStatusEffects()) {
            if (effect instanceof com.combatarena.domain.statuseffects.StunEffect) {
                return true;
            }
        }
        return false;
    }

    // -------------------------------------------------------------------------
    // Getters
    // -------------------------------------------------------------------------

    public Player getPlayer()             { return player; }
    public List<Enemy> getActiveEnemies() { return new ArrayList<>(activeEnemies); }
    public Level getCurrentLevel()        { return currentLevel; }
    public int getComboCounter()          { return comboCounter; }
    public BattleLogger getBattleLogger() { return battleLogger; }
}
