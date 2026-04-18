package com.combatarena.domain.combatants;

import com.combatarena.domain.statuseffects.SmokeBombEffect;
import com.combatarena.domain.statuseffects.StatusEffect;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Base type for all player and enemy units.
 */
public abstract class Combatant {
    private String name;
    private int hp;
    private int maxHp;
    private int attack;
    private int defense;
    private int speed;
    private List<StatusEffect> statusEffects;

    /**
        * Creates a combatant with base stats.
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
     * Reduces HP by the provided post-mitigated damage amount.
     * HP is clamped at 0.
     * If SmokeBombEffect is active, incoming damage is suppressed to 0.
     */
    public void takeDamage(int amount) {
        if (hasSmokeBombEffect()) {
            amount = 0;
        }
        this.hp = Math.max(0, this.hp - Math.max(0, amount));
    }

    /**
        * Checks whether smoke protection is active.
     */
    private boolean hasSmokeBombEffect() {
        for (StatusEffect effect : statusEffects) {
            if (effect instanceof SmokeBombEffect) {
                return true;
            }
        }
        return false;
    }

    /**
     * Heals the combatant by the specified amount.
     * HP cannot exceed maxHp.
     */
    public void heal(int amount) {
        this.hp = Math.min(this.maxHp, this.hp + amount);
    }

    /**
        * Returns true when HP is above zero.
     */
    public boolean isAlive() {
        return this.hp > 0;
    }

    /**
        * Applies an effect and tracks it as active.
     */
    public void applyStatusEffect(StatusEffect effect) {
        if (effect != null) {
            effect.apply(this);
            statusEffects.add(effect);
        }
    }

    /**
        * Adds an effect directly if it is not already present.
     */
    public void addStatusEffect(StatusEffect effect) {
        if (effect != null && !statusEffects.contains(effect)) {
            statusEffects.add(effect);
        }
    }

    /**
     * Ticks all active effects and removes expired ones.
     */
    public void applyStatusEffects() {
        Iterator<StatusEffect> iterator = statusEffects.iterator();
        while (iterator.hasNext()) {
            StatusEffect effect = iterator.next();
            effect.tick();

            if (effect.isExpired()) {
                effect.remove(this);
                iterator.remove();
            }
        }
    }

    /**
        * Called when this combatant takes a turn.
     */
    public abstract void performTurn();

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
        this.statusEffects = new ArrayList<>(statusEffects == null ? new ArrayList<>() : statusEffects);
    }

    /**
        * Removes one active effect.
     */
    public void removeStatusEffect(StatusEffect effect) {
        if (effect != null) {
            effect.remove(this);
            statusEffects.remove(effect);
        }
    }

    /**
        * Clears every active effect.
     */
    public void clearStatusEffects() {
        statusEffects.clear();
    }

    /**
        * Returns a compact summary of this combatant.
     */
    @Override
    public String toString() {
        return String.format("%s (HP: %d/%d, ATK: %d, DEF: %d, SPD: %d)",
                name, hp, maxHp, attack, defense, speed);
    }
}
