package com.combatarena.domain.combatants;

import com.combatarena.domain.actions.Action;
import com.combatarena.domain.actions.BasicAttack;

public class Goblin extends Enemy {

    /**
     * Constructor to initialize a Goblin with default attributes.
     */
    public Goblin(String name, int hp, int attack, int defense, int speed) {
        super(name, hp, attack, defense, speed);
    }

    /**
     * Goblin's AI decision-making for combat actions. Goblins typically perform
     * a basic attack. TODO: Implementation required by someone else - specific
     * action implementation.
     *
     * @return A BasicAttack action (or null if not available)
     */
    @Override
    public Action decideAction() {
        return new BasicAttack();
    }

    /**
     * Performs the Goblin's turn in combat. Calls the parent performTurn which
     * uses decideAction().
     */
    @Override
    public void performTurn() {
        System.out.println(getName() + " lunges at its target.");
    }

    /**
     * Returns a string representation of the Goblin.
     */
    @Override
    public String toString() {
        return "Goblin: " + super.toString();
    }
}
