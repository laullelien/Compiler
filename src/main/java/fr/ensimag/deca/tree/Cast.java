package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.*;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.DVal;
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
        DVal dval;
        Type typeOperand = this.getOperand().getType();
        Type typeAfterCast = this.typeAfterCast.getType();
        if (getOperand().getDval() != null)
            dval = this.getOperand().getDval();
        else {
            getOperand().codeGenInst(compiler);
            dval = compiler.getRegister();
        }
        if (typeOperand.isInt() && typeAfterCast.isFloat()) {
            compiler.addInstruction(new FLOAT(dval, compiler.getRegister()));
        } else if (typeOperand.isFloat() && typeAfterCast.isInt()) {
            compiler.addInstruction(new INT(dval, compiler.getRegister()));
        }
    }


    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
                           ClassDefinition currentClass) throws ContextualError {
        Type typeCast = this.typeAfterCast.verifyType(compiler);
        Type typeBeforeCast = this.getOperand().verifyExpr(compiler, localEnv, currentClass);
        System.out.println(typeCast.getName().getName());
        System.out.println(typeBeforeCast.getName().getName());
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
