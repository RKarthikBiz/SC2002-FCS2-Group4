package com.combatarena.domain.actions;

import com.combatarena.domain.combatants.Combatant;


/**
 * Action interface - represents any action a Combatant can perform on their turn.
 */
public interface Action {
    /**
     * Executes the action.
     *
     * @param attacker the Combatant performing the action
     * @param target   the Combatant receiving the action
     */
    void execute(Combatant attacker, Combatant target);
}
