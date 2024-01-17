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
        Type returnTypeMethod= methodReturnType.verifyType(compiler);
        Signature paramsSignature = methodParameters.verifyListDeclParam(compiler);
        TypeDefinition defSuperClass = compiler.environmentType.defOfType(superClassSymbol);
        if (defSuperClass != null && defSuperClass.isClass()) {
            ExpDefinition envExpSuper = ((ClassDefinition) defSuperClass).getMembers().get(this.methodName.getName());
            if (envExpSuper != null) {
                if (envExpSuper.isMethod()) {
                    Signature paramSignatureReDef = ((MethodDefinition) envExpSuper).getSignature();
                    if (paramsSignature.equals(paramSignatureReDef)) {
                        if (!((ClassType) returnTypeMethod).isSubClassOf((ClassType) envExpSuper.getType())){
                            throw new ContextualError("La redéfinition est mauvaise (problème de sous-type) et la règle (2.7) n'est pas respectée.", this.getLocation());
                        }
                        EnvironmentExp newMethodEnvironment = new EnvironmentExp();
                        try {
                            newMethodEnvironment.declare(methodName.getName(), envExpSuper);
                        }
                        catch (EnvironmentExp.DoubleDefException e){
                            throw new ContextualError("Cette exception ne sera jamais levée alors je peux mettre ce que je veux... Joyeux Anniversaire !", this.getLocation());
                        }
                        return newMethodEnvironment;
                    }
                    else{
                        throw new ContextualError("La redéfinition est mauvaise et la règle (2.7) n'est pas respectée.", this.getLocation());
                    }
                }
                else{
                    throw new ContextualError("La redéfinition est mauvaise (pas une méthode) et la règle (2.7) n'est pas respectée.", this.getLocation());
                }
            }
        }
        throw new ContextualError("La règle (2.3) n'est pas respectée... Pas de Super Classe", this.getLocation());
    }

}
