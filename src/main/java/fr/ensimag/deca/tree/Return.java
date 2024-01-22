package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.*;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.deca.tools.SymbolTable;
import fr.ensimag.ima.pseudocode.Instruction;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.*;

import java.io.PrintStream;

public class Return extends AbstractInst {

    private AbstractExpr returnExpr;
    private String className;
    private String methodName;
    public Return(AbstractExpr returnExpr){
        this.returnExpr = returnExpr;
    }

    public AbstractExpr getReturnExpr(){
        return this.returnExpr;
    }

    public void setClassName(String className) {this.className = className;}

    public void setMethodName(String methodName) {this.methodName = methodName;}

    @Override
    protected void verifyInst(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass, Type expectedReturnType) throws ContextualError {
        if (expectedReturnType.isVoid()){
            throw new ContextualError("Le type retourné par la fonction est nulle : la règle (3.24) n'est pas respectée.", this.getLocation());
        }
        returnExpr = this.getReturnExpr().verifyRValue(compiler, localEnv, currentClass, expectedReturnType);
        setClassName(currentClass.getType().getName().getName());
        setMethodName(currentClass.currentMethodNameForReturn);
    }

    @Override
    protected void codeGenInst(DecacCompiler compiler) {
        this.returnExpr.codeGenInst(compiler);
        compiler.addInstruction(new LOAD(compiler.getRegister(), Register.R0));
        compiler.addInstruction(new BRA(new Label("fin." + className + "." + methodName)));
    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.print("return ");
        this.returnExpr.decompile(s);
        s.print(";");
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        this.returnExpr.prettyPrint(s, prefix, true);
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        this.returnExpr.iter(f);
    }
}
