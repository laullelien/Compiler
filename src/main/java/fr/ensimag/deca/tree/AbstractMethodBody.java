package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;

import java.io.PrintStream;

public abstract class AbstractMethodBody extends Tree {
    public void verifyMethodBodyPass3(DecacCompiler compiler, EnvironmentExp envExpParam, ClassDefinition currentClass, Type returnType) {
    }
}
