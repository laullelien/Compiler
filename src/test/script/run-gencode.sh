#! /bin/sh

# Auteur : gl38
# Version initiale : 01/01/2024

# Script de test de la génération de code pour ima pour les sources dans codegen
PWD="$(pwd)"

# On se place dans le répertoire du projet (quel que soit le
# répertoire d'où est lancé le script) :
cd "$(dirname "$0")"/../../.. || exit 1

PATH=./src/test/script/launchers:./src/main/bin:"$PATH"
PATH=./src/main/bin:"$PATH"
PATH=./bin:"$PATH"

DIR=src/test/deca/codegen

test_gencode_invalid() {
    source="$1"
    source_ima="${source%.deca}.ima"
    source_ass="${source%.deca}.ass"
    source_error="${source%.deca}.err"
    filename="$(basename "$source")"
    if [ ! -f "$source_ima" ]
    then
        echo "    [WARNING] Fichier $source_ima n'existe pas, impossible de tester la compilation de $filename"
    else
        rm -f "$source_ass" 2> /dev/null
        decac "$source" 2> "$source_error"
        # on vérifie si la taille de source.err est supérieur à 0
        # celà signifie que decac a rencontré une erreur dans la compilation
        if [ -s "$source_error" ]
        then
            echo "    [ERREUR] Echec inattendu de decac pour $filename"
            echo "    [DEBUG] decac a reporté l'erreur suivant:"
            cat "$source_error"
            rm -f "$source_error"
            exit 1
        fi
        if [ ! -f "$source_ass" ]
        then
            echo "    [ERREUR] Fichier $filename.ass non généré."
            exit 1
        fi
        output_ima="${source%.deca}.out"
        ima "$source_ass" > "$output_ima" 2> "$source_error"
        rm -f "$source_ass"
        if diff "$output_ima" "$source_ima"
        then
            echo "    [OK] Echec attendu de ima pour $filename"
            rm -f "$output_ima"
            rm -f "$source_error"
        else
            echo "    [OK] Succès inattendu de ima pour $filename"
            rm -f "$output_ima"
            rm -f "$source_error"
            exit 1
        fi
    fi
}

test_gencode_valid() {
    source_res="${source%.deca}.res"
    source_ass="${source%.deca}.ass"
    source_error="${source%.deca}.err"
    filename="$(basename "$source")"
    if [ ! -f "$source_res" ]
    then
        echo "    [WARNING] Fichier $source_res n'existe pas, impossible de tester la compilation de $filename"
    else
        rm -f "$source_ass" 2> /dev/null
        decac "$source" 2> "$source_error"
        # on vérifie si la taille de source.err est supérieur à 0
        # celà signifie que decac a rencontré une erreur dans la compilation
        if [ -s "$source_error" ]
        then
            echo "    [ERREUR] Echec inattendu de decac pour $filename"
            echo "    [DEBUG] decac a reporté l'erreur suivant:"
            cat "$source_error"
            rm -f "$source_error"
            exit 1
        fi
        if [ ! -f "$source_ass" ]
        then
            echo "    [ERREUR] Fichier $filename.ass non généré."
            exit 1
        fi
        output_ima="${source%.deca}.out"
        ima "$source_ass" > "$output_ima" 2> "$source_error"
        rm -f "$source_ass"
        # on vérifie si la taille de source.err est supérieur à 0
        # celà signifie que ima a rencontré une erreur dans l'exécution
        if [ -s "$source_error" ]
        then
            echo "    [ERREUR] Echec inattendu de ima pour $filename"
            echo "    [DEBUG] ima a reporté l'erreur suivant:"
            cat "$source_error"
            rm -f "$output_ima"
            rm -f "$source_error"
            exit 1
        fi
        if diff "$output_ima" "$source_res" > /dev/null 2>&1
        then
            echo "    [OK] Résultat attendu de $filename"
            rm -f "$output_ima"
            rm -f "$source_error"
        else
            echo "    [ERREUR] Résultat inattendu pour $filename"
            echo "    [DEBUG] Sortie attendu:"
            cat "$source_res"
            echo "    [DEBUG] Sortie de ima:"
            cat "$output_ima"
            # décommenter pour voir la différence entre les sorties
            # echo "    [DEBUG] Différences constatées par diff:"
            # diff "$output_ima" "$source_res"
            rm -f "$output_ima"
            rm -f "$source_error"
            exit 1
        fi
    fi
}

if [ "$#" -eq 1 ]
then
    source="$PWD/$1"
    echo "$source"
    if [ ! -f "$source" ]
    then
        echo "Fichier $source introuvable ou n'est pas un fichier"
        exit 1
    fi
    if [ ! -r "$source" ]
    then
        echo "Impossible de lire $source"
    fi
    test_gencode_valid "$source"
else
    echo "Début tests codegen"
    for folder in 'invalid' 'valid'
    do
        source_path="$DIR/$folder"
        echo "Section $source_path"
        if [ -z "$(ls $source_path/*.deca 2> /dev/null)" ]
            then
                echo "    [WARNING] Pas de fichier a tester"
            else
            for source in "$source_path"/*.deca
            do
                test_gencode_"$folder" "$source"
            done
        fi
    done
    echo "Fin tests codegen"
fi