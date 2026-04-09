package com.combatarena.domain.actions;

import com.combatarena.domain.combatants.Combatant;
import com.combatarena.domain.combatants.Warrior;
import com.combatarena.domain.combatants.Wizard;
import com.combatarena.util.GameConstants;
import java.util.HashMap;
import java.util.Map;

/**
 * SpecialSkill - executes the class-specific special ability of the attacker.
 *
 * Cooldown rules
 * --------------
 *  - Cooldown is 3 turns, including the turn the skill is used.
 *  - A cooldown counter is tracked per-combatant in a static map.
 *  - The counter is decremented ONLY when that combatant takes a turn
 *    (call {@link #tick(Combatant)} from BattleEngine.processTurn()).
 *  - While cooldown > 0 the skill cannot be used.
 *
 * Class dispatch
 * --------------
 *  The special ability performed depends on the runtime type of the attacker:
 *    Warrior → shieldBash(target)
 *    Wizard  → arcaneBlast(target, allEnemies)   (multi-target; pass via constructor)
 *    Enemy   → decideAction() already handles this; falls back to basicAttack
 *
 * Usage (in BattleEngine):
 * <pre>
 *   // At start of combatant's turn, tick their cooldown:
 *   SpecialSkill.tick(combatant);
 *
 *   // When player selects Special Skill:
 *   if (SpecialSkill.isReady(player)) {
 *       Action action = new SpecialSkill(allEnemies);
 *       action.execute(player, target);
 *   }
 * </pre>
 */
public class SpecialSkill implements Action {

    /** Cooldown duration in turns (including the turn the skill is used). */
    public static final int COOLDOWN = GameConstants.SPECIAL_SKILL_COOLDOWN;

    /**
     * Per-combatant cooldown counters.
     * Key   = Combatant instance
     * Value = turns remaining before the skill can be used again (0 = ready)
     */
    private static final Map<Combatant, Integer> cooldowns = new HashMap<>();

    /**
     * For Wizard's arcaneBlast, a list of all enemies is needed.
     * May be null for non-Wizard combatants.
     */
    private final java.util.List<Combatant> allTargets;

    /**
     * Constructs a SpecialSkill action.
     *
     * @param allTargets full list of enemies in the current level; required for
     *                   Wizard's arcaneBlast (multi-target). Pass null or empty
     *                   list for Warrior / other single-target classes.
     */
    public SpecialSkill(java.util.List<Combatant> allTargets) {
        this.allTargets = allTargets;
    }

    // -------------------------------------------------------------------------
    // Static cooldown management (called by BattleEngine each turn)
    // -------------------------------------------------------------------------

    /**
     * Decrements the cooldown for {@code combatant} by 1 (minimum 0).
     * Must be called by BattleEngine at the START of each of that combatant's
     * turns so the cooldown only ticks when the combatant actually acts.
     *
     * @param combatant the combatant whose turn is being processed
     */
    public static void tick(Combatant combatant) {
        int current = cooldowns.getOrDefault(combatant, 0);
        if (current > 0) {
            cooldowns.put(combatant, current - 1);
        }
    }

    /**
     * Returns true if the combatant's special skill is off cooldown and ready.
     *
     * @param combatant the combatant to check
     * @return true if cooldown == 0
     */
    public static boolean isReady(Combatant combatant) {
        return cooldowns.getOrDefault(combatant, 0) == 0;
    }

    /**
     * Returns the remaining cooldown turns for a combatant.
     *
     * @param combatant the combatant to check
     * @return turns remaining (0 means ready)
     */
    public static int getCooldown(Combatant combatant) {
        return cooldowns.getOrDefault(combatant, 0);
    }

    /**
     * Resets cooldown tracking (e.g. between levels or for testing).
     */
    public static void resetAll() {
        cooldowns.clear();
    }

    // -------------------------------------------------------------------------
    // Action execution
    // -------------------------------------------------------------------------

    @Override
    public void execute(Combatant attacker, Combatant target) {
        // Guard: skill must be ready
        if (!isReady(attacker)) {
            System.out.println("  [INFO  ] " + attacker.getName()
                + " special skill is on cooldown (" + getCooldown(attacker)
                + " turn(s) remaining).");
            return;
        }

        // Set cooldown - does not include the current turn, so set to COOLDOWN + 1
        // tick() will reduce it on the NEXT turn this combatant acts
        cooldowns.put(attacker, COOLDOWN + 1);

        // Dispatch to the class-specific ability
        if (attacker instanceof Warrior) {
            executeWarriorSkill((Warrior) attacker, target);
        } else if (attacker instanceof Wizard) {
            executeWizardSkill((Wizard) attacker, target);
        } else {
            // Fallback for any other Combatant subclass: enhanced basic attack
            executeDefaultSkill(attacker, target);
        }
    }

    // -------------------------------------------------------------------------
    // Class-specific skill implementations
    // -------------------------------------------------------------------------

    /**
     * Warrior: shieldBash(target) — delegates to Warrior's own method.
     */
    private void executeWarriorSkill(Warrior warrior, Combatant target) {
        warrior.shieldBash(target);
    }

    /**
     * Wizard: arcaneBlast(target, allEnemies) — AoE attack on all enemies.
     * The primary target and allTargets list are passed to the Wizard's method.
     */
    private void executeWizardSkill(Wizard wizard, Combatant target) {
        System.out.println("  [SKILL ] " + wizard.getName() + " casts Arcane Blast.");
        if (allTargets != null && !allTargets.isEmpty()) {
            wizard.arcaneBlast(target, allTargets);
        } else {
            // Fallback if no target list provided: single-target arcane hit
            wizard.arcaneBlast(target, java.util.Collections.singletonList(target));
        }
    }

    /**
     * Default special skill: deals attack damage with no defense reduction.
     */
    private void executeDefaultSkill(Combatant attacker, Combatant target) {
        int damage = attacker.getAttack();
        System.out.println("  [SKILL ] " + attacker.getName() + " -> " + target.getName()
            + " | Special Skill | " + damage + " dmg");
        target.takeDamage(damage);
    }
}
