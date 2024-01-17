package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;

public class New extends AbstractUnaryExpr{


    public New(AbstractExpr operand) {
        super(operand);
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass) throws ContextualError {
        Type classType = ((Identifier) getOperand()).verifyType(compiler);
        if (!classType.isClass()) {
            throw new ContextualError("La règle 3.42 n'a pas été respectée, le type n'est pas un type de classe.", this.getLocation());
        }
        setType(classType);
        return classType;
    }

    @Override
    protected String getOperatorName() {
        return "new";
    }
}
