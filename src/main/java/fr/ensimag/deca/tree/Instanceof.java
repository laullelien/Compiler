package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.*;
import fr.ensimag.ima.pseudocode.*;
import fr.ensimag.ima.pseudocode.instructions.*;

import java.util.Objects;

public class Instanceof extends AbstractBinaryExpr{
    public Instanceof(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    protected void codeGenInst(DecacCompiler compiler) {
        compiler.addComment("Debut instanceof");
        String labelString = "label_" + compiler.getLabelId();
        compiler.incrementLabelId();
        Label returnTrue = new Label(labelString + "return_true");
        Label returnFalse = new Label(labelString + "return_false");
        Label whileStart = new Label(labelString + "while_start");
        Label instanceofEnd = new Label(labelString + "end_instanceof");

        getLeftOperand().codeGenInst(compiler);
        // une instance de classe se trouve dans compiler.gerRegister()
        String parentClassName = getRightOperand().getType().getName().getName();

        DAddr parentClassDAddr = compiler.listVTable.getVTable(parentClassName).getDAddr();

        // on enregistre l'adresse de la classe de leftOperand dans R1
        compiler.addInstruction(new LOAD(new RegisterOffset(0, compiler.getRegister()), Register.R1));

        // on enregistre l'adresse de la classe de rightOperand dans getRegister()
        compiler.addInstruction(new LEA(parentClassDAddr, compiler.getRegister()));

        // rightOperand is Object
        compiler.addInstruction(new CMP(new NullOperand(), compiler.getRegister()));
        compiler.addInstruction(new BEQ(returnTrue));

        compiler.addLabel(whileStart);

        //On a atteint null
        compiler.addInstruction(new CMP(new NullOperand(), Register.R1));

        compiler.addInstruction(new BEQ(returnFalse));

        compiler.addInstruction(new CMP(Register.R1, compiler.getRegister()));
        compiler.addInstruction(new BEQ(returnTrue));

        //leftOperand = *leftOperand
        compiler.addInstruction(new LOAD(new RegisterOffset(0, Register.R1), Register.R1));

        compiler.addInstruction(new BRA(whileStart));

        compiler.addLabel(returnFalse);
        compiler.addInstruction(new LOAD(0, compiler.getRegister()));
        compiler.addInstruction(new BRA(instanceofEnd));

        compiler.addLabel(returnTrue);
        compiler.addInstruction(new LOAD(1, compiler.getRegister()));

        compiler.addLabel(instanceofEnd);
        compiler.addComment("Fin instanceof");
    }

    @Override
    protected String getOperatorName() {
        return "instanceof";
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass) throws ContextualError {
        Type exprType = getLeftOperand().verifyExpr(compiler, localEnv, currentClass);
        Type typeType = ((Identifier)getRightOperand()).verifyType(compiler);
        typeInstanceof(exprType, typeType);
        setType(compiler.environmentType.BOOLEAN);
        return getType();
    }

    private void typeInstanceof(Type exprType, Type typeType) throws ContextualError {
        if(!(exprType.isClassOrNull() && typeType.isClass())) {
            throw new ContextualError("La regle 3.40 n'a pas ete respectee. Voir type_instance_of.", getLocation());
        }
    }
}
