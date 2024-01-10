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

    protected void MultOperands(AbstractExpr expr, DecacCompiler compiler, boolean isFloat) {
        if (expr != null && expr.getDval() != null) {
            if (expr.getType().isFloat() || !isFloat ){
                compiler.addInstruction(new MUL(expr.getDval(), Register.R1));
            }
            else {
                compiler.addInstruction(new FLOAT(expr.getDval(),Register.R0));
                compiler.addInstruction(new MUL(Register.R0,Register.R1));
            }
        }


        if (expr instanceof Plus) {
            Plus plusExpr = (Plus) expr;
            addOperands(plusExpr.getLeftOperand(), compiler, isFloat);
            addOperands(plusExpr.getRightOperand(), compiler, isFloat);
        }
        if (expr instanceof Minus) {
            Minus minusExpr = (Minus) expr;
            addOperands(minusExpr.getLeftOperand(),compiler, isFloat);
            subOperands(minusExpr.getRightOperand(), compiler, isFloat);
        }

    }


    protected void DivOperands(AbstractExpr expr, DecacCompiler compiler, Boolean isFloat) {
        if (expr != null && expr.getDval() != null) {

            if (!expr.getDval().equals(new ImmediateFloat(0.0F))&& !expr.getDval().equals(new ImmediateInteger(0))) {
                if (expr.getType().isFloat()){
                compiler.addInstruction(new DIV(expr.getDval(), Register.R1));}
                else {
                    compiler.addInstruction(new QUO(expr.getDval(), Register.R1));
                }
            } else {
                throw new IllegalArgumentException("La valeur est 0, un peu de concentration s'il vous plait.");

            }
        }



        if (expr instanceof Plus) {
            Plus plusExpr = (Plus) expr;
            addOperands(plusExpr.getLeftOperand(), compiler, true);
            addOperands(plusExpr.getRightOperand(), compiler, true);
        }
        if (expr instanceof Minus) {
            Minus minusExpr = (Minus) expr;
            addOperands(minusExpr.getLeftOperand(),compiler, true);
            subOperands(minusExpr.getRightOperand(), compiler, true);
        }

    }

    protected void addOperands(AbstractExpr expr, DecacCompiler compiler, boolean isFloat) {
        if (expr != null && expr.getDval() != null) {
            if (expr.getType().isFloat() || !isFloat ){
                compiler.addInstruction(new ADD(expr.getDval(), Register.R1));
            }
            else {
                compiler.addInstruction(new FLOAT(expr.getDval(),Register.R0));
                compiler.addInstruction(new ADD(Register.R0,Register.R1));
            }
        }

        if (expr instanceof Multiply) {
            Multiply multExpr = (Multiply) expr;
            addOperands(multExpr.getLeftOperand(), compiler, isFloat);
            MultOperands(multExpr.getRightOperand(), compiler, isFloat);
        }

        if (expr instanceof Divide) {
            Divide divExpr = (Divide) expr;
            addOperands(divExpr.getLeftOperand(), compiler, isFloat);
            DivOperands(divExpr.getRightOperand(), compiler, isFloat);
        }

        if (expr instanceof Modulo) {
            Modulo modExpr = (Modulo) expr;
            addOperands(modExpr.getLeftOperand(), compiler, isFloat);
            modOperands(modExpr.getRightOperand(), compiler, isFloat);
        }

        if (expr instanceof Plus) {
            Plus plusExpr = (Plus) expr;
            addOperands(plusExpr.getLeftOperand(), compiler, isFloat);
            addOperands(plusExpr.getRightOperand(), compiler, isFloat);
        }
        if (expr instanceof Minus) {
            Minus minusExpr = (Minus) expr;
            addOperands(minusExpr.getLeftOperand(),compiler, isFloat);
            subOperands(minusExpr.getRightOperand(), compiler, isFloat);
        }

    }

    protected void subOperands(AbstractExpr expr, DecacCompiler compiler, boolean isFloat) {
        System.out.println("1regeth");
        if (expr != null && expr.getDval() != null) {
            System.out.println("213");
            if (expr.getType().isFloat() || !isFloat ){
                compiler.addInstruction(new SUB(expr.getDval(), Register.R1));
            }
            else {
                compiler.addInstruction(new FLOAT(expr.getDval(),Register.R0));
                compiler.addInstruction(new SUB(Register.R0,Register.R1));
            }
        }


        if (expr instanceof Plus) {
            System.out.println("1regeth1");
            Plus plusExpr = (Plus) expr;
            addOperands(plusExpr.getLeftOperand(), compiler, isFloat);
            addOperands(plusExpr.getRightOperand(), compiler, isFloat);
        }

        if (expr instanceof Minus) {
            System.out.println("1regeth2");
            Minus minusExpr = (Minus) expr;
            addOperands(minusExpr.getLeftOperand(),compiler, isFloat);
            subOperands(minusExpr.getRightOperand(), compiler, isFloat);
        }

        if (expr instanceof Multiply) {
            System.out.println("1regeth3");
            Multiply multExpr = (Multiply) expr;
            subOperands(multExpr.getLeftOperand(), compiler, isFloat);
            MultOperands(multExpr.getRightOperand(), compiler, isFloat);
        }

        if (expr instanceof Divide) {
            System.out.println("1regeth4");
             Divide divExpr = (Divide) expr;
            subOperands(divExpr.getLeftOperand(), compiler, isFloat);
            DivOperands(divExpr.getRightOperand(), compiler, isFloat);
        }

        if (expr instanceof Modulo) {
            System.out.println("5regeth");
            Modulo modExpr = (Modulo) expr;
            subOperands(modExpr.getLeftOperand(), compiler, isFloat);
            modOperands(modExpr.getRightOperand(), compiler, isFloat);
        }

    }

    protected void modOperands(AbstractExpr expr, DecacCompiler compiler, boolean isFloat) {
        if (expr != null && expr.getDval() != null) {
            compiler.addInstruction(new REM(expr.getDval(), Register.R1));

        }

        if (expr instanceof Plus) {
            Plus plusExpr = (Plus) expr;
            addOperands(plusExpr.getLeftOperand(), compiler, isFloat);
            addOperands(plusExpr.getRightOperand(), compiler, isFloat);
        }

        if (expr instanceof Minus) {
            Minus minusExpr = (Minus) expr;
            addOperands(minusExpr.getLeftOperand(), compiler, isFloat);
            subOperands(minusExpr.getRightOperand(), compiler, isFloat);
        }
    }





}
