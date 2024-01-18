package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;

import java.io.PrintStream;

public class Return extends AbstractInst {

    private AbstractExpr returnExpr;
    public Return(AbstractExpr returnExpr){
        this.returnExpr = returnExpr;
    }

    public AbstractExpr getReturnExpr(){
        return this.returnExpr;
    }

    @Override
    protected void verifyInst(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass, Type expectedReturnType) throws ContextualError {
        if (expectedReturnType.isVoid()){
            throw new ContextualError("Le type retourné par la fonction est nulle : la règle (3.24) n'est pas respectée.", this.getLocation());
        }
        this.getReturnExpr().verifyRValue(compiler, localEnv, currentClass, expectedReturnType);
    }

    @Override
    protected void codeGenInst(DecacCompiler compiler) {

    }

    @Override
    public void decompile(IndentPrintStream s) {

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
