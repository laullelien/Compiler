#! /bin/sh

# Auteur : gl38
# Version initiale : 01/01/2024

# Script de test de la lexicographie pour les sources dans syntax

# On se place dans le répertoire du projet (quel que soit le
# répertoire d'où est lancé le script) :
cd "$(dirname "$0")"/../../.. || exit 1

PATH=./src/test/script/launchers:"$PATH"
DIR=src/test/deca/syntax

echo "Début tests lexer"
source_path="$DIR/invalid"
echo "Section $source_path"
if [ -z "$(ls $source_path/*.deca 2> /dev/null)" ]
    then
        echo "    [WARNING] Pas de fichier a tester"
    else
    for source in $source_path/*.deca
    do
        source_lex="${source%.deca}.lex"
        source_lis="${source%.deca}.lis"
        filename="$(basename "$source")"
        # pour pouvoir faire nos tests, il faut obligatoirement un fichier source.lex ou source.lis
        # qui contient les résultats attendus par test_lex et decac
        if [ ! -f "$source_lex" -a ! -f "$source_lis" ]
        then
            echo "    [WARNING] Fichier $source_lis n'existe pas, impossible de tester le lexer de $filename"
        else
            res_lex=$(test_lex "$source" 2>&1)
            # si on a qu'un fichier source.lis, test_lex doit marcher
            if [ -f "$source_lis" ]
            then
                if echo "$res_lex" | head -n 1 | grep -q "$filename:[0-9][0-9]*:"
                then
                    echo "    [ERREUR] Echec inattendu de test_lex pour $filename"
                    echo "    [DEBUG] Sortie de test_lex:"
                    echo "$res_lex"
                    exit 1
                else
                    echo "    [OK] Succès attendu pour $filename"
                fi
            # sinon, on a forcément un fichier source.lex qui contient l'erreur attendu
            else
                err_lex="$(cat "$source_lex")"
                if echo "$res_lex" | grep -q -e "$err_lex"
                then
                    echo "    [OK] Echec attendu pour $filename"
                else
                    echo "    [ERREUR] Erreur non détectée par test_lex pour $filename"
                    echo "    [DEBUG] Erreur attendu: $err_lex"
                    echo "    [DEBUG] Sortie de test_lex:"
                    echo "$res_lex"
                    exit 1
                fi
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
        res_lex=$(test_lex "$source" 2>&1)
        if echo "$res_lex" | grep -q -e "$filename:[0-9][0-9]*:"
        then
            echo "    [ERREUR] Echec inattendu de test_lex pour $filename"
            echo "    [DEBUG] Sortie de test_lex:"
            echo "$res_lex"
            exit 1
        else
            echo "    [OK] Succès attendu pour $filename"
        fi
    done
fi
echo "Fin tests lexer"
