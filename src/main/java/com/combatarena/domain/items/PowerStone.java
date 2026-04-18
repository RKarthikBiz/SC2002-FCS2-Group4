package com.combatarena.domain.items;

import java.util.List;
import java.util.Objects;
import com.combatarena.domain.combatants.Combatant;
import com.combatarena.domain.combatants.Warrior;
import com.combatarena.domain.combatants.Wizard;

/**
 * Triggers a class special skill once without changing cooldown state.
 */
public class PowerStone implements Item {

    /**
     * Active enemies, used by multi-target skills.
     */
    private final List<Combatant> allTargets;

    /**
     * Primary target selected by the player.
     */
    private final Combatant target;

    /**
     * Creates a Power Stone with a primary target and optional target list.
     */
    public PowerStone(Combatant target, List<Combatant> allTargets) {
        this.target     = target;
        this.allTargets = allTargets;
    }

    @Override
    public void use(Combatant user) {
        System.out.println("  [ITEM FX ] Power Stone: special skill triggers for free"
            + " (cooldown unchanged).");

        // Intentionally skip cooldown changes.
        if (user instanceof Warrior) {
            ((Warrior) user).shieldBash(target);

        } else if (user instanceof Wizard) {
            List<Combatant> targets = (allTargets != null && !allTargets.isEmpty())
                    ? allTargets
                    : java.util.Collections.singletonList(target);
            ((Wizard) user).arcaneBlast(target, targets);

        } else {
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
    }
}
