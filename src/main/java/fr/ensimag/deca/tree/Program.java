package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.TypeDefinition;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.deca.tools.SymbolTable;
import fr.ensimag.ima.pseudocode.*;
import java.io.PrintStream;

import fr.ensimag.ima.pseudocode.instructions.*;
import org.apache.commons.lang.Validate;
import org.apache.log4j.Logger;

/**
 * Deca complete program (class definition plus main block)
 *
 * @author gl38
 * @date 01/01/2024
 */
public class Program extends AbstractProgram {
    private static final Logger LOG = Logger.getLogger(Program.class);
    
    public Program(ListDeclClass classes, AbstractMain main) {
        Validate.notNull(classes);
        Validate.notNull(main);
        this.classes = classes;
        this.main = main;
    }
    public ListDeclClass getClasses() {
        return classes;
    }
    public AbstractMain getMain() {
        return main;
    }
    private ListDeclClass classes;
    private AbstractMain main;

    @Override
    public void verifyProgram(DecacCompiler compiler) throws ContextualError {
        LOG.debug("verify program: start");
        classes.verifyListClass(compiler); // Etape B, passe 1
        classes.verifyListClassMembers(compiler); // Etape B, passe 2
        classes.verifyListClassBody(compiler); // Etape B, passe 3 pour les classes
        main.verifyMain(compiler); // Etape B, passe 3 pour le programme principal
        LOG.debug("verify program: end");
    }

    @Override
    public void optimizeProgram(DecacCompiler compiler) {
        // on optimise les noeuds avec des ListInst
        classes.optimizeListClassBody(compiler); // passe 1, optimisation des corps de méthode
        main.optimizeMain(compiler); // passe 2, optimisation de main
    }

    @Override
    public void codeGenProgram(DecacCompiler compiler) {
        compiler.addComment("Main program");
        compiler.codegenHelper.codeGenTSTO();
        classes.codeGenListClassVTables(compiler); // Etape C, passe 1
        main.codeGenMain(compiler); // Etape C, passe 2 pour le programme principal
        compiler.codegenHelper.setMainInst();
        compiler.addInstruction(new HALT());
        classes.codeGenListClassBody(compiler); // Etape C, passe 2 pour les classes
        compiler.codegenHelper.codeGenListError();
    }

    @Override
    public void decompile(IndentPrintStream s) {
        getClasses().decompile(s);
        getMain().decompile(s);
    }
    
    @Override
    protected void iterChildren(TreeFunction f) {
        classes.iter(f);
        main.iter(f);
    }
    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        classes.prettyPrint(s, prefix, false);
        main.prettyPrint(s, prefix, true);
    }
}
