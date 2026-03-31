package com.combatarena.domain.combatants;


public class Warrior extends Player {
    private static final int INITIAL_COOLDOWN = 0;

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
            // TODO: Implementation required by someone else - stun effect mechanics
            int baseDamage = getAttack() + 5; // Slightly increased damage for special ability
            target.takeDamage(baseDamage);
            System.out.println(getName() + " uses Shield Bash on " + target.getName() + 
                             " for " + baseDamage + " damage!");
            
            // Apply stun effect
            // StatusEffect stunEffect = new StunEffect(); // TODO: Implement by someone else
            // target.applyStatusEffect(stunEffect);
        }
    }

    /**
     * Performs the Warrior's turn in combat.
     * Base implementation performs a basic attack.
     * This can be overridden by game logic to use AI decision-making.
     */
    @Override
    public void performTurn() {
        // TODO: Implementation required by someone else - warrior turn strategy
        decrementCooldown();
        System.out.println(getName() + " prepares for combat!");
    }

    /**
     * Returns a string representation of the Warrior.
     */
    @Override
    public String toString() {
        return "Warrior: " + super.toString();
    }
}
