package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.InlinePortion;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.Line;
import fr.ensimag.ima.pseudocode.instructions.WSTR;

import java.io.PrintStream;
import java.text.CharacterIterator;
import java.text.StringCharacterIterator;

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

    @Override
    protected void codeGenInst(DecacCompiler compiler) {
        String value = assemblyCode.getValue();
        String noQuote = value.substring(1, value.length() - 1);
        CharacterIterator it = new StringCharacterIterator(noQuote);
        StringBuilder lineInstr = new StringBuilder();
        while (it.current() != CharacterIterator.DONE) {
            lineInstr.append(it.current());
            System.out.println(it.current());
            if (it.current() == '\\' && it.next() == 'n') {
                compiler.add(new InlinePortion(lineInstr.substring(0, lineInstr.length() - 1)));
                lineInstr.delete(0, lineInstr.length());
            }
            it.next();
        }
        compiler.add(new InlinePortion(lineInstr.substring(0, lineInstr.length())));
    }

    @Override
    public int getVarNb() {
        return 0;
    }
}
