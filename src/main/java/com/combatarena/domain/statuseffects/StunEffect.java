package com.combatarena.domain.statuseffects;

import com.combatarena.domain.combatants.Combatant;

public abstract class StunEffect implements StatusEffect {
    private int duration;
    private final String name = "Stun";

    public StunEffect(int duration) {
        this.duration = duration;
    }

    @Override
    public void applyEffect(Combatant target) {
        target.setCanMove(false);
        System.out.println(target.getName() + " is stunned and cannot act!");
    }

    @Override
    public void tick(Combatant target) {
        if (duration > 0) duration--;
    }

    @Override
    public void removeEffect(Combatant target) {
        target.setCanMove(true);
        System.out.println(target.getName() + " is no longer stunned.");
    }

    @Override
    public boolean isExpired() { return duration <= 0; }
    @Override
    public int getDuration() { return duration; }
    @Override
    public String getName() { return name; }
}