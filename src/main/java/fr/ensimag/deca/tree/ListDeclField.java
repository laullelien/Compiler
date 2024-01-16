package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.IndentPrintStream;

public class ListDeclField extends TreeList<AbstractDeclField> {
    @Override
    public void decompile(IndentPrintStream s) {
        for (AbstractDeclField field : this.getList()){
            field.decompile(s);
            s.println("");
        }
    }

    public EnvironmentExp verifyListDeclField(DecacCompiler compiler) {
        return null;
    }
}
