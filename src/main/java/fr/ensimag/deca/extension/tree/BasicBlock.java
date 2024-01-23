package fr.ensimag.deca.extension.tree;

import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.deca.tree.*;

import java.io.PrintStream;
import java.util.LinkedList;
import java.util.List;

public class BasicBlock extends ListInst {

    private List<BasicBlock> preds;
    private List<BasicBlock> succs;

    private int id;

    public void setId(int id) {
        this.id = id;
    }

    public BasicBlock() {
        super();
        preds = new LinkedList<>();
        succs = new LinkedList<>();
    }

    public BasicBlock(int id) {
        super();
        preds = new LinkedList<>();
        succs = new LinkedList<>();
        this.id = id;
    }

    public void addInst(AbstractInst inst) {
        super.add(inst);
    }

    public void addPred(BasicBlock block) {
        preds.add(block);
        block.succs.add(this);
    }

    public void addSucc(BasicBlock block) {
        succs.add(block);
        block.preds.add(this);
    }

    private String prettyPrintSuccId() {
        String s = "";
        for (BasicBlock block : succs) {
            s += " #" + block.id;
        }
        return s;
    }

    @Override
    protected String prettyPrintNode() {
        return super.prettyPrintNode() +
                " #" + id + " ->" + prettyPrintSuccId();
    }
}