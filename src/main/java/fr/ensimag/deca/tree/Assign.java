package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Register;
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
    protected void codeGenInst(DecacCompiler compiler) {
        if (this.getLeftOperand() instanceof Identifier) {
            this.getRightOperand().codeGenInst(compiler);
            compiler.addInstruction(new STORE(Register.R2, ((Identifier) this.getLeftOperand()).getVariableDefinition().getOperand()));
        }
    }

    @Override
    protected void codeGenInstruction(DecacCompiler compiler, DVal value, GPRegister target) {
        //codeGenInst(compiler);
        compiler.addInstruction(new LOAD(value, target));
        compiler.addInstruction(new STORE(target, ((Identifier) this.getLeftOperand()).getVariableDefinition().getOperand()));
    }

    @Override
    protected void codeGenPrint(DecacCompiler compiler) {
        codeGenInst(compiler);
        compiler.addInstruction(new LOAD(Register.R2, Register.R1));
        if(getType().isInt()) {
            compiler.addInstruction(new WINT());
        }
        if(getType().isFloat()) {
            compiler.addInstruction(new WFLOAT());
        }
    }

    @Override
    protected void codeGenPrintX(DecacCompiler compiler) {
        codeGenInst(compiler);
        compiler.addInstruction(new LOAD(Register.R2, Register.R1));
        compiler.addInstruction(new WFLOATX());
    }
    @Override
    protected String getOperatorName() {
        return "=";
    }

}
