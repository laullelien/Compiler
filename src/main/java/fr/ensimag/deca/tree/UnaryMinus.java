package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.ImmediateFloat;
import fr.ensimag.ima.pseudocode.ImmediateInteger;
import fr.ensimag.ima.pseudocode.Register;
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
    public DVal getDval() {
        return getOperand().getNegativeDval();
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
        getOperand().codeGenInst(compiler);
        compiler.addInstruction(new OPP(Register.getR(2), Register.getR(2)));
    }

    @Override
    protected String getOperatorName() {
        return "-";
    }

}


