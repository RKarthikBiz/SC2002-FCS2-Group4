package com.combatarena.domain.level;

import com.combatarena.domain.combatants.Goblin;
import com.combatarena.domain.combatants.Wolf;
import com.combatarena.domain.combatants.Enemy;
import com.combatarena.util.GameConstants;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Builds predefined levels for each difficulty.
 */
public class LevelFactory {

    // Utility class.
    private LevelFactory() {}

    /**
     * Creates a level from a difficulty name.
     */
    public static Level createLevel(String difficulty) {
        switch (difficulty.toLowerCase()) {
            case "easy":
                return createEasyLevel();
            case "medium":
                return createMediumLevel();
            case "hard":
                return createHardLevel();
            default:
                throw new IllegalArgumentException(
                        "Unknown difficulty: " + difficulty +
                        ". Valid options are: Easy, Medium, Hard.");
        }
    }

    /** Creates a goblin using configured constants. */
    private static Goblin newGoblin() {
        return new Goblin(
                "Goblin",
                GameConstants.GOBLIN_HP,
                GameConstants.GOBLIN_ATK,
                GameConstants.GOBLIN_DEF,
                GameConstants.GOBLIN_SPD
        );
    }

    /** Creates a wolf using configured constants. */
    private static Wolf newWolf() {
        return new Wolf(
                "Wolf",
                GameConstants.WOLF_HP,
                GameConstants.WOLF_ATK,
                GameConstants.WOLF_DEF,
                GameConstants.WOLF_SPD
        );
    }

    /**
        * Easy level setup.
     */
    private static Level createEasyLevel() {
        List<Enemy> initial = Arrays.asList(
                newGoblin(),
                newGoblin(),
                newGoblin()
        );
        List<Enemy> backup = Collections.emptyList();
        return new Level("Easy", initial, backup);
    }

    /**
        * Medium level setup.
     */
    private static Level createMediumLevel() {
        List<Enemy> initial = Arrays.asList(
                newGoblin(),
                newWolf()
        );
        List<Enemy> backup = Arrays.asList(
                newWolf(),
                newWolf()
        );
        return new Level("Medium", initial, backup);
    }

    /**
        * Hard level setup.
     */
    private static Level createHardLevel() {
        List<Enemy> initial = Arrays.asList(
                newGoblin(),
                newGoblin()
        );
        List<Enemy> backup = Arrays.asList(
                newGoblin(),
                newWolf(),
                newWolf()
        );
        return new Level("Hard", initial, backup);
    }
}