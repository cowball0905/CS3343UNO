@echo off
rem Build script for CS3343UNO
rem Usage: run from repository root: build.bat

setlocal
set SRC=src
set OUT=bin\classes

echo Creating output directory %OUT% if needed...
if not exist "%OUT%" (
  mkdir "%OUT%"
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
echo Main-Class: controller.Main>manifest.txt
jar cfm "bin\UNOGame.jar" manifest.txt -C "%OUT%" .
if %errorlevel% neq 0 (
  echo Jar creation failed.
  del sources.txt
  del manifest.txt
  exit /b %errorlevel%
)

del sources.txt
del manifest.txt

echo Build successful: bin\UNOGame.jar
endlocal
