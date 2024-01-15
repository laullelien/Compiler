package fr.ensimag.deca.tree;


import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.BEQ;
import fr.ensimag.ima.pseudocode.instructions.CMP;

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
    protected void codeGenBinary(DecacCompiler compiler) {
        int n = 3;
        String labelString = "and_label_" + compiler.getLabelId();
        compiler.incrementLabelId();
        Label endLabel = new Label(labelString + "_fin");

        //codeExp(compiler, getLeftOperand(), n);

        compiler.addInstruction(new CMP(0, Register.getR(n)));
        compiler.addInstruction(new BEQ(endLabel));

        //codeExp(compiler, getRightOperand(), n);

        compiler.addLabel(endLabel);
    }
}
