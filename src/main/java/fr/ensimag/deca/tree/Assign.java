package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.ima.pseudocode.*;
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
    protected void codeGenInst(DecacCompiler compiler) {
        if(getLeftOperand() instanceof Selection) {
            if (compiler.isRegisterAvailable()) {
                GPRegister prevReg = compiler.getRegister();
                ((Selection)getLeftOperand()).codeGenAdress(compiler);
                compiler.incrementRegister();
                getRightOperand().codeGenInst(compiler);
                compiler.addInstruction(new STORE(compiler.getRegister(), new RegisterOffset(0, prevReg)));
                compiler.decrementRegister();
            } else {
                ((Selection)getLeftOperand()).codeGenAdress(compiler);
                compiler.addInstruction(new PUSH(compiler.getRegister()));
                compiler.codegenHelper.incPushDepth();
                getRightOperand().codeGenInst(compiler);
                compiler.addInstruction(new LOAD(compiler.getRegister(), Register.R0));
                compiler.addInstruction(new POP(compiler.getRegister()));
                compiler.codegenHelper.decPushDepth();
                compiler.addInstruction(new STORE(Register.R0, new RegisterOffset(0, compiler.getRegister())));
            }
            return;
        }
        if(getRightOperand().getDval() != null) {
            compiler.addInstruction(new LOAD(getRightOperand().getDval(), compiler.getRegister()));
        } else {
            getRightOperand().codeGenInst(compiler);
        }
        if(((Identifier) this.getLeftOperand()).getExpDefinition().getOperand() != null) {
            compiler.addInstruction(new STORE(compiler.getRegister(), ((Identifier) this.getLeftOperand()).getExpDefinition().getOperand()));
        }
        else {
            compiler.addInstruction(new LOAD(new RegisterOffset(-2, Register.LB), Register.R0));
            compiler.addInstruction(new CMP(new NullOperand(), Register.R0));
            compiler.addInstruction(new BEQ(new Label("dereferencement_null")));
            compiler.addInstruction(new STORE(compiler.getRegister(), new RegisterOffset(((Identifier)getLeftOperand()).getFieldDefinition().getIndex(), Register.R0)));
        }
    }
}
