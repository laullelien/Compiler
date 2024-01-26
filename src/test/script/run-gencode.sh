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

gencode_exec() {
    source="$1"
    ext_expected="$2"
    decac_options="$3 $4"
    decac_options_no_debug="$4"
    ima_options="$5"
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
        # on vérifie si l'option -r N est spécifiée
        nb_registers=$(echo "$decac_options_no_debug" | grep '\-r [0-9]' | cut -f2 -d ' ' 2> /dev/null)
        # test si $nb_registers est bien un entier
        expr "$nb_registers" + 0 > /dev/null 2> /dev/null
        if [ "$?" -lt 2 ]
        then
            # si on a l'option -r N, on vérifie qu'on utilise bien QUE les registres R0 à RN
            res_grep_invalid_registers="$(cat "$source_output" | grep "R$nb_registers")"
            if [ "$?" -eq 0 ]
            then
                echo "    [ERREUR] Echec inattendu de decac -r $nb_registers"
                echo "    [ERREUR] Le registre suivant a été utilisé: R$nb_registers"
                echo "    [DEBUG] Lignes qui utilise R$nb_registers:"
                echo "$res_grep_invalid_registers"
                # décommenter pour afficher le fichier .ass généré par decac
                # echo "    [DEBUG] Fichier $filename.$ext_output généré par decac:"
                # cat "$source_output"
                rm -f "$source_error"
                rm -f "$source_output"
                exit 1
            fi
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
            echo "    [OK] decac $decac_options_no_debug $filename"
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
    optim_option="$4"
    source_options="${source%.deca}.options"
    if [ "$type" = "invalid" ]
    then
        ext_expected="ima"
    else
        ext_expected="res"
    fi
    gencode_exec "$source" "$ext_expected" "$debug_options" "$optim_option"
    if [ -f "$source_options" ] && [ -s "$source_options" ]
    then
        while read -r decac_opt; read -r ima_opt
        do
            gencode_exec "$source" "$ext_expected" "$debug_options" "$optim_option $decac_opt" "$ima_opt"
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
            if [ ! "$folder" = 'valid' ] && [ ! "$folder" = 'invalid' ]
            then
                echo "    [WARNING] Impossible de déterminer si le test est valid ou invalid"
                echo "    [WARNING] Test valid executé par défaut"
            fi
            test_gencode_options "$source" "$folder" "$debug_options"
        fi
    done
    exit 0
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

echo "Début tests OPTIM"
for folder in 'valid' 'extension/valid' 'perf'
do
    source_path="$DIR/$folder"
    echo "Section $source_path"
    if [ -z "$(ls $source_path/*.deca 2> /dev/null)" ]
        then
            echo "    [WARNING] Pas de fichier a tester"
        else
        for source in "$source_path"/*.deca
        do
            test_gencode_options "$source" "valid" "" "-optim"
        done
    fi
done
echo "Fin tests OPTIM"
