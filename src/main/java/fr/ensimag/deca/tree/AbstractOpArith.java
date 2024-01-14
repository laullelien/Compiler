package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.*;
import org.apache.commons.lang.Validate;


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
        Type leftOperandType = getLeftOperand().verifyExpr(compiler, localEnv, currentClass);
        Type rightOperandType = getRightOperand().verifyExpr(compiler, localEnv, currentClass);
        if (!(leftOperandType.isInt() || leftOperandType.isFloat()) ||
                !(rightOperandType.isInt() || rightOperandType.isFloat()))
            throw new ContextualError("Le type ne respecte pas la règle 3.33", this.getLocation());
        if (leftOperandType.isInt() && rightOperandType.isInt()) {
            setType(compiler.environmentType.INT);
            return getType();
        }
        setType(compiler.environmentType.FLOAT);
        if (leftOperandType.isInt()) {
            ConvFloat convFloat = new ConvFloat(getLeftOperand());
            convFloat.verifyExpr(compiler, localEnv, currentClass);
        }
        else if (rightOperandType.isInt()) {
            ConvFloat convFloat = new ConvFloat(getRightOperand());
            convFloat.verifyExpr(compiler, localEnv, currentClass);
        }
        return getType();
    }
}
