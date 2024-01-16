package fr.ensimag.deca.tree;

import fr.ensimag.deca.tools.IndentPrintStream;

import java.io.PrintStream;

public abstract class AbstractDeclMethod extends Tree {
    private AbstractIdentifier methodReturnType;
    private AbstractIdentifier methodName;
    private ListDeclParam methodParameters;
    private AbstractMethodBody methodBody;

    public AbstractDeclMethod(AbstractIdentifier methodReturnType, AbstractIdentifier methodName, ListDeclParam methodParameters, AbstractMethodBody methodBody) {
        this.methodReturnType = methodReturnType;
        this.methodName = methodName;
        this.methodParameters = methodParameters;
        this.methodBody = methodBody;
    }

    @Override
    public void decompile(IndentPrintStream s) {
        methodReturnType.decompile(s);
        s.print(" ");
        methodName.decompile(s);
        s.print(" (");
        methodParameters.decompile(s);
        s.print(") ");
        methodBody.decompile(s);
        s.println();
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        methodReturnType.prettyPrint(s, prefix, false);
        methodName.prettyPrint(s, prefix, false);
        methodParameters.prettyPrint(s, prefix, false);
        methodBody.prettyPrint(s, prefix, true);
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        methodReturnType.iter(f);
        methodName.iter(f);
        methodParameters.iter(f);
        methodBody.iter(f);
    }
}
