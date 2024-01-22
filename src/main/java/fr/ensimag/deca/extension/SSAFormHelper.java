package fr.ensimag.deca.extension;

import fr.ensimag.deca.tree.*;
import org.apache.commons.lang.NotImplementedException;

import java.util.List;
import java.util.Map;

public class SSAFormHelper {

    Map<DeclVar, ListInst> currentDef;
    List<ListInst> sealedBlocks;

    public SSAFormHelper() {
    }

    public void writeVariable(AbstractDeclVar variable, ListInst block, AbstractExpr value) {
        block.getCurrentDef().put(variable, value);
    }

    public AbstractExpr readVariable(AbstractDeclVar variable, ListInst block) {
        if (block.getCurrentDef().containsKey(variable))
            return block.getCurrentDef().get(variable); // local value numbering
        else return readVariableRecursive(variable, block); // global value numbering
    }

    public AbstractExpr readVariableRecursive(AbstractDeclVar variable, ListInst block) {
        throw new NotImplementedException("not here yet");
    }

}
