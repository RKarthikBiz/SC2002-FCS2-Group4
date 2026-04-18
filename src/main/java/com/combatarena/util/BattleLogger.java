package com.combatarena.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * BattleLogger - records a running log of battle events with turn tracking.
 */
public class BattleLogger {

    private final List<String> log;

    private int turnNumber;

    private int lastLoggedTurn;



    public BattleLogger() {
        this.log        = new ArrayList<>();
        this.turnNumber = 0;
        this.lastLoggedTurn = -1;
    }

    public void record(String entry) {
        // Defensive fallback in case record() is called without incrementTurn().
        if (turnNumber != lastLoggedTurn) {
            String turnHeader = "Turn " + turnNumber;
            log.add(turnHeader);
            lastLoggedTurn = turnNumber;
        }

        log.add(entry);
    }

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

    public void incrementTurn() {
        turnNumber++;
        String turnHeader = "Turn " + turnNumber;
        log.add(turnHeader);
        System.out.println();
        System.out.println("-------------------- " + turnHeader + " --------------------");
        lastLoggedTurn = turnNumber;
    }

    public int getTurnNumber() {
        return turnNumber;
    }

    public List<String> getLog() {
        return Collections.unmodifiableList(log);
    }

    public void reset() {
        log.clear();
        turnNumber = 0;
        lastLoggedTurn = -1;
    }
}
