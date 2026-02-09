@echo off

REM Compilation
javac -d bin "CENOFM\*.java" "CENOFM\IHM\*.java" "CENOFM\metier\*.java"

REM Exécution
java --enable-preview -cp "bin" CENOFM.Controleur