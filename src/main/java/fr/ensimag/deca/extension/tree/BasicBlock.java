package fr.ensimag.deca.extension.tree;

import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.deca.tree.*;

import java.io.PrintStream;
import java.util.LinkedList;
import java.util.List;

public class BasicBlock extends Tree {

    List<BasicBlock> preds;
    List<BasicBlock> succs;
    ListInst insts;

    public ListInst getListInst() {
        return insts;
    }

    public BasicBlock() {
        preds = new LinkedList<>();
        succs = new LinkedList<>();
        insts = new ListInst();
    }

    public void addInst(AbstractInst inst) {
        insts.add(inst);
    }

    public void addPred(BasicBlock block) {
        preds.add(block);
        block.addSucc(this);
    }

    public void addSucc(BasicBlock block) {
        succs.add(block);
        block.addPred(this);
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