package com.combatarena.util;

import com.combatarena.util.GameConstants;
import com.combatarena.domain.items.Item;
import com.combatarena.domain.items.Potion;
import com.combatarena.domain.items.SmokeBomb;

import java.util.Random;

public class LootTable {

    private LootTable() {}

    private static final Random RANDOM = new Random();

    public static boolean shouldDrop() {
        return RANDOM.nextDouble() < GameConstants.LOOT_DROP_CHANCE;
    }

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
