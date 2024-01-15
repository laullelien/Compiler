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
    protected void codeGenInstruction(DecacCompiler compiler, DVal value, GPRegister target) {
        if (getType().isInt()) {
            if (!compiler.getCompilerOptions().getNocheck()) {
                compiler.addInstruction(new LOAD(value, Register.R1));
                compiler.addInstruction(new CMP(0, Register.R1));
                compiler.addInstruction(new BEQ(new Label("division_by_0")));
            }
            compiler.addInstruction(new QUO(value, target)); // division entière
        }
        else {
            if (!compiler.getCompilerOptions().getNocheck()) {
                compiler.addInstruction(new LOAD(value, Register.R1));
                compiler.addInstruction(new CMP(new ImmediateFloat(0), Register.R1));
                compiler.addInstruction(new BEQ(new Label("division_by_0")));
            }
            compiler.addInstruction(new DIV(value, target));
        }
    }
}
