package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.DecacInternalError;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.ImmediateFloat;
import fr.ensimag.ima.pseudocode.ImmediateInteger;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.*;

import javax.print.attribute.standard.MediaSize;


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
     * Cette fonction sera essentiellement appelée par les filles concrètes de la classe (les opérations)
     * qui font un appel à super et attend que le résultat de son opérande droite soit stocké dans R2
     * et que le résultat de son opérande gauche soit stocké dans R3
     */
    @Override
    protected void codeGenInst(DecacCompiler compiler) {
        if (!getLeftOperand().isTerminal())
            // appel récursif pour obtenir le résultat de l'opération à gauche
            // qui sera alors stocké dans R2
            getLeftOperand().codeGenInst(compiler);
        else 
            compiler.addInstruction(new LOAD(getLeftOperand().getDval(), Register.getR(2)));
        if (!getRightOperand().isTerminal()) {
            // On stocke temporairement le résultat de l'opérande gauche dans un autre registre
            // pour ne pas être écrasé par le calcul de l'opération à droite
            compiler.addInstruction(new LOAD(Register.getR(2), Register.R0));
            getRightOperand().codeGenInst(compiler);
            compiler.addInstruction(new LOAD(Register.getR(2), Register.getR(3)));
            compiler.addInstruction(new LOAD(Register.R0, Register.getR(2)));
        }
        else
            compiler.addInstruction(new LOAD(getRightOperand().getDval(), Register.getR(3)));
    }

    public AbstractOpArith(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        if (this.getLeftOperand().verifyExpr(compiler, localEnv, currentClass).isInt() && this.getRightOperand().verifyExpr(compiler, localEnv, currentClass).isInt()){
            setType(compiler.environmentType.INT);
            return getType();
        }
        if((this.getLeftOperand().verifyExpr(compiler, localEnv, currentClass).isFloat() && this.getRightOperand().verifyExpr(compiler, localEnv, currentClass).isInt())
            || (this.getLeftOperand().verifyExpr(compiler, localEnv, currentClass).isInt() && this.getRightOperand().verifyExpr(compiler, localEnv, currentClass).isFloat())
            || (this.getLeftOperand().verifyExpr(compiler, localEnv, currentClass).isFloat() && this.getRightOperand().verifyExpr(compiler, localEnv, currentClass).isFloat())){
            setType(compiler.environmentType.FLOAT);
            return getType();
        }
        throw new ContextualError("Le type ne respecte pas la règle 3.33", this.getLocation());
    }
}
