#!/bin/bash

echo "Compilation des fichiers Java..."
javac -encoding UTF-8 -cp "lib/*" -d bin CENOFM/*.java CENOFM/IHM/*.java CENOFM/metier/*.java

echo "Lancement de l'application..."
java -cp "bin:lib/*" CENOFM.Controleur