package com.combatarena.domain.statuseffects;

import com.combatarena.domain.combatants.Combatant;

/**
 * Prevents incoming damage while active.
 */
public class SmokeBombEffect implements StatusEffect {
    private int duration;
    private final String name = "Smoke Bomb";

    public SmokeBombEffect(int duration) {
        this.duration = duration;
    }

    @Override
    public void apply(Combatant target) {
    }

    @Override
    public void tick() {
        if (duration > 0) duration--;
    }

    @Override
    public void remove(Combatant target) {
    }

    @Override
    public boolean isExpired() { return duration <= 0; }
    @Override
    public int getDuration() { return duration; }
    @Override
    public String getName() { return name; }
}
