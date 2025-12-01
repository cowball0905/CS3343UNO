# CS3343UNO — Build & Run

This repository contains a Java UNO game used for the CS3343 project.

Quick build (Windows)

1. Ensure JDK is installed and `javac` and `jar` are on your PATH.
2. From the repository root run:

```powershell
build.bat
```

This compiles sources under `Source/CS3343UNO/src/` and creates `Release/CS3343-Group19-UNO.jar`.

Run:

```powershell
java -jar .\Release\CS3343-Group19-UNO.jar
```

Notes

- If you prefer Eclipse: import the project or create a Java project using `src` as the source folder, then use the Runnable JAR exporter and select a run configuration for `controller.Main`.
- If you need help pushing these changes or reversing them, tell me and I can create or revert commits as requested.
