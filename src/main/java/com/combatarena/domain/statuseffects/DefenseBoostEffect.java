package com.combatarena.domain.statuseffects;

import com.combatarena.domain.combatants.Combatant;

public class DefenseBoostEffect implements StatusEffect {
    private int duration;
    private int defenseBoost;
    private final String name = "Defense Boost";

    public DefenseBoostEffect(int duration, int defenseBoost) {
        this.duration = duration;
        this.defenseBoost = defenseBoost;
    }

    @Override
    public void apply(Combatant target) {
        target.setDefense(target.getDefense() + defenseBoost);
        System.out.println(target.getName() + " raises their guard! Defense increased by " + defenseBoost + ".");
    }

    @Override
    public void tick() {
        if (duration > 0) duration--;
    }

    @Override
    public void remove(Combatant target) {
        target.setDefense(target.getDefense() - defenseBoost);
        System.out.println(target.getName() + "'s defense returns to normal.");
    }

    @Override
    public boolean isExpired() { return duration <= 0; }
    @Override
    public int getDuration() { return duration; }
    @Override
    public String getName() { return name; }
}
