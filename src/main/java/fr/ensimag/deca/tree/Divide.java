package fr.ensimag.deca.tree;


import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.Register;
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
    protected void codeGenInst(DecacCompiler compiler) {
        super.codeGenInst(compiler);
        if (getType().isInt())
            compiler.addInstruction(new QUO(Register.getR(3), Register.getR(2))); // division entière
        else // nous avons une division de flottant
            compiler.addInstruction(new DIV(Register.getR(3), Register.getR(2)));

    }

    @Override
    protected String getOperatorName() {
        return "/";
    }

}
