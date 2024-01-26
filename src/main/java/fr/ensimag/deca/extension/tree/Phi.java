package fr.ensimag.deca.extension.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.deca.tree.AbstractExpr;
import fr.ensimag.deca.tree.TreeFunction;
import org.apache.commons.lang.Validate;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Phi extends AbstractExpr {

    private List<AbstractExpr> operands;
    private List<AbstractExpr> users;

    public List<AbstractExpr> getOperands() {
        return operands;
    }

    private BasicBlock blockLocation;

    public BasicBlock getBlockLocation() {
        return blockLocation;
    }

    public Phi() {
        operands = new ArrayList<>();
        users = new ArrayList<>();
        users.add(this);
    }

    public Phi(BasicBlock block) {
        this();
        blockLocation = block;
    }

    public void addUser(AbstractExpr use) {
        users.add(use);
    }

    public List<AbstractExpr> removeUser(AbstractExpr use) {
        users.remove(use);
        return users;
    }

    public void replaceBy(AbstractExpr same) {
        for (AbstractExpr use : users) {
            Validate.isTrue(use instanceof Phi);
            use = same;
        }
    }

    public void appendOperand(AbstractExpr operand) {
        operands.add(operand);
    }

//    public Phi(BasicBlock block, AbstractExpr... operands) {
//        this(block);
//        operands.addAll(Arrays.asList(operands));
//    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass) throws ContextualError {
        return null;
    }

    @Override
    public void decompile(IndentPrintStream s) {

    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {

    }

    @Override
    protected void iterChildren(TreeFunction f) {

    }
}
