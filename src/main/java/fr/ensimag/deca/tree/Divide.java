package fr.ensimag.deca.tree;


import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.*;

import java.sql.Struct;

/**
 *
 * @author gl38
 * @date 01/01/2024
 */
public class Divide extends AbstractOpArith {
    public Divide(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    protected void codeGenPrint(DecacCompiler compiler) {
        operationDepth++;
        if (this.getRightOperand() instanceof IntLiteral) {
            if (this.getLeftOperand() instanceof IntLiteral) {
                if (!isR1Init) {
                    compiler.addInstruction(new LOAD(this.getLeftOperand().getDval(), Register.R1));
                    isR1Init = true;
                } else {
                    compiler.addInstruction(new LOAD(this.getLeftOperand().getDval(), Register.R0));
                    isR0Init = true;
                }
            } else this.getRightOperand().codeGenPrint(compiler);
            if (isR1Init && isR0Init)
                compiler.addInstruction(new QUO(this.getRightOperand().getDval(), Register.R0));
            else

                compiler.addInstruction(new QUO(this.getRightOperand().getDval(), Register.R1));
        } else {
            this.getRightOperand().codeGenPrint(compiler);
            if (isR0Init) {
                compiler.addInstruction(new QUO(this.getLeftOperand().getDval(), Register.R0));
            } else compiler.addInstruction(new QUO(this.getLeftOperand().getDval(), Register.R1));
        }
        // on vérifie si on est à la racine
        if (operationDepth == 1) {
            // on remet isR1Init et isR0Init a leur valeur initiale pour la prochaine opération
            isR1Init = false;
            isR0Init = false;
            compiler.addInstruction(new WINT());
        } else operationDepth--;
    }



    @Override
    protected String getOperatorName() {
        return "/";
    }

}
