package fr.ensimag.deca.tree;


import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.*;

/**
 *
 * @author gl38
 * @date 01/01/2024
 */
public class NotEquals extends AbstractOpExactCmp {

    public NotEquals(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    protected void codeGenBinary(DecacCompiler compiler) {
        super.codeGenBinary(compiler);
        compiler.addInstruction(new SNE(compiler.getRegister()));
    }

    @Override
    protected String getOperatorName() {
        return "!=";
    }

    @Override
    public void codeGenCond(DecacCompiler compiler, boolean eq, Label jumpLabel) {
        codeGenValues(compiler);
        compiler.addInstruction(new CMP(compiler.getDVal(), compiler.getRegister()));
        if(eq) {
            compiler.addInstruction(new BNE(jumpLabel));
        }
        else {
            compiler.addInstruction(new BEQ(jumpLabel));
        }
    }
}
