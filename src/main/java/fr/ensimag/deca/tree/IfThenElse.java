package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.extension.tree.BasicBlock;
import fr.ensimag.deca.extension.tree.ListBasicBlock;
import fr.ensimag.deca.tools.IndentPrintStream;
import java.io.PrintStream;

import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.BEQ;
import fr.ensimag.ima.pseudocode.instructions.BRA;
import fr.ensimag.ima.pseudocode.instructions.CMP;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import org.apache.commons.lang.Validate;

/**
 * Full if/else if/else statement.
 *
 * @author gl38
 * @date 01/01/2024
 */
public class IfThenElse extends AbstractInst {
    
    private final AbstractExpr condition; 
    private ListInst thenBranch;
    private ListInst elseBranch;

    public IfThenElse(AbstractExpr condition, ListInst thenBranch, ListInst elseBranch) {
        Validate.notNull(condition);
        Validate.notNull(thenBranch);
        Validate.notNull(elseBranch);
        this.condition = condition;
        this.thenBranch = thenBranch;
        this.elseBranch = elseBranch;
    }

    public ListInst getElseBranch() {
        return elseBranch;
    }

    public void setElseBranch(ListInst elseBranch) {
        this.elseBranch = elseBranch;
    }

    @Override
    protected void verifyInst(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass, Type returnType)
            throws ContextualError {
        // règle 3.22
        condition.verifyCondition(compiler, localEnv, currentClass);
        thenBranch.verifyListInst(compiler, localEnv, currentClass, returnType);
        elseBranch.verifyListInst(compiler, localEnv, currentClass, returnType);
    }

    @Override
    public void appendToBlock(ListBasicBlock blocks) {
        // if (thenBranch.isEmpty() && elseBranch.isEmpty())
            // return; // optimisation: ne pas compiler un If trivial

        super.appendToBlock(blocks);
        BasicBlock exitBlock = new BasicBlock();

        // Création des blocs Then et Else
        BasicBlock thenBlock = new BasicBlock();
        blocks.getCurrentBlock().addSucc(thenBlock);
        BasicBlock elseBlock = new BasicBlock();
        blocks.getCurrentBlock().addSucc(elseBlock);

        // Traitement du bloc Then
        blocks.add(thenBlock);
        thenBranch.constructBasicBlocks(blocks);
        thenBranch = thenBlock;
        blocks.getCurrentBlock().addSucc(exitBlock);

        // Traitement du bloc Else
        blocks.add(elseBlock);
        elseBranch.constructBasicBlocks(blocks);
        elseBranch = elseBlock;
        blocks.getCurrentBlock().addSucc(exitBlock);

        // Rajout du prochain block pour les prochaines instructions
        blocks.add(exitBlock);
    }

    @Override
    protected void codeGenInst(DecacCompiler compiler) {
        String labelString = "label_" + compiler.getLabelId();
        compiler.incrementLabelId();
        Label elseLabel = new Label(labelString + "_else");
        Label endLabel = new Label(labelString + "_fin");

        condition.codeGenInst(compiler);

        if(!(compiler.lastIsLoad() && ((LOAD)(compiler.getLastInstruction())).getReg() == compiler.getRegister())) {
            compiler.addInstruction(new CMP(0, compiler.getRegister()));
        }
        compiler.addInstruction(new BEQ(elseLabel));

        thenBranch.codeGenListInst(compiler);
        // We don't jump if there is no reason to
        if(elseBranch.getList().size() != 0) {
            compiler.addInstruction(new BRA(endLabel));
        }

        compiler.addLabel(elseLabel);
        elseBranch.codeGenListInst(compiler);

        compiler.addLabel(endLabel);
    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.print("if(");
        condition.decompile(s);
        s.println(") {");
        s.indent();
        thenBranch.decompile(s);
        s.unindent();
        s.println("} else {");
        s.indent();
        elseBranch.decompile(s);
        s.unindent();
        s.println("}");
    }

    @Override
    protected
    void iterChildren(TreeFunction f) {
        condition.iter(f);
        thenBranch.iter(f);
        elseBranch.iter(f);
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        condition.prettyPrint(s, prefix, false);
        thenBranch.prettyPrint(s, prefix, false);
        elseBranch.prettyPrint(s, prefix, true);
    }
}
