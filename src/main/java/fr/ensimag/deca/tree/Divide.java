package fr.ensimag.deca.tree;


import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.*;
import fr.ensimag.ima.pseudocode.instructions.*;

import java.sql.Struct;

/**
 *
 * @author gl38
 * @date 01/01/2024
 */
public class Divide extends AbstractOpArith {
    public Divide(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    protected String getOperatorName() {
        return "/";
    }

    @Override
    protected void codegenCompute(DecacCompiler compiler) {
        if (getType().isInt()) {
            int leftVal = ((ImmediateInteger) (getLeftOperand().getDval())).getValue();
            int rightVal = ((ImmediateInteger) (getRightOperand().getDval())).getValue();
            compiler.addInstruction(new LOAD(new ImmediateInteger(leftVal / rightVal), compiler.getRegister()));
        } else {
            if (getLeftOperand().getType().isInt()) {
                int leftVal = ((ImmediateInteger) (getLeftOperand().getDval())).getValue();
                float rightVal = ((ImmediateFloat) (getRightOperand().getDval())).getValue();
                compiler.addInstruction(new LOAD(new ImmediateFloat(leftVal / rightVal), compiler.getRegister()));
            }
            else if (getRightOperand().getType().isInt()) {
                float leftVal = ((ImmediateFloat) (getLeftOperand().getDval())).getValue();
                int rightVal = ((ImmediateInteger) (getRightOperand().getDval())).getValue();
                compiler.addInstruction(new LOAD(new ImmediateFloat(leftVal / rightVal), compiler.getRegister()));
            }
            else {
                float leftVal = ((ImmediateFloat) (getLeftOperand().getDval())).getValue();
                float rightVal = ((ImmediateFloat) (getRightOperand().getDval())).getValue();
                compiler.addInstruction(new LOAD(new ImmediateFloat(leftVal / rightVal), compiler.getRegister()));
            }
        }
    }

    @Override
    protected void codeGenBinary(DecacCompiler compiler) {
        if (getType().isInt()) {
            if (!compiler.getCompilerOptions().getNocheck() && !compiler.getCompilerOptions().getOptim()) {
                compiler.addInstruction(new LOAD(compiler.getDVal(), Register.R1));
                compiler.addInstruction(new BEQ(new Label("division_by_0")));
            }
            if(compiler.getDVal() instanceof ImmediateInteger) {
                int value = ((ImmediateInteger)(compiler.getDVal())).getValue();
                if(value == 1) {
                    return;
                }
                else if(value > 0 && (((value - 1) & value) == 0)) {
                    // it is a power of 2
                    if(value >= 1<<20) {
                        // no opti
                        compiler.addInstruction(new QUO(compiler.getDVal(), compiler.getRegister())); // division entière
                        return;
                    }
                    while(value != 1) {
                        compiler.addInstruction(new SHR(compiler.getRegister()));
                        value >>= 1;
                    }
                    return;
                }
            }
            compiler.addInstruction(new QUO(compiler.getDVal(), compiler.getRegister())); // division entière
        }
        else {
            if (!compiler.getCompilerOptions().getNocheck() && !compiler.getCompilerOptions().getOptim()) {
                compiler.addInstruction(new LOAD(compiler.getDVal(), Register.R1));
                compiler.addInstruction(new BEQ(new Label("division_by_0")));
            }
            compiler.addInstruction(new DIV(compiler.getDVal(), compiler.getRegister()));
        }
    }
}
