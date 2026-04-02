package com.combatarena.control;
 
import com.combatarena.domain.combatants.Combatant;
import java.util.List;
 
/**
 * Interface for turn order strategies (Strategy Pattern).
 * BattleEngine depends on this abstraction, not on concrete implementations (DIP).
 */
public interface TurnOrderStrategy {
 
    /**
     * Determines the turn order of combatants for a given round.
     */
    List<Combatant> determineTurnOrder(List<Combatant> participants);
}
 