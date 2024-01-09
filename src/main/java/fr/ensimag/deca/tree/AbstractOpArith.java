package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;

/**
 * Arithmetic binary operations (+, -, /, ...)
 * 
 * @author gl38
 * @date 01/01/2024
 */
public abstract class AbstractOpArith extends AbstractBinaryExpr {

    public AbstractOpArith(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {

        Type leftOperandType = this.getLeftOperand().verifyExpr(compiler, localEnv, currentClass);
        Type rightOperandType = this.getRightOperand().verifyExpr(compiler, localEnv, currentClass);

        if (leftOperandType.isInt() && rightOperandType.isInt()){
            return compiler.environmentType.INT;
        }
        if((leftOperandType.isFloat() && rightOperandType.isInt())
                || (leftOperandType.isInt() && rightOperandType.isFloat())
                || (leftOperandType.isFloat() && rightOperandType.isFloat())){
            return compiler.environmentType.FLOAT;
        }
        throw new ContextualError("Le type ne respecte pas la règle 3.33", this.getLocation());
    }
}
