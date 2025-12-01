@echo off
rem Build script for CS3343UNO (root-based)
rem Usage: run from repository root: build.bat

setlocal
set SRC=Source\CS3343UNO\src
set OUT=Source\CS3343UNO\bin\classes
set JAR=Release\CS3343-Group19-UNO.jar

echo Creating output directory %OUT% if needed...
if not exist "%OUT%" (
  mkdir "%OUT%" -Force >nul 2>&1
)

echo Collecting Java source files...
if exist sources.txt del sources.txt
dir /b /s "%SRC%\*.java" > sources.txt

echo Compiling sources into %OUT%...
javac -d "%OUT%" @sources.txt
if %errorlevel% neq 0 (
  echo Compilation failed. See errors above.
  del sources.txt
  exit /b %errorlevel%
)

echo Creating manifest and packaging JAR...
(
  echo Main-Class: controller.Main
  echo.
)>manifest.txt
jar cfm "%JAR%" manifest.txt -C "%OUT%" .
if %errorlevel% neq 0 (
  echo Jar creation failed.
  del sources.txt
  del manifest.txt
  exit /b %errorlevel%
)

del sources.txt
del manifest.txt

echo Build successful: %JAR%
endlocal
