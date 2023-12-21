package fr.ensimag.deca;

import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.*;

import org.apache.log4j.Logger;

/**
 * Main class for the command-line Deca compiler.
 *
 * @author gl38
 * @date 01/01/2024
 */
public class DecacMain {
    private static Logger LOG = Logger.getLogger(DecacMain.class);
    
    public static void main(String[] args) {
        // example log4j message.
        LOG.info("Decac compiler started");
        boolean error = false;
        final CompilerOptions options = new CompilerOptions();
        try {
            options.parseArgs(args);
            LOG.trace("Niveau 3 activé");
            LOG.debug("Niveau 2 activé");
        } catch (CLIException e) {
            System.err.println("Error during option parsing:\n"
                    + e.getMessage());
            options.displayUsage();
            System.exit(1);
        }
        if (options.getPrintBanner()) {
            System.out.println("Projet GL38\n" +
                    "\n" +
                    "   _     _      _     _      _     _      _     _      _     _      _     _   \n" +
                    "  (c).-.(c)    (c).-.(c)    (c).-.(c)    (c).-.(c)    (c).-.(c)    (c).-.(c)  \n" +
                    "   / ._. \\      / ._. \\      / ._. \\      / ._. \\      / ._. \\      / ._. \\   \n" +
                    " __\\( Y )/__  __\\( Y )/__  __\\( Y )/__  __\\( Y )/__  __\\( Y )/__  __\\( Y )/__ \n" +
                    "(_.-/'-'\\-._)(_.-/'-'\\-._)(_.-/'-'\\-._)(_.-/'-'\\-._)(_.-/'-'\\-._)(_.-/'-'\\-._)\n" +
                    "   || P ||      || R ||      || O ||      || J ||      || E ||      || T ||   \n" +
                    " _.' `-' '._  _.' `-' '._  _.' `-' '._  _.' `-' '._  _.' `-' '._  _.' `-' '._ \n" +
                    "(.-./`-'\\.-.)(.-./`-`\\.-.)(.-./`-'\\.-.)(.-./`-'\\.-.)(.-./`-'\\.-.)(.-./`-'\\.-.)\n" +
                    " `-_     `-'  `-'     `-'  `-'     `-'  `-'     `-'  `-'     `-'  `-'     `-' \n" +
                    "  (c).-.(c)    (c).-.(c)    (c).-.(c)    (c).-.(c)\n" +
                    "   / ._. \\      / ._. \\\t     / ._. \\      / ._. \\                                                       \n" +
                    " __\\( Y )/__  __\\( Y )/__  __\\( Y )/__  __\\( Y )/__                                                  \n" +
                    "(_.-/'-'\\-._)(_.-/'-'\\-._)(_.-/'-'\\-._)(_.-/'-'\\-._)                                                    \n" +
                    "   || G ||      || L ||      || 3 ||      || 8 ||                                                 \n" +
                    " _.' `-' '._  _.' `-' '._  _.' `-' '._  _.' `-' '._                                                   \n" +
                    "(.-./`-'\\.-.)(.-./`-'\\.-.)(.-./`-'\\.-.)(.-./`-'\\.-.)                                                    \n" +
                    " `-'     `-'  `-'     `-'  `-'     `-'  `-'     `-'   ");
            System.exit(0);
        }
        if (options.getSourceFiles().isEmpty()) {
            options.displayUsage();
            System.out.println("\n. -b     (banner)        : affiche une bannière indiquant le nom de l'équipe\n"
                    + ". -p     (parse)         : arrête decac après l'étape de construction de\n"
                    + "                           l'arbre, et affiche la décompilation de ce dernier\n"
                    + "                           (i.e. s'il n'y a qu'un fichier source à\n"
                    + "                           compiler, la sortie doit être un programme\n"
                    + "                           deca syntaxiquement correct)\n"
                    + ". -v     (verification)  : arrête decac après l'étape de vérifications\n"
                    + "                           (ne produit aucune sortie en l'absence d'erreur)\n"
                    + ". -n     (no check)      : supprime les tests à l'exécution spécifiés dans\n"
                    + "                           les points 11.1 et 11.3 de la sémantique de Deca.\n"
                    + ". -r X   (registers)     : limite les registres banalisés disponibles à\n"
                    + "                           R0 ... R{X-1}, avec 4 <= X <= 16\n"
                    + ". -d     (debug)         : active les traces de debug. Répéter\n"
                    + "                           l'option plusieurs fois pour avoir plus de\n"
                    + "                           traces.\n"
                    + ". -P     (parallel)      : s'il y a plusieurs fichiers sources,\n"
                    + "                           lance la compilation des fichiers en\n"
                    + "                           parallèle (pour accélérer la compilation)");
        }
        if (options.getParallel()) {
            ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
            ArrayList<Future<Boolean>> futures = new ArrayList<>();

            for (File source : options.getSourceFiles()) {
                DecacCompiler compiler = new DecacCompiler(options, source);
                Callable<Boolean> compilationTask = compiler::compile;
                futures.add(executor.submit(compilationTask));

            }

            for (Future<Boolean> future : futures) {
                try {
                    if (future.get()) {
                        error = true;
                    }
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
            }

            executor.shutdown();
        } else {
            for (File source : options.getSourceFiles()) {
                DecacCompiler compiler = new DecacCompiler(options, source);
                if (compiler.compile()) {
                    error = true;
                }
            }
        }
        System.exit(error ? 1 : 0);
    }
}
