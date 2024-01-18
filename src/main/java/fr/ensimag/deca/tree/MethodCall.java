package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.*;
import fr.ensimag.deca.tools.IndentPrintStream;
import jdk.javadoc.internal.doclint.Env;

import java.io.PrintStream;
import java.util.Iterator;
import java.util.List;

public class MethodCall extends AbstractExpr {
    private AbstractExpr expr;
    private AbstractIdentifier identifier;
    private ListExpr listArgs;

    public MethodCall(AbstractExpr expr, AbstractIdentifier identifier, ListExpr listArgs) {
        this.expr = expr;
        this.identifier = identifier;
        this.listArgs = listArgs;
    }

    public MethodCall(AbstractIdentifier identifier, ListExpr listArgs) {
        this.identifier = identifier;
        this.listArgs = listArgs;
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass) throws ContextualError {
        Type exprType = expr.verifyExpr(compiler, localEnv, currentClass);
        if (!exprType.isClass()) {
            throw new ContextualError("La règle 3.71 n'est pas respectée, le type de l'expression n'est pas une classe", getLocation());
        }
        EnvironmentExp exprEnvExpr = ((ClassType) (exprType)).getDefinition().getMembers();
        ExpDefinition methodDef = identifier.verifyMethod(compiler, exprEnvExpr);
        Type methodType = methodDef.getType();
        Signature methodSig = ((MethodDefinition) (methodDef)).getSignature();
        List<Type> typeList = methodSig.getList();
        if (typeList.size() != listArgs.size()) {
            throw new ContextualError("La règle 3.71 n'est pas respectée, le nombre de paramètres ne correspond pas avec la signature", getLocation());
        }
        Iterator<Type> typeIterator = typeList.iterator();
        Iterator<AbstractExpr> argsIterator = listArgs.iterator();
        while (typeIterator.hasNext()) {
            try { argsIterator.next().verifyRValue(compiler, localEnv, currentClass, typeIterator.next());}
            catch (ContextualError e){
                throw new ContextualError("La règle 3.71 n'est pas respectée, Un des paramètres n'a pas le bon type", getLocation());
            }
        }
        setType(methodType);
        return methodType;
    }

    @Override
    public void decompile(IndentPrintStream s) {
        if (expr != null) {
            expr.decompile(s);
            s.print(".");
        }
        identifier.decompile(s);
        s.print("(");
        listArgs.decompile(s);
        s.print(")");
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        if (expr != null) {
            expr.prettyPrintChildren(s, prefix);
        }
        identifier.prettyPrintChildren(s, prefix);
        listArgs.prettyPrintChildren(s, prefix);
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        if (expr != null) {
            expr.iterChildren(f);
        }
        identifier.iterChildren(f);
        listArgs.iterChildren(f);
    }
}
