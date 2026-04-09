package com.combatarena.boundary;

import com.combatarena.control.BattleEngine;
import com.combatarena.domain.actions.Action;
import com.combatarena.domain.actions.BasicAttack;
import com.combatarena.domain.actions.SpecialSkill;
import com.combatarena.domain.actions.UseItem;
import com.combatarena.domain.combatants.Enemy;
import com.combatarena.domain.combatants.Player;
import com.combatarena.domain.items.Item;

import java.util.List;
import java.util.Scanner;

public class GameCLI {

    private static final String LINE = "============================================================";
    private static final String SUB_LINE = "------------------------------------------------------------";

    private BattleEngine battleEngine;
    private Scanner scanner;

    public GameCLI() {
        this.scanner = new Scanner(System.in);
    }

    /**
     * Dependency Injection: Links the engine to the CLI so the CLI
     * can fetch player and enemy stats for display.
     */
    public void setBattleEngine(BattleEngine battleEngine) {
        this.battleEngine = battleEngine;
    }

    /**
     * Kicks off the game loop inside the BattleEngine.
     */
    public void start() {
        System.out.println();
        System.out.println(LINE);
        System.out.println("                     BATTLE START");
        System.out.println(LINE);

        if (battleEngine != null) {
            battleEngine.runBattle();
        } else {
            System.out.println("Error: Battle Engine not linked to CLI!");
        }
    }

    /**
     * Displays the current HP and status of the player and active enemies.
     */
    public void displayBattleState() {
        if (battleEngine == null) return;

        Player player = battleEngine.getPlayer();
        List<Enemy> enemies = battleEngine.getActiveEnemies();

        System.out.println();
        System.out.println(LINE);
        System.out.println("                   CURRENT BATTLE STATE");
        System.out.println(LINE);

        // Display player
        System.out.printf("Player : %s%n", player.getName());
        System.out.printf("HP     : %d/%d%n", player.getHp(), player.getMaxHp());

        // Display inventory
        List<Item> inventory = player.getInventory();
        if (!inventory.isEmpty()) {
            System.out.print("Items  : ");
            for (int i = 0; i < inventory.size(); i++) {
                Item item = inventory.get(i);
                System.out.print((i > 0 ? ", " : "") + item.getClass().getSimpleName());
            }
            System.out.println();
        } else {
            System.out.println("Items  : none");
        }

        System.out.println(SUB_LINE);

        // Display enemies
        System.out.println("Enemies:");
        int index = 1;
        for (Enemy e : enemies) {
            if (e.isAlive()) {
                System.out.printf("  %d) %-22s HP: %d%n", index, e.getName(), e.getHp());
            } else {
                System.out.printf("  %d) %-22s DEFEATED%n", index, e.getName());
            }
            index++;
        }
        System.out.println(LINE);
    }

    /**
     * Prompts the player to choose an action and returns an implementation of the Action interface.
     */
    public Action getPlayerAction() {
        Player player = battleEngine.getPlayer();

        System.out.println();
        System.out.println(SUB_LINE);
        System.out.println("Choose your next action:");
        System.out.println("  1) Basic Attack");
        System.out.println("  2) Special Skill"
            + (SpecialSkill.isReady(player)
            ? " [Ready]"
            : " [Cooldown: " + SpecialSkill.getCooldown(player) + "]"));
        System.out.println("  3) Use Item" + (player.getInventory().isEmpty() ? " [No items]" : ""));
        System.out.println("  4) Defend");
        System.out.println(SUB_LINE);

        while (true) {
            System.out.print("Select action [1-4]: ");
            String input = scanner.nextLine();

            switch (input) {
                case "1":
                    return new BasicAttack();
                case "2":
                    if (SpecialSkill.isReady(player)) {
                        List<Enemy> enemies = battleEngine.getActiveEnemies();
                        java.util.List<com.combatarena.domain.combatants.Combatant> allTargets =
                                new java.util.ArrayList<>(enemies);
                        return new SpecialSkill(allTargets);
                    } else {
                        System.out.println("Special skill is on cooldown. Try another action.");
                    }
                    break;
                case "3":
                    List<Item> inventory = player.getInventory();
                    if (inventory.isEmpty()) {
                        System.out.println("No items in inventory. Try another action.");
                        break;
                    }
                    System.out.println();
                    System.out.println(SUB_LINE);
                    System.out.println("Select an item to use:");
                    for (int i = 0; i < inventory.size(); i++) {
                        System.out.printf("  %d) %-22s%n", (i + 1), inventory.get(i).getClass().getSimpleName());
                    }
                    System.out.println(SUB_LINE);
                    System.out.print("Select item [1-" + inventory.size() + "]: ");
                    String itemInput = scanner.nextLine();
                    try {
                        int itemIndex = Integer.parseInt(itemInput) - 1;
                        if (itemIndex >= 0 && itemIndex < inventory.size()) {
                            Item selectedItem = inventory.get(itemIndex);
                            return new UseItem(selectedItem);
                        } else {
                            System.out.println("Invalid item choice. Please try again.");
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid input. Please enter a number.");
                    }
                    break;
                case "4":
                    return new com.combatarena.domain.actions.Defend();
                default:
                    System.out.println("Invalid choice. Please enter a number between 1 and 4.");
            }
        }
    }

    public void displayVictory() {
        System.out.println();
        System.out.println(LINE);
        System.out.println("           VICTORY! All enemies defeated.");
        System.out.println(LINE);
    }

    public void displayDefeat() {
        System.out.println();
        System.out.println(LINE);
        System.out.println("            DEFEAT... You have fallen.");
        System.out.println(LINE);
    }

    public void closeScanner() {
        if (scanner != null) {
            scanner.close();
        }
    }
}
