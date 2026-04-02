package com.combatarena.domain.level;

import com.combatarena.domain.combatants.Enemy;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a battle level with a difficulty tier and two enemy waves.
 * The initial wave is the first group of enemies the player faces.
 * The backup wave spawns after the entire initial wave is wiped.
 */
public class Level {

    private final String difficulty;
    private final List<Enemy> initialEnemies;
    private final List<Enemy> backupEnemies;

    /** Tracks whether the initial wave has been fully cleared. */
    private boolean initialCleared;

    /**
     * Constructs a Level.
     */
    public Level(String difficulty, List<Enemy> initialEnemies, List<Enemy> backupEnemies) {
        this.difficulty     = difficulty;
        this.initialEnemies = new ArrayList<>(initialEnemies);
        this.backupEnemies  = new ArrayList<>(backupEnemies);
        this.initialCleared = false;
    }

    // -------------------------------------------------------------------------
    // Getters
    // -------------------------------------------------------------------------

    public String getDifficulty() {
        return difficulty;
    }

    public List<Enemy> getInitialEnemies() {
        return new ArrayList<>(initialEnemies);
    }

    public List<Enemy> getBackupEnemies() {
        return new ArrayList<>(backupEnemies);
    }

    // -------------------------------------------------------------------------
    // Wave management
    // -------------------------------------------------------------------------

    /**
     * Returns whether the initial enemy wave has been fully cleared.
     */
    public boolean isInitialCleared() {
        return initialCleared;
    }

    /**
     * Marks the initial wave as cleared. Should be called by BattleEngine
     * when all initial enemies are dead and the backup has not yet spawned.
     */
    public void markInitialCleared() {
        this.initialCleared = true;
    }

    /**
     * Provides the backup enemies for spawning.
     * Returns a fresh copy so BattleEngine can mutate its own active list.
     */
    public List<Enemy> spawnBackupEnemies() {
        return new ArrayList<>(backupEnemies);
    }

    /**
     * Returns true if there are backup enemies available to spawn.
     */
    public boolean hasBackup() {
        return !backupEnemies.isEmpty();
    }

    @Override
    public String toString() {
        return String.format("Level[difficulty=%s, initial=%d enemies, backup=%d enemies]",
                difficulty, initialEnemies.size(), backupEnemies.size());
    }
}