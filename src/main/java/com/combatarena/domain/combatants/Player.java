package com.combatarena.domain.combatants;

import com.combatarena.util.GameConstants;
import java.util.ArrayList;
import java.util.List;

/**
 * Abstract intermediate class representing a player character.
 * Extends Combatant with inventory management and special skill mechanics.
 */
public abstract class Player extends Combatant {
    // Private attributes for encapsulation
    private List<Object> inventory;
    private int specialSkillCooldown;

    /**
     * Constructor to initialize a player with core attributes.
     */
    public Player(String name, int hp, int attack, int defense, int speed) {
        super(name, hp, attack, defense, speed);
        this.inventory = new ArrayList<>();
        this.specialSkillCooldown = 0;
    }

    /**
     * Uses an item from the player's inventory on a target combatant.
     * TODO: Implementation required by someone else - item interaction logic.
     * 
     * @param item The item to use
     */
    public void useItem(Object item) {
        if (inventory.contains(item)) {
            invokeItemUse(item);
            inventory.remove(item);
        }
    }

    /**
     * Uses the player's special skill if it's not on cooldown.
     * This method should be overridden by subclasses to implement specific special skills.
     * TODO: Implementation required by someone else - special skill logic.
     */
    public void useSpecialSkill() {
        if (specialSkillCooldown <= 0) {
            System.out.println(getName() + " uses a special skill!");
            specialSkillCooldown = GameConstants.SPECIAL_SKILL_COOLDOWN;
        } else {
            System.out.println(getName() + "'s special skill is on cooldown for " + specialSkillCooldown + " more turns.");
        }
    }

    /**
     * Decrements the special skill cooldown by 1.
     * Should be called each turn to track cooldown.
     */
    public void decrementCooldown() {
        if (specialSkillCooldown > 0) {
            specialSkillCooldown--;
        }
    }

    /**
     * Performs the player's turn in combat.
     * This abstract method must be implemented by concrete player subclasses.
     */
    @Override
    public abstract void performTurn();

    // Getters and Setters for all attributes

    @SuppressWarnings("rawtypes")
    public List getInventory() {
        return new ArrayList<>(inventory);
    }

    public void setInventory(List<?> inventory) {
        this.inventory = new ArrayList<>(inventory == null ? new ArrayList<>() : inventory);
    }

    /**
     * Adds an item to the player's inventory.
     * 
     * @param item The item to add
     */
    public void addItem(Object item) {
        if (item != null) {
            inventory.add(item);
        }
    }

    /**
     * Removes an item from the player's inventory.
     * 
     * @param item The item to remove
     */
    public void removeItem(Object item) {
        inventory.remove(item);
    }

    /**
     * Gets the current special skill cooldown.
     * 
     * @return The cooldown count
     */
    public int getSpecialSkillCooldown() {
        return specialSkillCooldown;
    }

    /**
     * Sets the special skill cooldown.
     * 
     * @param cooldown The cooldown count
     */
    public void setSpecialSkillCooldown(int cooldown) {
        this.specialSkillCooldown = Math.max(0, cooldown);
    }

    /**
     * Checks if the special skill is available (not on cooldown).
     * 
     * @return true if cooldown is 0 or less, false otherwise
     */
    public boolean isSpecialSkillAvailable() {
        return specialSkillCooldown <= 0;
    }

    private void invokeItemUse(Object item) {
        try {
            java.lang.reflect.Method method = item.getClass().getMethod("use", Combatant.class);
            method.invoke(item, this);
        } catch (NoSuchMethodException e) {
            // Ignore unknown item types until the rest of the codebase is wired.
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException("Failed to use item", e);
        }
    }
}
