package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.*;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.ImmediateFloat;
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
        return "/* cast */";
    };

    @Override
    protected void codeGenInst(DecacCompiler compiler) {
        if (this.getOperand().getType().isInt() && this.typeAfterCast.getType().isFloat()) {

        }
        if (this.getOperand().getType().isFloat() && this.typeAfterCast.getType().isInt()) {

        }
        if (this.getOperand().getType().equals( this.typeAfterCast.getType())) {

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
        this.getOperand().setType(typeBeforeCast);
        setType(typeCast);
        return typeCast;
    }

}
