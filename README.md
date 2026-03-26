# SC2002 Turn-Based Combat Arena

## Group Members
| Name | Matriculation Number |
|------|-----------|--------|
| Qaizar Shabbar Jambughodawala  | U2521184H       | 

## How to Run
1. Clone the repo
2. Navigate to src/main/java
3. Compile: javac com/combatarena/Main.java
4. Run: java com.combatarena.Main

## Project Structure
- boundary/   → CLI interface
- control/    → BattleEngine, TurnOrderStrategy
- domain/     → All game entities (combatants, actions, items, effects, levels)
- util/       → Constants

## Design Patterns Used
- Strategy Pattern: TurnOrderStrategy
- Factory Pattern: LevelFactory
