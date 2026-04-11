package com.combatarena.domain.combatants;

import com.combatarena.util.GameConstants;
import java.util.Collections;
import java.util.List;

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
        arcaneBlast(target, target == null ? Collections.emptyList() : Collections.singletonList(target));
    }

    /**
     * Arcane Blast hits all active enemies in the provided target list.
     * Each enemy eliminated by this blast grants a permanent +ATK bonus.
     */
    public void arcaneBlast(Combatant target, List<Combatant> allTargets) {
        if (allTargets == null || allTargets.isEmpty()) {
            return;
        }

        for (Combatant enemy : allTargets) {
            if (enemy == null || !enemy.isAlive()) {
                continue;
            }

            int damage = Math.max(0, getAttack() + attackBonus - enemy.getDefense());
            enemy.takeDamage(damage);
                System.out.println("  [SKILL ] " + getName() + " -> " + enemy.getName()
                    + " | Arcane Blast | " + damage + " dmg");

            if (!enemy.isAlive()) {
                arcaneBlastBonus(GameConstants.ARCANE_BLAST_ATK_BONUS);
            }
        }

        setSpecialSkillCooldown(GameConstants.SPECIAL_SKILL_COOLDOWN);
    }

    /**
     * Adds a temporary attack bonus to the Wizard's arcane blasts.
     * The bonus persists until reset or overwritten.
     * 
     * @param amount The amount to increase attack bonus by
     */
    public void addAttackBonus(int amount) {
        arcaneBlastBonus(amount);
    }

    /**
     * Increases attack for the rest of the level when Arcane Blast secures a kill.
     */
    public void arcaneBlastBonus(int amount) {
        int gain = Math.max(0, amount);
        this.attackBonus += gain;
        setAttack(getAttack() + gain);
        System.out.println("  [STATUS] " + getName() + " Arcane Bonus +" + gain
                + " (total: " + attackBonus + ")");
    }

    /**
     * Resets the attack bonus to 0.
     */
    public void resetAttackBonus() {
        setAttack(Math.max(0, getAttack() - attackBonus));
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
        int clamped = Math.max(0, bonus);
        int delta = clamped - this.attackBonus;
        setAttack(Math.max(0, getAttack() + delta));
        this.attackBonus = clamped;
    }

    /**
     * Performs the Wizard's turn in combat.
     * Base implementation prepares for casting.
     * This can be overridden by game logic to use AI decision-making.
     */
    @Override
    public void performTurn() {
        decrementCooldown();
        System.out.println(getName() + " channels magical energy. (Arcane Bonus: " + attackBonus + ")");
    }

    /**
     * Returns a string representation of the Wizard.
     */
    @Override
    public String toString() {
        return "Wizard: " + super.toString() + " (Arcane Bonus: " + attackBonus + ")";
    }
}
