package fr.ensimag.deca.tree;


import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.*;

/**
 * @author gl38
 * @date 01/01/2024
 */
public class Plus extends AbstractOpArith {
    public Plus(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }


    @Override
    protected String getOperatorName() {
        return "+";
    }


    @Override
    protected void codeGenPrint(DecacCompiler compiler) {
        operationDepth++;
        if (this.getLeftOperand() instanceof IntLiteral) {
            if (this.getRightOperand() instanceof IntLiteral) {
                compiler.addInstruction(new LOAD(this.getRightOperand().getDval(), Register.R1));
            }
            else {
                this.getRightOperand().codeGenPrint(compiler);
                if (this.getRightOperand() instanceof Multiply && isR0Init)
                    compiler.addInstruction(new ADD(Register.R0, Register.R1));
            }
            compiler.addInstruction(new ADD(this.getLeftOperand().getDval(), Register.R1));
        }
        else {
            this.getLeftOperand().codeGenPrint(compiler);
            if (this.getLeftOperand() instanceof Multiply && isR0Init)
                compiler.addInstruction(new ADD(Register.R0, Register.R1));
            if (this.getRightOperand() instanceof IntLiteral)
                compiler.addInstruction(new ADD(this.getRightOperand().getDval(), Register.R1));
            else {
                this.getRightOperand().codeGenPrint(compiler);
                if (this.getRightOperand() instanceof Multiply && isR0Init)
                    compiler.addInstruction(new ADD(Register.R0, Register.R1));
            }
        }
        // on vérifie si on est à la racine
        if (operationDepth == 1) {
            // on remet isR1Init et isR0Init a leur valeur initiale pour la prochaine opération
            isR1Init = false;
            isR0Init = false;
            compiler.addInstruction(new WINT());
        }
        else operationDepth--;
//        if (this.getType().isFloat()) {
//
//            compiler.addInstruction(new LOAD(0.0F, Register.R1));
//            addOperands(this.getLeftOperand(), compiler, true);
//            addOperands(this.getRightOperand(), compiler, true);
//            compiler.addInstruction(new WFLOAT());
//        }
//        else if (this.getType().isInt()) {
//
//            compiler.addInstruction(new LOAD(0, Register.R1));
//            addOperands(this.getLeftOperand(), compiler, false);
//            addOperands(this.getRightOperand(), compiler, false);
//            compiler.addInstruction(new WINT());
//        }
    }

}