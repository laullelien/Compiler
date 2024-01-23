package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.extension.tree.BasicBlock;
import fr.ensimag.deca.extension.tree.ListBasicBlock;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.Label;
import java.io.PrintStream;

import fr.ensimag.ima.pseudocode.instructions.*;
import org.apache.commons.lang.Validate;

/**
 *
 * @author gl38
 * @date 01/01/2024
 */
public class While extends AbstractInst {
    private AbstractExpr condition;
    private ListInst body;

    public AbstractExpr getCondition() {
        return condition;
    }

    public ListInst getBody() {
        return body;
    }

    public While(AbstractExpr condition, ListInst body) {
        Validate.notNull(condition);
        Validate.notNull(body);
        this.condition = condition;
        this.body = body;
    }

    @Override
    protected void codeGenInst(DecacCompiler compiler) {
        String labelString = "label_" + compiler.getLabelId();
        compiler.incrementLabelId();
        Label startWhileLabel = new Label(labelString + "_while");
        Label endWhileLabel = new Label(labelString + "_endWhile");

        compiler.addLabel(startWhileLabel);

        condition.codeGenInst(compiler);

        if(!(compiler.lastIsLoad() && ((LOAD)(compiler.getLastInstruction())).getReg() == compiler.getRegister())) {
            compiler.addInstruction(new CMP(0, compiler.getRegister()));
        }
        compiler.addInstruction(new BEQ(endWhileLabel));

        body.codeGenListInst(compiler);

        compiler.addInstruction(new BRA(startWhileLabel));

        compiler.addLabel(endWhileLabel);
    }
    @Override
    protected void appendToBlock(DecacCompiler compiler, ListBasicBlock blocks) {
        // if (body.isEmpty())
            // return; // optimisation: ne pas compiler un While trivial

        super.appendToBlock(compiler, blocks);
        BasicBlock exitBlock = new BasicBlock();
        BasicBlock entryBlock = blocks.getCurrentBlock();
        blocks.getCurrentBlock().addSucc(exitBlock);

        // Traitement du bloc Body
        BasicBlock bodyBlock = new BasicBlock();
        blocks.getCurrentBlock().addSucc(bodyBlock);
        blocks.add(bodyBlock);
        body.constructBasicBlocks(compiler, blocks);
        body = bodyBlock;
        blocks.getCurrentBlock().addSucc(entryBlock);

        // Rajout du prochain block pour les prochaines instructions
        blocks.add(exitBlock);
    }

    @Override
    protected void verifyInst(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass, Type returnType)
            throws ContextualError {
        // regle (3.25)
        this.condition.verifyCondition(compiler, localEnv, currentClass);
        this.body.verifyListInst(compiler, localEnv, currentClass, returnType);
    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.print("while (");
        getCondition().decompile(s);
        s.println(") {");
        s.indent();
        getBody().decompile(s);
        s.unindent();
        s.print("}");
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        condition.iter(f);
        body.iter(f);
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        // TODO remove
        // condition.prettyPrint(s, prefix, false);
        // body.prettyPrint(s, prefix, true);
    }

}
