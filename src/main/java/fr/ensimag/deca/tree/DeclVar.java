package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.*;
import fr.ensimag.deca.extension.tree.ListBasicBlock;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.*;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.STORE;
import org.apache.commons.lang.Validate;

import java.io.PrintStream;

/**
 * @author gl38
 * @date 01/01/2024
 */
public class DeclVar extends AbstractDeclVar {


    final private AbstractIdentifier type;
    final private AbstractIdentifier varName;
    final private AbstractInitialization initialization;

    public DeclVar(AbstractIdentifier type, AbstractIdentifier varName, AbstractInitialization initialization) {
        Validate.notNull(type);
        Validate.notNull(varName);
        Validate.notNull(initialization);
        this.type = type;
        this.varName = varName;
        this.initialization = initialization;
    }

    @Override
    protected void verifyDeclVar(DecacCompiler compiler,
                                 EnvironmentExp localEnv, ClassDefinition currentClass)
            throws ContextualError {
        Type typeVariable = type.verifyType(compiler);
        // règle 3.17
        if (typeVariable.isVoid()) {
            throw new ContextualError("Une variable de type void est invalide : la règle (3.17) n'est pas respectée", this.getLocation());
        }
        initialization.verifyInitialization(compiler, typeVariable, localEnv.stackEnvironment(localEnv, localEnv.getParentEnvironment()), currentClass);
        // ajout de la VariableDefinition dans localEnv
        try {
            ExpDefinition def = new VariableDefinition(typeVariable, this.getLocation());
            localEnv.declare(this.varName.getName(), def);
            varName.setDefinition(def);
        } catch (EnvironmentExp.DoubleDefException e) {
            throw new ContextualError("Le symbole existe déjà : la règle (3.17) n'est pas respectée", this.getLocation());
        }
    }

    @Override
    public void decompile(IndentPrintStream s) {
        type.decompile(s);
        s.print(" ");
        varName.decompile(s);
        initialization.decompile(s);
        s.println(";");
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        type.iter(f);
        varName.iter(f);
        initialization.iter(f);
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        type.prettyPrint(s, prefix, false);
        varName.prettyPrint(s, prefix, false);
        initialization.prettyPrint(s, prefix, true);
    }

    @Override
    public void setOperand(DecacCompiler compiler) {
        varName.getVariableDefinition().setOperand(compiler.getVarDVal());
    }

    @Override
    public void setLocalOperand(int offset) {
        varName.getVariableDefinition().setOperand(new RegisterOffset(offset, Register.LB));
    }

    @Override
    public void appendToBlock(DecacCompiler compiler, ListBasicBlock blocks) {
        if (initialization instanceof Initialization) {
            ((Initialization) initialization).setExpression(((Initialization) initialization).getExpression().evaluate(compiler, blocks));
            // local value numbering
            compiler.ssaFormHelper.writeVariable(varName, blocks.getCurrentBlock(), ((Initialization) initialization).getExpression());
        }
    }

    @Override
    public void codeGenDeclVar(DecacCompiler compiler) {
        initialization.codeGenInst(compiler);
        if (initialization instanceof Initialization) {
            DVal operand = varName.getVariableDefinition().getOperand();
            if(operand instanceof GPRegister) {
                compiler.addInstruction(new LOAD(compiler.getRegister(), (GPRegister) operand));
            } else {
                compiler.addInstruction(new STORE(compiler.getRegister(), (DAddr) operand));
            }
        }
    }
}
