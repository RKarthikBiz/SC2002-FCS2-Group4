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

    // Warrior

    public static final int WARRIOR_HP  = 260;
    public static final int WARRIOR_ATK = 40;
    public static final int WARRIOR_DEF = 20;
    public static final int WARRIOR_SPD = 30;

    // Wizard

    public static final int WIZARD_HP  = 200;
    public static final int WIZARD_ATK = 50;
    public static final int WIZARD_DEF = 10;
    public static final int WIZARD_SPD = 20;

    // Goblin

    public static final int GOBLIN_HP  = 55;
    public static final int GOBLIN_ATK = 35;
    public static final int GOBLIN_DEF = 15;
    public static final int GOBLIN_SPD = 25;

    // Wolf

    public static final int WOLF_HP  = 40;
    public static final int WOLF_ATK = 45;
    public static final int WOLF_DEF = 5;
    public static final int WOLF_SPD = 35;

    // Skills

    /**
     * Cooldown for SpecialSkill in turns, including the turn it was used.
     * Skill is unavailable for the next (SPECIAL_SKILL_COOLDOWN - 1) turns.
     */
    public static final int SPECIAL_SKILL_COOLDOWN = 3;

    public static final int DEFEND_BONUS_DEF = 10;

    public static final int DEFEND_DURATION = 2;

    public static final int STUN_DURATION = 1;

    public static final int ARCANE_BLAST_ATK_BONUS = 10;

    // Items

    public static final int POTION_HEAL_AMOUNT = 100;

    public static final int SMOKE_BOMB_DURATION = 2;

    // Mechanics

    public static final double CRIT_CHANCE = 0.2;

    public static final double CRIT_MULTIPLIER = 1.5;

    public static final int COMBO_THRESHOLD_EASY = 60;
    public static final int COMBO_THRESHOLD_MEDIUM = 55;
    public static final int COMBO_THRESHOLD_HARD = 45;
    
    public static final int COMBO_BONUS_THRESHOLD = 3;

    public static final int COMBO_ATK_BONUS = 10;

    public static final double LOOT_DROP_CHANCE = 0.4;
}
