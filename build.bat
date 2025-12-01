@echo off
rem Build script for CS3343UNO (root-based)
rem Usage: run from repository root: build.bat

setlocal
set SRC=Source\CS3343UNO\src
set OUT=Source\CS3343UNO\bin\classes
set JAR=Release\UNOGame.jar

echo Creating output directory %OUT% if needed...
if not exist "%OUT%" (
  mkdir "%OUT%" >nul 2>&1
)
echo Ensuring Release directory exists...
if not exist "Release" (
  mkdir "Release" >nul 2>&1
)

echo Collecting Java source files (excluding tests)...
if exist sources_all.txt del sources_all.txt
dir /b /s "%SRC%\*.java" > sources_all.txt
rem exclude test sources (paths containing \src\test\)
findstr /I /V /C:"\src\test\" sources_all.txt > sources.txt
del sources_all.txt

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
