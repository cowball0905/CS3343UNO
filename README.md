UNO Game
Overview
The objective of developing the game is for the people who want to play UNO but they can not find friends to play with. The game ends when a player discards all cards in their hand. The players will take turns to play the game.
Installation

Game Components
Number Cards: The card’s number is between 0 to 9, with color ‘Red’ or ‘Blue’ or ‘Green’ or ‘Yellow’
Action Cards: Skip, Reverse and Draw Two in each color
Wild Cards: Wild and Wild Draw Four
Gameplay
Players take turns in a clockwise direction.
On your turn, you can play a card that matches the top card of the discard pile by either color or number.
If you don’t have a playable card, draw a card from the draw pile.
If the drawn card is playable, you may play it immediately; otherwise, your turn ends.
Special cards:
Skip: Next player loses their turn.
Reverse: Changes the direction of play.
Draw Two: The next player draws two cards and loses their turn.
Wild: Allows you to change the current color.You can choose from the four colors. 
Wild Draw Four: Allows you to change the current color and forces the next player to draw four cards (can only be played if you don't have a card that matches the color). The color can be chosen from the four colors. 
When you have one card left, you must shout "UNO!" If another player catches you before your next turn, you must draw two cards.
Ending the Game 
Whenever a player discards all the cards, the game will end and a screen will be shown and conclude all the score of each player according to the number of remaining cards.
Bug Report
If there is any bug in the program, please report to us.


# CS3343UNO — Build & Run

This repository contains a Java UNO game used for the CS3343 project.

# UNO Game — CS3343UNO

## Overview
This is a Java implementation of the UNO card game. The game lets a single user play UNO (against CPU players) when they don't have friends available to play. The game ends when a player discards all cards in their hand.

## Game components
- Number cards: 0–9 in four colors (Red, Blue, Green, Yellow).
- Action cards: Skip, Reverse, Draw Two (per color).
- Wild cards: Wild and Wild Draw Four.

## Gameplay (short)
- Players take turns in clockwise order.
- On your turn you may play a card that matches the top discard card by color or number.
- If you have no playable card, draw one from the draw pile. If it is playable you may play it immediately.
- Special cards:
	- Skip: next player loses a turn.
	- Reverse: switches play direction.
	- Draw Two: next player draws two cards and loses a turn.
	- Wild: choose the current color.
	- Wild Draw Four: choose color and next player draws four (only legal if you have no matching color).
- When you have one card left you must shout "UNO!" — if another player catches you before your next turn you draw two cards.

## Ending the game
The game ends when a player has no cards left. The final screen shows scores based on remaining cards.

## Bug reports
If you find a bug please open an issue or contact the maintainers.

---

# Build & Run
This repository contains the source and a pre-built JAR. The repository layout is:

- `Release/` — packaged JARs (contains `CS3343-Group19-UNO.jar`).
- `Source/CS3343UNO/src/` — Java source files.

### Quick build (Windows)

1. Ensure the JDK is installed and `javac` and `jar` are on your PATH.
2. From the repository root run:

```powershell
.\build.bat
```

This script compiles sources under `Source/CS3343UNO/src/` and writes the packaged JAR to `Release/UNOGame.jar`.

### Run the packaged JAR

```powershell
java -jar .\Release\UNOGame.jar
```

### Notes
- The `build.bat` excludes test sources (no JUnit dependency required).
- If you prefer Eclipse: import `Source/CS3343UNO` as a Java project (set `src/` as source folder) and use the Runnable JAR exporter to create a jar.
- If you want a reproducible build in CI, I can add a `pom.xml` (Maven) or `build.gradle` (Gradle).
