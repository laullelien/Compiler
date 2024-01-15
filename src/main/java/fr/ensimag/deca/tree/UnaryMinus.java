package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.ima.pseudocode.*;
import fr.ensimag.ima.pseudocode.instructions.*;

/**
 * @author gl38
 * @date 01/01/2024
 */
public class UnaryMinus extends AbstractUnaryExpr {

    public UnaryMinus(AbstractExpr operand) {
        super(operand);
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        //regle 3.37
        Type typeOperand = this.getOperand().verifyExpr(compiler, localEnv, currentClass);
        if (typeOperand.isInt()){
            setType(compiler.environmentType.INT);
            return getType();
        }
        if (typeOperand.isFloat()){
            setType(compiler.environmentType.FLOAT);
            return getType();
        }
        throw new ContextualError("Le type ne respecte pas la règle 3.37", this.getLocation());
    }

    @Override
    protected void codeGenInst(DecacCompiler compiler) {
        if (getOperand().getDval() != null) {
            compiler.addInstruction(new OPP(getOperand().getDval(), Register.getR(2)));
        }
        else {
            getOperand().codeGenInst(compiler);
            compiler.addInstruction(new OPP(Register.getR(2), Register.getR(2)));
        }
    }

    @Override
    protected void codeGenPrint(DecacCompiler compiler) {
        if (getOperand().getDval() != null)
            compiler.addInstruction(new OPP(getOperand().getDval(), Register.getR(1)));
        else
            super.codeGenPrint(compiler);
    }

    @Override
    protected void codeGenInstruction(DecacCompiler compiler, DVal value, GPRegister target) {
        compiler.addInstruction(new OPP(value, target));
    }

    @Override
    protected String getOperatorName() {
        return "-";
    }

}


