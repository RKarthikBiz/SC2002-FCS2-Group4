package com.combatarena.util;

import com.combatarena.util.GameConstants;
import com.combatarena.domain.items.Item;
import com.combatarena.domain.items.Potion;
import com.combatarena.domain.items.SmokeBomb;

import java.util.Random;

/**
 * LootTable - determines whether an enemy drops an item on defeat, and which item.
 *
 * XMI-derived members (both static, as marked isStatic="true" in XMI):
 *   + rollDrop()  : Item    — randomly selects and returns a droppable item
 *   + shouldDrop() : boolean — rolls against LOOT_DROP_CHANCE to decide if a drop occurs
 *
 * Usage pattern (BattleEngine, after an enemy is killed):
 * <pre>
 *   if (LootTable.shouldDrop()) {
 *       Item dropped = LootTable.rollDrop();
 *       System.out.println("Enemy dropped: " + dropped.getClass().getSimpleName());
 *       player.getInventory().add(dropped);   // only if player has room (max 2 items)
 *   }
 * </pre>
 */
public class LootTable {

    // -------------------------------------------------------------------------
    // Prevent instantiation — all methods are static
    // -------------------------------------------------------------------------

    private LootTable() {}

    // -------------------------------------------------------------------------
    // Shared RNG
    // -------------------------------------------------------------------------

    private static final Random RANDOM = new Random();

    // -------------------------------------------------------------------------
    // Static methods (from XMI)
    // -------------------------------------------------------------------------

    /**
     * Determines whether a loot drop should occur based on LOOT_DROP_CHANCE.
     *
     * @return true if a drop occurs (roll < LOOT_DROP_CHANCE)
     */
    public static boolean shouldDrop() {
        return RANDOM.nextDouble() < GameConstants.LOOT_DROP_CHANCE;
    }

    /**
     * Randomly selects one item from the available loot pool and returns it.
     * Each item type has an equal probability of being selected.
     *
     * Loot pool: Potion, SmokeBomb
     * (PowerStone is excluded from drops — it requires target/allTargets
     *  that can only be determined at use-time by the player.)
     *
     * @return a new instance of a randomly chosen Item
     */
    public static Item rollDrop() {
        int roll = RANDOM.nextInt(2); // 0 or 1

        switch (roll) {
            case 0:
                return new Potion();
            case 1:
                return new SmokeBomb();
            default:
                // Unreachable, but satisfies compiler
                return new Potion();
        }
    }
}
