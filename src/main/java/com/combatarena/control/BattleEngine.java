package com.combatarena.control;

import com.combatarena.boundary.GameCLI;
import com.combatarena.domain.actions.Action;
import com.combatarena.domain.actions.BasicAttack;
import com.combatarena.domain.actions.SpecialSkill;
import com.combatarena.domain.actions.UseItem;
import com.combatarena.domain.combatants.Combatant;
import com.combatarena.domain.combatants.Enemy;
import com.combatarena.domain.combatants.Player;
import com.combatarena.domain.combatants.Warrior;
import com.combatarena.domain.combatants.Wizard;
import com.combatarena.domain.items.Item;
import com.combatarena.domain.level.Level;
import com.combatarena.domain.statuseffects.StatusEffect;
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

    /** Tracks how much damage the player has taken in the current round. */
    private int damageTakenThisRound;

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

            damageTakenThisRound = 0;

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

        // Stunned combatants skip their turn
        if (isStunned(combatant)) {
            String entry = "  [STATUS] " + combatant.getName() + " cannot act this turn (stunned).";
            System.out.println(entry);
            battleLogger.record(entry);
            clearStunEffectsSilently(combatant);
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

                battleLogger.record(formatActionRecapEntry(combatant, action, target));

            // Combo tracking — did the player deal damage this turn?
            boolean dealtDamage = target.getHp() < targetHpBefore;
            if (dealtDamage) {
                incrementCombo();
            }

            // Loot drop check — did the player kill the target?
            if (!target.isAlive()) {
                checkLootDrop((Enemy) target);
            }
            
            // Tick special skill cooldown at the end of each combatant's turn
            com.combatarena.domain.actions.SpecialSkill.tick(combatant);

        // ── Enemy turn ───────────────────────────────────────────────────────
        } else {
            Enemy enemy       = (Enemy) combatant;
            Action action     = enemy.decideAction();
            Combatant target  = resolveTarget(combatant);
            if (target == null) return;

            int playerHpBefore = player.getHp();

            action.execute(combatant, target);

                battleLogger.record(formatActionRecapEntry(combatant, action, target));

            // If player took damage, break the combo
            int damageTakenThisTurn= playerHpBefore - player.getHp();
            
            if (damageTakenThisTurn > 0){
                damageTakenThisRound += damageTakenThisTurn;

                int threshold = getComboDamageThreshold();

                if (damageTakenThisRound >= threshold){
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
        String chainMsg = "[COMBO] Chain x" + comboCounter;
        System.out.println("  " + chainMsg);
        battleLogger.record(chainMsg);

        if (comboCounter % GameConstants.COMBO_BONUS_THRESHOLD == 0) {
            int bonus = GameConstants.COMBO_ATK_BONUS;
            player.setAttack(player.getAttack() + bonus);
            comboBonusApplied += bonus;
            String msg = "  [COMBO ] Bonus: +" + bonus + " ATK this turn for "
                    + player.getName() + ".";
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
            String breakMsg = "[COMBO] Broken (was x" + comboCounter + ")";
            System.out.println("  " + breakMsg);
            battleLogger.record(breakMsg);
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
                String msg = "  [LOOT  ] " + enemy.getName() + " dropped "
                        + dropped.getClass().getSimpleName()
                        + ". Added to inventory.";
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

    /**
     * Removes active stun effects without emitting extra status messages.
     * This keeps stun behavior to a single skipped action and avoids
     * same-turn "recovered" lines cluttering the combat feed.
     */
    private void clearStunEffectsSilently(Combatant combatant) {
        List<StatusEffect> filtered = new ArrayList<>();
        for (StatusEffect effect : combatant.getStatusEffects()) {
            if (!(effect instanceof com.combatarena.domain.statuseffects.StunEffect)) {
                filtered.add(effect);
            }
        }
        combatant.setStatusEffects(filtered);
    }

    private String formatActionRecapEntry(Combatant actor, Action action, Combatant target) {
        String actorName = actor == null ? "Unknown" : actor.getName();
        String targetName = target == null ? "Unknown target" : target.getName();

        if (action == null) {
            return actorName + " acted.";
        }

        String actionLabel;
        if (action instanceof SpecialSkill) {
            if (actor instanceof Warrior) {
                actionLabel = "Shield Bash";
            } else if (actor instanceof Wizard) {
                actionLabel = "Arcane Blast";
            } else {
                actionLabel = "Special Skill";
            }
        } else if (action instanceof UseItem useItem) {
            actionLabel = "Use Item (" + useItem.getItemName() + ")";
        } else if (action instanceof BasicAttack basicAttack) {
            String critSuffix = basicAttack.wasLastCritical() ? " [CRITICAL]" : "";
            String blockedSuffix = basicAttack.wasLastBlocked() ? " [BLOCKED]" : "";
            return actorName + " used Basic Attack on " + targetName
                    + " (" + basicAttack.getLastActualDamage() + " dmg)"
                    + critSuffix + blockedSuffix;
        } else {
            actionLabel = action.getClass().getSimpleName();
        }
        return actorName + " used " + actionLabel + " on " + targetName;
    }

    /**
     * Determines the combo damage reset threshold based on the current level's difficulty.
     */
    private int getComboDamageThreshold() {
        // NOTE: If your Level class uses a different method name to get the difficulty level 
        // (like getDifficulty() or getId()), change "getLevelNo()" to match it!
        int level = currentLevel.getLevelNo(); 
        switch(level) {
            case 1: return GameConstants.COMBO_THRESHOLD_EASY;
            case 2: return GameConstants.COMBO_THRESHOLD_MEDIUM;
            case 3: return GameConstants.COMBO_THRESHOLD_HARD;
            default: return GameConstants.COMBO_THRESHOLD_EASY;
        }
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
