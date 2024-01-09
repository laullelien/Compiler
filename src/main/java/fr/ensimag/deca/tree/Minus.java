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

    @Override
    protected void codeGenPrint(DecacCompiler compiler) {
        if (this.getRightOperand().getType().isFloat()) {

            compiler.addInstruction(new LOAD(0.0F, Register.R1));
            addOperands(this.getLeftOperand(), compiler);
            subOperands(this.getRightOperand(), compiler);
            compiler.addInstruction(new WFLOAT());
        }
        else if (this.getRightOperand().getType().isInt()) {

            compiler.addInstruction(new LOAD(0, Register.R1));
            addOperands(this.getLeftOperand(), compiler);
            subOperands(this.getRightOperand(), compiler);
            compiler.addInstruction(new WINT());
        }
    }


    
}
