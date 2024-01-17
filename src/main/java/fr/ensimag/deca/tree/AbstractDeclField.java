package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.*;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.deca.tools.SymbolTable;

import java.io.PrintStream;

abstract public class AbstractDeclField extends Tree {
    private Visibility fieldVisibility;
    private AbstractIdentifier fieldType;
    private AbstractIdentifier fieldName;
    private AbstractInitialization fieldInitialization;
    private Type type;
    private ExpDefinition definition;

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

    abstract public EnvironmentExp verifyDeclField(DecacCompiler compiler, SymbolTable.Symbol superClassSymbol, ClassDefinition classDef) throws ContextualError;

    abstract public void verifyDeclField(DecacCompiler compiler, EnvironmentExp env, ClassDefinition classDef) throws ContextualError;

    @Override
    public void decompile(IndentPrintStream s) {

    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {

    }

    @Override
    protected void iterChildren(TreeFunction f) {

    }

    public Type getType() {
        return type;
    }

    public ExpDefinition getDefinition() {
        return definition;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public void setDefinition(ExpDefinition definition) {
        this.definition = definition;
    }
}
