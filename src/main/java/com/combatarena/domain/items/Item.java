import com.combatarena.domain.combatants.Combatant;

/**
 * Item interface - represents a single-use item a Player can carry into battle.
 *
 * Design notes (per spec):
 *  - Two items are chosen at game start; duplicates are allowed.
 *  - Items are consumed when used via the UseItem action.
 *  - Multiple item types exist by implementing this interface.
 */
public interface Item {
    /**
     * Applies this item's effect to the user.
     *
     * @param user the Combatant using the item (always the Player)
     */
    void use(Combatant user);
}
