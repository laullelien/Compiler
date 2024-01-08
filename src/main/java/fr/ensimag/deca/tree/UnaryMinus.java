package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.ima.pseudocode.ImmediateFloat;
import fr.ensimag.ima.pseudocode.ImmediateInteger;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.SUB;
import fr.ensimag.ima.pseudocode.instructions.WFLOAT;
import fr.ensimag.ima.pseudocode.instructions.WINT;

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
            return compiler.environmentType.INT;
        }
        if (typeOperand.isFloat()){
            return compiler.environmentType.FLOAT;
        }
        throw new ContextualError("Le type ne respecte pas la règle 3.37", this.getLocation());
    }

    @Override
    protected void codeGenPrint(DecacCompiler compiler) {
        compiler.addInstruction(new LOAD(this.getOperand().getNegativeDval(), Register.R1));
        this.getOperand().codeGenPrint(compiler);
    }

    @Override
    protected String getOperatorName() {
        return "-";
    }

}


