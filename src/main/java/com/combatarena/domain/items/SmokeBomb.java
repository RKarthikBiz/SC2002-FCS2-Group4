package com.combatarena.domain.items;

import com.combatarena.domain.combatants.Combatant;
import com.combatarena.domain.statuseffects.SmokeBombEffect;
import com.combatarena.util.GameConstants;

/**
 * Defensive item that blocks incoming damage for a short time.
 */
public class SmokeBomb implements Item {

    @Override
    public void use(Combatant user) {
        System.out.println("  [ITEM FX ] Smoke Bomb active: enemy attacks deal 0 damage"
            + " this turn and next turn.");

        SmokeBombEffect effect = new SmokeBombEffect(GameConstants.SMOKE_BOMB_DURATION);
        user.applyStatusEffect(effect);
    }
}
