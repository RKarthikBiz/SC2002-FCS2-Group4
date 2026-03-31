package com.combatarena.domain.combatants;


public class Wizard extends Player {
    private int attackBonus;

    /**
     * Constructor to initialize a Wizard with default attributes.
     */
    public Wizard(String name, int hp, int attack, int defense, int speed) {
        super(name, hp, attack, defense, speed);
        this.attackBonus = 0;
    }

    /**
     * Wizard's special ability - Arcane Blast.
     * Performs a magical attack on a target that deals increased damage based on attack bonus.
     * TODO: Implementation required by someone else - arcane buff effect application.
     * 
     * @param target The combatant to blast with arcane magic
     */
    public void arcaneBlast(Combatant target) {
        if (target != null && target.isAlive()) {
            // TODO: Implementation required by someone else - arcane effect mechanics
            int baseDamage = getAttack() + attackBonus; // Use attack bonus for increased damage
            target.takeDamage(baseDamage);
            System.out.println(getName() + " casts Arcane Blast on " + target.getName() + 
                             " for " + baseDamage + " damage! (with +" + attackBonus + " bonus)");
            
            // Apply arcane buff effect
            // StatusEffect arcaneBuffEffect = new ArcaneBlastBuff(); // TODO: Implement by someone else
            // target.applyStatusEffect(arcaneBuffEffect);
        }
    }

    /**
     * Adds a temporary attack bonus to the Wizard's arcane blasts.
     * The bonus persists until reset or overwritten.
     * 
     * @param amount The amount to increase attack bonus by
     */
    public void addAttackBonus(int amount) {
        this.attackBonus += Math.max(0, amount);
        System.out.println(getName() + " gains +" + amount + " attack bonus (total: " + attackBonus + ")");
    }

    /**
     * Resets the attack bonus to 0.
     */
    public void resetAttackBonus() {
        this.attackBonus = 0;
    }

    /**
     * Gets the current attack bonus.
     * 
     * @return The attack bonus value
     */
    public int getAttackBonus() {
        return attackBonus;
    }

    /**
     * Sets the attack bonus to a specific value.
     * 
     * @param bonus The bonus value to set
     */
    public void setAttackBonus(int bonus) {
        this.attackBonus = Math.max(0, bonus);
    }

    /**
     * Performs the Wizard's turn in combat.
     * Base implementation prepares for casting.
     * This can be overridden by game logic to use AI decision-making.
     */
    @Override
    public void performTurn() {
        // TODO: Implementation required by someone else - wizard turn strategy
        decrementCooldown();
        System.out.println(getName() + " channels magical energy! (Attack Bonus: " + attackBonus + ")");
    }

    /**
     * Returns a string representation of the Wizard.
     */
    @Override
    public String toString() {
        return "Wizard: " + super.toString() + " (Arcane Bonus: " + attackBonus + ")";
    }
}
