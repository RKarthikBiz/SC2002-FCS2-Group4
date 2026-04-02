package com.combatarena.domain.combatants;

import com.combatarena.domain.actions.Action;

/**
 * Abstract intermediate class representing an enemy combatant.
 * Extends Combatant with AI decision-making for combat actions.
 */
public abstract class Enemy extends Combatant {

    /**
     * Constructor to initialize an enemy with core attributes.
     */
    public Enemy(String name, int hp, int attack, int defense, int speed) {
        super(name, hp, attack, defense, speed);
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
}
