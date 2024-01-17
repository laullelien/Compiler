package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;

public class Instanceof extends AbstractBinaryExpr{
    public Instanceof(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    protected void codeGenBinary(DecacCompiler compiler) {
        //On verra plus tard
    }

    @Override
    protected String getOperatorName() {
        return "instanceof";
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass) throws ContextualError {
        Type exprType = getLeftOperand().verifyExpr(compiler, localEnv, currentClass);
        Type typeType = ((Identifier)getRightOperand()).verifyType(compiler);
        typeInstanceof(exprType, typeType);
        setType(compiler.environmentType.BOOLEAN);
        return getType();
    }

    private void typeInstanceof(Type exprType, Type typeType) throws ContextualError {
        if(!(exprType.isClassOrNull() && typeType.isClass())) {
            throw new ContextualError("La regle 3.40 n'a pas ete respectee. Voir type_instance_of.", getLocation());
        }
    }
}
