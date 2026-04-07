package com.combatarena.domain.items;
import com.combatarena.domain.combatants.Combatant;
import com.combatarena.util.GameConstants;

/**
 * Potion - a single-use healing item.
 *
 * Effect : Restores POTION_HEAL_AMOUNT HP to the user.
 * Formula: newHP = min(currentHP + healAmount, maxHP)
 *          HP is capped at the user's maximum HP.
 */
public class Potion implements Item {

    @Override
    public void use(Combatant user) {
        int healAmount = GameConstants.POTION_HEAL_AMOUNT;
        int currentHp = user.getHp();
        int maxHp     = user.getMaxHp();

        int newHp = Math.min(currentHp + healAmount, maxHp);
        int actualHealed = newHp - currentHp;   // may be less than healAmount if near max HP

        user.healDamage(actualHealed);

        System.out.println(user.getName() + " uses a Potion and restores "
                + actualHealed + " HP! (" + newHp + "/" + maxHp + ")");
    }
}
