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
    public EnvironmentExp verifyDeclField(DecacCompiler compiler, SymbolTable.Symbol superClassSymbol, SymbolTable.Symbol className) throws ContextualError {
        // rule 2.5
        EnvironmentExp env = new EnvironmentExp();
        Type fieldType = getFieldType().verifyType(compiler);

        if(fieldType.isVoid()) {
            throw new ContextualError("Field de type void. Cela ne respecte pas la regle 2.5", this.getLocation());
        }

        TypeDefinition superEnv = compiler.environmentType.defOfType(superClassSymbol);
        if(superEnv != null && superEnv.isClass()) {
            if(!((ClassDefinition)superEnv).getSuperClass().getMembers().get(getFieldName().getName()).isField()) {
                throw new ContextualError("Cette variable apparait dans la classe super mais n'est pas un field. Cela ne respecte pas la regle 2.5", this.getLocation());
            }
        }

        TypeDefinition definition = compiler.environmentType.defOfType(className);
        if(!definition.isClass()) {
            throw new RuntimeException("Cela devrait etre impossible, que s'est il passe?");
        }

        ClassDefinition classDefinition = ((ClassDefinition)definition);
        classDefinition.incNumberOfFields();
        ExpDefinition def = new FieldDefinition(fieldType, this.getLocation(), getFieldVisibility(), classDefinition, classDefinition.getNumberOfFields());

        try {
            env.declare(getFieldName().getName(), def);
        } catch (EnvironmentExp.DoubleDefException e) {
            throw new RuntimeException("Cela devrait etre impossible, que s'est il passe?");
        }

        setType(fieldType);
        setDefinition(def);

        return env;
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
        getFieldVisibility().prettyPrint(s, prefix, false);
        getFieldType().prettyPrint(s, prefix, false);
        getFieldName().prettyPrint(s, prefix, false);
        getFieldInitialization().prettyPrint(s, prefix, true);

    }
}
