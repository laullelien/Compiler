package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.*;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.deca.tools.SymbolTable;

import java.io.PrintStream;

public abstract class AbstractDeclMethod extends Tree {
    private AbstractIdentifier methodReturnType;

    public AbstractIdentifier getMethodReturnType() {
        return methodReturnType;
    }

    private AbstractIdentifier methodName;

    public AbstractIdentifier getMethodName() {
        return methodName;
    }

    private ListDeclParam methodParameters;
    private AbstractMethodBody methodBody;

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

    public EnvironmentExp verifyMethod(DecacCompiler compiler, SymbolTable.Symbol superClassSymbol, ClassDefinition currentClass) throws ContextualError {
        Type returnType = methodReturnType.verifyType(compiler);
        Signature paramsSignature = methodParameters.verifyListDeclParam(compiler);
        TypeDefinition defSuperClass = compiler.environmentType.defOfType(superClassSymbol);
        if (defSuperClass == null || !(defSuperClass.isClass())) {
            throw new ContextualError("La règle (2.3) n'est pas respectée... Pas de Super Classe", this.getLocation());
        }
        ExpDefinition methodDefinitionInSuperClass = ((ClassDefinition) defSuperClass).getMembers().get(this.methodName.getName());
        if (!(methodDefinitionInSuperClass == null || methodDefinitionInSuperClass.isMethod())){
            throw new ContextualError("L'identificateur de la méthode à déclarée existe dans la super classe mais ne définit pas une méthode : la règle (2.7) n'est pas respectée.", this.getLocation());
        }
        if (methodDefinitionInSuperClass != null){
            Signature paramSignatureReDef = ((MethodDefinition) methodDefinitionInSuperClass).getSignature();
            if (!paramsSignature.equals(paramSignatureReDef)) {
                throw new ContextualError("La signature n'est pas la même et la règle (2.7) n'est pas respectée.", this.getLocation());
            }
            if (!(returnType.isSubType(methodDefinitionInSuperClass.getType()))) {
                throw new ContextualError("La redéfinition est mauvaise (problème de sous-type) et la règle (2.7) n'est pas respectée.", this.getLocation());
            }
        }
        EnvironmentExp methodEnvExp = new EnvironmentExp();
        ExpDefinition methodDef = new MethodDefinition(returnType, this.getLocation(), paramsSignature, currentClass.getNumberOfMethods());
        try {
            methodEnvExp.declare(methodName.getName(), methodDef);
        } catch (EnvironmentExp.DoubleDefException e) {
            throw new ContextualError("Cette exception ne sera jamais levée alors je peux mettre ce que je veux... Joyeux Anniversaire !", this.getLocation());
        }

        methodName.setDefinition(methodDef);
        methodName.setType(returnType);
        return methodEnvExp;
    }

    public void verifyMethodPass3(DecacCompiler compiler, ClassDefinition currentClass) throws ContextualError{
        // règle 3.11
        Type returnType = methodReturnType.verifyType(compiler);
        EnvironmentExp envExpParam = methodParameters.verifyListDeclParamPass3(compiler);
        currentClass.currentMethodNameForReturn = this.methodName.getName().getName();
        methodBody.verifyMethodBodyPass3(compiler, envExpParam, currentClass, returnType);
    }

    public AbstractMethodBody getMethodBody() {
        return methodBody;
    }

    protected abstract void codeGenMethod(DecacCompiler compiler, String className);
}
