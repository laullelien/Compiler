package fr.ensimag.deca.tree;


import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.*;

/**
 * @author gl38
 * @date 01/01/2024
 */
public class Minus extends AbstractOpArith {
    public Minus(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }



    @Override
    protected String getOperatorName() {
        return "-";
    }

//    @Override
//    protected void codeGenPrint(DecacCompiler compiler) {
//        operationDepth++;
//        if (this.getLeftOperand() instanceof IntLiteral) {
//            if (this.getRightOperand() instanceof IntLiteral) {
//                if (!isR1Init) {
//                    compiler.addInstruction(new LOAD(this.getRightOperand().getNegativeDval(), Register.R1));
//                    isR1Init = true;
//                }else {
//                    compiler.addInstruction(new LOAD(this.getRightOperand().getNegativeDval(), Register.R0));
//                    isR0Init = true;
//                }
//
//            }
//            else {
//                this.getRightOperand().codeGenPrint(compiler);
//                if (this.getRightOperand() instanceof Multiply && isR0Init) {
//                    compiler.addInstruction(new ADD(Register.R0, Register.R1));
//                    isR0Init = false;
//                }
//            }
//            compiler.addInstruction(new ADD(this.getLeftOperand().getDval(), Register.R1));
//        }
//        else {
//            this.getLeftOperand().codeGenPrint(compiler);
//            if (this.getLeftOperand() instanceof Multiply && isR0Init)
//                compiler.addInstruction(new ADD(Register.R0, Register.R1));
//            isR0Init = false;
//            if (this.getRightOperand() instanceof IntLiteral) {
//                compiler.addInstruction(new SUB(this.getRightOperand().getDval(), Register.R1));
//            }
//            else {
//                this.getRightOperand().codeGenPrint(compiler);
//                if (this.getRightOperand() instanceof Multiply)
//                    if (isR0Init) compiler.addInstruction(new SUB(Register.R0, Register.R1));
//
//            }
//        }
//        // on vérifie si on est à la racine
//        if (operationDepth == 1) {
//            // on remet isR1Init et isR0Init a leur valeur initiale pour la prochaine opération
//            isR1Init = false;
//            isR0Init = false;
//            compiler.addInstruction(new WINT());
//        }
//        else operationDepth--;
//    }

    @Override
    protected void codeGenInst(DecacCompiler compiler) {
        super.codeGenInst(compiler);
        compiler.addInstruction(new SUB(Register.getR(3), Register.getR(2)));
    }
}
