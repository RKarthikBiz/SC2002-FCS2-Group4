package com.combatarena.domain.statuseffects;

import com.combatarena.domain.combatants.Combatant;

public class StunEffect implements StatusEffect {
    private int duration;
    private final String name = "Stun";

    public StunEffect(int duration) {
        this.duration = duration;
    }

    @Override
    public void apply(Combatant target) {
        System.out.println("  [STATUS] " + target.getName() + " is stunned.");
    }

    @Override
    public void tick() {
        if (duration > 0) duration--;
    }

    @Override
    public void remove(Combatant target) {
        System.out.println("  [STATUS] " + target.getName() + " recovered from stun.");
    }

    @Override
    public boolean isExpired() { return duration <= 0; }
    @Override
    public int getDuration() { return duration; }
    @Override
    public String getName() { return name; }
}
