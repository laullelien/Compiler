package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.tools.IndentPrintStream;
import java.io.PrintStream;

import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.*;
import org.apache.commons.lang.Validate;

/**
 * Binary expressions.
 *
 * @author gl38
 * @date 01/01/2024
 */
public abstract class AbstractBinaryExpr extends AbstractExpr {

    public AbstractExpr getLeftOperand() {
        return leftOperand;
    }

    public AbstractExpr getRightOperand() {
        return rightOperand;
    }

    protected void setLeftOperand(AbstractExpr leftOperand) {
        Validate.notNull(leftOperand);
        this.leftOperand = leftOperand;
    }

    protected void setRightOperand(AbstractExpr rightOperand) {
        Validate.notNull(rightOperand);
        this.rightOperand = rightOperand;
    }

    private AbstractExpr leftOperand;
    private AbstractExpr rightOperand;

    public AbstractBinaryExpr(AbstractExpr leftOperand,
            AbstractExpr rightOperand) {
        Validate.notNull(leftOperand, "left operand cannot be null");
        Validate.notNull(rightOperand, "right operand cannot be null");
        Validate.isTrue(leftOperand != rightOperand, "Sharing subtrees is forbidden");
        this.leftOperand = leftOperand;
        this.rightOperand = rightOperand;
    }

    @Override
    protected void codeGenPrint(DecacCompiler compiler) {
        codeGenInst(compiler);
        compiler.addInstruction(new LOAD(Register.getR(2), Register.R1));
        if (getType().isInt())
            compiler.addInstruction(new WINT());
        else if (getType().isFloat())
            compiler.addInstruction(new WFLOAT());
    }

    protected void codeGenInst(DecacCompiler compiler) {
        codeExp(compiler, this, 2);
    }

    /**
     * Génération de code naïve pour expressions arithmétiques
     * donné par les profs
     * cf. diapo étape C, page 12
     */
    protected void codeExp(DecacCompiler compiler, AbstractExpr e, int n) {
        // On vérifie que e est un litéral ou une variable
        if (e.getDval() != null)
            compiler.addInstruction(new LOAD(e.getDval(), Register.getR(n)));
        else if (e instanceof AbstractUnaryExpr) {
            AbstractUnaryExpr op = (AbstractUnaryExpr) e;
            codeExp(compiler, op.getOperand(), n);
            op.codeGenInstruction(compiler, Register.getR(n), Register.getR(n));
        }
        else if (e instanceof AbstractOpBool) {
            ((AbstractOpBool)e).codeGenBool(compiler, n);
        }
        else if (e instanceof AbstractBinaryExpr) {
            AbstractBinaryExpr op = (AbstractBinaryExpr) e;
            if (op.getRightOperand().getDval() != null) {
                codeExp(compiler, op.getLeftOperand(), n);
                op.codeGenInstruction(compiler, op.getRightOperand().getDval(), Register.getR(n));
            }
            else {
                if (n < compiler.getCompilerOptions().getMaxRegisters()) {
                    codeExp(compiler, op.getLeftOperand(), n);
                    codeExp(compiler, op.getRightOperand(), n + 1);
                    op.codeGenInstruction(compiler, Register.getR(n + 1), Register.getR(n));
                }
                else {
                    codeExp(compiler, op.getLeftOperand(), n);
                    compiler.addInstruction(new PUSH(Register.getR(n)));
                    codeExp(compiler, op.getRightOperand(), n + 1);
                    compiler.addInstruction(new LOAD(Register.getR(n), Register.R0));
                    compiler.addInstruction(new POP(Register.getR(n)));
                    op.codeGenInstruction(compiler, Register.R0, Register.getR(n));
                }
            }
        }
        else {
            // fix temporaire pour toute autre expression
            if (n == 2)
                // le retour du calcul de notre opérande se trouve directement dans le registre attendu
                e.codeGenInst(compiler);
            else {
                // sinon on stocke temporairement ce qu'il y a dans R2 pour calculer notre opérande
                compiler.addInstruction(new PUSH(Register.getR(2)));
                e.codeGenInst(compiler);
                compiler.addInstruction(new LOAD(Register.getR(2), Register.getR(n)));
                compiler.addInstruction(new POP(Register.getR(2)));
            }
        }
    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.print("(");
        getLeftOperand().decompile(s);
        s.print(" " + getOperatorName() + " ");
        getRightOperand().decompile(s);
        s.print(")");
    }

    abstract protected String getOperatorName();

    @Override
    protected void iterChildren(TreeFunction f) {
        leftOperand.iter(f);
        rightOperand.iter(f);
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        leftOperand.prettyPrint(s, prefix, false);
        rightOperand.prettyPrint(s, prefix, true);
    }



}
