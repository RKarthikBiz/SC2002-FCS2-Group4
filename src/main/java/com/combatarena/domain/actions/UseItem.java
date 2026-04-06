package com.combatarena.domain.actions;

import com.combatarena.domain.combatants.Combatant;
import com.combatarena.domain.combatants.Player;
import com.combatarena.domain.items.Item;

/**
 * UseItem - the combatant uses a chosen Item from the Player's inventory.
 *
 * Per spec:
 *  - Items are single-use; they are removed from inventory after use.
 *  - The item to use is selected by the player before this Action is created.
 *  - item.use(user) is called on the attacker (self-targeted by the Item interface),
 *    consistent with the UML signature: use(user : Combatant) : void
 *  - Per the UML, Player carries 0..2 Items.
 */
public class UseItem implements Action {

    private final Item item;
    private final Player player; // held to remove the item from inventory after use

    /**
     * @param item   the Item the player has chosen to use (must not be null)
     * @param player the Player whose inventory will be updated (must not be null)
     */
    public UseItem(Item item, Player player) {
        if (item == null)   throw new IllegalArgumentException("Item cannot be null.");
        if (player == null) throw new IllegalArgumentException("Player cannot be null.");
        this.item   = item;
        this.player = player;
    }

    @Override
    public void execute(Combatant attacker, Combatant target) {
        System.out.println(attacker.getName() + " uses " + item.getClass().getSimpleName() + "!");

        // Apply the item's effect to the user (attacker)
        item.use(attacker);

        // Remove the item from inventory — items are single-use
        player.getInventory().remove(item);
    }
}
