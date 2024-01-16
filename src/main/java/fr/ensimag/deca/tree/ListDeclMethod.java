package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.IndentPrintStream;

public class ListDeclMethod extends TreeList<AbstractDeclMethod> {

    @Override
    public void decompile(IndentPrintStream s) {
        for (AbstractDeclMethod method : this.getList()){
            method.decompile(s);
        }
    }

    public EnvironmentExp verifyListDeclMethod(DecacCompiler compiler) {
        for (AbstractDeclMethod a : this.getList()) {
            a.verifyMethod(compiler);
        }
        return null;
    }
}
