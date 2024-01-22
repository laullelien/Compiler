package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.*;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;

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

    public EnvironmentExp verifyListDeclParamPass3(DecacCompiler compiler) throws ContextualError{
        EnvironmentExp environmentListParam = new EnvironmentExp();
        int indexParam = -3;
        for (AbstractDeclParam a : this.getList()) {
            EnvironmentExp envParam = a.verifyDeclParamPass3(compiler);
            ((ParamDefinition) a.getParamName().getDefinition()).setOperand(new RegisterOffset(indexParam, Register.LB));
            indexParam--;
            try {
                environmentListParam.declare(envParam);
            } catch (EnvironmentExp.DoubleDefException e) {
                throw new ContextualError("Paramètre déclaré deux fois : la règle (3.12) n'est pas respectée.", a.getLocation());
            }

        }
        return environmentListParam;
    }
}
