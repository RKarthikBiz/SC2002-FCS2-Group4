package com.combatarena.control;
 
import com.combatarena.domain.combatants.Combatant;
import java.util.List;
 
public interface TurnOrderStrategy {
 
    List<Combatant> determineTurnOrder(List<Combatant> participants);
}
 