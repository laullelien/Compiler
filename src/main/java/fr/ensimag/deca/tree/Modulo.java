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
        compiler.addInstruction(new REM(compiler.getDVal(), compiler.getRegister())); // division entière
    }

    @Override
    ImmediateInteger compute(int leftVal, int rightVal) {
        return new ImmediateInteger(leftVal % rightVal);
    }

    // The 3 functions below will never be called
    @Override
    ImmediateFloat compute(float leftVal, int rightVal) {
        return new ImmediateFloat(leftVal % rightVal);
    }

    @Override
    ImmediateFloat compute(int leftVal, float rightVal) {
        return new ImmediateFloat(leftVal % rightVal);
    }

    @Override
    ImmediateFloat compute(float leftVal, float rightVal) {
        return new ImmediateFloat(leftVal % rightVal);
    }
}
