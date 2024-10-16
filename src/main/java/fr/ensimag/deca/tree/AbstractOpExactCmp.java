package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;

/**
 * @author gl38
 * @date 01/01/2024
 */
public abstract class AbstractOpExactCmp extends AbstractOpCmp {

    public AbstractOpExactCmp(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
                           ClassDefinition currentClass) throws ContextualError {
        Type leftOperandType = this.getLeftOperand().verifyExpr(compiler, localEnv, currentClass);
        Type rightOperandType = this.getRightOperand().verifyExpr(compiler, localEnv, currentClass);
        //les opérandes sont tous les 2 des nombres
        if ((leftOperandType.isFloat() || leftOperandType.isInt()) && (rightOperandType.isFloat() || rightOperandType.isInt())) {
            if (leftOperandType != rightOperandType) {
                if (leftOperandType.isInt()) {
                    setLeftOperand(new ConvFloat(getLeftOperand()));
                    getLeftOperand().setType(compiler.environmentType.FLOAT);
                } else {
                    setRightOperand(new ConvFloat(getRightOperand()));
                    getRightOperand().setType(compiler.environmentType.FLOAT);
                }
            }
        } else if (!(leftOperandType.isBoolean() && rightOperandType.isBoolean())) {
            if (!(leftOperandType.isClassOrNull() && rightOperandType.isClassOrNull())) {
                throw new ContextualError("Les types des opérandes de la comparaison ne sont pas compatibles. Regle 3.33", this.getLocation());
            }
        }
        setType(compiler.environmentType.BOOLEAN);
        return getType();
    }
}
