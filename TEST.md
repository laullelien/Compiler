# Documentation pour les tests
Pour chaque fichier *.deca* à tester, il faut rajouter un fichier possédant le même nom de fichier de base et une extension qui dépend du type de test. Cet autre fichier contient les éléments qui permettent de vérifier qui le test s'est bien passé. Par exemple, pour un test de codegen valide, ce fichier a pour extension *.res* et il contient la sortie attendue du test.

**hello-world.deca**
// Description:
//    Affiche Hello World!
//
// Resultats:
//    Hello World!

{
    println("Hello World!");
}

**hello-world.res**
Hello World!

Voici un tableau qui décrit les extensions et le contenu du fichier à rajouter.
Ici *basename* désigne le nom de base du fichier .deca et *n* désigne l'indice de la ligne où se situe l'erreur.
Exemple: le *basename* de **hello-world.deca** est *hello-world* . 

|   | Extension  | Contenu  |
|---|---|---|
| Codegen valide | *.res* | La sortie attendue du programme |
| Codegen invalide | *.ima* | Pas encore implémenté |
| Context valide | Pas de fichier à ajouter |  |
| Context invalide | *.context* | *basename.deca*:*n*: |
| Synt valide | *.pp* | Sortie de *decac -p basename.deca* |
| Synt invalide | *.synt* | *basename.deca*:*n*: |
| Lex valide | Utiliser Synt valide ou invalide |  |
| Lex invalide | *.lex* | *basename.deca*:*n*: |

## SKIP
On peut passer un test en ajoutant *.skip* . Par exemple,pour passer le test hello-world:
hello-world.deca -> hello-world.deca.skip
