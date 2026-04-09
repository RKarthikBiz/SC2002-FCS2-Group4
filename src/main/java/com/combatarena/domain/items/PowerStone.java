package com.combatarena.domain.items;

import java.util.List;
import java.util.Objects;
import com.combatarena.domain.combatants.Combatant;
import com.combatarena.domain.combatants.Warrior;
import com.combatarena.domain.combatants.Wizard;

/**
 * PowerStone - a single-use item that grants a free use of the special skill.
 *
 * Effect : Executes the user's class-specific special skill effect once.
 *          Does NOT start the cooldown timer and does NOT change an existing
 *          cooldown. The SpecialSkill.tick() / cooldown map is left untouched.
 *
 * Implementation: Reuses the private skill dispatch logic by extending
 *                 SpecialSkill's execute path directly, bypassing the cooldown
 *                 gate and the cooldown setter.
 */
public class PowerStone implements Item {

    /**
     * The list of all current enemies in the level, needed by Wizard's
     * arcaneBlast (multi-target). Supplied at construction time by the
     * BattleEngine / GameCLI before the item is used.
     */
    private final List<Combatant> allTargets;

    /**
     * The chosen target for the skill (e.g. the enemy the player selected).
     * For multi-target skills (Wizard) this is the primary target; the rest
     * come from allTargets.
     */
    private final Combatant target;

    /**
     * Constructs a PowerStone.
     *
     * @param target     the primary skill target
     * @param allTargets all enemies currently in the level (for AoE skills)
     */
    public PowerStone(Combatant target, List<Combatant> allTargets) {
        this.target     = target;
        this.allTargets = allTargets;
    }

    @Override
    public void use(Combatant user) {
        System.out.println("  [ITEM FX ] Power Stone: special skill triggers for free"
            + " (cooldown unchanged).");

        // Dispatch to the correct class-specific skill WITHOUT touching cooldown
        if (user instanceof Warrior) {
            ((Warrior) user).shieldBash(target);

        } else if (user instanceof Wizard) {
            List<Combatant> targets = (allTargets != null && !allTargets.isEmpty())
                    ? allTargets
                    : java.util.Collections.singletonList(target);
            ((Wizard) user).arcaneBlast(target, targets);

        } else {
            // Fallback for any other Combatant subtype
            Combatant activeTarget = target;
            if (activeTarget == null && allTargets != null && !allTargets.isEmpty()) {
                activeTarget = allTargets.get(0);
            }
            if (activeTarget == null) {
                System.out.println("  [ITEM FX ] Power Stone had no valid target.");
                return;
            }
            Combatant resolvedTarget = Objects.requireNonNull(activeTarget);
            int damage = Math.max(0, user.getAttack() - resolvedTarget.getDefense());
            System.out.println("  [ITEM FX ] " + user.getName() + " -> " + resolvedTarget.getName()
                + " | Power Stone strike | " + damage + " dmg");
            resolvedTarget.takeDamage(damage);
        }
        // Cooldown is intentionally NOT modified here
    }
}
