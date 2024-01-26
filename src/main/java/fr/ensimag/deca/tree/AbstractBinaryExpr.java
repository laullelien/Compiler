package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.extension.tree.ListBasicBlock;
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

    public AbstractExpr getLeftOperand() {
        return leftOperand;
    }

    protected void setLeftOperand(AbstractExpr leftOperand) {
        Validate.notNull(leftOperand);
        this.leftOperand = leftOperand;
    }

    public AbstractExpr getRightOperand() {
        return rightOperand;
    }

    protected void setRightOperand(AbstractExpr rightOperand) {
        Validate.notNull(rightOperand);
        this.rightOperand = rightOperand;
    }

    @Override
    protected AbstractExpr evaluate(DecacCompiler compiler, ListBasicBlock blocks) {
        setLeftOperand(getLeftOperand().evaluate(compiler, blocks));
        setRightOperand(getRightOperand().evaluate(compiler, blocks));
        return this;
    }

    protected void codeGenInst(DecacCompiler compiler) {
        if (getRightOperand().getDval() != null) {
            getLeftOperand().codeGenInst(compiler);
            compiler.setDval(getRightOperand().getDval());
            codeGenBinary(compiler);
        } else {
            if (compiler.isRegisterAvailable()) {
                getLeftOperand().codeGenInst(compiler);
                compiler.incrementRegister();
                getRightOperand().codeGenInst(compiler);
                compiler.setDval(compiler.getRegister());
                compiler.decrementRegister();
                codeGenBinary(compiler);
            } else {
                getLeftOperand().codeGenInst(compiler);
                compiler.addInstruction(new PUSH(compiler.getRegister()));
                compiler.codegenHelper.incPushDepth();
                getRightOperand().codeGenInst(compiler);
                compiler.addInstruction(new LOAD(compiler.getRegister(), Register.R0));
                compiler.addInstruction(new POP(compiler.getRegister()));
                compiler.codegenHelper.decPushDepth();
                compiler.setDval(Register.R0);
                codeGenBinary(compiler);
            }
        }
    }

    protected void codeGenValues(DecacCompiler compiler) {
        if (getRightOperand().getDval() != null) {
            getLeftOperand().codeGenInst(compiler);
            compiler.setDval(getRightOperand().getDval());
        } else {
            if (compiler.isRegisterAvailable()) {
                getLeftOperand().codeGenInst(compiler);
                compiler.incrementRegister();
                getRightOperand().codeGenInst(compiler);
                compiler.setDval(compiler.getRegister());
                compiler.decrementRegister();
            } else {
                getLeftOperand().codeGenInst(compiler);
                compiler.addInstruction(new PUSH(compiler.getRegister()));
                compiler.codegenHelper.incPushDepth();
                getRightOperand().codeGenInst(compiler);
                compiler.addInstruction(new LOAD(compiler.getRegister(), Register.R0));
                compiler.addInstruction(new POP(compiler.getRegister()));
                compiler.codegenHelper.decPushDepth();
                compiler.setDval(Register.R0);
            }
        }
    }

    protected void codeGenBinary(DecacCompiler compiler) {
        throw new RuntimeException("Not yet implemented");
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
