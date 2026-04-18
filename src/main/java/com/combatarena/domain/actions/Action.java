package com.combatarena.domain.actions;

import com.combatarena.domain.combatants.Combatant;


/**
 * Represents one action a combatant can take during a turn.
 */
public interface Action {
    /**
     * Runs the action.
     * attacker is the unit acting, and target is the unit being acted on.
     */
    void execute(Combatant attacker, Combatant target);
}
