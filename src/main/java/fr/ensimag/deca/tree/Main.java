package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.extension.tree.BasicBlock;
import fr.ensimag.deca.extension.tree.ListBasicBlock;
import fr.ensimag.deca.tools.IndentPrintStream;
import java.io.PrintStream;

import org.apache.commons.lang.Validate;
import org.apache.log4j.Logger;

/**
 * @author gl38
 * @date 01/01/2024
 */
public class Main extends AbstractMain {
    private static final Logger LOG = Logger.getLogger(Main.class);
    private ListDeclVar declVariables;
    private ListInst insts;

    private ListBasicBlock blocks;

    public Main(ListDeclVar declVariables,
            ListInst insts) {
        Validate.notNull(declVariables);
        Validate.notNull(insts);
        this.declVariables = declVariables;
        this.insts = insts;
    }

    @Override
    protected void verifyMain(DecacCompiler compiler) throws ContextualError {
        LOG.debug("verify Main: start");
        // regle (3.4), (3.18)
        EnvironmentExp localEnv = new EnvironmentExp();
        declVariables.verifyListDeclVariable(compiler, localEnv, null);
        // ne pas oublier de changer currentClass
        insts.verifyListInst(compiler, localEnv.stackEnvironment(localEnv, localEnv.getParentEnvironment()), null, compiler.environmentType.VOID);
        LOG.debug("verify Main: end");
    }

    @Override
    protected void optimizeMain(DecacCompiler compiler) {
        BasicBlock entryBlock = new BasicBlock();
        blocks = new ListBasicBlock(entryBlock);
        declVariables.appendToBlock(compiler, blocks);
        insts.constructBasicBlocks(compiler, blocks);
    }

    @Override
    protected void codeGenMain(DecacCompiler compiler) {
        compiler.addComment("Beginning of main instructions:");
        declVariables.setOperand(compiler);
        declVariables.codeGenListDeclVar(compiler);
        insts.codeGenListInst(compiler);
    }
    
    @Override
    public void decompile(IndentPrintStream s) {
        s.println("{");
        s.indent();
        declVariables.decompile(s);
        insts.decompile(s);
        s.unindent();
        s.println("}");
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        declVariables.iter(f);
        insts.iter(f);
    }
 
    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        // TODO remove
        declVariables.prettyPrint(s, prefix, false);
        // insts.prettyPrint(s, prefix, true);
        blocks.prettyPrint(s, prefix, false);
    }
}
