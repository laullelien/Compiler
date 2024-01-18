package fr.ensimag.deca.codegen;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
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

        compiler.addLabel(new Label("heap_full"));
        compiler.addInstruction(new WSTR("Le tas est pleine"));
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

    public void codeGenObjectEquals() {
        compiler.addComment("Classe Object");
        compiler.addLabel(new Label("code.Object.equals"));
        // Enregistrer this dans R0
        compiler.addInstruction(new LOAD(new RegisterOffset(-2, Register.LB), Register.R0 ));
        // Enregistrer le parametre dans R1
        compiler.addInstruction(new LOAD(new RegisterOffset(-3, Register.LB), Register.R1 ));
        compiler.addInstruction(new CMP(Register.R0, Register.R1));
        compiler.addInstruction(new SEQ(Register.R0));
        compiler.addInstruction(new RTS());
    }
}
