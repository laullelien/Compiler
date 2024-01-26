package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.Instruction;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.ADDSP;
import fr.ensimag.ima.pseudocode.instructions.BOV;
import fr.ensimag.ima.pseudocode.instructions.TSTO;

import java.util.Iterator;

/**
 * List of declarations (e.g. int x; float y,z).
 * 
 * @author gl38
 * @date 01/01/2024
 */

public class ListDeclVar extends TreeList<AbstractDeclVar> {

    @Override
    public void decompile(IndentPrintStream s) {
        Iterator<AbstractDeclVar> iterator = iterator();

        while (iterator.hasNext()) {
            AbstractDeclVar declaration = iterator.next();
            declaration.decompile(s);
        }
    }

    public void setOperand(DecacCompiler compiler) {
        compiler.setRegVar((compiler.getCompilerOptions().getMaxRegisters() - 2) / 2);
        for (AbstractDeclVar declVar : this.getList()) {
            declVar.setOperand(compiler);
        }
    }

    public void codeGenListDeclVar(DecacCompiler compiler){
        for (AbstractDeclVar declVar : this.getList()) {
            declVar.codeGenDeclVar(compiler);
        }
    }


    /**
     * Implements non-terminal "list_decl_var" of [SyntaxeContextuelle] in pass 3
     * @param compiler contains the "env_types" attribute
     * @param localEnv 
     *   its "parentEnvironment" corresponds to "env_exp_sup" attribute
     *   in precondition, its "current" dictionary corresponds to 
     *      the "env_exp" attribute
     *   in postcondition, its "current" dictionary corresponds to 
     *      the "env_exp_r" attribute
     * @param currentClass 
     *          corresponds to "class" attribute (null in the main bloc).
     */    
    void verifyListDeclVariable(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        // règle 3.16 (EnvironmentExp non renvoyé mais localEnv modifié)
        for (AbstractDeclVar e : this.getList()) {
            e.verifyDeclVar(compiler, localEnv, currentClass);
        }
    }


}
