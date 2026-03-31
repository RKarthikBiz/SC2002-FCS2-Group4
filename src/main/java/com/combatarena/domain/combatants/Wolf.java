package com.combatarena.domain.combatants;

import com.combatarena.domain.actions.Action;


public class Wolf extends Enemy {

    /**
     * Constructor to initialize a Wolf with default attributes.
     */
    public Wolf(String name, int hp, int attack, int defense, int speed) {
        super(name, hp, attack, defense, speed);
    }

    /**
     * Wolf's AI decision-making for combat actions.
     * Wolves typically perform a fierce attack.
     * TODO: Implementation required by someone else - specific action implementation.
     * 
     * @return A BasicAttack action (or null if not available)
     */
    @Override
    public Action decideAction() {
        // TODO: Implementation required by someone else - action instantiation
        // Return a BasicAttack action or similar
        // For now, return null as placeholder
        System.out.println(getName() + " snarls and prepares to attack!");
        return null; // TODO: Return new BasicAttack(this, target);
    }

    /**
     * Performs the Wolf's turn in combat.
     * Calls the parent performTurn which uses decideAction().
     */
    @Override
    public void performTurn() {
        super.performTurn();
    }

    /**
     * Returns a string representation of the Wolf.
     */
    @Override
    public String toString() {
        return "Wolf: " + super.toString();
    }
}
