package com.combatarena.util;

/**
 * GameConstants - central store for every magic number used across the game.
 *
 * All fields are public static final so they can be referenced from anywhere
 * without instantiating this class. The constructor is private to prevent
 * accidental instantiation.
 *
 */
public final class GameConstants {

    // -------------------------------------------------------------------------
    // Prevent instantiation
    // -------------------------------------------------------------------------
    private GameConstants() {}

    // =========================================================================
    // Warrior base stats
    // =========================================================================

    /** Warrior starting / maximum HP. */
    public static final int WARRIOR_HP  = 200;

    /** Warrior base attack stat. */
    public static final int WARRIOR_ATK = 35;

    /** Warrior base defense stat. */
    public static final int WARRIOR_DEF = 20;

    /** Warrior base speed stat (determines turn order). */
    public static final int WARRIOR_SPD = 15;

    // =========================================================================
    // Wizard base stats
    // =========================================================================

    /** Wizard starting / maximum HP. */
    public static final int WIZARD_HP  = 150;

    /** Wizard base attack stat. */
    public static final int WIZARD_ATK = 45;

    /** Wizard base defense stat. */
    public static final int WIZARD_DEF = 10;

    /** Wizard base speed stat. */
    public static final int WIZARD_SPD = 20;

    // =========================================================================
    // Goblin base stats
    // =========================================================================

    /** Goblin starting / maximum HP. */
    public static final int GOBLIN_HP  = 80;

    /** Goblin base attack stat. */
    public static final int GOBLIN_ATK = 20;

    /** Goblin base defense stat. */
    public static final int GOBLIN_DEF = 5;

    /** Goblin base speed stat. */
    public static final int GOBLIN_SPD = 25;

    // =========================================================================
    // Wolf base stats
    // =========================================================================

    /** Wolf starting / maximum HP. */
    public static final int WOLF_HP  = 100;

    /** Wolf base attack stat. */
    public static final int WOLF_ATK = 30;

    /** Wolf base defense stat. */
    public static final int WOLF_DEF = 8;

    /** Wolf base speed stat. */
    public static final int WOLF_SPD = 30;

    // =========================================================================
    // Skill / action tuning
    // =========================================================================

    /**
     * Cooldown for the SpecialSkill action in turns, including the turn it
     * was used. A value of 3 means the skill cannot be reused for the next
     * 2 turns after it is used.
     */
    public static final int SPECIAL_SKILL_COOLDOWN = 3;

    /**
     * Flat defense bonus granted by the Defend action.
     * Active for the current round and the next round (DEFEND_DURATION turns).
     */
    public static final int DEFEND_BONUS_DEF = 10;

    /**
     * Number of turns the Defend bonus lasts (current turn + next turn = 2).
     */
    public static final int DEFEND_DURATION = 2;

    /**
     * Number of turns a StunEffect keeps a target stunned.
     */
    public static final int STUN_DURATION = 1;

    /**
     * Extra attack damage added to every target hit by Wizard's Arcane Blast.
     */
    public static final int ARCANE_BLAST_ATK_BONUS = 10;

    // =========================================================================
    // Item tuning
    // =========================================================================

    /**
     * HP restored when a Potion is used.
     * Formula: newHP = min(currentHP + POTION_HEAL_AMOUNT, maxHP)
     */
    public static final int POTION_HEAL_AMOUNT = 100;

    /**
     * Number of turns a SmokeBomb blocks incoming enemy damage (current + next).
     */
    public static final int SMOKE_BOMB_DURATION = 2;
}
