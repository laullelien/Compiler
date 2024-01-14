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
public abstract class AbstractOpCmp extends AbstractBinaryExpr {

    public AbstractOpCmp(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        Type leftOperandType = this.getLeftOperand().verifyExpr(compiler, localEnv, currentClass);
        Type rightOperandType = this.getRightOperand().verifyExpr(compiler, localEnv, currentClass);
        if((!(leftOperandType.isFloat() || leftOperandType.isInt())) || (!(rightOperandType.isFloat() || rightOperandType.isInt()))) {
            //type_binary_op
            throw new ContextualError("Au moins un des opérandes de la comparaison n'est pas de type int ou float. Regle 3.33", this.getLocation());
        }
        // Confloat si les opérandes ne sont pas du même type
        if(leftOperandType != rightOperandType) {
            if(leftOperandType.isInt()) {
                setLeftOperand(new ConvFloat(getLeftOperand()));
                getLeftOperand().setType(compiler.environmentType.FLOAT);
            }
            else {
                setRightOperand(new ConvFloat(getRightOperand()));
                getRightOperand().setType(compiler.environmentType.FLOAT);
            }
        }
        setType(compiler.environmentType.BOOLEAN);
        return getType();
    }
}
