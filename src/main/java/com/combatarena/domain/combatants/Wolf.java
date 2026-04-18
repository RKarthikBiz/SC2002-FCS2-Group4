package com.combatarena.domain.combatants;

import com.combatarena.domain.actions.Action;
import com.combatarena.domain.actions.BasicAttack;
import java.util.Arrays;


public class Wolf extends Enemy {

    /**
     * Creates a wolf and assigns a random title.
     */
    public Wolf(String name, int hp, int attack, int defense, int speed) {
        super(name, hp, attack, defense, speed);
        setNamePool(Arrays.asList("Rabid", "Dire", "Frostmaw", "Ashfur", "Bloodfang"));
        assignDynamicName("Wolf");
    }

    /**
     * Wolves currently default to basic attack.
     */
    @Override
    public Action decideAction() {
        return new BasicAttack();
    }

    /**
     * Flavor text for the wolf's turn.
     */
    @Override
    public void performTurn() {
        System.out.println(getName() + " snaps aggressively.");
    }

    @Override
    public String toString() {
        return "Wolf: " + super.toString();
    }
}
