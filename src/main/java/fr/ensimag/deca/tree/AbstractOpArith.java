package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.ADD;
import fr.ensimag.ima.pseudocode.instructions.SUB;

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
            setType(compiler.environmentType.INT);
            return getType();
        }
        if((leftOperandType.isFloat() && rightOperandType.isInt())
                || (leftOperandType.isInt() && rightOperandType.isFloat())
                || (leftOperandType.isFloat() && rightOperandType.isFloat())){
            setType(compiler.environmentType.FLOAT);
            return getType();
        }
        throw new ContextualError("Le type ne respecte pas la règle 3.33", this.getLocation());
    }

    protected void addOperands(AbstractExpr expr, DecacCompiler compiler) {
        if (expr != null && expr.getDval() != null) {
            compiler.addInstruction(new ADD(expr.getDval(), Register.R1));
        }


        if (expr instanceof Plus) {
            Plus plusExpr = (Plus) expr;
            addOperands(plusExpr.getLeftOperand(), compiler);
            addOperands(plusExpr.getRightOperand(), compiler);
        }
        if (expr instanceof Minus) {
            Minus minusExpr = (Minus) expr;
            addOperands(minusExpr.getLeftOperand(),compiler);
            subOperands(minusExpr.getRightOperand(), compiler);
        }

        if (expr instanceof UnaryMinus) {
            UnaryMinus unaryMinusExpr = (UnaryMinus) expr;
            subOperands(unaryMinusExpr.getOperand(), compiler);
        }
    }

    protected void subOperands(AbstractExpr expr, DecacCompiler compiler) {
        if (expr != null && expr.getDval() != null) {
            compiler.addInstruction(new SUB(expr.getDval(), Register.R1));
        }

        if (expr instanceof Minus) {
            Minus minusExpr = (Minus) expr;
            addOperands(minusExpr.getLeftOperand(),compiler);
            subOperands(minusExpr.getRightOperand(), compiler);
        }

        if (expr instanceof Plus) {
            Plus plusExpr = (Plus) expr;
            addOperands(plusExpr.getLeftOperand(), compiler);
            addOperands(plusExpr.getRightOperand(), compiler);
        }

        if (expr instanceof UnaryMinus) {
            UnaryMinus unaryMinusExpr = (UnaryMinus) expr;
            addOperands(unaryMinusExpr.getOperand(), compiler);
        }
    }
}
