package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;

import java.io.PrintStream;

public class AbstractDeclParam extends Tree{
    private AbstractIdentifier paramType;
    private AbstractIdentifier paramName;

    public AbstractDeclParam(AbstractIdentifier paramType, AbstractIdentifier paramName) {
        this.paramType = paramType;
        this.paramName = paramName;
    }

    @Override
    public void decompile(IndentPrintStream s) {
        paramType.decompile(s);
        s.print(" ");
        paramName.decompile(s);
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        paramType.prettyPrint(s, prefix, false);
        paramName.prettyPrint(s, prefix, true);
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        paramType.iter(f);
        paramName.iter(f);
    }

    public Type verifyDeclParam(DecacCompiler compiler) throws ContextualError {
        Type typeParam = this.paramType.verifyType(compiler);
        if (typeParam.isVoid()){
            throw new ContextualError("Le type du paramètre est void : la règle (2.9) n'est pas respectée", this.getLocation());
        }
        return typeParam;
    }
}
