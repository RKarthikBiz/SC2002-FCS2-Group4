package com.combatarena.util;

/**
 * GameConstants - single source of truth for every magic number in the game.
 *
 * All constants are public static final, sourced directly from the UML/XMI.
 * The class is final with a private constructor to prevent instantiation.
 *
 * Constant groups (in XMI declaration order):
 *   Warrior stats  · Wizard stats  · Goblin stats  · Wolf stats
 *   Skill tuning   · Item tuning   · Critical hits
 *   Flee           · Combo system  · Loot
 */
public final class GameConstants {

    private GameConstants() {}

    // =========================================================================
    // Warrior base stats                              (return type: int)
    // =========================================================================

    public static final int WARRIOR_HP  = 260;
    public static final int WARRIOR_ATK = 40;
    public static final int WARRIOR_DEF = 20;
    public static final int WARRIOR_SPD = 30;

    // =========================================================================
    // Wizard base stats                               (return type: int)
    // =========================================================================

    public static final int WIZARD_HP  = 200;
    public static final int WIZARD_ATK = 50;
    public static final int WIZARD_DEF = 10;
    public static final int WIZARD_SPD = 20;

    // =========================================================================
    // Goblin base stats                               (return type: int)
    // =========================================================================

    public static final int GOBLIN_HP  = 55;
    public static final int GOBLIN_ATK = 35;
    public static final int GOBLIN_DEF = 15;
    public static final int GOBLIN_SPD = 25;

    // =========================================================================
    // Wolf base stats                                 (return type: int)
    // =========================================================================

    public static final int WOLF_HP  = 40;
    public static final int WOLF_ATK = 45;
    public static final int WOLF_DEF = 5;
    public static final int WOLF_SPD = 35;

    // =========================================================================
    // Skill / action tuning                          (return type: int)
    // =========================================================================

    /**
     * Cooldown for SpecialSkill in turns, including the turn it was used.
     * Skill is unavailable for the next (SPECIAL_SKILL_COOLDOWN - 1) turns.
     */
    public static final int SPECIAL_SKILL_COOLDOWN = 3;

    /** Defense bonus granted by the Defend action (current + next round). */
    public static final int DEFEND_BONUS_DEF = 10;

    /** Number of turns the Defend bonus lasts (current turn + next turn). */
    public static final int DEFEND_DURATION = 2;

    /** Number of turns a StunEffect keeps its target unable to act. */
    public static final int STUN_DURATION = 1;

    /** Extra attack damage added to every target hit by Wizard's Arcane Blast. */
    public static final int ARCANE_BLAST_ATK_BONUS = 10;

    // =========================================================================
    // Item tuning                                    (return type: int)
    // =========================================================================

    /**
     * HP restored when a Potion is used.
     * Formula: newHP = min(currentHP + POTION_HEAL_AMOUNT, maxHP)
     */
    public static final int POTION_HEAL_AMOUNT = 100;

    /** Number of turns a SmokeBomb blocks incoming enemy damage. */
    public static final int SMOKE_BOMB_DURATION = 2;

    // =========================================================================
    // Critical hit system                            (return type: double)
    // =========================================================================

    /** Probability (0.0–1.0) that a basic attack is a critical hit. */
    public static final double CRIT_CHANCE = 0.2;

    /** Damage multiplier applied when a critical hit occurs. */
    public static final double CRIT_MULTIPLIER = 1.5;

    // =========================================================================
    // Flee                                           (return type: double)
    // =========================================================================

    /** Probability (0.0–1.0) that a Flee action succeeds. */
    public static final double FLEE_CHANCE = 0.5;

    // =========================================================================
    // Combo system                                   (return type: int)
    // =========================================================================

    /** Number of consecutive hits required to trigger the combo bonus. */
    public static final int COMBO_BONUS_THRESHOLD = 3;

    /** Bonus attack damage added when a combo is triggered. */
    public static final int COMBO_ATK_BONUS = 10;

    // =========================================================================
    // Loot                                           (return type: double)
    // =========================================================================

    /** Probability (0.0–1.0) that an enemy drops an item on death. */
    public static final double LOOT_DROP_CHANCE = 0.4;
}
