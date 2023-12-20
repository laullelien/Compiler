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