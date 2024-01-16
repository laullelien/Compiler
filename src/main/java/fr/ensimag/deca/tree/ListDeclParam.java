package fr.ensimag.deca.tree;

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
}
