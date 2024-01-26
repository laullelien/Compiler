package fr.ensimag.deca.tree;


import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.*;

/**
 * Operator "x >= y"
 * 
 * @author gl38
 * @date 01/01/2024
 */
public class GreaterOrEqual extends AbstractOpIneq {

    public GreaterOrEqual(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    protected void codeGenBinary(DecacCompiler compiler) {
        super.codeGenBinary(compiler);
        compiler.addInstruction(new SGE(compiler.getRegister()));
    }

    @Override
    protected String getOperatorName() {
        return ">=";
    }

    @Override
    public void codeGenCond(DecacCompiler compiler, boolean eq, Label jumpLabel) {
        codeGenValues(compiler);
        compiler.addInstruction(new CMP(compiler.getDVal(), compiler.getRegister()));
        if(eq) {
            compiler.addInstruction(new BGE(jumpLabel));
        }
        else {
            compiler.addInstruction(new BLT(jumpLabel));
        }
    }
}
