package com.combatarena.domain.combatants;

import com.combatarena.domain.actions.Action;
import com.combatarena.domain.actions.BasicAttack;
import java.util.Arrays;

public class Goblin extends Enemy {

    /**
    * Creates a goblin and assigns a random title.
     */
    public Goblin(String name, int hp, int attack, int defense, int speed) {
        super(name, hp, attack, defense, speed);
        setNamePool(Arrays.asList("Chieftain", "Skulker", "Bonepicker", "Nightfang", "Ravager"));
        assignDynamicName("Goblin");
    }

    /**
     * Goblins currently default to basic attack.
     */
    @Override
    public Action decideAction() {
        return new BasicAttack();
    }

    /**
     * Flavor text for the goblin's turn.
     */
    @Override
    public void performTurn() {
        System.out.println(getName() + " lunges at its target.");
    }

    @Override
    public String toString() {
        return "Goblin: " + super.toString();
    }
}
