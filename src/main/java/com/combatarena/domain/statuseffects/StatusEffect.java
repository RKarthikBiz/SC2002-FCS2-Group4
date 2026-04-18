package com.combatarena.domain.statuseffects;

import com.combatarena.domain.combatants.Combatant;

/**
 * Common contract for turn-based status effects.
 */
public interface StatusEffect {
    void apply(Combatant target);
    void tick();
    void remove(Combatant target);
    boolean isExpired();
    int getDuration();
    String getName();
}
