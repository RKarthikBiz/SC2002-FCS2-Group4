package com.combatarena.domain.actions;

import com.combatarena.domain.combatants.Combatant;
import com.combatarena.util.GameConstants;

/**
 * BasicAttack - performs a standard attack on a user-selected target.
 *
 * Damage formula : max(0, attacker.attack - target.defense)
 * Critical hits  : CRIT_CHANCE probability to deal CRIT_MULTIPLIER× damage
 * Target HP floor: 0  (HP cannot go below 0)
 */
public class BasicAttack implements Action {

    private final double CRIT_CHANCE = GameConstants.CRIT_CHANCE;
    private final double CRIT_MULTIPLIER = GameConstants.CRIT_MULTIPLIER;
    private boolean lastCritical;
    private boolean lastBlocked;
    private int lastActualDamage;

    @Override
    public void execute(Combatant attacker, Combatant target) {
        int damage = Math.max(0, attacker.getAttack() - target.getDefense());
        boolean isCrit = isCriticalHit();

        if (isCrit) {
            damage = (int) Math.ceil(damage * CRIT_MULTIPLIER);
        }

        int hpBefore = target.getHp();
        target.takeDamage(damage);
        int actualDamage = Math.max(0, hpBefore - target.getHp());

        this.lastCritical = isCrit;
        this.lastBlocked = damage > 0 && actualDamage == 0;
        this.lastActualDamage = actualDamage;

        String critText = isCrit ? " | CRITICAL" : "";
        String blockedText = lastBlocked ? " | BLOCKED" : "";
        System.out.println("  [ATK   ] " + attacker.getName() + " -> " + target.getName()
            + " | Basic Attack | " + actualDamage + " dmg" + critText + blockedText);
    }

    public boolean wasLastCritical() {
        return lastCritical;
    }

    public boolean wasLastBlocked() {
        return lastBlocked;
    }

    public int getLastActualDamage() {
        return lastActualDamage;
    }

    /**
     * Determines if this attack is a critical hit based on CRIT_CHANCE.
     *
     * @return true if the attack is a critical hit
     */
    private boolean isCriticalHit() {
        return Math.random() < CRIT_CHANCE;
    }
}
