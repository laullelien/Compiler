package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.deca.tools.SymbolTable;

public class ListDeclField extends TreeList<AbstractDeclField> {
    @Override
    public void decompile(IndentPrintStream s) {
        for (AbstractDeclField field : this.getList()){
            field.decompile(s);
        }
    }

    public EnvironmentExp verifyListDeclField(DecacCompiler compiler, SymbolTable.Symbol superClassSymbol, ClassDefinition classDef) throws ContextualError {
        // rule 2.4
        EnvironmentExp env = new EnvironmentExp();
        for(AbstractDeclField decl: getList()) {
            //env.declare(decl.verifyDeclField(compiler, superClassSymbol, classDef));
        }
        return env;
    }

    public void verifyListDeclField(DecacCompiler compiler, EnvironmentExp env, ClassDefinition classDef) throws ContextualError {
        //rule 3.6
        for(AbstractDeclField decl: getList()) {
            decl.verifyDeclField(compiler, env, classDef);
        }
    }
}
