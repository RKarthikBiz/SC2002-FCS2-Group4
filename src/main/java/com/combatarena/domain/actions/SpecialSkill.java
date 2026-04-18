package com.combatarena.domain.actions;

import com.combatarena.domain.combatants.Combatant;
import com.combatarena.domain.combatants.Warrior;
import com.combatarena.domain.combatants.Wizard;
import com.combatarena.util.GameConstants;
import java.util.HashMap;
import java.util.Map;

/**
 * Runs each class-specific special skill and manages cooldown state.
 */
public class SpecialSkill implements Action {

    /** Cooldown duration in turns. */
    public static final int COOLDOWN = GameConstants.SPECIAL_SKILL_COOLDOWN;

    /**
     * Remaining cooldown per combatant. A value of 0 means the skill is ready.
     */
    private static final Map<Combatant, Integer> cooldowns = new HashMap<>();

    /**
     * Used by Wizard skills that need the active enemy list.
     */
    private final java.util.List<Combatant> allTargets;

    /**
     * Creates a special-skill action.
     * allTargets can be null for single-target classes.
     */
    public SpecialSkill(java.util.List<Combatant> allTargets) {
        this.allTargets = allTargets;
    }

    /**
     * Reduces a combatant's cooldown by one turn if it is above zero.
     */
    public static void tick(Combatant combatant) {
        int current = cooldowns.getOrDefault(combatant, 0);
        if (current > 0) {
            cooldowns.put(combatant, current - 1);
        }
    }

    /**
     * Checks whether the combatant can use their skill right now.
     */
    public static boolean isReady(Combatant combatant) {
        return cooldowns.getOrDefault(combatant, 0) == 0;
    }

    /**
     * Gets the remaining cooldown in turns.
     */
    public static int getCooldown(Combatant combatant) {
        return cooldowns.getOrDefault(combatant, 0);
    }

    /**
     * Clears all stored cooldown values.
     */
    public static void resetAll() {
        cooldowns.clear();
    }

    @Override
    public void execute(Combatant attacker, Combatant target) {
        if (!isReady(attacker)) {
            System.out.println("  [INFO  ] " + attacker.getName()
                + " special skill is on cooldown (" + getCooldown(attacker)
                + " turn(s) remaining).");
            return;
        }

        // Set +1 so the current turn and future ticks line up as intended.
        cooldowns.put(attacker, COOLDOWN + 1);

        if (attacker instanceof Warrior) {
            executeWarriorSkill((Warrior) attacker, target);
        } else if (attacker instanceof Wizard) {
            executeWizardSkill((Wizard) attacker, target);
        } else {
            executeDefaultSkill(attacker, target);
        }
    }

    /**
     * Warrior skill path.
     */
    private void executeWarriorSkill(Warrior warrior, Combatant target) {
        warrior.shieldBash(target);
    }

    /**
     * Wizard skill path.
     */
    private void executeWizardSkill(Wizard wizard, Combatant target) {
        System.out.println("  [SKILL ] " + wizard.getName() + " casts Arcane Blast.");
        if (allTargets != null && !allTargets.isEmpty()) {
            wizard.arcaneBlast(target, allTargets);
        } else {
            wizard.arcaneBlast(target, java.util.Collections.singletonList(target));
        }
    }

    /**
     * Default skill path for non-player combatants.
     */
    private void executeDefaultSkill(Combatant attacker, Combatant target) {
        int damage = attacker.getAttack();
        System.out.println("  [SKILL ] " + attacker.getName() + " -> " + target.getName()
            + " | Special Skill | " + damage + " dmg");
        target.takeDamage(damage);
    }
}
