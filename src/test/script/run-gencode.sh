#! /bin/sh

# Auteur : gl38
# Version initiale : 01/01/2024

# Script de test de la génération de code pour ima pour les sources dans codegen

# On se place dans le répertoire du projet (quel que soit le
# répertoire d'où est lancé le script) :
cd "$(dirname "$0")"/../../.. || exit 1

PATH=./src/test/script/launchers:./src/main/bin:"$PATH"
DIR=src/test/deca/codegen

echo "Début tests codegen"
source_path="$DIR/invalid"
echo "Section $source_path"
if [ -z "$(ls $source_path/*.deca 2> /dev/null)" ]
    then
        echo "    [WARNING] Pas de fichier a tester"
    else
    for source in $source_path/*.deca
rm -f ./src/test/deca/codegen/valid/provided/cond0.ass 2>/dev/null
decac ./src/test/deca/codegen/valid/provided/cond0.deca || exit 1
if [ ! -f ./src/test/deca/codegen/valid/provided/cond0.ass ]; then
    echo "Fichier cond0.ass non généré."
    exit 1
fi

resultat=$(ima ./src/test/deca/codegen/valid/provided/cond0.ass) || exit 1
rm -f ./src/test/deca/codegen/valid/provided/cond0.ass

# On code en dur la valeur attendue.
attendu=ok

if [ "$resultat" = "$attendu" ]; then
    echo "Tout va bien"
else
    echo "Résultat inattendu de ima:"
    echo "$resultat"
    exit 1
fi
