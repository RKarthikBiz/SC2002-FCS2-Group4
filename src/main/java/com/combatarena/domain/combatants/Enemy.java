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
 * Abstract intermediate class representing an enemy combatant.
 * Extends Combatant with AI decision-making for combat actions.
 */
public abstract class Enemy extends Combatant {
    private static final Random RANDOM = new Random();

    private List<String> namePool;

    /**
     * Constructor to initialize an enemy with core attributes.
     */
    public Enemy(String name, int hp, int attack, int defense, int speed) {
        super(name, hp, attack, defense, speed);
        this.namePool = new ArrayList<>();
    }

    /**
     * Abstract method for enemy AI to decide what action to take.
     * Concrete enemy subclasses must implement this to define their unique behavior.
     * TODO: Implementation required by someone else - AI decision logic.
     * 
     * @return The action the enemy decides to perform
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
     * Performs the enemy's turn in combat.
     * This method calls decideAction() to determine what the enemy should do.
     * Concrete enemy subclasses may override this for custom turn behavior.
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
