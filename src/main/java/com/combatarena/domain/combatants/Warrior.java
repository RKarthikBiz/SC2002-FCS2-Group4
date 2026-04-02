package com.combatarena.domain.combatants;

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
     * TODO: Implementation required by someone else - stun effect application.
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
     * Base implementation performs a basic attack.
     * This can be overridden by game logic to use AI decision-making.
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

    private void applyStun(Combatant target) {
        Object stunEffect = createEffect(
                "com.combatarena.domain.statuseffects.StunEffect",
                GameConstants.STUN_DURATION + 1
        );

        if (stunEffect == null) {
            stunEffect = createEffect(
                    "main.java.com.combatarena.domain.statuseffects.StunEffect",
                    GameConstants.STUN_DURATION + 1
            );
        }

        if (stunEffect != null) {
            target.applyStatusEffect(stunEffect);
        }
    }

    private Object createEffect(String className, int duration) {
        try {
            Class<?> clazz = Class.forName(className);
            try {
                return clazz.getConstructor(int.class).newInstance(duration);
            } catch (NoSuchMethodException ex) {
                return clazz.getConstructor().newInstance();
            }
        } catch (ReflectiveOperationException e) {
            return null;
        }
    }
}
