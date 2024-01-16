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

    public Visibility getFieldVisibility() {
        return fieldVisibility;
    }

    public AbstractIdentifier getFieldType() {
        return fieldType;
    }

    public AbstractIdentifier getFieldName() {
        return fieldName;
    }

    public AbstractInitialization getFieldInitialization() {
        return fieldInitialization;
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
