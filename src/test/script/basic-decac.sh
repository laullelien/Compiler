#! /bin/sh

# Test de l'interface en ligne de commande de decac.
# On ne met ici qu'un test trivial, a vous d'en ecrire de meilleurs.

PATH=./src/main/bin:"$PATH"

decac_moins_b=$(decac -b)

if [ "$?" -ne 0 ]; then
    echo "ERREUR: decac -b a termine avec un status different de zero."
    exit 1
fi

if [ "$decac_moins_b" = "" ]; then
    echo "ERREUR: decac -b n'a produit aucune sortie"
    exit 1
fi

if echo "$decac_moins_b" | grep -i -e "erreur" -e "error"; then
    echo "ERREUR: La sortie de decac -b contient erreur ou error"
    exit 1
fi

echo "Pas de probleme detecte avec decac -b."

# ... et ainsi de suite.

if decac 2>&1 | \
    grep -q -e 'les points 11.1 et 11.3 de la sémantique de Deca.'
then
    echo "Succes attendu de decac (sans option)"
else
    echo "Echec inattendu de decac (sans option)"
    exit 1
fi

if decac -b 2>&1 | \
    grep -q -e 'Projet GL38'
then
    echo "Succes attendu de decac -b"
else
    echo "Succes inattendu de decac -b"
    exit 1
fi

if decac -b dummy.deca 2>&1 | \
    grep -q -e "-b doit être tout seul en tant d'argument"
then
    echo "Echec attendu de decac -b dummy.deca"
else
    echo "Succes inattendu de decac -b dummy.deca"
    exit 1
fi

if decac -p -v 2>&1 | \
    grep -q -e "Impossible de faire -p et -v en même temps, il faut choisir l'un ou l'autre"
then
    echo "Echec attendu de decac -p -v"
else
    echo "Succes inattendu de decac -p -v"
    exit 1
fi

if decac src/test/deca/context/valid/provided/hello-world.deca -n 2>&1 | \
    grep -q -e "Les options doivent être placé avant les fichiers .deca"
then
    echo "Echec attendu de decac hello-world.deca -n"
else
    echo "Succes inattendu de decac hello-world.deca -n"
    exit 1
fi

if decac -r 0 src/test/deca/context/valid/provided/hello-world.deca 2>&1 | \
    grep -q -e "Le nombre de registres n'est pas valide (entre 4 et 16)"
then
    echo "Echec attendu de decac -r 0 hello-world.deca"
else
    echo "Succes inattendu de decac -r 0 hello-world.deca"
    exit 1
fi

if decac -r 20 src/test/deca/context/valid/provided/hello-world.deca 2>&1 | \
    grep -q -e "Le nombre de registres n'est pas valide (entre 4 et 16)"
then
    echo "Echec attendu de decac -r 20 hello-world.deca"
else
    echo "Succes inattendu de decac -r 20 hello-world.deca"
    exit 1
fi

if decac -r deux src/test/deca/context/valid/provided/hello-world.deca 2>&1 | \
    grep -q -e "Il faut renseigner un nombre de registres entre 4 et 16"
then
    echo "Echec attendu de decac -r deux hello-world.deca"
else
    echo "Succes inattendu de decac -r deux hello-world.deca"
    exit 1
fi

if decac -r src/test/deca/context/valid/provided/hello-world.deca 2>&1 | \
    grep -q -e "Il faut renseigner un nombre de registres entre 4 et 16"
then
    echo "Echec attendu de decac -r hello-world.deca"
else
    echo "Succes inattendu de decac -r hello-world.deca"
    exit 1
fi

if decac -r 2>&1 | \
    grep -q -e "Il faut renseigner un nombre de registres entre 4 et 16"
then
    echo "Echec attendu de decac -r"
else
    echo "Succes inattendu de decac -r"
    exit 1
fi

if decac -f src/test/deca/context/valid/provided/hello-world.deca 2>&1 | \
    grep -q -e "Argument -f non reconnu"
then
    echo "Echec attendu de decac -f hello-world.deca"
else
    echo "Succes inattendu de decac -f hello-world.deca"
    exit 1
fi

if decac -f src/test/deca/context/valid/provided/hello-world.deca 2>&1 | \
    grep -q -e 'Usage:'
then
    echo "Echec attendu de decac -f hello-world.deca avec affichage de Usage"
else
    echo "Succes inattendu de decac -f hello-world.deca avec affichage de Usage"
    exit 1
fi

if decac -p 2>&1 | \
    grep -q -e "Fichier(s) source(s) manquant(s)"
then
    echo "Echec attendu de decac -p"
else
    echo "Succes inattendu de decac -p"
    exit 1
fi

if decac -p -p src/test/deca/context/valid/provided/hello-world.deca 2>&1 | \
    grep -q -e "-p est écrit 2 fois"
then
    echo "Echec attendu de decac -p -p hello-world.deca"
else
    echo "Succes inattendu de decac -p -p hello-world.deca"
    exit 1
fi

if decac -r 8 8 src/test/deca/context/valid/provided/hello-world.deca 2>&1 | \
    grep -q -e "Argument 8 non reconnu"
then
    echo "Echec attendu de decac -r 8 8 hello-world.deca"
else
    echo "Succes inattendu de decac -r 8 8 hello-world.deca"
    exit 1
fi

if decac -r 8 src/test/deca/context/valid/provided/hello-world.deca | \
    grep -i -e "erreur" -e "error"
then
    echo "ERREUR: La sortie de decac -r 8 hello-world.deca"
    exit 1
fi

if decac -v src/test/deca/context/valid/provided/hello-world.deca | \
    grep -i -e "erreur" -e "error"
then
    echo "ERREUR: La sortie de decac -v hello-world.deca"
    exit 1
fi

if decac -P src/test/deca/context/valid/provided/hello-world.deca \
    src/test/deca/syntax/valid/provided/hello.deca | \
    grep -i -e "erreur" -e "error"
then
    echo "ERREUR: La sortie de decac -P hello-world.deca hello.deca"
    exit 1
fi

decac -p src/test/deca/context/valid/provided/hello-world.deca > src/test/deca/context/valid/provided/hello-world_p.deca
decac -p src/test/deca/context/valid/provided/hello-world_p.deca > src/test/deca/context/valid/provided/hello-world_p2.deca
if ! diff src/test/deca/context/valid/provided/hello-world_p.deca src/test/deca/context/valid/provided/hello-world_p2.deca
then
    echo "ERREUR: Le résultat de decac -p n'est pas idempotente"
    exit 1
fi

rm src/test/deca/context/valid/provided/hello-world_p.deca src/test/deca/context/valid/provided/hello-world_p2.deca
