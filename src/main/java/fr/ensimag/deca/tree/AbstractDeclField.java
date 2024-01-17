package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.*;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.deca.tools.SymbolTable;

import java.io.PrintStream;

public abstract class AbstractDeclField extends Tree {
    private Visibility fieldVisibility;
    private AbstractIdentifier fieldType;
    private AbstractIdentifier fieldName;
    private AbstractInitialization fieldInitialization;
    private Type type;
    private ExpDefinition definition;

    @Override
    String printNodeLine(PrintStream s, String prefix, boolean last,
                         boolean inlist, String nodeName) {
        s.print(prefix);
        if (inlist) {
            s.print("[]>");
        } else if (last) {
            s.print("`>");
        } else {
            s.print("+>");
        }
        if (getLocation() != null) {
            s.print(" " + getLocation().toString());
        }
        if (getFieldVisibility() != null){
            s.print(" " + getFieldVisibility().toString());
        }
        s.print(" ");
        s.print(nodeName);
        s.println();
        String newPrefix;
        if (last) {
            if (inlist) {
                newPrefix = prefix + "    ";
            } else {
                newPrefix = prefix + "   ";
            }
        } else {
            if (inlist) {
                newPrefix = prefix + "||  ";
            } else {
                newPrefix = prefix + "|  ";
            }
        }
        prettyPrintType(s, newPrefix);
        return newPrefix;
    }

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

    abstract public void verifyDeclFieldPass3(DecacCompiler compiler, EnvironmentExp env, ClassDefinition classDef) throws ContextualError;

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

    public void setFieldVisibility(Visibility v) {this.fieldVisibility = v;}
}
