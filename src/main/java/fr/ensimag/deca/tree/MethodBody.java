package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;

import java.io.PrintStream;

public class MethodBody extends AbstractMethodBody {
    private ListDeclVar declVar;
    private ListInst inst;

    public MethodBody(ListDeclVar declVar, ListInst inst) {
        this.declVar = declVar;
        this.inst = inst;
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        declVar.prettyPrint(s, prefix, false);
        inst.prettyPrint(s, prefix, true);
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        declVar.iter(f);
        inst.iter(f);
    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.println("{");
        s.indent();
        declVar.decompile(s);
        inst.decompile(s);
        s.unindent();
        s.println("}");
    }

    @Override
    public void verifyMethodBodyPass3(DecacCompiler compiler, EnvironmentExp envExpParam, ClassDefinition currentClass, Type returnType)
                                        throws ContextualError {
        declVar.verifyListDeclVariable(compiler, envExpParam, currentClass);
        inst.verifyListInst(compiler, envExpParam, currentClass, returnType);
    }
}
