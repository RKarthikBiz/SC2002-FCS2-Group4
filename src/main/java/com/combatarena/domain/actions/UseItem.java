package com.combatarena.domain.actions;

import com.combatarena.domain.combatants.Combatant;
import com.combatarena.domain.items.Item;

/**
 * Action that applies one chosen item.
 */
public class UseItem implements Action {

    private final Item item;

    /**
     * Stores the selected item for this action.
     */
    public UseItem(Item item) {
        if (item == null) throw new IllegalArgumentException("Item cannot be null.");
        this.item = item;
    }

    @Override
    public void execute(Combatant attacker, Combatant target) {
        System.out.println("  [ITEM USE] " + attacker.getName() + " used "
            + item.getClass().getSimpleName() + ".");

        item.use(attacker);

        // Items are single-use.
        if (attacker instanceof com.combatarena.domain.combatants.Player) {
            com.combatarena.domain.combatants.Player player =
                    (com.combatarena.domain.combatants.Player) attacker;
            player.removeItem(item);
        }
    }

    public String getItemName() {
        return item.getClass().getSimpleName();
    }
}
