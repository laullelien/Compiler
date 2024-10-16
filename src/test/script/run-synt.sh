#! /bin/sh

# Auteur : gl38
# Version initiale : 01/01/2024

# Script de test de la syntaxe pour les sources dans syntax

# On se place dans le répertoire du projet (quel que soit le
# répertoire d'où est lancé le script) :
cd "$(dirname "$0")"/../../.. || exit 1

PATH=./src/test/script/launchers:"$PATH"
PATH=./src/main/bin:"$PATH"
PATH=./bin:"$PATH"

DIR=src/test/deca/syntax

echo "Début tests parser"
source_path="$DIR/invalid"
echo "Section $source_path"
if [ -z "$(ls $source_path/*.deca 2> /dev/null)" ]
    then
        echo "    [WARNING] Pas de fichier a tester"
    else
    for source in "$source_path"/*.deca
    do
        source_synt="${source%.deca}.synt"
        filename="$(basename "$source")"
        if [ -f "$source_synt" ]
        then
            res_synt="$(test_synt "$source" 2>&1)"
            err_synt="$(cat "$source_synt")"
            if echo "$res_synt" | grep -q -e "$err_synt"
            then
                echo "    [OK] Echec attendu pour $filename"
            else
                echo "    [ERREUR] Succès inattendu ou erreur non détectée par test_synt pour $filename"
                echo "    [DEBUG] Erreur attendu: $err_synt"
                echo "    [DEBUG] Sortie de test_lex:"
                echo "$res_synt"
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
    for source in "$source_path"/*.deca
    do
        source_pp="${source%.deca}.pp"
        filename="$(basename "$source")"
        if [ ! -f "$source_pp" ]
        then
            echo "    [WARNING] Fichier $source_pp n'existe pas, impossible de tester le parser de $filename"
        else
            # pour les tests valides, on lance decac avec -p pour obtenir l'arbre plus ppible
            # on exploite ensuite le theoreme 2 pour le résultat de decac -p avec
            # le résultat contenu dans source.pp, qui contient le résultat de decac -p sur source.deca
            source_p="$DIR/valid/$filename.p"
            decac -p "$source" > "$source_p" 2>&1 
            if diff "$source_p" "$source_pp" > /dev/null 2>&1
            then
                echo "    [OK] Succès attendu de $filename"
                rm "$source_p"
            else
                echo "    [ERREUR] decac -p ne respecte pas le théorème 2 pour $filename"
                echo "    [DEBUG] Sortie attendu:"
                cat "$source_pp"
                echo "    [DEBUG] Sortie de decac -p:"
                cat "$source_p"
                # décommenter pour voir la différence entre les sorties
                # echo "    [DEBUG] Différences constatées par diff:"
                # diff "$source_p" "$source_pp"
                rm "$source_p"
                exit 1
            fi
        fi
    done
fi
echo "Fin tests parser"
