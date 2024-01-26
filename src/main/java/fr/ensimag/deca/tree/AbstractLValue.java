package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.extension.tree.ListBasicBlock;

/**
 * Left-hand side value of an assignment.
 * 
 * @author gl38
 * @date 01/01/2024
 */
public abstract class AbstractLValue extends AbstractExpr {
    @Override
    protected AbstractExpr evaluate(DecacCompiler compiler, ListBasicBlock blocks) {
        AbstractExpr localValue = compiler.ssaFormHelper.readVariable(this, blocks.getCurrentBlock());
        if (localValue == null)
            return this;
        return localValue;
    }
}
