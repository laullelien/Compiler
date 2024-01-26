package fr.ensimag.deca.tree;


import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.ImmediateFloat;
import fr.ensimag.ima.pseudocode.ImmediateInteger;
import fr.ensimag.ima.pseudocode.instructions.ADD;

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
    protected void codeGenBinary(DecacCompiler compiler) {
        compiler.addInstruction(new ADD(compiler.getDVal(), compiler.getRegister()));
    }

    @Override
    ImmediateInteger compute(int leftVal, int rightVal) {
        return new ImmediateInteger(leftVal + rightVal);
    }

    @Override
    ImmediateFloat compute(float leftVal, int rightVal) {
        return new ImmediateFloat(leftVal + rightVal);
    }

    @Override
    ImmediateFloat compute(int leftVal, float rightVal) {
        return new ImmediateFloat(leftVal + rightVal);
    }

    @Override
    ImmediateFloat compute(float leftVal, float rightVal) {
        return new ImmediateFloat(leftVal + rightVal);
    }
}
