package com.combatarena.domain.actions;

import com.combatarena.domain.combatants.Combatant;

/**
 * BasicAttack - performs a standard attack on a user-selected target.
 *
 * Damage formula : max(0, attacker.attack - target.defense)
 * Target HP floor: 0  (HP cannot go below 0)
 */
public class BasicAttack implements Action {

    @Override
    public void execute(Combatant attacker, Combatant target) {
        int damage = Math.max(0, attacker.getAttack() - target.getDefense());

        System.out.println(attacker.getName() + " performs a Basic Attack on "
                + target.getName() + " for " + damage + " damage!");

        target.takeDamage(damage); // Combatant.takeDamage() must enforce HP >= 0
    }
}
