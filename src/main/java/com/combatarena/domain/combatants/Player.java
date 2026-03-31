package com.combatarena.domain.combatants;

import com.combatarena.domain.items.Item;
import java.util.ArrayList;
import java.util.List;

/**
 * Abstract intermediate class representing a player character.
 * Extends Combatant with inventory management and special skill mechanics.
 */
public abstract class Player extends Combatant {
    // Private attributes for encapsulation
    private List<Item> inventory;
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
    public void useItem(Item item) {
        // TODO: Implementation required by someone else - item usage mechanics
        if (inventory.contains(item)) {
            item.use(this);
            inventory.remove(item);
        }
    }

    /**
     * Uses the player's special skill if it's not on cooldown.
     * This method should be overridden by subclasses to implement specific special skills.
     * TODO: Implementation required by someone else - special skill logic.
     */
    public void useSpecialSkill() {
        // TODO: Implementation required by someone else - special skill mechanics
        if (specialSkillCooldown <= 0) {
            // Override in subclasses to define actual special skill behavior
            System.out.println(getName() + " uses a special skill!");
            specialSkillCooldown = 3; // Example cooldown of 3 turns
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

    public List<Item> getInventory() {
        return new ArrayList<>(inventory);
    }

    public void setInventory(List<Item> inventory) {
        this.inventory = new ArrayList<>(inventory);
    }

    /**
     * Adds an item to the player's inventory.
     * 
     * @param item The item to add
     */
    public void addItem(Item item) {
        if (item != null) {
            inventory.add(item);
        }
    }

    /**
     * Removes an item from the player's inventory.
     * 
     * @param item The item to remove
     */
    public void removeItem(Item item) {
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
}
