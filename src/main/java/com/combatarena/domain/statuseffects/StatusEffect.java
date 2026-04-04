package main.java.com.combatarena.domain.statuseffects;

import com.combatarena.domain.combatants.Combatant;

public interface StatusEffect {
    void applyEffect(Combatant target);
    void tick(Combatant target);
    void removeEffect(Combatant target);
    boolean isExpired();
    int getDuration();
    String getName();
}