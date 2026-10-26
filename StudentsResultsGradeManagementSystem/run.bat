@echo off
setlocal
cd /d "%~dp0"
echo ====================================================
echo   Students Results and Grade Management System
echo   Java Swing Edition - Starting Application...
echo ====================================================

REM Locate java runtime
set "JAVA_CMD=java"
where java >nul 2>&1
if errorlevel 1 (
    if exist "C:\Program Files\Eclipse Adoptium\jdk-21.0.12.101-hotspot\bin\java.exe" (
        set "JAVA_CMD=C:\Program Files\Eclipse Adoptium\jdk-21.0.12.101-hotspot\bin\java.exe"
    ) else if defined JAVA_HOME (
        if exist "%JAVA_HOME%\bin\java.exe" (
            set "JAVA_CMD=%JAVA_HOME%\bin\java.exe"
        )
    )
)

REM Check if compiled classes exist
if not exist "out\Main.class" (
    echo [INFO] Application not yet compiled. Compiling now...
    call compile.bat
    if not exist "out\Main.class" (
        echo [ERROR] Compilation failed. Cannot start application.
        pause
        exit /b 1
    )
)

echo [INFO] Launching UI using: "%JAVA_CMD%"

REM Run the application
"%JAVA_CMD%" -cp out Main

pause
