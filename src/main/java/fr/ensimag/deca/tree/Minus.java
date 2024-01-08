package fr.ensimag.deca.tree;


import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.ADD;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.SUB;
import fr.ensimag.ima.pseudocode.instructions.WINT;

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

        compiler.addInstruction(new LOAD(0, Register.R1));
        addOperands(this.getLeftOperand(), compiler);
        subOperands(this.getRightOperand(), compiler);

        compiler.addInstruction(new WINT());
    }


    
}
