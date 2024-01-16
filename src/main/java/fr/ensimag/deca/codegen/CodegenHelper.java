package fr.ensimag.deca.codegen;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.*;

public class CodegenHelper {

    private DecacCompiler compiler;

    public CodegenHelper(DecacCompiler compiler) {
        this.compiler = compiler;
    }

    public void codeGenListError() {
        compiler.addComment("error handling");
        compiler.addLabel(new Label("stack_full"));
        compiler.addInstruction(new WSTR("La pile est pleine"));
        compiler.addInstruction(new WNL());
        compiler.addInstruction(new ERROR());

        compiler.addLabel(new Label("division_by_0"));
        compiler.addInstruction(new WSTR("Erreur de division par 0"));
        compiler.addInstruction(new WNL());
        compiler.addInstruction(new ERROR());

        compiler.addLabel(new Label("input_error"));
        compiler.addInstruction(new WSTR("Depassement ou mauvais format"));
        compiler.addInstruction(new WNL());
        compiler.addInstruction(new ERROR());
    }

    public void codeGenTSTO() {
        compiler.addInstruction(new TSTO(compiler.getNbDeclVar()));
        compiler.addInstruction(new BOV(new Label("stack_full")));
        compiler.addInstruction(new ADDSP(compiler.getNbDeclVar()));
    }
}
