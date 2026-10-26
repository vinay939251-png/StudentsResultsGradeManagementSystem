@echo off
setlocal
cd /d "%~dp0"
echo ====================================================
echo   Students Results and Grade Management System
echo   Java Swing Edition - Compiling Application...
echo ====================================================

REM Locate javac compiler
set "JAVAC_CMD=javac"
where javac >nul 2>&1
if errorlevel 1 (
    if exist "C:\Program Files\Eclipse Adoptium\jdk-21.0.12.101-hotspot\bin\javac.exe" (
        set "JAVAC_CMD=C:\Program Files\Eclipse Adoptium\jdk-21.0.12.101-hotspot\bin\javac.exe"
    ) else if defined JAVA_HOME (
        if exist "%JAVA_HOME%\bin\javac.exe" (
            set "JAVAC_CMD=%JAVA_HOME%\bin\javac.exe"
        )
    )
)

REM Create output directory
if not exist "out" mkdir out

echo [INFO] Using compiler: "%JAVAC_CMD%"

REM Find all java source files
dir /s /b src\*.java > sources.txt

REM Compile using sources list
"%JAVAC_CMD%" -d out @sources.txt
set COMPILE_STATUS=%ERRORLEVEL%
if exist sources.txt del sources.txt

if %COMPILE_STATUS% NEQ 0 (
    echo [ERROR] Compilation failed. Please check the error messages above.
    pause
    exit /b 1
)

echo ====================================================
echo   [SUCCESS] Compilation successful!
echo   All classes compiled to 'out' directory.
echo   To run the application, double-click run.bat
echo ====================================================

pause
