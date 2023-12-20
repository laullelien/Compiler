package fr.ensimag.deca;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

/**
 * User-specified options influencing the compilation.
 *
 * @author gl38
 * @date 01/01/2024
 */
public class CompilerOptions {
    public static final int QUIET = 0;
    public static final int INFO = 1;
    public static final int DEBUG = 2;
    public static final int TRACE = 3;

    public int getDebug() {
        return debug;
    }

    public boolean getParallel() {
        return parallel;
    }

    public boolean getPrintBanner() {
        return printBanner;
    }

    public boolean getParse() {
        return parse;
    }

    public List<File> getSourceFiles() {
        return Collections.unmodifiableList(sourceFiles);
    }

    private int debug = 0;

    private boolean noCheck = false;
    private boolean parallel = false;

    private boolean verification = false;
    private boolean printBanner = false;

    private boolean parse = false;

    private boolean customRegister = false;

    // On choisit tous les registres par défault
    private int nbRegisters = 16;

    private List<File> sourceFiles = new ArrayList<File>();

    public void parseArgs(String[] args) throws CLIException {

        boolean expectDeca = false;
        for (int i = 0; i < args.length; i++) {
            String s = args[i];

            if (expectDeca) {
                if (s.endsWith(".deca")) {
                    sourceFiles.add(new File(s));
                } else {
                    throw new CLIException("Les options doivent être placé avant les fichiers .deca");
                }
            }
            if (s.equals("-b")) {
                if (args.length == 1) {
                    this.printBanner = true;
                } else {
                    throw new CLIException("-b doit être tout seul en tant d'argument");
                }
            } else if (s.equals("-p")) {
                this.parse = true;
            } else if (s.equals("-v")) {
                this.verification = true;
            } else if (s.equals("-n")) {
                this.noCheck = true;
            } else if (s.equals("-r")) {
                try {
                    this.customRegister = true;
                    int nbRegisters = Integer.parseInt(args[i + 1]);
                    if (nbRegisters < 4 || nbRegisters > 16) {
                        throw new CLIException("Le nombre de registres n'est pas valide (entre 4 et 16)");
                    }
                    this.nbRegisters = nbRegisters;
                } catch (NumberFormatException e) {
                    throw new CLIException("Il faut renseigner un nombre de registres entre 4 et 16");
                } catch (IndexOutOfBoundsException e) {
                    throw new CLIException("Il faut renseigner un nombre de registres entre 4 et 16");
                }
            } else if (s.equals("-d")) {
                this.debug += 1;
            } else if (s.equals("-P")) {
                this.parallel = true;
            } else {
                if (s.endsWith(".deca")) {
                    expectDeca = true;
                    sourceFiles.add(new File(s));
                } else {
                    throw new CLIException("Argument " + s + " non reconnu");
                }
            }
        }

        if (this.parse && this.verification)
            throw new CLIException("Impossible de faire -p et -v en même temps, il faut choisir l'un ou l'autre");

        if ((this.parse | this.verification | this.noCheck | this.customRegister | this.debug > 0 | this.parallel)
                && !expectDeca)
            throw new CLIException("Fichier(s) source(s) manquant(s)");

        Logger logger = Logger.getRootLogger();
        // map command-line debug option to log4j's level.
        switch (getDebug()) {
            case QUIET:
                break; // keep default
            case INFO:
                logger.setLevel(Level.INFO);
                break;
            case DEBUG:
                logger.setLevel(Level.DEBUG);
                break;
            case TRACE:
                logger.setLevel(Level.TRACE);
                break;
            default:
                logger.setLevel(Level.ALL);
                break;
        }
        logger.info("Application-wide trace level set to " + logger.getLevel());

        boolean assertsEnabled = false;
        assert assertsEnabled = true; // Intentional side effect!!!
        if (assertsEnabled) {
            logger.info("Java assertions enabled");
        } else {
            logger.info("Java assertions disabled");
        }
    }

    protected void displayUsage() {
        System.out.println("Usage: decac [[-p | -v] [-n] [-r X] [-d]* [-P] [-w] <fichier deca>...] | [-b]");
    }
}
