package com.combatarena.domain.combatants;

import com.combatarena.domain.statuseffects.StunEffect;
import com.combatarena.util.GameConstants;

public class Warrior extends Player {
    /**
     * Constructor to initialize a Warrior with default attributes.
     */
    public Warrior(String name, int hp, int attack, int defense, int speed) {
        super(name, hp, attack, defense, speed);
    }

    /**
     * Warrior's special ability - Shield Bash.
     * Performs a melee attack on a target that also stuns them.
     *
     * @param target The combatant to bash with the shield
     */
    public void shieldBash(Combatant target) {
        if (target != null && target.isAlive()) {
            int damage = Math.max(0, getAttack() - target.getDefense());
            target.takeDamage(damage);
            applyStun(target);
            setSpecialSkillCooldown(GameConstants.SPECIAL_SKILL_COOLDOWN);
            System.out.println(getName() + " uses Shield Bash on " + target.getName()
                    + " for " + damage + " damage!");
        }
    }

    /**
     * Performs the Warrior's turn in combat.
     * Base implementation prepares for close combat.
     */
    @Override
    public void performTurn() {
        decrementCooldown();
        System.out.println(getName() + " braces for close combat.");
    }

    /**
     * Returns a string representation of the Warrior.
     */
    @Override
    public String toString() {
        return "Warrior: " + super.toString();
    }

    /**
     * Applies a stun effect to the target for STUN_DURATION turns.
     */
    private void applyStun(Combatant target) {
        StunEffect stun = new StunEffect(GameConstants.STUN_DURATION);
        target.applyStatusEffect(stun);
    }
}
