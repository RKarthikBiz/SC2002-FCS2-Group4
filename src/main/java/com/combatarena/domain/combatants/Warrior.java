package com.combatarena.domain.combatants;

import com.combatarena.domain.statuseffects.StunEffect;
import com.combatarena.util.GameConstants;

public class Warrior extends Player {
    /**
     * Creates a warrior.
     */
    public Warrior(String name, int hp, int attack, int defense, int speed) {
        super(name, hp, attack, defense, speed);
    }

    /**
     * Deals damage and applies stun.
     */
    public void shieldBash(Combatant target) {
        if (target != null && target.isAlive()) {
            int damage = Math.max(0, getAttack() - target.getDefense());
            target.takeDamage(damage);
            applyStun(target);
            setSpecialSkillCooldown(GameConstants.SPECIAL_SKILL_COOLDOWN);
            System.out.println("  [SKILL ] " + getName() + " -> " + target.getName()
                    + " | Shield Bash | " + damage + " dmg");
        }
    }

    /**
        * Default warrior turn behavior.
     */
    @Override
    public void performTurn() {
        decrementCooldown();
        System.out.println(getName() + " braces for close combat.");
    }

    @Override
    public String toString() {
        return "Warrior: " + super.toString();
    }

    /**
     * Applies the configured stun effect.
     */
    private void applyStun(Combatant target) {
        StunEffect stun = new StunEffect(GameConstants.STUN_DURATION);
        target.applyStatusEffect(stun);
    }
}
