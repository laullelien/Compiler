package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.*;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.*;

import java.io.PrintStream;

public class New extends AbstractExpr{

    private AbstractIdentifier nameClass;

    public New(AbstractIdentifier nameClass) {
        this.nameClass = nameClass;
    }

    @Override
    protected void codeGenInst(DecacCompiler compiler) {
        int instanceSize = ((ClassType)this.getType()).getDefinition().getNumberOfFields() + 1;
        compiler.addInstruction(new NEW(instanceSize, compiler.getRegister()));
        compiler.addInstruction(new BOV(new Label("heap_full")));
        compiler.addInstruction(new LEA(compiler.listVTable.getVTable(getType().getName().getName()).getDAddr(), Register.R0));
        compiler.addInstruction(new STORE(Register.R0, new RegisterOffset(0, compiler.getRegister())));
        compiler.addInstruction(new PUSH(compiler.getRegister()));
        compiler.addInstruction(new BSR(new Label("init." + getType().getName())));
        compiler.addInstruction(new POP(compiler.getRegister()));
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass) throws ContextualError {
        Type classType = this.nameClass.verifyType(compiler);
        if (!classType.isClass()) {
            throw new ContextualError("La règle 3.42 n'a pas été respectée, le type n'est pas un type de classe.", this.getLocation());
        }
        setType(classType);
        return classType;
    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.print("new ");
        this.nameClass.decompile(s);
        s.print("()");
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        this.nameClass.prettyPrint(s, prefix, true);
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        this.nameClass.iter(f);
    }
}
