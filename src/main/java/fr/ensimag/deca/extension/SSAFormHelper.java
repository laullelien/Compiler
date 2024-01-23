package fr.ensimag.deca.extension;

import fr.ensimag.deca.extension.tree.BasicBlock;
import fr.ensimag.deca.tree.*;
import org.apache.commons.lang.NotImplementedException;

import java.util.List;
import java.util.Map;

public class SSAFormHelper {

    Map<DeclVar, ListInst> currentDef;
    List<ListInst> sealedBlocks;

    public SSAFormHelper() {
    }

    public void writeVariable(AbstractDeclVar variable, BasicBlock block, AbstractExpr value) {
        variable.getCurrentDef().put(block, new Initialization(value));
    }

    public AbstractInitialization readVariable(AbstractDeclVar variable, BasicBlock block) {
        if (variable.getCurrentDef().containsKey(block))
            return variable.getCurrentDef().get(block);
        return readVariableRecursive(variable, block);
    }

    public AbstractInitialization readVariableRecursive(AbstractDeclVar variable, BasicBlock block) {
        throw new NotImplementedException("not here yet");
    }
}
