package com.combatarena.domain.combatants;

import com.combatarena.domain.statuseffects.StatusEffect;
import java.util.ArrayList;
import java.util.List;

/**
 * Abstract base class representing a combatant in the combat arena.
 * Defines core attributes and methods for all combatants (players and enemies).
 */
public abstract class Combatant {
    // Private attributes for encapsulation
    private String name;
    private int hp;
    private int maxHp;
    private int attack;
    private int defense;
    private int speed;
    private List<StatusEffect> statusEffects;

    /**
     * Constructor to initialize a combatant with core attributes.
     */
    public Combatant(String name, int hp, int attack, int defense, int speed) {
        this.name = name;
        this.maxHp = hp;
        this.hp = hp;
        this.attack = attack;
        this.defense = defense;
        this.speed = speed;
        this.statusEffects = new ArrayList<>();
    }

    /**
     * Reduces the combatant's HP based on damage amount and defense.
     * Damage is reduced by a percentage of defense (10% per 1 defense point, minimum 0).
     * 
     * @param amount The base damage amount to apply
     */
    public void takeDamage(int amount) {
        // TODO: Implementation required by someone else - damage calculation logic
        // Defense reduces damage: each defense point reduces damage by 10% (capped at 90% reduction)
        double defenseReduction = Math.min(0.9, defense * 0.1);
        int actualDamage = Math.max(1, (int)(amount * (1 - defenseReduction)));
        this.hp = Math.max(0, this.hp - actualDamage);
    }

    /**
     * Heals the combatant by the specified amount.
     * HP cannot exceed maxHp.
     * 
     * @param amount The amount to heal
     */
    public void heal(int amount) {
        this.hp = Math.min(this.maxHp, this.hp + amount);
    }

    /**
     * Checks if the combatant is still alive.
     * 
     * @return true if hp > 0, false otherwise
     */
    public boolean isAlive() {
        return this.hp > 0;
    }

    /**
     * Applies a status effect to this combatant.
     * Uses the StatusEffect's apply method and adds it to the list.
     * 
     * @param effect The status effect to apply
     */
    public void applyStatusEffect(StatusEffect effect) {
        // TODO: Implementation required by someone else - status effect application
        if (effect != null) {
            effect.apply(this);
            statusEffects.add(effect);
        }
    }

    /**
     * Adds a status effect to the combatant's status effects list.
     * This is an alternative method for managing the statusEffects collection.
     * 
     * @param effect The status effect to add
     */
    public void addStatusEffect(StatusEffect effect) {
        // TODO: Implementation required by someone else - status effect collection management
        if (effect != null && !statusEffects.contains(effect)) {
            statusEffects.add(effect);
        }
    }

    /**
     * Abstract method to be implemented by subclasses to define behavior during a turn.
     */
    public abstract void performTurn();

    // Getters and Setters for all attributes

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = Math.max(0, Math.min(hp, this.maxHp));
    }

    public int getMaxHp() {
        return maxHp;
    }

    public void setMaxHp(int maxHp) {
        this.maxHp = maxHp;
        if (this.hp > maxHp) {
            this.hp = maxHp;
        }
    }

    public int getAttack() {
        return attack;
    }

    public void setAttack(int attack) {
        this.attack = attack;
    }

    public int getDefense() {
        return defense;
    }

    public void setDefense(int defense) {
        this.defense = defense;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public List<StatusEffect> getStatusEffects() {
        return new ArrayList<>(statusEffects);
    }

    public void setStatusEffects(List<StatusEffect> statusEffects) {
        this.statusEffects = new ArrayList<>(statusEffects);
    }

    /**
     * Removes a status effect from this combatant.
     * 
     * @param effect The status effect to remove
     */
    public void removeStatusEffect(StatusEffect effect) {
        // TODO: Implementation required by someone else - status effect removal
        if (effect != null) {
            effect.remove(this);
            statusEffects.remove(effect);
        }
    }

    /**
     * Clears all status effects from this combatant.
     */
    public void clearStatusEffects() {
        statusEffects.clear();
    }

    /**
     * Returns a string representation of the combatant.
     */
    @Override
    public String toString() {
        return String.format("%s (HP: %d/%d, ATK: %d, DEF: %d, SPD: %d)",
                name, hp, maxHp, attack, defense, speed);
    }
}
