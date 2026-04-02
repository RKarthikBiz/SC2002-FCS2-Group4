package com.combatarena.control;

import com.combatarena.domain.combatants.Combatant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Concrete implementation of TurnOrderStrategy.
 * Sorts combatants by Speed descending. Highest Speed goes first.
 * Ties are broken by name alphabetically for deterministic ordering.
 */
public class SpeedBasedTurnOrder implements TurnOrderStrategy {

    /**
     * Returns participants sorted by Speed (descending).
     * Dead combatants (hp <= 0) are excluded from the turn order.
     */
    @Override
    public List<Combatant> determineTurnOrder(List<Combatant> participants) {
        List<Combatant> ordered = new ArrayList<>();

        for (Combatant c : participants) {
            if (c.isAlive()) {
                ordered.add(c);
            }
        }

        // Sort by speed descending; break ties alphabetically by name
        ordered.sort(Comparator
                .comparingInt(Combatant::getSpeed).reversed()
                .thenComparing(Combatant::getName));

        return ordered;
    }
}   