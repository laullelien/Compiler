package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.IndentPrintStream;

import java.io.PrintStream;

import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.*;
import org.apache.commons.lang.Validate;

/**
 * Print statement (print, println, ...).
 *
 * @author gl38
 * @date 01/01/2024
 */
public abstract class AbstractPrint extends AbstractInst {

    private boolean printHex;
    private ListExpr arguments = new ListExpr();

    abstract String getSuffix();

    public AbstractPrint(boolean printHex, ListExpr arguments) {
        Validate.notNull(arguments);
        this.arguments = arguments;
        this.printHex = printHex;
    }

    public ListExpr getArguments() {
        return arguments;
    }

    @Override
    protected void verifyInst(DecacCompiler compiler, EnvironmentExp localEnv,
                              ClassDefinition currentClass, Type returnType)
            throws ContextualError {
        // regle (3.21)
        arguments.verifyListExprPrint(compiler, localEnv, currentClass);
    }

    @Override
    protected void codeGenInst(DecacCompiler compiler) {
        for (AbstractExpr a : getArguments().getList()) {
            if(a.getType().isString()) {
                a.codeGenPrint(compiler);
                continue;
            }
            if(a.getDval() != null) {
                a.codeGenPrint(compiler);
            } else {
                a.codeGenInst(compiler);
                compiler.addInstruction(new LOAD(compiler.getRegister(), Register.R1));
            }
            if(a.getType().isInt()) {
                compiler.addInstruction(new WINT());
                continue;
            }
            if(printHex) {
                compiler.addInstruction(new WFLOATX());
                continue;
            }
            compiler.addInstruction(new WFLOAT());
        }
    }

    private boolean getPrintHex() {
        return printHex;
    }

    @Override
    public void decompile(IndentPrintStream s) {
        switch (this.getSuffix() + (printHex ? "x" : "")) {
            case "lnx":
                s.print("printlnx(");
                break;
            case "ln":
                s.print("println(");
                break;
            case "x":
                s.print("printx(");
                break;
            case "":
                s.print("print(");
                break;
        }
        this.arguments.decompile(s);
        s.print(")");
        s.print(";");
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        arguments.iter(f);
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        arguments.prettyPrint(s, prefix, true);
    }

}
