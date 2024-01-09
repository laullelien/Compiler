package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.LOAD;

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
        Type typeOperand = this.getOperand().verifyExpr(compiler, localEnv, currentClass);
        if (!(typeOperand.isInt() || typeOperand.isFloat())){
            throw new ContextualError("Le type ne respecte pas la règle 3.37", this.getLocation());
        }
        setType(typeOperand);
        return getType();
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
