package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.ClassType;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.tools.IndentPrintStream;
import java.io.PrintStream;

/**
 * Declaration of a class (<code>class name extends superClass {members}<code>).
 * 
 * @author gl38
 * @date 01/01/2024
 */
public class DeclClass extends AbstractDeclClass {
    private AbstractIdentifier name;
    private AbstractIdentifier nameSuperClass;
    private ListDeclMethod methods;
    private ListDeclField fields;
    public DeclClass(AbstractIdentifier name, AbstractIdentifier nameSuperClass, ListDeclMethod methods,
                             ListDeclField fields) {
        this.name = name;
        this.nameSuperClass = nameSuperClass;
        this.methods = methods;
        this.fields = fields;
    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.print("class { ... A FAIRE ... }");
    }

    @Override
    protected void verifyClass(DecacCompiler compiler) throws ContextualError {
        throw new UnsupportedOperationException("not yet implemented");
    }

    @Override
    protected void verifyClassMembers(DecacCompiler compiler)
            throws ContextualError {
        throw new UnsupportedOperationException("not yet implemented");
    }
    
    @Override
    protected void verifyClassBody(DecacCompiler compiler) throws ContextualError {
        throw new UnsupportedOperationException("not yet implemented");
    }


    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        name.prettyPrint(s, prefix, true);
        nameSuperClass.prettyPrint(s, prefix, true);
        methods.prettyPrint(s, prefix, false);
        fields.prettyPrint(s, prefix, false);
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        throw new UnsupportedOperationException("Not yet supported");
    }

}
