package com.combatarena.domain.combatants;

import java.util.ArrayList;
import java.util.Iterator;
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
    private List<Object> statusEffects;

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
     * Reduces HP by the provided post-mitigation damage amount.
     * HP is clamped at 0.
     *
     * @param amount final damage value to apply
     */
    public void takeDamage(int amount) {
        this.hp = Math.max(0, this.hp - Math.max(0, amount));
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
    public void applyStatusEffect(Object effect) {
        if (effect != null) {
            invokeEffectMethod(effect, "apply", this);
            statusEffects.add(effect);
        }
    }

    /**
     * Adds a status effect to the combatant's status effects list.
     * This is an alternative method for managing the statusEffects collection.
     * 
     * @param effect The status effect to add
     */
    public void addStatusEffect(Object effect) {
        if (effect != null && !statusEffects.contains(effect)) {
            statusEffects.add(effect);
        }
    }

    /**
     * Ticks all active effects and removes expired ones.
     */
    public void applyStatusEffects() {
        Iterator<Object> iterator = statusEffects.iterator();
        while (iterator.hasNext()) {
            Object effect = iterator.next();
            invokeEffectMethod(effect, "tick");

            if (isEffectExpired(effect)) {
                invokeEffectMethod(effect, "remove", this);
                iterator.remove();
            }
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

    @SuppressWarnings("rawtypes")
    public List getStatusEffects() {
        return new ArrayList<>(statusEffects);
    }

    public void setStatusEffects(List<?> statusEffects) {
        this.statusEffects = new ArrayList<>(statusEffects == null ? new ArrayList<>() : statusEffects);
    }

    /**
     * Removes a status effect from this combatant.
     * 
     * @param effect The status effect to remove
     */
    public void removeStatusEffect(Object effect) {
        if (effect != null) {
            invokeEffectMethod(effect, "remove", this);
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

    private void invokeEffectMethod(Object effect, String methodName, Object... args) {
        try {
            for (java.lang.reflect.Method method : effect.getClass().getMethods()) {
                if (!method.getName().equals(methodName)) {
                    continue;
                }

                Class<?>[] parameterTypes = method.getParameterTypes();
                if (parameterTypes.length != args.length) {
                    continue;
                }

                boolean compatible = true;
                for (int i = 0; i < parameterTypes.length; i++) {
                    if (args[i] != null && !parameterTypes[i].isAssignableFrom(args[i].getClass())) {
                        compatible = false;
                        break;
                    }
                }

                if (compatible) {
                    method.invoke(effect, args);
                    return;
                }
            }
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException("Failed to invoke effect method: " + methodName, e);
        }
    }

    private boolean isEffectExpired(Object effect) {
        try {
            java.lang.reflect.Method method = effect.getClass().getMethod("isExpired");
            Object result = method.invoke(effect);
            return result instanceof Boolean && (Boolean) result;
        } catch (NoSuchMethodException e) {
            return false;
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException("Failed to check effect expiration", e);
        }
    }
}
