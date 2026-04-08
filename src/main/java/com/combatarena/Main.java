package com.combatarena;

import com.combatarena.boundary.GameCLI;
import com.combatarena.control.BattleEngine;
import com.combatarena.control.SpeedBasedTurnOrder;
import com.combatarena.domain.combatants.Warrior;
import com.combatarena.domain.combatants.Wizard;
import com.combatarena.domain.combatants.Player;
import com.combatarena.domain.level.Level;
import com.combatarena.domain.level.LevelFactory;
import com.combatarena.util.GameConstants;

/**
 * Main entry point for the Combat Arena application.
 */
public class Main {
    public static void main(String[] args) {
        GameCLI cli = new GameCLI();

        Level level = LevelFactory.createLevel("Medium");

        Player player = new Warrior(
                "Hero",
                GameConstants.WARRIOR_HP,
                GameConstants.WARRIOR_ATK,
                GameConstants.WARRIOR_DEF,
                GameConstants.WARRIOR_SPD
        );

        BattleEngine engine = new BattleEngine(
                new SpeedBasedTurnOrder(),
                level,
                player,
                cli
        );

        cli.setBattleEngine(engine);
        cli.start();
        cli.closeScanner();
    }
}
