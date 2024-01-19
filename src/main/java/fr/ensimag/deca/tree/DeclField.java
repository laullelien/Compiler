package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.*;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.deca.tools.SymbolTable;

import java.io.PrintStream;

public class DeclField extends AbstractDeclField {

    public DeclField(Visibility fieldVisibility, AbstractIdentifier fieldType, AbstractIdentifier fieldName, AbstractInitialization fieldInitialization) {
        super(fieldVisibility, fieldType, fieldName, fieldInitialization);
    }

    @Override
    public EnvironmentExp verifyDeclField(DecacCompiler compiler, SymbolTable.Symbol superClassSymbol, ClassDefinition classDef) throws ContextualError {
        // rule 2.5
        EnvironmentExp env = new EnvironmentExp();
        Type fieldType = getFieldType().verifyType(compiler);
        if(fieldType.isVoid()) {
            throw new ContextualError("Field de type void. Cela ne respecte pas la regle 2.5", this.getLocation());
        }
        TypeDefinition superEnv = compiler.environmentType.defOfType(superClassSymbol);
        if(superEnv != null && superEnv.isClass()) {
            ExpDefinition newField = ((ClassDefinition)superEnv).getMembers().get(getFieldName().getName());
            if(!(newField == null || (newField != null && newField.isField()))) {
                throw new ContextualError("Cette variable apparait dans la classe super mais n'est pas un field. Cela ne respecte pas la regle 2.5", this.getLocation());
            }
        }
        else{
            throw new ContextualError("La règle (2.3) n'est pas respectée car la super classe n'est pas définie correctement.", this.getLocation());
        }

        ExpDefinition def = new FieldDefinition(fieldType, this.getLocation(), getFieldVisibility(), classDef, classDef.getNumberOfFields());

        try {
            env.declare(getFieldName().getName(), def);
        } catch (EnvironmentExp.DoubleDefException e) {
            throw new RuntimeException("Cela devrait etre impossible, que s'est il passe?");
        }

        this.getFieldName().setDefinition(def);
        this.getFieldName().setType(fieldType);

        return env;
    }

    @Override
    public void verifyDeclFieldPass3(DecacCompiler compiler, EnvironmentExp env, ClassDefinition classDef) throws ContextualError {
        // rule 3.7
        getFieldInitialization().verifyInitialization(compiler, getFieldType().verifyType(compiler), env, classDef);
    }

    @Override
    public void decompile(IndentPrintStream s) {
        getFieldVisibility().decompile(s);
        getFieldType().decompile(s);
        s.print(" ");
        getFieldName().decompile(s);
        getFieldInitialization().decompile(s);
        s.println(";");
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        getFieldType().iter(f);
        getFieldName().iter(f);
        getFieldInitialization().iter(f);
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        getFieldType().prettyPrint(s, prefix, false);
        getFieldName().prettyPrint(s, prefix, false);
        getFieldInitialization().prettyPrint(s, prefix, true);

    }
}
