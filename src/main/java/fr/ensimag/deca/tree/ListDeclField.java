package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.deca.tools.SymbolTable;

import java.util.function.DoubleUnaryOperator;

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
            try{
                env.declare(decl.verifyDeclField(compiler, superClassSymbol, classDef));
            }
            catch (EnvironmentExp.DoubleDefException e){
                throw new ContextualError("Double définition d'un champ : la règle (2.4) n'est pas respectée", this.getLocation());
            }
            classDef.incNumberOfFields();
        }
        return env;
    }

    public void verifyListDeclFieldPass3(DecacCompiler compiler, EnvironmentExp env, ClassDefinition classDef) throws ContextualError {
        //rule 3.6
        for(AbstractDeclField decl: getList()) {
            decl.verifyDeclFieldPass3(compiler, env, classDef);
        }
    }
}
