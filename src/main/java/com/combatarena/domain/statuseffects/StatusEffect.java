package com.combatarena.domain.statuseffects;

import com.combatarena.domain.combatants.Combatant;

public interface StatusEffect {
    void apply(Combatant target);
    void tick();
    void remove(Combatant target);
    boolean isExpired();
    int getDuration();
    String getName();
}
