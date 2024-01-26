package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ContextualError;

/**
 * Entry point for contextual verifications and code generation from outside the package.
 * 
 * @author gl38
 * @date 01/01/2024
 *
 */
public abstract class AbstractProgram extends Tree {
    public abstract void verifyProgram(DecacCompiler compiler) throws ContextualError;

    /**
     * [Extension OPTIM]
     * Modifie l'Arbre Enrichi pour obtenir une Représentation Intermédiaire (SSA form)
     * et lancer des algorithmes d'optimisation
     */
    public abstract void optimizeProgram(DecacCompiler compiler);

    public abstract void codeGenProgram(DecacCompiler compiler) ;
}
