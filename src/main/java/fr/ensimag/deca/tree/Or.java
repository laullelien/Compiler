package fr.ensimag.deca.tree;


import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.*;
import fr.ensimag.ima.pseudocode.instructions.BEQ;
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
        String labelString = "or_label_" + compiler.getLabelId();
        compiler.incrementLabelId();
        Label endLabel = new Label(labelString + "_fin");

        DVal leftDVal = getLeftOperand().getDval();
        if(leftDVal != null) {
            // true || rightOperand = true
            if(((ImmediateInteger)leftDVal).getValue() == 1) {
                compiler.addInstruction(new LOAD(1, compiler.getRegister()));
                return;
            }
            // false || rightOperand = rightOperand
            getRightOperand().codeGenInst(compiler);
            return;
        }

        getLeftOperand().codeGenInst(compiler);

        compiler.addInstruction(new CMP(1, compiler.getRegister()));
        compiler.addInstruction(new BEQ(endLabel));

        getRightOperand().codeGenInst(compiler);

        compiler.addLabel(endLabel);
    }
}
