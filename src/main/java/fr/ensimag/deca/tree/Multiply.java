package fr.ensimag.deca.tree;


import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.ImmediateInteger;
import fr.ensimag.ima.pseudocode.instructions.*;

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

    @Override
    protected void codeGenBinary(DecacCompiler compiler) {
        if (getType().isInt()) {
            if (compiler.getDVal() instanceof ImmediateInteger) {
                int value = ((ImmediateInteger) (compiler.getDVal())).getValue();
                if (value == 1) {
                    return;
                } else if (value > 0 && (((value - 1) & value) == 0)) {
                    // it is a power of 2
                    if (value >= 1 << 10) {
                        // no opti
                        compiler.addInstruction(new MUL(compiler.getDVal(), compiler.getRegister())); // division entière
                        return;
                    }
                    while (value != 1) {
                        compiler.addInstruction(new SHL(compiler.getRegister()));
                        value >>= 1;
                    }
                    return;
                }
            }
        }
        compiler.addInstruction(new MUL(compiler.getDVal(), compiler.getRegister()));
   }

    @Override
    IntLiteral compute(DecacCompiler compiler, int leftVal, int rightVal) {
        return new IntLiteral(leftVal * rightVal, compiler);
    }

    @Override
    FloatLiteral compute(DecacCompiler compiler, float leftVal, int rightVal) {
        return new FloatLiteral(leftVal * rightVal, compiler);
    }

    @Override
    FloatLiteral compute(DecacCompiler compiler, int leftVal, float rightVal) {
        return new FloatLiteral(leftVal * rightVal, compiler);
    }

    @Override
    FloatLiteral compute(DecacCompiler compiler, float leftVal, float rightVal) {
        return new FloatLiteral(leftVal * rightVal, compiler);
    }
}
