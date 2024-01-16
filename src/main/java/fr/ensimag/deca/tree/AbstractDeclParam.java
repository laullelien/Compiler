package fr.ensimag.deca.tree;

import fr.ensimag.deca.tools.IndentPrintStream;

import java.io.PrintStream;

public class AbstractDeclParam extends Tree{
    private AbstractIdentifier paramType;
    private AbstractIdentifier paramName;

    public AbstractDeclParam(AbstractIdentifier paramType, AbstractIdentifier paramName) {
        this.paramType = paramType;
        this.paramName = paramName;
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
