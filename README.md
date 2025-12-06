# UNO Game — CS3343UNO

## Overview

This is a Java implementation of the UNO card game. The game lets a single user play UNO (against CPU players) when they don't have friends available to play with. The game ends when a player plays all cards in their hand.

## Game components

- Number cards: 0–9 in four colors (Red, Blue, Green, Yellow).
- Action cards: Skip, Reverse, Draw Two (per color).
- Wild cards: Wild and Wild Draw Four.

## Gameplay (simplifed)

- Players take turns in clockwise order.
- On your turn, you may play a card that matches the top card on the table by color or number.
- If you have no playable cards, draw one from the deck. If it is playable you may play it immediately.
- Special cards:
  - Skip: next player loses a turn.
  - Reverse: switches play direction.
  - Draw Two: next player draws two cards and loses a turn.
  - Wild: choose the current color.
  - Wild Draw Four: choose color and next player draws four (only legal if you have no matching color).
- When you have one card left you must shout "UNO!" — if another player catches you forgetting to call UNO, you have to draw two cards.

## Ending the game

The game ends when a player has no cards left. The final screen shows scores based on remaining cards.

## Bug reports

If you find a bug, please open an issue or contact the maintainer team.

---

# Build & Run

This repository contains the source code and a pre-built JAR. The repository layout is:

- `Release/` — packaged JARs (contains `CS3343-Group19-UNO.jar`).
- `Source/CS3343UNO/src/` — Java source code files.

### Quick build (Windows)

1. Ensure the JDK is installed and `javac` and `jar` are on your PATH.
2. Navigate to the `Release` directory and run:

```powershell
cd Release
.\build.bat
```

This script compiles sources under `../Source/CS3343UNO/src/` and writes the packaged JAR to `UNOGame.jar` in the current directory.

### Run the packaged JAR

```powershell
java -jar .\Release\UNOGame.jar
```

### Notes

- The `build.bat` excludes test sources (no JUnit dependency required).
- If you prefer Eclipse: import `Source/CS3343UNO` as a Java project (set `src/` as source folder) and use the Runnable JAR exporter to create a jar.
- If you want a reproducible build in CI, a `pom.xml` (Maven) or `build.gradle` (Gradle) can be added.
- If there are any enquires or problems regarding the project, please feel free to contact hlwong397-c@my.cityu.edu.hk
