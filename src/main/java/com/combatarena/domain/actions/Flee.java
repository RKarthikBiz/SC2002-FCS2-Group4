package com.combatarena.domain.actions;

import com.combatarena.util.GameConstants;
import com.combatarena.domain.combatants.Combatant;

import java.util.Random;

/**
 * Flee - the player attempts to escape from battle.
 *
 * Mechanic:
 *  - Has a FLEE_CHANCE probability of succeeding (sourced from GameConstants).
 *  - On SUCCESS : the battle ends immediately in favour of the player escaping.
 *                 A fleeFailed flag is set to false; callers (BattleEngine)
 *                 should check hasFled() after execute() and terminate the
 *                 battle loop accordingly.
 *  - On FAILURE : nothing happens; the turn is consumed and the battle continues.
 *
 * UML notes:
 *  - Flee implements Action (realization confirmed in XMI).
 *  - FLEE_CHANCE is typed as double (e.g. 0.5 = 50% success rate).
 *  - execute(attacker: Combatant, target: Combatant): void — target is unused
 *    for Flee but kept to satisfy the Action interface contract.
 */
public class Flee implements Action {

    // -------------------------------------------------------------------------
    // State
    // -------------------------------------------------------------------------

    /** Probability of a successful flee attempt, read from GameConstants. */
    private final double FLEE_CHANCE;

    /**
     * Set to true if the last call to execute() resulted in a successful flee.
     * BattleEngine should call hasFled() after processTurn() to detect this.
     */
    private boolean fled;

    /** Shared Random instance for the flee roll. */
    private final Random random;

    // -------------------------------------------------------------------------
    // Constructor
    // -------------------------------------------------------------------------

    public Flee() {
        this.FLEE_CHANCE = GameConstants.FLEE_CHANCE;
        this.fled        = false;
        this.random      = new Random();
    }

    // -------------------------------------------------------------------------
    // Action implementation
    // -------------------------------------------------------------------------

    /**
     * Attempts to flee from battle.
     *
     * @param attacker the Combatant trying to flee (always the Player)
     * @param target   unused for Flee; present to satisfy the Action interface
     */
    @Override
    public void execute(Combatant attacker, Combatant target) {
        double roll = random.nextDouble(); // [0.0, 1.0)

        if (roll < FLEE_CHANCE) {
            fled = true;
            System.out.println(attacker.getName()
                    + " successfully fled from battle!");
        } else {
            fled = false;
            System.out.println(attacker.getName()
                    + " tried to flee but failed! (rolled "
                    + String.format("%.2f", roll)
                    + ", needed < " + FLEE_CHANCE + ")");
        }
    }

    // -------------------------------------------------------------------------
    // Query
    // -------------------------------------------------------------------------

    /**
     * Returns true if the last execute() call resulted in a successful flee.
     * BattleEngine should call this immediately after execute() to decide
     * whether to terminate the battle loop.
     *
     * @return true if the player has successfully fled
     */
    public boolean hasFled() {
        return fled;
    }
}
