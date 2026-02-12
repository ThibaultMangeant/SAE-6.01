@echo off

REM Compilation
javac -cp "lib\*" -d bin "CENOFM\*.java" "CENOFM\IHM\*.java" "CENOFM\metier\*.java"

REM Exécution
java --enable-preview -cp "bin;lib\*" CENOFM.Controleur