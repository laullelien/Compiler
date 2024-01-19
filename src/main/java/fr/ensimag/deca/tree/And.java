package fr.ensimag.deca.tree;


import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.ImmediateInteger;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.BEQ;
import fr.ensimag.ima.pseudocode.instructions.CMP;
import fr.ensimag.ima.pseudocode.instructions.LOAD;

/**
 *
 * @author gl38
 * @date 01/01/2024
 */
public class And extends AbstractOpBool {

    public And(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    protected String getOperatorName() {
        return "&&";
    }


    @Override
    protected void codeGenInst(DecacCompiler compiler) {
        DVal leftDVal = getLeftOperand().getDval();
        if(leftDVal != null && leftDVal instanceof ImmediateInteger) {
            // true && rightOperand = rightOperand
            if(((ImmediateInteger)leftDVal).getValue() == 1) {
                getRightOperand().codeGenInst(compiler);
                return;
            }
            // false && rightOperand = false
            compiler.addInstruction(new LOAD(0, compiler.getRegister()));
            return;
        }

        String labelString = "and_label_" + compiler.getLabelId();
        compiler.incrementLabelId();
        Label endLabel = new Label(labelString + "_fin");

        getLeftOperand().codeGenInst(compiler);

        if(!(compiler.lastIsLoad() && ((LOAD)(compiler.getLastInstruction())).getReg() == compiler.getRegister())) {
            compiler.addInstruction(new CMP(0, compiler.getRegister()));
        }
        compiler.addInstruction(new BEQ(endLabel));

        getRightOperand().codeGenInst(compiler);

        compiler.addLabel(endLabel);
    }
}
