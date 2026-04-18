package com.combatarena.domain.combatants;

import com.combatarena.domain.actions.Action;
import com.combatarena.domain.items.Item;
import com.combatarena.domain.items.Potion;
import com.combatarena.domain.items.SmokeBomb;
import com.combatarena.util.GameConstants;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Base class for enemy units that choose actions through AI.
 */
public abstract class Enemy extends Combatant {
    private static final Random RANDOM = new Random();

    private List<String> namePool;

    /**
        * Creates an enemy with base stats.
     */
    public Enemy(String name, int hp, int attack, int defense, int speed) {
        super(name, hp, attack, defense, speed);
        this.namePool = new ArrayList<>();
    }

    /**
        * Returns the action this enemy wants to perform this turn.
     */
    public abstract Action decideAction();

    /**
     * Small chance to drop an item when the enemy is defeated.
     * Returns null when no item drops.
     */
    public Item drop() {
        if (RANDOM.nextDouble() >= GameConstants.LOOT_DROP_CHANCE) {
            return null;
        }

        return RANDOM.nextBoolean() ? new Potion() : new SmokeBomb();
    }

    /**
        * Default turn behavior.
     */
    @Override
    public void performTurn() {
        Action action = decideAction();
        if (action != null) {
            System.out.println(getName() + " prepares " + action.getClass().getSimpleName() + ".");
        }
    }

    protected void setNamePool(List<String> namePool) {
        this.namePool = new ArrayList<>(namePool);
    }

    protected void assignDynamicName(String baseName) {
        if (namePool == null || namePool.isEmpty()) {
            setName(baseName);
            return;
        }

        String title = namePool.get(RANDOM.nextInt(namePool.size()));
        setName(baseName + " " + title);
    }
}
