package fr.ensimag.deca.tree;


import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.WFLOAT;
import fr.ensimag.ima.pseudocode.instructions.WINT;

/**
 * @author gl38
 * @date 01/01/2024
 */
public class Multiply extends AbstractOpArith {
    public Multiply(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }


    @Override
    protected String getOperatorName() {
        return "*";
    }

    protected void codeGenPrint(DecacCompiler compiler) {
        if (this.getType().isFloat()) {

            compiler.addInstruction(new LOAD(0.0F, Register.R1));
            addOperands(this.getLeftOperand(), compiler, true);
            MultOperands(this.getRightOperand(), compiler, true);
            compiler.addInstruction(new WFLOAT());
        }
        else if (this.getType().isInt()) {

            compiler.addInstruction(new LOAD(0, Register.R1));
            addOperands(this.getLeftOperand(), compiler, false);
            MultOperands(this.getRightOperand(), compiler, false);
            compiler.addInstruction(new WINT());
        }
    }

}
