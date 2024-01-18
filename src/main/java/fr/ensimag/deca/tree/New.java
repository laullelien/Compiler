package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.*;

public class New extends AbstractUnaryExpr{

    private ClassDefinition classDefinition;

    public New(AbstractExpr operand) {
        super(operand);
    }

    @Override
    protected void codeGenInst(DecacCompiler compiler) {
        int instanceSize = classDefinition.getNumberOfFields() + 1;
        compiler.addInstruction(new NEW(instanceSize, compiler.getRegister()));
        compiler.addInstruction(new LEA());
        compiler.addInstruction(new BOV(new Label("heap_full")));
        compiler.addInstruction(new STORE(Register.R0, new RegisterOffset(0, compiler.getRegister())));
        compiler.addInstruction(new PUSH(compiler.getRegister()));
        compiler.addInstruction(new BSR());
        compiler.addInstruction(new POP(compiler.getRegister()));

    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass) throws ContextualError {
        Type classType = ((Identifier) getOperand()).verifyType(compiler);
        if (!classType.isClass()) {
            throw new ContextualError("La règle 3.42 n'a pas été respectée, le type n'est pas un type de classe.", this.getLocation());
        }
        setType(classType);
        classDefinition = currentClass;
        return classType;
    }

    @Override
    protected String getOperatorName() {
        return "new";
    }
}
