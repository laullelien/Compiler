#! /bin/sh

# Auteur : gl38
# Version initiale : 01/01/2024

# Script de test de la syntaxe pour les sources dans syntax

# On se place dans le répertoire du projet (quel que soit le
# répertoire d'où est lancé le script) :
cd "$(dirname "$0")"/../../.. || exit 1

PATH=./src/test/script/launchers:"$PATH"
DIR=src/test/deca/syntax

echo "Début tests parser"
echo "Section $DIR/invalid"
for source in $DIR/invalid/*.deca
do
    source_lis="${source%.deca}.lis"
    filename="$(basename "$source")"
    if [ -f "$source_lis" ]
    then
        # pour les tests invalides, on lance decac avec -v pour arrêter dès que nous avons une erreur
        # si pas d'erreur, decac s'arrête avec aucune sortie
        res_decac=$(test_synt "$source" 2>&1)
        err_decac="$(cat "$source_lis")"
        if echo "$res_decac" | grep -q -e "$err_decac"
        then
            echo "    [OK] Echec attendu pour $filename"
        else
            echo "    [ERREUR] Succès inattendu ou erreur non détectée par decac -v pour $filename"
            echo "    [DEBUG] Erreur attendu: $err_decac"
            echo "    [DEBUG] Sortie de test_lex:"
            echo "    $res_decac"
            exit 1
        fi
    fi
done

echo "Section $DIR/valid"
for source in $DIR/valid/*.deca
do
    source_lis="${source%.deca}.lis"
    filename="$(basename "$source")"
    if [ ! -f "$source_lis" ]
    then
        echo "[WARNING] Fichier $source_lis n'existe pas, impossible de tester le parser de $filename"
    else
        # pour les tests valides, on lance decac avec -p pour obtenir l'arbre plus lisible
        # on exploite ensuite le theoreme 2 pour le résultat de decac -p avec
        # le résultat contenu dans source.lis, qui contient le résultat de decac -p sur source.deca
        source_p="$DIR/valid/$filename.p"
        decac -p "$source" 2>&1 > "$source_p"
        if diff "$source_p" "$source_lis" > /dev/null 2>&1
        then
            echo "    [OK] Succès attendu de $filename"
            rm "$source_p"
        else
            echo "    [ERREUR] decac -p ne respecte pas le théorème 2 pour $filename"
            echo "    [DEBUG] Sortie attendu:"
            cat "$source_lis"
            echo "    [DEBUG] Sortie de decac -p:"
            cat "$source_p"
            rm "$source_p"
            exit 1
        fi
    fi
done
echo "Fin tests parser"
