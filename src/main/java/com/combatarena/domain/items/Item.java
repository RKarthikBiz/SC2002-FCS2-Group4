package com.combatarena.domain.items;

import com.combatarena.domain.combatants.Combatant;

/**
 * Represents a usable item.
 */
public interface Item {
    /**
     * Applies the item's effect to the user.
     */
    void use(Combatant user);
}
