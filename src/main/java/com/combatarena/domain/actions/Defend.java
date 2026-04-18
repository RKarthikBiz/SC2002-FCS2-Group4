package com.combatarena.domain.actions;

import com.combatarena.domain.combatants.Combatant;
import com.combatarena.domain.statuseffects.DefenseBoostEffect;
import com.combatarena.util.GameConstants;

/**
 * Self-buff action that raises defense for a short duration.
 */
public class Defend implements Action {

    @Override
    public void execute(Combatant attacker, Combatant target) {
        System.out.println("  [DEFEND] " + attacker.getName() + " braces for impact"
            + " ( +" + GameConstants.DEFEND_BONUS_DEF + " DEF, "
            + GameConstants.DEFEND_DURATION + " turns )");

        DefenseBoostEffect defenseBoost = new DefenseBoostEffect(GameConstants.DEFEND_DURATION, GameConstants.DEFEND_BONUS_DEF);
        attacker.applyStatusEffect(defenseBoost);
    }
}
