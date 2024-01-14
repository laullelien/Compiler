package fr.ensimag.deca.tree;


import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.SEQ;

/**
 *
 * @author gl38
 * @date 01/01/2024
 */
public class Equals extends AbstractOpExactCmp {

    public Equals(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    protected void codeGenInstruction(DecacCompiler compiler, DVal value, GPRegister target) {
        compiler.addInstruction(new LOAD(value, target));
        compiler.addInstruction(new SEQ(target));
    }

    @Override
    protected String getOperatorName() {
        return "==";
    }    
    
}
