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
     *
     * @param difficulty "Easy", "Medium", or "Hard" (case-insensitive)
     * @return a fully configured Level instance
     * @throws IllegalArgumentException if an unrecognised difficulty is passed
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
    // Private helper: creates a fresh Goblin using stats from GameConstants
    // -------------------------------------------------------------------------
    private static Goblin newGoblin() {
        return new Goblin(
                "Goblin",
                GameConstants.GOBLIN_HP,
                GameConstants.GOBLIN_ATK,
                GameConstants.GOBLIN_DEF,
                GameConstants.GOBLIN_SPD
        );
    }

    // -------------------------------------------------------------------------
    // Private helper: creates a fresh Wolf using stats from GameConstants
    // -------------------------------------------------------------------------
    private static Wolf newWolf() {
        return new Wolf(
                "Wolf",
                GameConstants.WOLF_HP,
                GameConstants.WOLF_ATK,
                GameConstants.WOLF_DEF,
                GameConstants.WOLF_SPD
        );
    }

    // -------------------------------------------------------------------------
    // Private level builders
    // -------------------------------------------------------------------------

    /**
     * Easy: 3 Goblins, no backup wave.
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
     * Medium: 1 Goblin + 1 Wolf initially, backup wave of 2 Wolves.
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
     * Hard: 2 Goblins initially, backup wave of 1 Goblin + 2 Wolves.
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