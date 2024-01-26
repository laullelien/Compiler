package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;

import java.io.PrintStream;

public abstract class AbstractMethodBody extends Tree {
    public abstract void verifyMethodBodyPass3(DecacCompiler compiler, EnvironmentExp classEnv, EnvironmentExp envExpParam, ClassDefinition currentClass, Type returnType) throws ContextualError;

    abstract protected void codeGenInst(DecacCompiler compiler);

    abstract public int getVarNb();

    void setLocalOperand() {}

    abstract public void optimizeMethodBody(DecacCompiler compiler);

    public void codeGenDecl(DecacCompiler compiler) {}
}
