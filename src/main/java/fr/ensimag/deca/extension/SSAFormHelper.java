package fr.ensimag.deca.extension;

import fr.ensimag.deca.extension.tree.BasicBlock;
import fr.ensimag.deca.tree.*;
import org.apache.commons.lang.NotImplementedException;
import org.apache.commons.lang.Validate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SSAFormHelper {

    Map<AbstractIdentifier, Map<BasicBlock, AbstractExpr>> currentDef;
    List<ListInst> sealedBlocks;

    public SSAFormHelper() {
        currentDef = new HashMap<>();
    }

    public void writeVariable(AbstractIdentifier variable, BasicBlock block, AbstractExpr value) {
        if (!currentDef.containsKey(variable)) {
            currentDef.put(variable, new HashMap<>());
        }
        currentDef.get(variable).put(block, value);
    }

    public AbstractExpr readVariable(AbstractIdentifier variable, BasicBlock block) {
        Validate.isTrue(currentDef.containsKey(variable));
        if (currentDef.get(variable).containsKey(block))
            return currentDef.get(variable).get(block);
        // return readVariableRecursive(variable, block);
        return null;
    }

    public AbstractExpr readVariableRecursive(AbstractIdentifier variable, BasicBlock block) {
        throw new NotImplementedException("not here yet");
    }
}
