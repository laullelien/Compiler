package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.*;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.deca.tools.SymbolTable;

import java.io.PrintStream;

public class Null extends AbstractExpr{
    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass) throws ContextualError {
        SymbolTable.Symbol nullSymbol = compiler.createSymbol("null");
        setType(new NullType(nullSymbol));
        return getType();
    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.print(" null ");
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        // leaf, nothing to do
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        // leaf, nothing to do
    }
}
