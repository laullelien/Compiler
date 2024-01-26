package fr.ensimag.deca.extension;

import fr.ensimag.deca.extension.tree.BasicBlock;
import fr.ensimag.deca.extension.tree.Phi;
import fr.ensimag.deca.extension.tree.UndefPhi;
import fr.ensimag.deca.tree.*;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class SSAFormHelper {

    Map<AbstractLValue, Map<BasicBlock, AbstractExpr>> currentDef;
    Map<BasicBlock, Map<AbstractLValue, Phi>> incompletePhis;
    List<ListInst> sealedBlocks;

    public void sealBlock(BasicBlock block) {
        for (Map.Entry<AbstractLValue, Phi> entry : incompletePhis.get(block).entrySet()) {
            AbstractLValue variable  = entry.getKey();
            addPhiOperands(variable, incompletePhis.get(block).get(variable));
        }
        sealedBlocks.add(block);
    }

    public SSAFormHelper() {
        currentDef = new LinkedHashMap<>();
        incompletePhis = new LinkedHashMap<>();
    }

    public void writeVariable(AbstractLValue variable, BasicBlock block, AbstractExpr value) {
        if (!value.isConstant()) {
            return;
        }
        if (!currentDef.containsKey(variable)) {
            currentDef.put(variable, new LinkedHashMap<>());
        }
        currentDef.get(variable).put(block, value);
    }

    public AbstractExpr readVariable(AbstractLValue variable, BasicBlock block) {
        if (currentDef.containsKey(variable)) {
            if (currentDef.get(variable).containsKey(block)) {
                AbstractExpr val = currentDef.get(variable).get(block);
                return val;
            }
        }
        return null;
        // global value numbering, non stable/operationnel
        // return readVariableRecursive(variable, block);
    }

    public AbstractExpr readVariableRecursive(AbstractLValue variable, BasicBlock block) {
        AbstractExpr val;
        if (!(sealedBlocks.contains(block))) {
            // Incomplete CFG
            val = new Phi(block);
            if (!incompletePhis.containsKey(block)) {
                incompletePhis.put(block, new LinkedHashMap<>());
            }
            incompletePhis.get(block).put(variable, (Phi) val);
        } else if (block.getPreds().size() == 1) {
            // Optimize the common case of one predecessor : No phi needed
            val = readVariable(variable, block.getPreds().get(0));
        } else {
            // Break potential cycles with operandless phi
            val = new Phi(block);
            writeVariable(variable, block, val);
            val = addPhiOperands(variable, (Phi) val);
        }
        writeVariable(variable, block, val);
        return val;
    }

    private AbstractExpr addPhiOperands(AbstractLValue variable, Phi phi) {
        // Determine operands from predecessors
        for (BasicBlock pred : phi.getBlockLocation().getPreds()) {
            AbstractExpr val = readVariable(variable, pred);
            if (val instanceof Phi) {
                ((Phi) val).addUser(phi);
            }
            phi.appendOperand(val);
        }
        return tryRemoveTrivialPhi(phi);
    }

    private AbstractExpr tryRemoveTrivialPhi(Phi phi) {
        AbstractExpr same = null;
        for (AbstractExpr op : phi.getOperands()) {
            if (op.equals(same) || op.equals(phi)) {
                continue; // Unique value or self-reference
            }
            if (same != null) {
                return phi; // The phi merges at least two values: not trivial
            }
            same = op;
        }
        if (same == null) {
            same = new UndefPhi(); // The phi is unreachable or in the start block
        }
        List<AbstractExpr> users = phi.removeUser(phi); // Remember all users except the phi itself
        phi.replaceBy(same); // Reroute all uses of phi to same and remove phi

        for (AbstractExpr use : users) {
            if (use instanceof Phi)
                tryRemoveTrivialPhi((Phi) use);
        }
        return same;
    }
}
