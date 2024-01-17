package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;

import java.io.PrintStream;

public class MethodAsmBody extends AbstractMethodBody {
    private StringLiteral assemblyCode;

    public MethodAsmBody(StringLiteral assemblyCode) {
        this.assemblyCode = assemblyCode;
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        assemblyCode.prettyPrint(s, prefix, false);
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        assemblyCode.iter(f);
    }
    @Override
    public void decompile(IndentPrintStream s) {
        s.print("asm (");
        assemblyCode.decompile(s);
        s.print(");");
    }

    @Override
    public void verifyMethodBodyPass3(DecacCompiler compiler, EnvironmentExp envExpParam, ClassDefinition currentClass, Type returnType) throws ContextualError {
        this.assemblyCode.verifyExpr(compiler, envExpParam, currentClass);
    }
}
