package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;

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
        if (!typeOperand.isBoolean())
            throw new ContextualError("Le type ne respecte pas la règle 3.37", this.getLocation());
        setType(typeOperand);
        return getType();
    }


    @Override
    protected String getOperatorName() {
        return "!";
    }
}
