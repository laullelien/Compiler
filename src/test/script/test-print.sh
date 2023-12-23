#! /bin/sh
cd "$(dirname "$0")"/../../.. || exit 1

PATH="$(pwd)"/src/test/script/launchers:"$(pwd)"/src/main/bin:"$PATH"
FILENAME='print'

rm -f src/test/deca/codegen/valid/"$FILENAME".ass 2>/dev/null
decac src/test/deca/codegen/valid/"$FILENAME".deca || exit 1
if [ ! -f ./src/test/deca/codegen/valid/"$FILENAME".ass ]; then
    echo "Fichier $FILENAME.ass non généré."
    exit 1
fi

resultat=$(ima ./src/test/deca/codegen/valid/"$FILENAME".ass) || exit 1

# On code en dur la valeur attendue.
attendu='43
several expr'

if [ "$resultat" = "$attendu" ]; then
    echo "Résultat attendu de ima:"
    echo "$resultat"
else
    echo "Résultat inattendu de ima:"
    echo "$resultat"
    exit 1
fi
