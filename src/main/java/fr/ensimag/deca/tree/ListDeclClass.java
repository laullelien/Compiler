package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.tools.IndentPrintStream;
import org.apache.log4j.Logger;

/**
 *
 * @author gl38
 * @date 01/01/2024
 */
public class ListDeclClass extends TreeList<AbstractDeclClass> {
    private static final Logger LOG = Logger.getLogger(ListDeclClass.class);
    
    @Override
    public void decompile(IndentPrintStream s) {
        for (AbstractDeclClass c : getList()) {
            c.decompile(s);
            s.println();
        }
    }

    /**
     * Pass 1 of [SyntaxeContextuelle]
     */
    void verifyListClass(DecacCompiler compiler) throws ContextualError {
        LOG.debug("verify listClass: start");
        // To add decorations
        compiler.environmentType.objectClassIdentifier.verifyObjectClass(compiler);
        // rule 1.2
        for (AbstractDeclClass c : this.getList()) {
            c.verifyClass(compiler);
        }
        LOG.debug("verify listClass: end");
    }

    /**
     * Pass 2 of [SyntaxeContextuelle]
     */
    public void verifyListClassMembers(DecacCompiler compiler) throws ContextualError {
        LOG.debug("verify listClassMembers: start");
        for (AbstractDeclClass c : this.getList()) {
            c.verifyClassMembers(compiler);
        }
        LOG.debug("verify listClassMembers: end");
    }

    /**
     * Pass 3 of [SyntaxeContextuelle]
     */
    public void verifyListClassBody(DecacCompiler compiler) throws ContextualError {
        LOG.debug("verify listClassBody: start");
        for (AbstractDeclClass c : this.getList()) {
            c.verifyClassBody(compiler);
        }
        LOG.debug("verify listClassBody: end");
    }

    /**
     * Pass 1 of [Gencode]
     */
    public void codeGenListClassVTables(DecacCompiler compiler) {
        LOG.debug("codeGen listClassVTables: start");
        // creation de la VTable de Object
        compiler.listVTable.codeGenVTable(compiler, "Object", null, null);
        for (AbstractDeclClass c : this.getList()) {
            c.codeGenClassVTable(compiler);
        }
        LOG.debug("codeGen listClassVTables: end");
    }

    /**
     * Pass 2 of [Gencode]
     */
    public void codeGenListClassBody(DecacCompiler compiler) {
        LOG.debug("codeGen listClassBody: start");
        // on commence par générer la méthode equals de Object
        compiler.codegenHelper.codeGenObjectEquals();
        for (AbstractDeclClass c : this.getList()) {
            c.codeGenClassBody(compiler);
        }
        LOG.debug("codeGen listClassBody: end");
    }
}
