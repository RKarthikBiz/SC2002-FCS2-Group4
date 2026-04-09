package com.combatarena.domain.items;

import com.combatarena.domain.combatants.Combatant;
import com.combatarena.domain.statuseffects.SmokeBombEffect;
import com.combatarena.util.GameConstants;

/**
 * SmokeBomb - a single-use defensive item.
 *
 * Effect : Enemy attacks deal 0 damage to the user on the current turn
 *          and the next turn (2 turns total).
 *
 * Mechanic: Applies a SmokeBombEffect (StatusEffect) to the user.
 *           The BattleEngine / Combatant.takeDamage() must check for an
 *           active SmokeBombEffect and suppress incoming enemy damage to 0.
 */
public class SmokeBomb implements Item {

    @Override
    public void use(Combatant user) {
        System.out.println("  [ITEM FX ] Smoke Bomb active: enemy attacks deal 0 damage"
            + " this turn and next turn.");

        SmokeBombEffect effect = new SmokeBombEffect(GameConstants.SMOKE_BOMB_DURATION);
        user.addStatusEffect(effect);
    }
}
