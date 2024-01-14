package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.CMP;
import org.apache.commons.lang.Validate;

/**
 *
 * @author gl38
 * @date 01/01/2024
 */
public abstract class AbstractOpCmp extends AbstractBinaryExpr {

    public AbstractOpCmp(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        // regle 3.33
        Type leftOperandType = getLeftOperand().verifyExpr(compiler, localEnv, currentClass);
        Type rightOperandType = getRightOperand().verifyExpr(compiler, localEnv, currentClass);
        if (!(leftOperandType.isInt() || leftOperandType.isFloat()) ||
                !(rightOperandType.isInt() || rightOperandType.isFloat()))
            throw new ContextualError("Le type ne respecte pas la règle 3.33", this.getLocation());
        setType(compiler.environmentType.BOOLEAN);
        return getType();
    }

    @Override
    protected void codeGenInstruction(DecacCompiler compiler, DVal value, GPRegister target) {
        compiler.addInstruction(new CMP(value, target));
    }
}
