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
        if (this.getType().isFloat()) {

            compiler.addInstruction(new LOAD(0.0F, Register.R1));
            addOperands(this.getLeftOperand(), compiler, true);
            addOperands(this.getRightOperand(), compiler, true);
            compiler.addInstruction(new WFLOAT());
        }
        else if (this.getType().isInt()) {

            compiler.addInstruction(new LOAD(0, Register.R1));
            addOperands(this.getLeftOperand(), compiler, false);
            addOperands(this.getRightOperand(), compiler, false);
            compiler.addInstruction(new WINT());
        }
    }

}