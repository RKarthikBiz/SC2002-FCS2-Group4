package com.combatarena.domain.combatants;

import com.combatarena.domain.actions.Action;
import com.combatarena.domain.actions.BasicAttack;
import java.util.Arrays;


public class Wolf extends Enemy {

    /**
     * Constructor to initialize a Wolf with default attributes.
     */
    public Wolf(String name, int hp, int attack, int defense, int speed) {
        super(name, hp, attack, defense, speed);
        setNamePool(Arrays.asList("Rabid", "Dire", "Frostmaw", "Ashfur", "Bloodfang"));
        assignDynamicName("Wolf");
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
        return new BasicAttack();
    }

    /**
     * Performs the Wolf's turn in combat.
     * Calls the parent performTurn which uses decideAction().
     */
    @Override
    public void performTurn() {
        System.out.println(getName() + " snaps aggressively.");
    }

    /**
     * Returns a string representation of the Wolf.
     */
    @Override
    public String toString() {
        return "Wolf: " + super.toString();
    }
}
