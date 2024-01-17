package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.*;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.deca.tools.SymbolTable;

import javax.naming.Context;
import java.io.PrintStream;

public abstract class AbstractDeclMethod extends Tree {
    private AbstractIdentifier methodReturnType;
    private AbstractIdentifier methodName;
    private ListDeclParam methodParameters;
    private AbstractMethodBody methodBody;
    private Type returnType;
    private Definition definition;

    public Type getReturnType() {
        return returnType;
    }

    public void setReturnType(Type returnType) {
        this.returnType = returnType;
    }

    public Definition getDefinition() {
        return definition;
    }

    public void setDefinition(Definition definition) {
        this.definition = definition;
    }

    public AbstractDeclMethod(AbstractIdentifier methodReturnType, AbstractIdentifier methodName, ListDeclParam methodParameters, AbstractMethodBody methodBody) {
        this.methodReturnType = methodReturnType;
        this.methodName = methodName;
        this.methodParameters = methodParameters;
        this.methodBody = methodBody;
    }

    @Override
    public void decompile(IndentPrintStream s) {
        methodReturnType.decompile(s);
        s.print(" ");
        methodName.decompile(s);
        s.print(" (");
        methodParameters.decompile(s);
        s.print(") ");
        methodBody.decompile(s);
        s.println();
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        methodReturnType.prettyPrint(s, prefix, false);
        methodName.prettyPrint(s, prefix, false);
        methodParameters.prettyPrint(s, prefix, false);
        methodBody.prettyPrint(s, prefix, true);
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        methodReturnType.iter(f);
        methodName.iter(f);
        methodParameters.iter(f);
        methodBody.iter(f);
    }

    public EnvironmentExp verifyMethod(DecacCompiler compiler, SymbolTable.Symbol superClassSymbol) throws ContextualError {
        Type returnType = methodReturnType.verifyType(compiler);
        this.returnType = returnType;
        Signature paramsSignature = methodParameters.verifyListDeclParam(compiler);
        TypeDefinition defSuperClass = compiler.environmentType.defOfType(superClassSymbol);
        if (defSuperClass == null || defSuperClass.isClass()) {
            throw new ContextualError("La règle (2.3) n'est pas respectée... Pas de Super Classe", this.getLocation());
        }
        EnvironmentExp methodEnvExp = new EnvironmentExp();
        int misteryIndex = 0;
        ExpDefinition methodDef = new MethodDefinition(returnType, this.getLocation(), paramsSignature, misteryIndex);
        this.returnType = returnType;
        this.definition = methodDef;
        try {
            methodEnvExp.declare(methodName.getName(), methodDef);
        } catch (EnvironmentExp.DoubleDefException e) {
            throw new ContextualError("Cette exception ne sera jamais levée alors je peux mettre ce que je veux... Joyeux Anniversaire !", this.getLocation());
        }
        ExpDefinition envExpSuper = ((ClassDefinition) defSuperClass).getMembers().get(this.methodName.getName());
        if (envExpSuper == null) {
            return methodEnvExp;
        }
        if (!envExpSuper.isMethod()) {
            throw new ContextualError("La redéfinition est mauvaise (pas une méthode) et la règle (2.7) n'est pas respectée.", this.getLocation());
        }
        Signature paramSignatureReDef = ((MethodDefinition) envExpSuper).getSignature();
        if (!paramsSignature.equals(paramSignatureReDef)) {
            throw new ContextualError("La redéfinition est mauvaise et la règle (2.7) n'est pas respectée.", this.getLocation());
        }
        if (!((ClassType) returnType).isSubClassOf((ClassType) envExpSuper.getType())) {
            throw new ContextualError("La redéfinition est mauvaise (problème de sous-type) et la règle (2.7) n'est pas respectée.", this.getLocation());
        }
        return methodEnvExp;
    }
}
