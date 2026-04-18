package com.combatarena.domain.statuseffects;

import com.combatarena.domain.combatants.Combatant;

/**
 * Temporary attack boost effect.
 */
public class ArcaneBlastBuff implements StatusEffect {
    private int duration;
    private int attackBoost;
    private final String name = "Arcane Blast Buff";

    public ArcaneBlastBuff(int duration, int attackBoost) {
        this.duration = duration;
        this.attackBoost = attackBoost;
    }

    @Override
    public void apply(Combatant target) {
        target.setAttack(target.getAttack() + attackBoost);
        System.out.println(target.getName() + " channels arcane energy! Attack increased by " + attackBoost + ".");
    }

    @Override
    public void tick() {
        if (duration > 0) duration--;
    }

    @Override
    public void remove(Combatant target) {
        target.setAttack(target.getAttack() - attackBoost);
        System.out.println(target.getName() + "'s arcane energy fades.");
    }

    @Override
    public boolean isExpired() { return duration <= 0; }
    @Override
    public int getDuration() { return duration; }
    @Override
    public String getName() { return name; }
}
