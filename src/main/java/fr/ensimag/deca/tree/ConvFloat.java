package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.*;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.extension.tree.ListBasicBlock;
import fr.ensimag.ima.pseudocode.*;
import fr.ensimag.ima.pseudocode.instructions.FLOAT;
import org.apache.commons.lang.Validate;

/**
 * Conversion of an int into a float. Used for implicit conversions.
 * 
 * @author gl38
 * @date 01/01/2024
 */
public class ConvFloat extends AbstractUnaryExpr {
    public ConvFloat(AbstractExpr operand) {
        super(operand);
    }

    @Override
    public DVal getDval() {
        if(getOperand() instanceof IntLiteral) {
            return new ImmediateFloat(((IntLiteral) getOperand()).getValue());
        }
        return null;
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) {
        // No setLocation because the example in the handout doesn't have one
        // No verifyExpr of the operand because it has been done already
        setType(compiler.environmentType.FLOAT);
        return compiler.environmentType.FLOAT;
    }

    @Override
    protected AbstractExpr evaluate(DecacCompiler compiler, ListBasicBlock blocks) {
        setOperand(getOperand().evaluate(compiler, blocks));
        if (getOperand().isConstant()) {
            Validate.isTrue(getOperand() instanceof IntLiteral);
            return new FloatLiteral(((IntLiteral) getOperand()).getValue(), compiler);
        }
        return this;
    }

    @Override
    protected String getOperatorName() {
        return "/* conv float */";
    }


    @Override
    protected void codeGenInst(DecacCompiler compiler) {
        if (getOperand().getDval() != null) {
            compiler.addInstruction(new FLOAT(getOperand().getDval(), compiler.getRegister()));
        }
        else {
            getOperand().codeGenInst(compiler);
            compiler.addInstruction(new FLOAT(compiler.getRegister(), compiler.getRegister()));
        }
    }

}



