package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.extension.tree.ListBasicBlock;
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
            this.setType(convFloat.verifyExpr(compiler, localEnv, currentClass));
            this.setLeftOperand(convFloat);
        }
        else if (rightOperandType.isInt()) {
            ConvFloat convFloat = new ConvFloat(getRightOperand());
            this.setType(convFloat.verifyExpr(compiler, localEnv, currentClass));
            this.setRightOperand(convFloat);
        }
        return getType();
    }

    /** temp fix for only working on Int and Float */
    @Override
    protected AbstractExpr evaluate(DecacCompiler compiler, ListBasicBlock blocks) {
        super.evaluate(compiler, blocks);
        if (getLeftOperand().isConstant() && getRightOperand().isConstant()) {
            if (getLeftOperand().getType().isInt()) {
                Validate.isTrue(getLeftOperand() instanceof IntLiteral);
                int leftVal = ((IntLiteral) (getLeftOperand())).getValue();
                if (getRightOperand().getType().isInt()) {
                    Validate.isTrue(getRightOperand() instanceof IntLiteral);
                    int rightVal = ((IntLiteral) (getRightOperand())).getValue();
                    return ((AbstractOpArith) this).compute(compiler, leftVal, rightVal);
                } else {
                    Validate.isTrue(getRightOperand() instanceof FloatLiteral);
                    float rightVal = ((FloatLiteral) (getRightOperand())).getValue();
                    return ((AbstractOpArith) this).compute(compiler, leftVal, rightVal);
                }
            } else {
                Validate.isTrue(getLeftOperand() instanceof FloatLiteral);
                float leftVal = ((FloatLiteral) (getLeftOperand())).getValue();
                if (getRightOperand().getType().isInt()) {
                    Validate.isTrue(getRightOperand() instanceof IntLiteral);
                    int rightVal = ((IntLiteral) (getRightOperand())).getValue();
                    return ((AbstractOpArith) this).compute(compiler, leftVal, rightVal);
                } else {
                    Validate.isTrue(getRightOperand() instanceof FloatLiteral);
                    float rightVal = ((FloatLiteral) (getRightOperand())).getValue();
                    return ((AbstractOpArith) this).compute(compiler, leftVal, rightVal);
                }
            }
        }
        return this;
    }

    abstract IntLiteral compute(DecacCompiler compiler, int leftVal, int rightVal);
    abstract FloatLiteral compute(DecacCompiler compiler, float leftVal, int rightVal);
    abstract FloatLiteral compute(DecacCompiler compiler, int leftVal, float rightVal);
    abstract FloatLiteral compute(DecacCompiler compiler, float leftVal, float rightVal);
}
