package fr.ensimag.deca.tree;


import fr.ensimag.deca.DecacCompiler;
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
    protected void codeGenBinary(DecacCompiler compiler) {
        compiler.addInstruction(new SUB(compiler.getDVal(), compiler.getRegister()));
    }

    @Override
    IntLiteral compute(DecacCompiler compiler, int leftVal, int rightVal) {
        return new IntLiteral(leftVal - rightVal, compiler);
    }

    @Override
    FloatLiteral compute(DecacCompiler compiler, float leftVal, int rightVal) {
        return new FloatLiteral(leftVal - rightVal, compiler);
    }

    @Override
    FloatLiteral compute(DecacCompiler compiler, int leftVal, float rightVal) {
        return new FloatLiteral(leftVal - rightVal, compiler);
    }

    @Override
    FloatLiteral compute(DecacCompiler compiler, float leftVal, float rightVal) {
        return new FloatLiteral(leftVal - rightVal, compiler);
    }
}
