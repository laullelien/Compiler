package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.ima.pseudocode.*;
import fr.ensimag.ima.pseudocode.instructions.*;

/**
 *
 * @author gl38
 * @date 01/01/2024
 */
public class Modulo extends AbstractOpArith {

    public Modulo(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        if (this.getLeftOperand().verifyExpr(compiler, localEnv, currentClass).isInt() && this.getRightOperand().verifyExpr(compiler, localEnv, currentClass).isInt()) {
            setType(compiler.environmentType.INT);
            return getType();
        }
        throw new ContextualError("Le type ne respecte pas la règle 3.33", this.getLocation());
    }

    @Override
    protected String getOperatorName() {
        return "%";
    }

    @Override
    protected void codeGenBinary(DecacCompiler compiler) {
        if (!compiler.getCompilerOptions().getNocheck() && !compiler.getCompilerOptions().getOptim()) {
            compiler.addInstruction(new LOAD(compiler.getDVal(), Register.R1));
            compiler.addInstruction(new BEQ(new Label("division_by_0")));
        }
        if(compiler.getDVal() instanceof ImmediateInteger) {
            int val = (((ImmediateInteger) compiler.getDVal()).getValue());
            if(val == 2) {
                if (compiler.isRegisterAvailable()) {
                    GPRegister currReg = compiler.getRegister();
                    compiler.incrementRegister();
                    compiler.addInstruction(new LOAD(currReg, compiler.getRegister()));
                    compiler.addInstruction(new SHR(compiler.getRegister()));
                    compiler.addInstruction(new SHL(compiler.getRegister()));
                    compiler.addInstruction(new SUB(compiler.getRegister(), currReg));
                    compiler.decrementRegister();
                }
                return;
            }
        }
        compiler.addInstruction(new REM(compiler.getDVal(), compiler.getRegister())); // division entière
    }

    @Override
    IntLiteral compute(DecacCompiler compiler, int leftVal, int rightVal) {
        return new IntLiteral(leftVal % rightVal, compiler);
    }

    // The 3 functions below will never be called
    @Override
    FloatLiteral compute(DecacCompiler compiler, float leftVal, int rightVal) {
        return new FloatLiteral(leftVal % rightVal, compiler);
    }

    @Override
    FloatLiteral compute(DecacCompiler compiler, int leftVal, float rightVal) {
        return new FloatLiteral(leftVal % rightVal, compiler);
    }

    @Override
    FloatLiteral compute(DecacCompiler compiler, float leftVal, float rightVal) {
        return new FloatLiteral(leftVal % rightVal, compiler);
    }
}
