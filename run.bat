@echo off
cd /d "%~dp0"
title Exam Timetable and Seating Generator
echo ======================================================================
echo Starting Exam Timetable and Seating Generator (Java OOP)
echo ======================================================================

REM Check if bin folder exists, if not compile
if not exist "bin\com\examscheduler\Main.class" (
    echo Compiling Java source files...
    "C:\Program Files\Microsoft\jdk-17.0.20.101-hotspot\bin\javac.exe" -d bin src\com\examscheduler\model\*.java src\com\examscheduler\strategy\*.java src\com\examscheduler\service\*.java src\com\examscheduler\validation\*.java src\com\examscheduler\exception\*.java src\com\examscheduler\report\*.java src\com\examscheduler\util\*.java src\com\examscheduler\*.java
)

"C:\Program Files\Microsoft\jdk-17.0.20.101-hotspot\bin\java.exe" -cp bin com.examscheduler.Main
pause
