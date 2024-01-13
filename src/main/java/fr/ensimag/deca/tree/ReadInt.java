package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.*;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.RINT;
import fr.ensimag.ima.pseudocode.instructions.WFLOAT;
import fr.ensimag.ima.pseudocode.instructions.WINT;

import java.io.PrintStream;

/**
 *
 * @author gl38
 * @date 01/01/2024
 */
public class ReadInt extends AbstractReadExpr {

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        setType(compiler.environmentType.INT);
        return compiler.environmentType.INT;

    }

    @Override
    protected void codeGenInst(DecacCompiler compiler) {
        compiler.addInstruction(new RINT());
        compiler.addInstruction(new LOAD(Register.getR(1), Register.R2));

    }

    @Override
    protected void codeGenPrint(DecacCompiler compiler) {
        compiler.addInstruction(new RINT());
        compiler.addInstruction(new WINT());

    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.print("readInt()");
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        // leaf node => nothing to do
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        // leaf node => nothing to do
    }

}
