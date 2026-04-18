package com.combatarena.domain.combatants;

import com.combatarena.util.GameConstants;
import java.util.Collections;
import java.util.List;

public class Wizard extends Player {
    private int attackBonus;

    /**
     * Creates a wizard.
     */
    public Wizard(String name, int hp, int attack, int defense, int speed) {
        super(name, hp, attack, defense, speed);
        this.attackBonus = 0;
    }

    /**
     * Single-target helper for Arcane Blast.
     */
    public void arcaneBlast(Combatant target) {
        arcaneBlast(target, target == null ? Collections.emptyList() : Collections.singletonList(target));
    }

    /**
     * Arcane Blast hits all active enemies in the provided target list.
     * Each enemy eliminated by this blast grants a permanent +ATK bonus.
     */
    public void arcaneBlast(Combatant target, List<Combatant> allTargets) {
        if (allTargets == null || allTargets.isEmpty()) {
            return;
        }

        for (Combatant enemy : allTargets) {
            if (enemy == null || !enemy.isAlive()) {
                continue;
            }

            int damage = Math.max(0, getAttack() + attackBonus - enemy.getDefense());
            enemy.takeDamage(damage);
                System.out.println("  [SKILL ] " + getName() + " -> " + enemy.getName()
                    + " | Arcane Blast | " + damage + " dmg");

            if (!enemy.isAlive()) {
                arcaneBlastBonus(GameConstants.ARCANE_BLAST_ATK_BONUS);
            }
        }

        setSpecialSkillCooldown(GameConstants.SPECIAL_SKILL_COOLDOWN);
    }

    /**
     * Public helper that forwards to arcaneBlastBonus.
     */
    public void addAttackBonus(int amount) {
        arcaneBlastBonus(amount);
    }

    /**
     * Increases attack for the rest of the level when Arcane Blast secures a kill.
     */
    public void arcaneBlastBonus(int amount) {
        int gain = Math.max(0, amount);
        this.attackBonus += gain;
        setAttack(getAttack() + gain);
        System.out.println("  [STATUS] " + getName() + " Arcane Bonus +" + gain
                + " (total: " + attackBonus + ")");
    }

    /**
     * Resets accumulated Arcane Blast bonus.
     */
    public void resetAttackBonus() {
        setAttack(Math.max(0, getAttack() - attackBonus));
        this.attackBonus = 0;
    }

    /**
     * Current attack bonus from Arcane Blast kills.
     */
    public int getAttackBonus() {
        return attackBonus;
    }

    /**
     * Replaces the current bonus and keeps attack in sync.
     */
    public void setAttackBonus(int bonus) {
        int clamped = Math.max(0, bonus);
        int delta = clamped - this.attackBonus;
        setAttack(Math.max(0, getAttack() + delta));
        this.attackBonus = clamped;
    }

    /**
     * Default wizard turn behavior.
     */
    @Override
    public void performTurn() {
        decrementCooldown();
        System.out.println(getName() + " channels magical energy. (Arcane Bonus: " + attackBonus + ")");
    }

    @Override
    public String toString() {
        return "Wizard: " + super.toString() + " (Arcane Bonus: " + attackBonus + ")";
    }
}
