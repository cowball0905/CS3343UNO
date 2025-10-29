# UNO Game Build Script
Write-Host "Building UNO Game..." -ForegroundColor Cyan

# Remove old .class files from src
Write-Host "Cleaning src directory..." -ForegroundColor Yellow
Get-ChildItem -Path src -Recurse -Filter "*.class" -ErrorAction SilentlyContinue | Remove-Item -Force

# Clean and create bin directory
Write-Host "Cleaning bin directory..." -ForegroundColor Yellow
if (Test-Path bin) {
    Remove-Item -Recurse -Force bin\controller, bin\model, bin\view -ErrorAction SilentlyContinue
}
New-Item -ItemType Directory -Force bin | Out-Null

# Compile
Write-Host "Compiling..." -ForegroundColor Yellow
javac -d bin -sourcepath src src/controller/Main.java

if ($LASTEXITCODE -eq 0) {
    Write-Host "Compilation successful!" -ForegroundColor Green
    
    # Copy resources
    Write-Host "Copying resources..." -ForegroundColor Yellow
    if (-not (Test-Path bin\asset)) {
        Copy-Item -Recurse -Force src\asset bin\
    }
    
    Write-Host "Build complete!" -ForegroundColor Green
    Write-Host ""
    Write-Host "To run the game, use:" -ForegroundColor Cyan
    Write-Host "  java -cp bin controller.Main" -ForegroundColor White
} else {
    Write-Host "Compilation failed!" -ForegroundColor Red
}
