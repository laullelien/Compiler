package fr.ensimag.deca.tree;


import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.ADD;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.SUB;
import fr.ensimag.ima.pseudocode.instructions.WINT;

/**
 * @author gl38
 * @date 01/01/2024
 */
public class Minus extends AbstractOpArith {
    public Minus(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }



    @Override
    protected String getOperatorName() {
        return "-";
    }

    @Override
    protected void codeGenPrint(DecacCompiler compiler) {

        compiler.addInstruction(new LOAD(0, Register.R1));
        addOperands(this.getLeftOperand(), compiler);
        subOperands(this.getRightOperand(), compiler);

        compiler.addInstruction(new WINT());
    }


    private void addOperands(AbstractExpr expr, DecacCompiler compiler) {
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
            UnaryMinus unaryminusExpr = (UnaryMinus) expr;
            subOperands(unaryminusExpr.getOperand(), compiler);
        }
    }

    private void subOperands(AbstractExpr expr, DecacCompiler compiler) {
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
            UnaryMinus unaryminusExpr = (UnaryMinus) expr;
            addOperands(unaryminusExpr.getOperand(), compiler);
        }
    }
    
}
