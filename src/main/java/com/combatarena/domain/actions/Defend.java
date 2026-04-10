package com.combatarena.domain.actions;

import com.combatarena.domain.combatants.Combatant;
import com.combatarena.domain.statuseffects.DefenseBoostEffect;
import com.combatarena.util.GameConstants;

/**
 * Defend - the combatant braces for incoming attacks.
 *
 * Effect  : +DEFENSE_BONUS defense for the current round and the next round (2 turns total).
 * Mechanic: Applies a DefenseBoostEffect to the attacker (self-buff).
 *           The effect lasts DEFEND_DURATION turns and is decremented each time the
 *           combatant takes a turn (via Combatant.applyStatusEffects /
 *           StatusEffect.tick()).
 */
public class Defend implements Action {

    @Override
    public void execute(Combatant attacker, Combatant target) {
        System.out.println(attacker.getName() + " takes a defensive stance! "
                + "(+" + GameConstants.DEFEND_BONUS_DEF + " DEF for " + GameConstants.DEFEND_DURATION + " turns)");

        DefenseBoostEffect defenseBoost = new DefenseBoostEffect(GameConstants.DEFEND_DURATION, GameConstants.DEFEND_BONUS_DEF);
        attacker.applyStatusEffect(defenseBoost);
    }
}
