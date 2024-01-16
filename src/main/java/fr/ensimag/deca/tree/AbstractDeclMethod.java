package fr.ensimag.deca.tree;

import fr.ensimag.deca.tools.IndentPrintStream;

import java.io.PrintStream;

public class AbstractDeclMethod extends Tree {
    private AbstractIdentifier methodReturnType;
    private AbstractIdentifier methodName;

    public AbstractIdentifier getMethodName() {
        return methodName;
    }

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

    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {

    }

    @Override
    protected void iterChildren(TreeFunction f) {

    }
}
