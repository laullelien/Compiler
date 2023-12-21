#! /bin/sh
cd "$(dirname "$0")"/../../.. || exit 1

PATH="$(pwd)"/src/test/script/launchers:"$(pwd)"/src/main/bin:"$PATH"

rm -f src/test/deca/codegen/valid/hello-world.ass 2>/dev/null
decac src/test/deca/codegen/valid/hello-world.deca || exit 1
if [ ! -f ./src/test/deca/codegen/valid/hello-world.ass ]; then
    echo "Fichier hello-world.ass non généré."
    exit 1
fi

resultat=$(ima ./src/test/deca/codegen/valid/hello-world.ass) || exit 1
rm -f ./src/test/deca/codegen/valid/hello-world.ass

# On code en dur la valeur attendue.
attendu='Hello World!'

if [ "$resultat" = "$attendu" ]; then
    echo "Résultat attendu de ima:"
    echo "$resultat"
else
    echo "Résultat inattendu de ima:"
    echo "$resultat"
    exit 1
fi
