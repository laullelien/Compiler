package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.deca.tools.SymbolTable;

public class ListDeclMethod extends TreeList<AbstractDeclMethod> {

    @Override
    public void decompile(IndentPrintStream s) {
        for (AbstractDeclMethod method : this.getList()){
            method.decompile(s);
        }
    }

    public EnvironmentExp verifyListDeclMethod(DecacCompiler compiler, SymbolTable.Symbol superClassSymbol, ClassDefinition currentClass) throws ContextualError {
        EnvironmentExp envExpListMethods = new EnvironmentExp();
        for (AbstractDeclMethod a : this.getList()) {
            EnvironmentExp envExpMethod = a.verifyMethod(compiler, superClassSymbol, currentClass);
            currentClass.incNumberOfMethods();
            try {
                envExpListMethods.declare(envExpMethod);
            } catch (EnvironmentExp.DoubleDefException e) {
                throw new ContextualError("Des méthodes ont été déclarées avec le même nom, la règle (2.6) n'est pas respectée", this.getLocation());
            }
        }
        return envExpListMethods;
    }


    public void verifyListDeclMethodPass3(DecacCompiler compiler, ClassDefinition currentClass) throws ContextualError {
        // règle 3.10
        for (AbstractDeclMethod a : this.getList()) {
            a.verifyMethodPass3(compiler, currentClass);
        }
    }
}
