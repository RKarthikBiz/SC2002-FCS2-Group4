package com.combatarena.domain.combatants;

import com.combatarena.domain.items.Item;
import com.combatarena.util.GameConstants;
import java.util.ArrayList;
import java.util.List;

/**
 * Base class for player-controlled combatants.
 */
public abstract class Player extends Combatant {
    private List<Item> inventory;
    private int specialSkillCooldown;

    /**
     * Creates a player with an empty inventory.
     */
    public Player(String name, int hp, int attack, int defense, int speed) {
        super(name, hp, attack, defense, speed);
        this.inventory = new ArrayList<>();
        this.specialSkillCooldown = 0;
    }

    /**
        * Uses an item if it exists in inventory.
     */
    public void useItem(Item item) {
        if (item != null && inventory.contains(item)) {
            item.use(this);
            inventory.remove(item);
        }
    }

    /**
        * Basic special-skill behavior with cooldown handling.
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
        * Reduces skill cooldown by one turn.
     */
    public void decrementCooldown() {
        if (specialSkillCooldown > 0) {
            specialSkillCooldown--;
        }
    }

    /**
        * Called when this player takes a turn.
     */
    @Override
    public abstract void performTurn();

    public List<Item> getInventory() {
        return new ArrayList<>(inventory);
    }

    public void setInventory(List<Item> inventory) {
        this.inventory = new ArrayList<>(inventory == null ? new ArrayList<>() : inventory);
    }

    /**
        * Adds one item to inventory.
     */
    public void addItem(Item item) {
        if (item != null) {
            inventory.add(item);
        }
    }

    /**
     * Removes one item from inventory.
     */
    public void removeItem(Item item) {
        inventory.remove(item);
    }

    /**
     * Current cooldown value.
     */
    public int getSpecialSkillCooldown() {
        return specialSkillCooldown;
    }

    /**
     * Sets cooldown and clamps it to non-negative values.
     */
    public void setSpecialSkillCooldown(int cooldown) {
        this.specialSkillCooldown = Math.max(0, cooldown);
    }

    /**
     * Returns true when the special skill is available.
     */
    public boolean isSpecialSkillAvailable() {
        return specialSkillCooldown <= 0;
    }
}
