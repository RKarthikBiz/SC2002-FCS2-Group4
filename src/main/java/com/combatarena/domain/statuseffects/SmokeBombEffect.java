package com.combatarena.domain.statuseffects;

import com.combatarena.domain.combatants.Combatant;

/**
 * SmokeBombEffect - grants evasion from enemy attacks for its duration.
 *
 * Mechanic: While this effect is active, the combatant blocks all incoming
 * enemy damage (sets it to 0). BattleEngine or Combatant.takeDamage() must
 * check for an active SmokeBombEffect and suppress incoming damage.
 */
public class SmokeBombEffect implements StatusEffect {
    private int duration;
    private final String name = "Smoke Bomb";

    public SmokeBombEffect(int duration) {
        this.duration = duration;
    }

    @Override
    public void apply(Combatant target) {
        // System.out.println(target.getName() + " is obscured by a Smoke Bomb! Enemy attacks will deal 0 damage.");
    }

    @Override
    public void tick() {
        if (duration > 0) duration--;
    }

    @Override
    public void remove(Combatant target) {
        // System.out.println("The smoke clears around " + target.getName() + ".");
    }

    @Override
    public boolean isExpired() { return duration <= 0; }
    @Override
    public int getDuration() { return duration; }
    @Override
    public String getName() { return name; }
}
