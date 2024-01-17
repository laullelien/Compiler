package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ContextualError;

/**
 * Class declaration.
 *
 * @author gl38
 * @date 01/01/2024
 */
public abstract class AbstractDeclClass extends Tree {

    protected void setDefinition(DecacCompiler compiler){
        
    }

    /**
     * Pass 1 of [SyntaxeContextuelle]. Verify that the class declaration is OK
     * without looking at its content.
     */
    protected abstract void verifyClass(DecacCompiler compiler)
            throws ContextualError;

    /**
     * Pass 2 of [SyntaxeContextuelle]. Verify that the class members (fields and
     * methods) are OK, without looking at method body and field initialization.
     */
    protected abstract void verifyClassMembers(DecacCompiler compiler)
            throws ContextualError;

    /**
     * Pass 3 of [SyntaxeContextuelle]. Verify that instructions and expressions
     * contained in the class are OK.
     */
    protected abstract void verifyClassBody(DecacCompiler compiler)
            throws ContextualError;

    /**
     * Pass 1 of [Codegen]. Generate the VTable of the class and append the instructions
     */
    protected abstract void codeGenClassVTable(DecacCompiler compiler);

    /**
     * Pass 2 of [Codegen]. Generate the instructions for field initialization and methods body
     */
    protected abstract void codeGenClassBody(DecacCompiler compiler);

}
