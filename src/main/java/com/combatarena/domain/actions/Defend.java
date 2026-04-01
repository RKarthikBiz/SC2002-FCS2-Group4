package com.combatarena.domain.actions;

import com.combatarena.domain.combatants.Combatant;
import com.combatarena.domain.statuseffects.DefenseBoostEffect;

/**
 * Defend - the combatant braces for incoming attacks.
 *
 * Effect  : +10 defense for the current round and the next round (2 turns total).
 * Mechanic: Applies a DefenseBoostEffect to the attacker (self-buff).
 *           The effect lasts 2 turns and is decremented each time the
 *           combatant takes a turn (via Combatant.applyStatusEffects /
 *           StatusEffect.tick()).
 */
public class Defend implements Action {

    /** Bonus defense granted while defending. */
    private static final int DEFENSE_BONUS = 10;

    /**
     * Duration = 2:
     *   turn 0 (current round) - buff is active
     *   turn 1 (next round)    - buff is still active
     *   tick after turn 1      - buff expires
     */
    private static final int DEFENSE_DURATION = 2;

    @Override
    public void execute(Combatant attacker, Combatant target) {
        System.out.println(attacker.getName() + " takes a defensive stance! "
                + "(+" + DEFENSE_BONUS + " DEF for " + DEFENSE_DURATION + " turns)");

        DefenseBoostEffect defenseBoost = new DefenseBoostEffect(DEFENSE_DURATION, DEFENSE_BONUS);
        attacker.addStatusEffect(defenseBoost);
    }
}
