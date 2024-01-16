package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.*;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.ImmediateFloat;
import fr.ensimag.ima.pseudocode.instructions.FLOAT;
import fr.ensimag.ima.pseudocode.instructions.INT;
import org.apache.commons.lang.Validate;

import java.io.PrintStream;

public class Cast extends AbstractUnaryExpr {
    private AbstractIdentifier typeAfterCast;

    public Cast(AbstractIdentifier typeAfterCast, AbstractExpr operand) {
        super(operand);
        Validate.notNull(typeAfterCast);
        this.typeAfterCast = typeAfterCast;

    }

        protected String getOperatorName() {
            return "(" + this.typeAfterCast.getName().getName() + ") ";
        };

    @Override
    protected void codeGenInst(DecacCompiler compiler) {
        if (getOperand().getDval() != null) {
            if (this.getOperand().getType().isInt() && this.typeAfterCast.getType().isFloat()) {
                compiler.addInstruction(new FLOAT(getOperand().getDval(), compiler.getRegister()));
            } else if (this.getOperand().getType().isFloat() && this.typeAfterCast.getType().isInt()) {
                compiler.addInstruction(new INT(getOperand().getDval(), compiler.getRegister()));
            }
        }
        else {
            getOperand().codeGenInst(compiler);
            if (this.getOperand().getType().isInt() && this.typeAfterCast.getType().isFloat()) {
                compiler.addInstruction(new FLOAT(compiler.getRegister(), compiler.getRegister()));
            } else if (this.getOperand().getType().isFloat() && this.typeAfterCast.getType().isInt()) {
                compiler.addInstruction(new INT(compiler.getRegister(), compiler.getRegister()));
            }
        }
    }


    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
                           ClassDefinition currentClass) throws ContextualError {
        Type typeCast = this.typeAfterCast.verifyType(compiler);
        Type typeBeforeCast = this.getOperand().verifyExpr(compiler, localEnv, currentClass);
        if (!compiler.environmentType.castCompatible(typeBeforeCast, typeCast)){
            throw new ContextualError("Le cast n'est pas valide et la règle 3.39 n'est pas respectée", this.getLocation());
        }
        if (typeCast.isFloat() && typeBeforeCast.isInt()){
            AbstractExpr convFloat = new ConvFloat(this.getOperand());
            this.setOperand(convFloat);
            this.getOperand().verifyExpr(compiler, localEnv, currentClass);
        }
        setType(typeCast);
        return getType();
    }


    @Override
    public void decompile(IndentPrintStream s) {
        s.print(getOperatorName());
        s.print("(");
        this.getOperand().decompile(s);
        s.print(")");
    }

}
