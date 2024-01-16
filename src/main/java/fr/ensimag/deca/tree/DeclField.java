package fr.ensimag.deca.tree;

import fr.ensimag.deca.tools.IndentPrintStream;

import java.io.PrintStream;

public class DeclField extends AbstractDeclField {



    public DeclField(Visibility fieldVisibility, AbstractIdentifier fieldType, AbstractIdentifier fieldName, AbstractInitialization fieldInitialization) {
        super(fieldVisibility, fieldType, fieldName, fieldInitialization);
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
