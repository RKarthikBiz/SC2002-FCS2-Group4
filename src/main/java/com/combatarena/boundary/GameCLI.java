package gameCLI;

import com.combatarena.control.BattleEngine;
import com.combatarena.domain.actions.Action;
// Import your concrete action classes here:
// import com.combatarena.domain.actions.BasicAttack; 
import com.combatarena.domain.combatants.Enemy;
import com.combatarena.domain.combatants.Player;

import java.util.List;
import java.util.Scanner;

public class GameCLI {
    
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
        System.out.println("=========================================");
        System.out.println("             BATTLE START!               ");
        System.out.println("=========================================");
        
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

        System.out.println("\n--- CURRENT BATTLE STATE ---");
        
        // Display Player
        System.out.printf("[PLAYER] %s - HP: %d\n", 
                player.getName(), player.getHp()); // Assuming getHp() exists
        
        // Display Enemies
        System.out.println("[ENEMIES]");
        for (Enemy e : enemies) {
            if (e.isAlive()) {
                System.out.printf("  - %s - HP: %d\n", e.getName(), e.getHp());
            } else {
                System.out.printf("  - %s - [DEFEATED]\n", e.getName());
            }
        }
        System.out.println("----------------------------\n");
    }

    /**
     * Prompts the player to choose an action and returns an implementation of the Action interface.
     */
    public Action getPlayerAction() {
        System.out.println("Choose your next action:");
        System.out.println("1. Basic Attack");
        System.out.println("2. Use Skill (Placeholder)");
        System.out.println("3. Use Item (Placeholder)");
        
        while (true) {
            System.out.print("Enter your choice (1-3): ");
            String input = scanner.nextLine();
            
            switch (input) {
                case "1":
                    // Return your concrete BasicAttack class that implements the Action interface.
                    // Replace the anonymous class below with: return new BasicAttack();
                    return new Action() {
                        @Override
                        public void execute(com.combatarena.domain.combatants.Combatant attacker, com.combatarena.domain.combatants.Combatant target) {
                            System.out.println(attacker.getName() + " attacks " + target.getName() + "!");
                            // attacker does damage to target...
                        }
                    };
                case "2":
                    System.out.println("Skills not yet implemented, defaulting to attack.");
                    return null; // Replace with: return new UseSkillAction();
                case "3":
                    System.out.println("Items not yet implemented, defaulting to attack.");
                    return null; // Replace with: return new UseItemAction();
                default:
                    System.out.println("Invalid choice. Please enter 1, 2, or 3.");
            }
        }
    }

    public void displayVictory() {
        System.out.println("\n=========================================");
        System.out.println("    VICTORY! All enemies defeated!       ");
        System.out.println("=========================================");
    }

    public void displayDefeat() {
        System.out.println("\n=========================================");
        System.out.println("    DEFEAT... You have fallen in battle. ");
        System.out.println("=========================================");
    }
    
    public void closeScanner() {
        if (scanner != null) {
            scanner.close();
        }
    }
}
