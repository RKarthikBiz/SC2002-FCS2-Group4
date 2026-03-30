package main.java.com.combatarena.domain.level;

import main.java.com.combatarena.domain.combatants.Goblin;
import main.java.com.combatarena.domain.combatants.Wolf;
import main.java.com.combatarena.domain.combatants.Enemy;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Factory class responsible for constructing pre-configured Level objects.
 *
 * Centralises all level-composition knowledge in one place (SRP).
 * Adding a new difficulty only requires a new case here — no other
 * class needs to change (OCP).
 */
public class LevelFactory {

    // Private constructor — this class is never instantiated, only called statically.
    private LevelFactory() {}

    /**
     * Creates and returns a Level based on the requested difficulty.
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

    // -------------------------------------------------------------------------
    // Private level builders
    // -------------------------------------------------------------------------

    /**
     * Easy: 3 Goblins, no backup wave.
     */
    private static Level createEasyLevel() {
        List<Enemy> initial = Arrays.asList(
                new Goblin(),
                new Goblin(),
                new Goblin()
        );
        List<Enemy> backup = Collections.emptyList();
        return new Level("Easy", initial, backup);
    }

    /**
     * Medium: 1 Goblin + 1 Wolf initially, backup wave of 2 Wolves.
     */
    private static Level createMediumLevel() {
        List<Enemy> initial = Arrays.asList(
                new Goblin(),
                new Wolf()
        );
        List<Enemy> backup = Arrays.asList(
                new Wolf(),
                new Wolf()
        );
        return new Level("Medium", initial, backup);
    }

    /**
     * Hard: 2 Goblins initially, backup wave of 1 Goblin + 2 Wolves.
     */
    private static Level createHardLevel() {
        List<Enemy> initial = Arrays.asList(
                new Goblin(),
                new Goblin()
        );
        List<Enemy> backup = Arrays.asList(
                new Goblin(),
                new Wolf(),
                new Wolf()
        );
        return new Level("Hard", initial, backup);
    }
}