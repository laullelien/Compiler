package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.Signature;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;

import java.util.Iterator;

public class ListDeclParam extends TreeList<AbstractDeclParam>{

    @Override
    public void decompile(IndentPrintStream s) {
        Iterator<AbstractDeclParam> paramsIterator = this.getList().iterator();
        if (paramsIterator.hasNext()){
            AbstractDeclParam firstParam = paramsIterator.next();
            firstParam.decompile(s);
        }
        while (paramsIterator.hasNext()){
            s.print(", ");
            paramsIterator.next().decompile(s);
        }
    }

    public Signature verifyListDeclParam(DecacCompiler compiler) throws ContextualError {
        Signature sigListDeclParam = new Signature();
        for (AbstractDeclParam param : this.getList()){
            Type paramType = param.verifyDeclParam(compiler);
            sigListDeclParam.add(paramType);
        }
        return sigListDeclParam;
    }
}
