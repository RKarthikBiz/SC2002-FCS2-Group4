package com.combatarena.domain.statuseffects;

import com.combatarena.domain.combatants.Combatant;

public class SmokeBombEffect implements StatusEffect {
    private int duration;
    private int evasionBoost;
    private final String name = "Smoke Bomb";

    public SmokeBombEffect(int duration, int evasionBoost) {
        this.duration = duration;
        this.evasionBoost = evasionBoost;
    }

    @Override
    public void applyEffect(Combatant target) {
        target.setEvasion(target.getEvasion() + evasionBoost);
        System.out.println(target.getName() + " is obscured by a Smoke Bomb! Evasion increased.");
    }

    @Override
    public void tick(Combatant target) {
        if (duration > 0) duration--;
    }

    @Override
    public void removeEffect(Combatant target) {
        target.setEvasion(target.getEvasion() - evasionBoost);
        System.out.println("The smoke clears around " + target.getName() + ".");
    }

    @Override
    public boolean isExpired() { return duration <= 0; }
    @Override
    public int getDuration() { return duration; }
    @Override
    public String getName() { return name; }
}