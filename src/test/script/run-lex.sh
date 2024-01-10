#! /bin/sh

# Auteur : gl38
# Version initiale : 01/01/2024

# Script de test de la lexicographie pour les sources dans syntax

# On se place dans le répertoire du projet (quel que soit le
# répertoire d'où est lancé le script) :
cd "$(dirname "$0")"/../../.. || exit 1

PATH=./src/test/script/launchers:"$PATH"

echo "Début tests lexer"
for source in src/test/deca/syntax/invalid/*.deca
do
    source_lex="${source%.deca}.lex"
    source_lis="${source%.deca}.lis"
    filename="$(basename "$source")"
    if [ ! -f "$source_lex" ]
    then
        echo "[WARNING] $source ne possède pas de tests de lexer"
    else
        res_lex=$(test_lex "$source" 2>&1)
        if [ -f "$source_lis" ]
        then
            if echo "$res_lex" | head -n 1 | grep -q "$filename:[0-9]"
            then
                echo "[ERREUR] Echec inattendu de test_lex pour $filename"
                echo "[DEBUG] Sortie de test_lex:"
                echo "$res_lex"
                exit 1
            else
                echo "[OK] Succès attendu pour $filename"
            fi
        else
            if echo "$res_lex" | grep -q -e "$(cat "$source_lex")"
            then
                echo "[OK] Echec attendu pour $filename"
            else
                echo "[ERREUR] Erreur non détectée par test_lex pour $filename"
                echo "[DEBUG] Sortie de test_lex:"
                echo "$res_lex"
                exit 1
            fi
        fi
    fi
done
echo "Fin tests lexer"
