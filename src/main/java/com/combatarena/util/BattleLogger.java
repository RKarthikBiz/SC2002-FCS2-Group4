package com.combatarena.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * BattleLogger - records a running log of battle events with turn tracking.
 *
 * XMI-derived members:
 *   Fields    : log : List<String>  (private)
 *               turnNumber : int    (private)
 *   Methods   : record(entry : String) : void
 *               printLog() : void
 *               incrementTurn() : void
 *               getTurnNumber() : int
 *
 * Usage pattern (BattleEngine):
 * <pre>
 *   BattleLogger logger = new BattleLogger();
 *   logger.incrementTurn();
 *   logger.record(attacker.getName() + " attacks " + target.getName());
 *   logger.printLog();
 * </pre>
 */
public class BattleLogger {

    // -------------------------------------------------------------------------
    // Fields (from XMI)
    // -------------------------------------------------------------------------

    /** Ordered list of all logged battle events. */
    private final List<String> log;

    /** Current turn number; incremented by incrementTurn(). */
    private int turnNumber;

    /** Tracks the most recent turn header already written to the log. */
    private int lastLoggedTurn;

    // -------------------------------------------------------------------------
    // Constructor
    // -------------------------------------------------------------------------

    public BattleLogger() {
        this.log        = new ArrayList<>();
        this.turnNumber = 0;
        this.lastLoggedTurn = -1;
    }

    // -------------------------------------------------------------------------
    // Methods (from XMI)
    // -------------------------------------------------------------------------

    /**
     * Appends a new event entry to the log.
     * Turn headers are normally written by incrementTurn().
     *
     * @param entry the event description to record
     */
    public void record(String entry) {
        // Defensive fallback in case record() is called without incrementTurn().
        if (turnNumber != lastLoggedTurn) {
            String turnHeader = "Turn " + turnNumber;
            log.add(turnHeader);
            lastLoggedTurn = turnNumber;
        }

        log.add(entry);
    }

    /**
     * Prints every entry in the log to standard output.
     * Useful for displaying a full battle recap after the fight ends.
     */
    public void printLog() {
        System.out.println();
        System.out.println("============================================================");
        System.out.println("                      BATTLE LOG RECAP");
        System.out.println("============================================================");
        for (String entry : log) {
            if (entry != null && entry.startsWith("Turn ")) {
                String turnValue = entry.substring("Turn ".length()).trim();
                System.out.println("[TURN " + turnValue + "]");
            } else {
                System.out.println("  - " + (entry == null ? "" : entry.trim()));
            }
        }
        System.out.println("============================================================");
        System.out.println();
    }

    /**
     * Advances the turn counter by 1.
     * Should be called by BattleEngine at the start of each new round.
     */
    public void incrementTurn() {
        turnNumber++;
        String turnHeader = "Turn " + turnNumber;
        log.add(turnHeader);
        System.out.println();
        System.out.println("-------------------- " + turnHeader + " --------------------");
        lastLoggedTurn = turnNumber;
    }

    /**
     * Returns the current turn number.
     *
     * @return current turn number (0 before the first incrementTurn() call)
     */
    public int getTurnNumber() {
        return turnNumber;
    }

    // -------------------------------------------------------------------------
    // Convenience helpers (not in UML, but support common BattleEngine needs)
    // -------------------------------------------------------------------------

    /**
     * Returns an unmodifiable view of the full log for external inspection.
     *
     * @return read-only list of all recorded entries
     */
    public List<String> getLog() {
        return Collections.unmodifiableList(log);
    }

    /**
     * Clears the log and resets the turn counter.
     * Use between levels so each level starts with a fresh log.
     */
    public void reset() {
        log.clear();
        turnNumber = 0;
        lastLoggedTurn = -1;
    }
}
