package fr.ensimag.deca.codegen;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.ImmediateInteger;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.*;

public class CodegenHelper {

    private DecacCompiler compiler;

    private TSTO mainTSTO;

    private ADDSP mainADDSP;

    private int maxPushDepth;

    private int pushDepth;

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
        compiler.addInstruction(new WSTR("Le tas est plein"));
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

        compiler.addLabel(new Label("return_error"));
        compiler.addInstruction(new WSTR("Une méthode de type de retour non void n'a rien retourné"));
        compiler.addInstruction(new WNL());
        compiler.addInstruction(new ERROR());

        compiler.addLabel(new Label("dereferencement_null"));
        compiler.addInstruction(new WSTR("Déréférencement de null"));
        compiler.addInstruction(new WNL());
        compiler.addInstruction(new ERROR());

        compiler.addLabel(new Label("impossible_cast"));
        compiler.addInstruction(new WSTR("Cast impossible"));
        compiler.addInstruction(new WNL());
        compiler.addInstruction(new ERROR());
    }

    public int getMaxPushDepth() {
        return maxPushDepth;
    }

    public void codeGenTSTO() {
        // The number for TSTO will be modified later
        mainTSTO = new TSTO(0);
        mainADDSP = new ADDSP(0);
        if(!compiler.getCompilerOptions().getOptim()) {
            compiler.addInstruction(mainTSTO);
            compiler.addInstruction(new BOV(new Label("stack_full")));
        }
        compiler.addInstruction(mainADDSP);
    }


    public void setMainInst() {
        mainTSTO.setOperand(new ImmediateInteger(compiler.getNbDeclVar() + maxPushDepth + compiler.listVTable.getOffset()));
        mainADDSP.setOperand(new ImmediateInteger(compiler.getNbDeclVar() + compiler.listVTable.getOffset()));
    }

    public void incPushDepth() {
        pushDepth++;
        if(pushDepth > maxPushDepth) {
            maxPushDepth = pushDepth;
        }
    }

    public void addPushDepth(int i) {
        pushDepth += i;
        if(pushDepth > maxPushDepth) {
            maxPushDepth = pushDepth;
        }
    }

    public void decPushDepth(int i) {
        pushDepth -= i;
    }

    public void decPushDepth() {
        pushDepth--;
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

    public void reset() {
        pushDepth = 0;
        maxPushDepth = 0;
    }
}
