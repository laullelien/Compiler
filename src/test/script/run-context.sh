#! /bin/sh

# Auteur : gl38
# Version initiale : 01/01/2024

# Script de test de la vérification contextuelle pour les sources dans context

# On se place dans le répertoire du projet (quel que soit le
# répertoire d'où est lancé le script) :
cd "$(dirname "$0")"/../../.. || exit 1

PATH=./src/test/script/launchers:"$PATH"
DIR=src/test/deca/context

echo "Début tests context"
source_path="$DIR/invalid"
echo "Section $source_path"
if [ -z "$(ls $source_path/*.deca 2> /dev/null)" ]
    then
        echo "    [WARNING] Pas de fichier a tester"
    else
    for source in $source_path/*.deca
    do
        source_lis="${source%.deca}.lis"
        filename="$(basename "$source")"
        if [ ! -f "$source_lis" ]
        then
            echo "    [WARNING] Fichier $source_lis n'existe pas, impossible de tester le context de $filename"
        else
            res_context=$(test_context "$source" 2>&1)
            err_context="$(cat "$source_lis")"
            if echo "$res_context" | grep -q -e "$err_context"
            then
                echo "    [OK] Echec attendu pour $filename"
            else
                echo "    [ERREUR] Succès inattendu ou erreur non détectée par test_context pour $filename"
                echo "    [DEBUG] Erreur attendu: $err_context"
                echo "    [DEBUG] Sortie de test_lex:"
                echo "$res_context"
                exit 1
            fi
        fi
    done
fi

source_path="$DIR/valid"
echo "Section $source_path"
if [ -z "$(ls $source_path/*.deca 2> /dev/null)" ]
    then
        echo "    [WARNING] Pas de fichier a tester"
    else
    for source in $DIR/valid/*.deca
    do
        filename="$(basename "$source")"
        # pour les tests valides, on lance decac avec -v pour detecter une eventuelle erreur
        source_v="$DIR/valid/$filename.v"
        res_context=$(decac -v "$source" 2>&1)
        if echo "$res_context" | grep -q -e "$filename:[0-9][0-9]*:"
        then
            echo "    [ERREUR] Echec inattendu de test_context pour $filename"
            echo "    [DEBUG] Sortie de decac -v:"
            echo "$res_context"
            exit 1
        else
            # la sortie de decac -v est vide, le programme est valide contextuellement
            echo "    [OK] Succès attendu de $filename"
        fi
    done
fi
echo "Fin tests context"