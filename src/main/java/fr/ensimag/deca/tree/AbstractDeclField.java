package fr.ensimag.deca.tree;

import fr.ensimag.deca.tools.IndentPrintStream;

import java.io.PrintStream;

public class AbstractDeclField extends Tree {
    private Visibility fieldVisibility;
    private AbstractIdentifier fieldType;
    private AbstractIdentifier fieldName;
    private AbstractInitialization fieldInitialization;

    public AbstractDeclField(Visibility fieldVisibility, AbstractIdentifier fieldType, AbstractIdentifier fieldName,
                             AbstractInitialization fieldInitialization) {
        this.fieldVisibility = fieldVisibility;
        this.fieldType = fieldType;
        this.fieldName = fieldName;
        this.fieldInitialization = fieldInitialization;
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
