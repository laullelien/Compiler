package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.WFLOAT;
import fr.ensimag.ima.pseudocode.instructions.WINT;

/**
 *
 * @author gl38
 * @date 01/01/2024
 */
public class Modulo extends AbstractOpArith {

    public Modulo(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        if (this.getLeftOperand().verifyExpr(compiler, localEnv, currentClass).isInt() && this.getRightOperand().verifyExpr(compiler, localEnv, currentClass).isInt()) {
            setType(compiler.environmentType.INT);
            return getType();
        }
        throw new ContextualError("Le type ne respecte pas la règle 3.33", this.getLocation());
    }

    protected void codeGenPrint(DecacCompiler compiler) {
            compiler.addInstruction(new LOAD(0, Register.R1));
            compiler.addInstruction(new WINT());
        }



    @Override
    protected String getOperatorName() {
        return "%";
    }

}
