package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.*;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.*;
import fr.ensimag.ima.pseudocode.instructions.*;

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
    public void codeGenInst(DecacCompiler compiler) {
        // reserve de la place pour les params
        int paramNumber = 1 + listArgs.size();
        compiler.addInstruction(new ADDSP(paramNumber));
        DAddr addrInstanceOfClass = compiler.listVTable.getVTable(expr.getType().getName().getName()).getDAddr();

        // empilement du paramètre implicite
        compiler.addInstruction(new LOAD(addrInstanceOfClass, Register.R2));
        compiler.addInstruction(new STORE(Register.R2, new RegisterOffset(0, Register.SP)));

        // empilement des paramètres (vérifier si ils sont dans le bon sens)
        int index = -1;
        for (AbstractExpr a : listArgs.getList()) {
            compiler.addInstruction(new LOAD(a.getDval(), Register.R2));
            compiler.addInstruction(new STORE(Register.R2, new RegisterOffset(index, Register.SP)));
            index--;
        }

        // récupère le param implicite
        compiler.addInstruction(new LOAD(new RegisterOffset(0, Register.SP), Register.R2));

        // test s'il est égal à null
        compiler.addInstruction(new CMP(new NullOperand(), Register.R2));
        compiler.addInstruction(new BEQ(new Label("dereferencement.null")));

        // récupère adresse table méthodes
        compiler.addInstruction(new LOAD(new RegisterOffset(0, Register.R2), Register.R2));

        compiler.addInstruction(new BSR(new RegisterOffset(1, Register.R2)));

        compiler.addInstruction(new SUBSP(paramNumber));




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
            expr.prettyPrint(s, prefix, false);
        }
        identifier.prettyPrint(s, prefix, false);
        listArgs.prettyPrint(s, prefix, true);
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        if (expr != null) {
            expr.iter(f);
        }
        identifier.iter(f);
        listArgs.iter(f);
    }
}
