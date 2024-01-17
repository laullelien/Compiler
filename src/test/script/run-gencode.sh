#! /bin/sh

# Auteur : gl38
# Version initiale : 01/01/2024

# Script de test de la génération de code pour ima pour les sources dans codegen

terminal_path="$PWD"
# On se place dans le répertoire du projet (quel que soit le
# répertoire d'où est lancé le script) :
cd "$(dirname "$0")"/../../.. || exit 1

PATH=./src/test/script/launchers:./src/main/bin:"$PATH"
PATH=./src/main/bin:"$PATH"
PATH=./bin:"$PATH"

DIR=src/test/deca/codegen

debug() {
    source_expected="$1"
    source_output="$2"
    decac_options="$3"
    ima_options="$4"
    echo "    [DEBUG] Arguments de decac: $decac_options"
    echo "    [DEBUG] Arguments de ima: $ima_options"
    echo "    [DEBUG] Sortie attendu:"
    cat "$source_expected"
    echo "    [DEBUG] Sortie de ima:"
    cat "$source_output"
    # décommenter pour voir la différence entre les sorties
    # echo "    [DEBUG] Différences constatées par diff:"
    # diff "$output_ima" "$source_expected"
}

test_gencode() {
    source="$1"
    decac_options="$2 $3"
    decac_options_no_debug="$3"
    ima_options="$4"
    ok_message="$5"
    ext_expected="$6"
    ext_output="ass"
    source_expected="${source%.deca}.$ext_expected"
    source_output="${source%.deca}.$ext_output"
    source_error="${source%.deca}.err"
    filename="$(basename "$source")"
    if [ ! -f "$source_expected" ]
    then
        echo "    [WARNING] Fichier $source_expected n'existe pas, impossible de tester la compilation de $filename"
    else
        rm -f "$source_output" 2> /dev/null
        decac $decac_options "$source" 2> "$source_error"
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
        if [ ! -f "$source_output" ]
        then
            echo "    [ERREUR] Fichier $filename.$ext_output non généré."
            exit 1
        fi
        output_ima="${source%.deca}.out"
        ima $ima_options "$source_output" > "$output_ima" 2> "$source_error"
        rm -f "$source_output"
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
        if diff "$output_ima" "$source_expected" > /dev/null 2>&1
        then
            if [ "$debug_option" ]
            then
                debug "$source_expected" "$output_ima" "$decac_options_no_debug" "$ima_options"
            fi
            echo "    [OK] $filename: $ok_message attendu de decac $decac_options_no_debug"
            rm -f "$output_ima"
            rm -f "$source_error"
        else
            echo "    [ERREUR] Résultat inattendu pour $filename"
            debug "$source_expected" "$output_ima" "$decac_options_no_debug" "$ima_options"
            rm -f "$output_ima"
            rm -f "$source_error"
            exit 1
        fi
    fi
}

test_gencode_options() {
    source="$1"
    type="$2"
    debug_options="$3"
    source_options="${source%.deca}.options"
    if [ "$type" = "invalid" ]
    then
        ok_message="Echec"
        ext_expected="ima"
    else
        # par défaut nous essayons un test valid
        ok_message="Succès"
        ext_expected="res"
    fi
    test_gencode "$source" "$debug_options" "" "" "$ok_message" "$ext_expected"
    if [ -f "$source_options" ] && [ -s "$source_options" ]
    then
        while read -r decac_opt; read -r ima_opt
        do
            test_gencode "$source" "$debug_options" "$decac_opt" "$ima_opt" "$ok_message" "$ext_expected"
        done < "$source_options"
    fi
}

if [ "$#" -gt 0 ]
then
    for param
    do
        if [ "$param" = '-d' ]
        then
            debug_options='-d -d -d -d'
        else
            source="$terminal_path/$param"
            if [ ! -f "$source" ] || [ ! -r "$source" ]
            then
                echo "Fichier $source introuvable ou inaccessible"
                echo "Usage: $0 [-d] <fichier deca>..."
                exit 1
            fi
            dirname="${source%/*}"
            folder="${dirname##*/}"
            echo "Section $source"
            if [ ! "$folder" = 'valid' ] && [ ! "$folder" = 'invalid' ]
            then
                echo "    [WARNING] Impossible de déterminer si le test est valid ou invalid"
                echo "    [WARNING] Test valid executé par défaut"
            fi
            test_gencode_options "$source" "$folder" "$debug_options"
            exit 0
        fi
    done
fi

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
            test_gencode_options "$source" "$folder"
        done
    fi
done
echo "Fin tests codegen"
