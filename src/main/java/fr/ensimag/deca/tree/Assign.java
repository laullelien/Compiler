package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.extension.tree.ListBasicBlock;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.instructions.*;

/**
 * Assignment, i.e. lvalue = expr.
 *
 * @author gl38
 * @date 01/01/2024
 */
public class Assign extends AbstractBinaryExpr {

    @Override
    public AbstractLValue getLeftOperand() {
        // The cast succeeds by construction, as the leftOperand has been set
        // as an AbstractLValue by the constructor.
        return (AbstractLValue)super.getLeftOperand();
    }

    public Assign(AbstractLValue leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        // règle 3.32
        Type type = this.getLeftOperand().verifyExpr(compiler, localEnv, currentClass);
        AbstractExpr expr = this.getRightOperand().verifyRValue(compiler, localEnv, currentClass, type);
        setRightOperand(expr);
        setType(type);
        return type;
    }

    @Override
    protected String getOperatorName() {
        return "=";
    }

    @Override
    protected void appendToBlock(DecacCompiler compiler, ListBasicBlock blocks) {
        blocks.getCurrentBlock().addInst(this);
        if (getRightOperand() instanceof AbstractIdentifier)
            setRightOperand(compiler.ssaFormHelper.readVariable((AbstractIdentifier) getRightOperand(), blocks.getCurrentBlock()));
        else if (!(getRightOperand() instanceof IntLiteral)) {
            getRightOperand().appendToBlock(compiler, blocks);
        }
        compiler.ssaFormHelper.writeVariable((AbstractIdentifier) getLeftOperand(), blocks.getCurrentBlock(), getRightOperand());
    }

    @Override
    protected void codeGenInst(DecacCompiler compiler) {
        if(getRightOperand().getDval() != null) {
            compiler.addInstruction(new LOAD(getRightOperand().getDval(), compiler.getRegister()));
            compiler.addInstruction(new STORE(compiler.getRegister(), ((Identifier) this.getLeftOperand()).getVariableDefinition().getOperand()));
            return;
        }
        getRightOperand().codeGenInst(compiler);
        compiler.addInstruction(new STORE(compiler.getRegister(), ((Identifier) this.getLeftOperand()).getVariableDefinition().getOperand()));
    }

    @Override
    protected void codeGenBinary(DecacCompiler compiler) {
        if(compiler.getDVal() instanceof  GPRegister) {
            compiler.addInstruction(new STORE((GPRegister)compiler.getDVal(), ((Identifier) this.getLeftOperand()).getVariableDefinition().getOperand()));
            return;
        }
        compiler.addInstruction(new LOAD(compiler.getDVal(), compiler.getRegister()));
        compiler.addInstruction(new STORE(compiler.getRegister(), ((Identifier) this.getLeftOperand()).getVariableDefinition().getOperand()));
    }
}
