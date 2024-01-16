package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
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

    public EnvironmentExp verifyListDeclField(DecacCompiler compiler, SymbolTable.Symbol superClassSymbol, SymbolTable.Symbol className) {
        // rule 2.4
        EnvironmentExp env = new EnvironmentExp();
        for(AbstractDeclField decl: getList()) {
            env.declare(decl.verifyDeclField(compiler, superClassSymbol, className));
        }
        return env;
    }
}
