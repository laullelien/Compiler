package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.*;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.*;
import fr.ensimag.ima.pseudocode.instructions.*;
import org.apache.commons.lang.Validate;

import java.io.PrintStream;

public class Cast extends AbstractUnaryExpr {
    private AbstractIdentifier typeAfterCastIdent;

    public Cast(AbstractIdentifier typeAfterCast, AbstractExpr operand) {
        super(operand);
        Validate.notNull(typeAfterCast);
        this.typeAfterCastIdent = typeAfterCast;

    }

        protected String getOperatorName() {
            return "(" + this.typeAfterCastIdent.getName().getName() + ") ";
        };

    @Override
    protected void codeGenInst(DecacCompiler compiler) {
        Type typeOperand = this.getOperand().getType();
        Type typeAfterCast = this.typeAfterCastIdent.getType();

        if(typeOperand.sameType(typeAfterCast)) {
            getOperand().codeGenInst(compiler);
            return;
        }
        if(typeOperand.isBoolean()) {
            compiler.addInstruction(new BRA(new Label("impossible_cast")));
            return;
        }
        if(typeOperand.isInt() || typeOperand.isFloat()) {
            DVal dval;
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
            return;
        }
        if(typeOperand.isSubType(typeAfterCast)) {
            getOperand().codeGenInst(compiler);
            compiler.addInstruction(new LEA(compiler.listVTable.getVTable(typeAfterCast.getName().getName()).getDAddr(), Register.R0));
            compiler.addInstruction(new STORE(Register.R0, new RegisterOffset(0, compiler.getRegister())));
            return;
        }
        // On doit checker à partir du type dynamique
        Label castPossible = new Label("label_" + compiler.getLabelId() + "cast_fin");
        compiler.incrementLabelId();

        Instanceof dynamicInstanceOf = new Instanceof(getOperand(), typeAfterCastIdent);
        dynamicInstanceOf.codeGenInst(compiler);
        compiler.addInstruction(new BNE(castPossible));
        // instanceof returned false
        compiler.addInstruction(new BRA(new Label("impossible_cast")));

        compiler.addLabel(castPossible);
        // instanceof returned true
        getOperand().codeGenInst(compiler);
        compiler.addInstruction(new LEA(compiler.listVTable.getVTable(typeAfterCast.getName().getName()).getDAddr(), Register.R0));
        compiler.addInstruction(new STORE(Register.R0, new RegisterOffset(0, compiler.getRegister())));
    }


    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
                           ClassDefinition currentClass) throws ContextualError {
        Type typeCast = this.typeAfterCastIdent.verifyType(compiler);
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
