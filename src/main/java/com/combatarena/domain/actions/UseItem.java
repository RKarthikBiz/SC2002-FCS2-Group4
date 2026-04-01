package com.combatarena.domain.actions;

import com.combatarena.domain.combatants.Combatant;
import com.combatarena.domain.items.Item;

/**
 * UseItem - the combatant uses an Item from their inventory.
 *
 * The Item to use is supplied at construction time (chosen by the user before
 * the action is created). Calling execute() triggers Item.use(user), which
 * applies the item's effect to the user (e.g. Potion heals, SmokeBomb applies
 * a status effect, PowerStone buffs attack).
 *
 * Per the UML, Player carries 0..2 Items.
 */
public class UseItem implements Action {

    private final Item item;

    /**
     * @param item the Item the combatant has chosen to use; must not be null.
     */
    public UseItem(Item item) {
        if (item == null) {
            throw new IllegalArgumentException("Item cannot be null.");
        }
        this.item = item;
    }

    @Override
    public void execute(Combatant attacker, Combatant target) {
        System.out.println(attacker.getName() + " uses "
                + item.getClass().getSimpleName() + "!");
        // Item.use() targets the user (attacker), consistent with UML signature
        // use(user : Combatant) : void
        item.use(attacker);
    }
}
