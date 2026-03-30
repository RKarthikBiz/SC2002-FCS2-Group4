package main.java.com.combatarena.control;

import main.java.com.combatarena.boundary.GameCLI;
import main.java.com.combatarena.domain.actions.Action;
import main.java.com.combatarena.domain.combatants.Combatant;
import main.java.com.combatarena.domain.combatants.Enemy;
import main.java.com.combatarena.domain.combatants.Player;
import main.java.com.combatarena.domain.effects.StatusEffect;
import main.java.com.combatarena.domain.level.Level;
import java.util.ArrayList;
import java.util.List;

/**
 * Core controller that drives the battle loop.
 *
 * Responsibilities (SRP):
 *   - Manage the turn cycle (order, tick effects, process actions).
 *   - Check win/loss conditions.
 *   - Trigger backup enemy spawning.
 *   - Delegate display to GameCLI and turn ordering to TurnOrderStrategy (DIP).
 *
 * BattleEngine depends on the TurnOrderStrategy interface, not on
 * SpeedBasedTurnOrder directly
 */
public class BattleEngine {

    // -------------------------------------------------------------------------
    // Fields (match UML)
    // -------------------------------------------------------------------------

    private final TurnOrderStrategy turnStrategy;
    private Level currentLevel;
    private final List<Enemy> activeEnemies;
    private final Player player;

    /** Reference to the boundary layer for display calls. */
    private final GameCLI gameCLI;

    /** Tracks whether the backup wave has already been spawned this level. */
    private boolean backupSpawned;

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
    }

    // -------------------------------------------------------------------------
    // Main battle loop
    // -------------------------------------------------------------------------

    /**
     * Runs the full battle until the player wins or loses.
     * Called by GameCLI after setup.
     */
    public void runBattle() {
        gameCLI.displayBattleState();

        while (!checkGameOver()) {
            // Build the participant list for this round
            List<Combatant> participants = buildParticipantList();

            // Determine turn order via the injected strategy
            List<Combatant> turnOrder = turnStrategy.determineTurnOrder(participants);

            // Process each combatant's turn
            for (Combatant combatant : turnOrder) {
                if (!combatant.isAlive()) {
                    continue; // May have died mid-round
                }
                processTurn(combatant);

                // After every action, check game-over / backup conditions
                if (checkGameOver()) {
                    break;
                }
                spawnBackups();
            }

            // Tick status effects at end of round for all participants
            tickAllStatusEffects(participants);

            gameCLI.displayBattleState();
        }

        // Display final outcome
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
     * Skips the turn if the combatant is stunned or otherwise unable to act.
     */
    public void processTurn(Combatant combatant) {
        // Apply start-of-turn status effect hooks (stun check happens here)
        for (StatusEffect effect : combatant.getStatusEffects()) {
            effect.tick();
        }
        combatant.applyStatusEffects(); // removes expired effects

        // Stunned combatants cannot act — check after tick so stun counts down
        if (isStunned(combatant)) {
            System.out.println(combatant.getName() + " is stunned and skips their turn!");
            return;
        }

        // Decide action
        Action action;
        if (combatant instanceof Player) {
            // Human player: delegate action selection to GameCLI
            action = gameCLI.getPlayerAction();
        } else {
            // AI enemy: always returns BasicAttack
            action = ((Enemy) combatant).decideAction();
        }

        // Determine target
        Combatant target = resolveTarget(combatant);
        if (target == null) {
            return; // No valid targets (should not happen during a normal round)
        }

        // Execute the chosen action
        action.execute(combatant, target);
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
                return false; // Backup will spawn — not over yet
            }
            return true;
        }
        return false;
    }

    /**
     * Checks whether all active enemies are dead.
     */
    private boolean allEnemiesDead() {
        for (Enemy e : activeEnemies) {
            if (e.isAlive()) {
                return false;
            }
        }
        return true;
    }

    /**
     * Spawns the backup wave if the initial wave is wiped and backup
     * has not yet been spawned.
     */
    public void spawnBackups() {
        if (!backupSpawned && allEnemiesDead() && currentLevel.hasBackup()) {
            List<Enemy> backups = currentLevel.spawnBackupEnemies();
            activeEnemies.addAll(backups);
            backupSpawned = true;
            currentLevel.markInitialCleared();
            System.out.println("=== Backup enemies have arrived! ===");
            for (Enemy e : backups) {
                System.out.println("  + " + e.getName() + " joins the fight!");
            }
        }
    }

    // -------------------------------------------------------------------------
    // Utility helpers
    // -------------------------------------------------------------------------

    /**
     * Builds the full participant list: player + all living active enemies.
     */
    private List<Combatant> buildParticipantList() {
        List<Combatant> list = new ArrayList<>();
        list.add(player);
        list.addAll(activeEnemies);
        return list;
    }

    /**
     * Ticks status effects for all combatants at end of round and removes
     * expired effects.
     */
    private void tickAllStatusEffects(List<Combatant> participants) {
        for (Combatant c : participants) {
            if (c.isAlive()) {
                c.applyStatusEffects();
            }
        }
    }

    /**
     * Determines the appropriate target for a combatant's action.
     * Players target the first living enemy; enemies target the player.
     */
    private Combatant resolveTarget(Combatant combatant) {
        if (combatant instanceof Player) {
            // Target the first alive enemy
            for (Enemy e : activeEnemies) {
                if (e.isAlive()) {
                    return e;
                }
            }
            return null;
        } else {
            // Enemy targets the player
            return player.isAlive() ? player : null;
        }
    }

    /**
     * Checks if the given combatant has an active StunEffect.
     */
    private boolean isStunned(Combatant combatant) {
        for (StatusEffect effect : combatant.getStatusEffects()) {
            // Using class name check to avoid cross-package import of StunEffect here;
            // alternatively, StatusEffect could expose an isStun() method.
            if (effect.getClass().getSimpleName().equals("StunEffect")) {
                return true;
            }
        }
        return false;
    }

    // -------------------------------------------------------------------------
    // Getters (used by GameCLI for display)
    // -------------------------------------------------------------------------

    public Player getPlayer() {
        return player;
    }

    public List<Enemy> getActiveEnemies() {
        return new ArrayList<>(activeEnemies);
    }

    public Level getCurrentLevel() {
        return currentLevel;
    }
}