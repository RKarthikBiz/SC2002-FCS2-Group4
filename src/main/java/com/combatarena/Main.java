import com.combatarena.boundary.GameCLI;
import com.combatarena.control.BattleEngine;
import com.combatarena.control.SpeedBasedTurnOrder;
import com.combatarena.domain.actions.SpecialSkill;
import com.combatarena.domain.combatants.Combatant;
import com.combatarena.domain.combatants.Enemy;
import com.combatarena.domain.combatants.Player;
import com.combatarena.domain.combatants.Warrior;
import com.combatarena.domain.combatants.Wizard;
import com.combatarena.domain.items.Potion;
import com.combatarena.domain.items.PowerStone;
import com.combatarena.domain.items.SmokeBomb;
import com.combatarena.domain.level.Level;
import com.combatarena.domain.level.LevelFactory;
import com.combatarena.util.GameConstants;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * Main entry point for the Combat Arena application.
 */
public class Main {
    public static void main(String[] args) {
        // Reset static cooldown state so each new run starts clean.
        SpecialSkill.resetAll();

        try (Scanner setupScanner = new Scanner(System.in)) {
            printLoadingScreen();
            Player player = choosePlayer(setupScanner);
            int difficultyChoice = chooseDifficulty(setupScanner);
            Level level = createLevelFromChoice(difficultyChoice);
            chooseTwoStartingItems(setupScanner, player, level);

            System.out.println("\n=== Starting Battle ===");
            System.out.println("Player: " + player.getName());
            System.out.println("Difficulty: " + level.getDifficulty());
            System.out.println("Items chosen: " + inventoryAsString(player));

            GameCLI cli = new GameCLI();
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

    private static void printLoadingScreen() {
        Level easyLevel = LevelFactory.createLevel("Easy");
        Level mediumLevel = LevelFactory.createLevel("Medium");
        Level hardLevel = LevelFactory.createLevel("Hard");

        System.out.println("=========================================");
        System.out.println("      SC2002 TURN-BASED COMBAT ARENA    ");
        System.out.println("=========================================");
        System.out.println("\nPlayers:");
        System.out.println("1. Warrior - HP: " + GameConstants.WARRIOR_HP
            + ", ATK: " + GameConstants.WARRIOR_ATK
            + ", DEF: " + GameConstants.WARRIOR_DEF
            + ", SPD: " + GameConstants.WARRIOR_SPD + " | Skill: Shield Bash");
        System.out.println("2. Wizard  - HP: " + GameConstants.WIZARD_HP
            + ", ATK: " + GameConstants.WIZARD_ATK
            + ", DEF: " + GameConstants.WIZARD_DEF
            + ", SPD: " + GameConstants.WIZARD_SPD + " | Skill: Arcane Blast");

        System.out.println("\nItems (choose 2, duplicates allowed):");
        System.out.println("1. Potion      - Heal 100 HP");
        System.out.println("2. Power Stone - Free extra use of special skill (no cooldown change)");
        System.out.println("3. Smoke Bomb  - Enemy damage is 0 for current + next turn");

        System.out.println("\nEnemies:");
        System.out.println("- Goblin - HP: " + GameConstants.GOBLIN_HP
            + ", ATK: " + GameConstants.GOBLIN_ATK
            + ", DEF: " + GameConstants.GOBLIN_DEF
            + ", SPD: " + GameConstants.GOBLIN_SPD);
        System.out.println("- Wolf   - HP: " + GameConstants.WOLF_HP
            + ", ATK: " + GameConstants.WOLF_ATK
            + ", DEF: " + GameConstants.WOLF_DEF
            + ", SPD: " + GameConstants.WOLF_SPD);

        System.out.println("\nDifficulties:");
        System.out.println("1. Easy   - Initial: " + formatWave(easyLevel.getInitialEnemies())
            + " | Backup: " + formatWave(easyLevel.getBackupEnemies()));
        System.out.println("2. Medium - Initial: " + formatWave(mediumLevel.getInitialEnemies())
            + " | Backup: " + formatWave(mediumLevel.getBackupEnemies()));
        System.out.println("3. Hard   - Initial: " + formatWave(hardLevel.getInitialEnemies())
            + " | Backup: " + formatWave(hardLevel.getBackupEnemies()));
    }

    private static Player choosePlayer(Scanner scanner) {
        System.out.println("\nChoose player class:");
        System.out.println("1. Warrior");
        System.out.println("2. Wizard");
        int choice = readChoice(scanner, 1, 2);

        if (choice == 2) {
            return new Wizard(
                "Wizard",
                GameConstants.WIZARD_HP,
                GameConstants.WIZARD_ATK,
                GameConstants.WIZARD_DEF,
                GameConstants.WIZARD_SPD
            );
        }
        return new Warrior(
            "Warrior",
            GameConstants.WARRIOR_HP,
            GameConstants.WARRIOR_ATK,
            GameConstants.WARRIOR_DEF,
            GameConstants.WARRIOR_SPD
        );
    }

    private static int chooseDifficulty(Scanner scanner) {
        System.out.println("\nChoose difficulty:");
        System.out.println("1. Easy");
        System.out.println("2. Medium");
        System.out.println("3. Hard");
        return readChoice(scanner, 1, 3);
    }

    private static Level createLevelFromChoice(int difficultyChoice) {
        String difficulty = switch (difficultyChoice) {
            case 1 -> "Easy";
            case 2 -> "Medium";
            default -> "Hard";
        };
        return LevelFactory.createLevel(difficulty);
    }

    private static void chooseTwoStartingItems(Scanner scanner, Player player, Level level) {
        for (int slot = 1; slot <= 2; slot++) {
            System.out.println("\nChoose item " + slot + " of 2:");
            System.out.println("1. Potion");
            System.out.println("2. Power Stone");
            System.out.println("3. Smoke Bomb");
            int itemChoice = readChoice(scanner, 1, 3);

            switch (itemChoice) {
                case 1 -> player.addItem(new Potion());
                case 2 -> player.addItem(createInitialWavePowerStone(level));
                default -> player.addItem(new SmokeBomb());
            }
        }
    }

    private static int readChoice(Scanner scanner, int min, int max) {
        while (true) {
            System.out.print("Enter choice (" + min + "-" + max + "): ");
            String input = scanner.nextLine().trim();
            try {
                int choice = Integer.parseInt(input);
                if (choice >= min && choice <= max) {
                    return choice;
                }
            } catch (NumberFormatException ignored) {
                // Keep prompting until a valid number is entered.
            }
            System.out.println("Invalid choice. Please try again.");
        }
    }

    private static String inventoryAsString(Player player) {
        List<String> names = new ArrayList<>();
        for (Object item : player.getInventory()) {
            names.add(item.getClass().getSimpleName());
        }
        return String.join(", ", names);
    }

    private static String formatWave(List<Enemy> enemies) {
        if (enemies == null || enemies.isEmpty()) {
            return "none";
        }

        Map<String, Integer> counts = new LinkedHashMap<>();
        for (Enemy enemy : enemies) {
            String key = enemy.getClass().getSimpleName();
            counts.put(key, counts.getOrDefault(key, 0) + 1);
        }

        List<String> parts = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : counts.entrySet()) {
            parts.add(entry.getValue() + " " + entry.getKey() + (entry.getValue() > 1 ? "s" : ""));
        }
        return String.join(" + ", parts);
    }

    private static PowerStone createInitialWavePowerStone(Level level) {
        List<Enemy> initialEnemies = level.getInitialEnemies();
        List<Combatant> allTargets = new ArrayList<>();
        Combatant primaryTarget = null;

        for (Enemy enemy : initialEnemies) {
            if (enemy == null) {
                continue;
            }
            allTargets.add(enemy);
            if (primaryTarget == null && enemy.isAlive()) {
                primaryTarget = enemy;
            }
        }

        return new PowerStone(primaryTarget, allTargets);
    }
}
