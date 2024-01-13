package fr.ensimag.deca.tree;


import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;

/**
 *
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
        if((!(leftOperandType.isFloat() || leftOperandType.isInt())) || (!(rightOperandType.isFloat() || rightOperandType.isInt()))) {
            //les opérandes sont tous les 2 des booleans
            if(!(leftOperandType.isBoolean() && rightOperandType.isBoolean())) {
                //Plus tard ajouter la compatibilité avec les objets
                //type_binary_op
                throw new ContextualError("Les types des opérandes de la comparaison ne sont pas compatibles.", this.getLocation());
            }
        }
        setType(compiler.environmentType.BOOLEAN);
        return getType();
    }
}
