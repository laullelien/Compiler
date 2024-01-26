package fr.ensimag.deca.extension.tree;

import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.deca.tree.TreeList;

public class ListBasicBlock extends TreeList<BasicBlock> {

    /**
     * The current block being used/accessed
     * when constructing the CFG
     */
    private BasicBlock currentBlock;

    /**
     * The next block to be used/accessed
     * when constructing the CFG
     * If the previous ends in a control inst,
     * this block will be the exit block
     */
    private BasicBlock nextBlock;

    public BasicBlock getCurrentBlock() {
        return currentBlock;
    }

    public BasicBlock getNextBlock() {
        return nextBlock;
    }

    public ListBasicBlock(BasicBlock entryBlock) {
        super();
        add(entryBlock);
    }

    @Override
    public void add(BasicBlock i) {
        i.setId(size());
        super.add(i);
        currentBlock = i;
    }

    @Override
    public void decompile(IndentPrintStream s) {
        for (BasicBlock i : getList()) {
            i.decompile(s);
            s.println();
        }
    }
}
