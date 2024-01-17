package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.DecacInternalError;
import fr.ensimag.ima.pseudocode.*;
import fr.ensimag.ima.pseudocode.instructions.*;

/**
 *
 * @author gl38
 * @date 01/01/2024
 */
public class Not extends AbstractUnaryExpr {

    public Not(AbstractExpr operand) {
        super(operand);
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        Type typeOperand = this.getOperand().verifyExpr(compiler, localEnv, currentClass);
        if (!typeOperand.isBoolean()){
            throw new ContextualError("Le type ne respecte pas la règle 3.37", this.getLocation());
        }
        setType(compiler.environmentType.BOOLEAN);
        return getType();
    }

    @Override
    protected String getOperatorName() {
        return "!";
    }

    @Override
    protected void codeGenInst(DecacCompiler compiler) {
        //opti : !!exp = exp;
        if(getOperand() instanceof Not) {
            ((Not) getOperand()).getOperand().codeGenInst(compiler);
            return;
        }

        DVal operandDVal = getOperand().getDval();
        if (operandDVal != null && operandDVal instanceof ImmediateInteger) {
            if (((ImmediateInteger) operandDVal).getValue() == 1) {
                compiler.addInstruction(new LOAD(0, compiler.getRegister()));
            } else {
                compiler.addInstruction(new LOAD(1, compiler.getRegister()));
            }
            return;
        }

        getOperand().codeGenInst(compiler);

        compiler.addInstruction(new SEQ(compiler.getRegister()));
    }
}
