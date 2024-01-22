#! /bin/sh

# Script de test du théorème 2 (avec decac -p) pour les sources dans context

# On se place dans le répertoire du script (quel que soit le
# répertoire d'où est lancé le script) :
cd "$(dirname "$0")" || exit 1

echo "Début tests décompilation (un peu long, veuillez patienter)"
python3 'run-decompilation.py'
echo "Fin tests décompilation"