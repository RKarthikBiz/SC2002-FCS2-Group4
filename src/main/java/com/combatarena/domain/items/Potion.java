package com.combatarena.domain.items;
import com.combatarena.domain.combatants.Combatant;
import com.combatarena.util.GameConstants;

/**
 * Healing item that restores a fixed amount of HP.
 */
public class Potion implements Item {

    @Override
    public void use(Combatant user) {
        int healAmount = GameConstants.POTION_HEAL_AMOUNT;
        int currentHp = user.getHp();
        int maxHp     = user.getMaxHp();

        user.heal(healAmount);

        int newHp = user.getHp();
        int actualHealed = newHp - currentHp;

        System.out.println("  [ITEM FX ] Restored " + actualHealed
            + " HP (" + newHp + "/" + maxHp + ").");
    }
}
