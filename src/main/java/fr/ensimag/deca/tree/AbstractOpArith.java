package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.*;
import org.apache.commons.lang.Validate;


/**
 * Arithmetic binary operations (+, -, /, ...)
 * 
 * @author gl38
 * @date 01/01/2024
 */
public abstract class AbstractOpArith extends AbstractBinaryExpr {

    @Override
    protected void codeGenPrint(DecacCompiler compiler) {
        codeGenInst(compiler);
        compiler.addInstruction(new LOAD(Register.getR(2), Register.R1));
        if (getType().isInt())
            compiler.addInstruction(new WINT());
        else if (getType().isFloat())
            compiler.addInstruction(new WFLOAT());
    }

    /**
     * Permet de générer le résultat d'une opération arithmétique.
     * Le résultat sera stocké dans le registre R2
     */
    @Override
    protected void codeGenInst(DecacCompiler compiler) {
        codeExp(compiler, this, 2);
    }

    /**
     * Génération de code naïve pour expressions arithmétiques
     * donné par les profs
     * cf. diapo étape C, page 12
     */
    private void codeExp(DecacCompiler compiler, AbstractExpr e, int n) {
        // On vérifie que e est un litéral ou une variable
        if (e.getDval() != null)
            compiler.addInstruction(new LOAD(e.getDval(), Register.getR(n)));
        else {
            // e est donc une opération
            Validate.isTrue(e instanceof AbstractBinaryExpr);
            AbstractBinaryExpr op = (AbstractBinaryExpr) e;
            if (op.getRightOperand().getDval() != null) {
                codeExp(compiler, op.getLeftOperand(), n);
                op.codeGenInstruction(compiler, op.getRightOperand().getDval(), Register.getR(n));
            }
            else {
                if (n < compiler.getCompilerOptions().getMaxRegisters()) {
                    codeExp(compiler, op.getLeftOperand(), n);
                    codeExp(compiler, op.getRightOperand(), n + 1);
                    op.codeGenInstruction(compiler, Register.getR(n + 1), Register.getR(n));
                }
                else {
                    codeExp(compiler, op.getLeftOperand(), n);
                    compiler.addInstruction(new PUSH(Register.getR(n)));
                    codeExp(compiler, op.getRightOperand(), n + 1);
                    compiler.addInstruction(new LOAD(Register.getR(n), Register.R0));
                    compiler.addInstruction(new POP(Register.getR(n)));
                    op.codeGenInstruction(compiler, Register.R0, Register.getR(n));
                }
            }
        }
    }

    public AbstractOpArith(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
                           ClassDefinition currentClass) throws ContextualError {
        if (this.getLeftOperand().verifyExpr(compiler, localEnv, currentClass).isInt() && this.getRightOperand().verifyExpr(compiler, localEnv, currentClass).isInt()) {
            setType(compiler.environmentType.INT);
            return getType();
        } else if (this.getLeftOperand().verifyExpr(compiler, localEnv, currentClass).isFloat() && this.getRightOperand().verifyExpr(compiler, localEnv, currentClass).isFloat()) {
            setType(compiler.environmentType.FLOAT);
            return getType();
        } else if (this.getLeftOperand().verifyExpr(compiler, localEnv, currentClass).isFloat() && this.getRightOperand().verifyExpr(compiler, localEnv, currentClass).isInt()) {
            ConvFloat convFloat = new ConvFloat(this.getRightOperand());
            setType(convFloat.verifyExpr(compiler, localEnv, currentClass));
            this.setRightOperand(convFloat);
            return getType();
        } else if (this.getLeftOperand().verifyExpr(compiler, localEnv, currentClass).isInt() && this.getRightOperand().verifyExpr(compiler, localEnv, currentClass).isFloat()) {
            ConvFloat convFloat = new ConvFloat(this.getLeftOperand());
            setType(convFloat.verifyExpr(compiler, localEnv, currentClass));
            this.setLeftOperand(convFloat);
            return getType();
        }
        throw new ContextualError("Le type ne respecte pas la règle 3.33", this.getLocation());
    }
}
