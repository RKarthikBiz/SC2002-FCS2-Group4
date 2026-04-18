package com.combatarena.domain.level;

import com.combatarena.domain.combatants.Enemy;
import java.util.ArrayList;
import java.util.List;

/**
 * Holds enemy waves and metadata for one level.
 */
public class Level {

    private final String difficulty;
    private final List<Enemy> initialEnemies;
    private final List<Enemy> backupEnemies;

    /** True once the first wave is fully defeated. */
    private boolean initialCleared;

    /**
     * Creates a level with initial and backup waves.
     */
    public Level(String difficulty, List<Enemy> initialEnemies, List<Enemy> backupEnemies) {
        this.difficulty     = difficulty;
        this.initialEnemies = new ArrayList<>(initialEnemies);
        this.backupEnemies  = new ArrayList<>(backupEnemies);
        this.initialCleared = false;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public List<Enemy> getInitialEnemies() {
        return new ArrayList<>(initialEnemies);
    }

    public List<Enemy> getBackupEnemies() {
        return new ArrayList<>(backupEnemies);
    }

    /**
     * Returns whether the first wave is cleared.
     */
    public boolean isInitialCleared() {
        return initialCleared;
    }

    /**
     * Marks the first wave as cleared.
     */
    public void markInitialCleared() {
        this.initialCleared = true;
    }

    /**
     * Returns a copy of the backup wave.
     */
    public List<Enemy> spawnBackupEnemies() {
        return new ArrayList<>(backupEnemies);
    }

    /**
     * Returns true if a backup wave exists.
     */
    public boolean hasBackup() {
        return !backupEnemies.isEmpty();
    }

    /**
     * Maps difficulty text to a level number.
     */
    public int getLevelNo() {
        return switch (difficulty.toLowerCase()) {
            case "easy"   -> 1;
            case "medium" -> 2;
            case "hard"   -> 3;
            default       -> 1;
        };
    }

    @Override
    public String toString() {
        return String.format("Level[difficulty=%s, initial=%d enemies, backup=%d enemies]",
                difficulty, initialEnemies.size(), backupEnemies.size());
    }
}