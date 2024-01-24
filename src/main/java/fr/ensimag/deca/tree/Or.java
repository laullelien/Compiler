package fr.ensimag.deca.tree;


import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.*;
import fr.ensimag.ima.pseudocode.instructions.BEQ;
import fr.ensimag.ima.pseudocode.instructions.BNE;
import fr.ensimag.ima.pseudocode.instructions.CMP;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import org.apache.commons.lang.ObjectUtils;

/**
 *
 * @author gl38
 * @date 01/01/2024
 */
public class Or extends AbstractOpBool {

    public Or(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    protected String getOperatorName() {
        return "||";
    }

    @Override
    protected void codeGenInst(DecacCompiler compiler) {
        DVal leftDVal = getLeftOperand().getDval();
        if(leftDVal != null && leftDVal instanceof ImmediateInteger) {
            // true || rightOperand = true
            if(((ImmediateInteger)leftDVal).getValue() == 1) {
                compiler.addInstruction(new LOAD(1, compiler.getRegister()));
                return;
            }
            // false || rightOperand = rightOperand
            getRightOperand().codeGenInst(compiler);
            return;
        }

        String labelString = "or_label_" + compiler.getLabelId();
        compiler.incrementLabelId();
        Label endLabel = new Label(labelString + "_fin");

        getLeftOperand().codeGenInst(compiler);

        if(!(compiler.lastIsLoad() && ((LOAD)(compiler.getLastInstruction())).getReg() == compiler.getRegister())) {
            compiler.addInstruction(new CMP(0, compiler.getRegister()));
        }
        compiler.addInstruction(new BNE(endLabel));

        getRightOperand().codeGenInst(compiler);

        compiler.addLabel(endLabel);
    }

    @Override
    public void codeGenCond(DecacCompiler compiler, boolean eq, Label jumpLabel) {
        if(eq) {
            getLeftOperand().codeGenCond(compiler, eq, jumpLabel);
            getRightOperand().codeGenCond(compiler, eq, jumpLabel);
        } else {
            codeGenInst(compiler);
            if(!(compiler.lastIsLoad() && ((LOAD)(compiler.getLastInstruction())).getReg() == compiler.getRegister())) {
                compiler.addInstruction(new CMP(0, compiler.getRegister()));
            }
            compiler.addInstruction(new BEQ(jumpLabel));
        }
    }
}
