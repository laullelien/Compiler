package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.LOAD;

/**
 * 
 * @author gl38
 * @date 01/01/2024
 */
public class ListInst extends TreeList<AbstractInst> {

    /**
     * Implements non-terminal "list_inst" of [SyntaxeContextuelle] in pass 3
     * @param compiler contains "env_types" attribute
     * @param localEnv corresponds to "env_exp" attribute
     * @param currentClass 
     *          corresponds to "class" attribute (null in the main bloc).
     * @param returnType
     *          corresponds to "return" attribute (void in the main bloc).
     */
    public void verifyListInst(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass, Type returnType) throws ContextualError {
        // regle (3.19)
        for (AbstractInst inst : this.getList()) {
            inst.verifyInst(compiler, localEnv, currentClass, returnType);
        }
    }

    public void codeGenListInst(DecacCompiler compiler) {
        for (AbstractInst i : getList()) {
            if(i instanceof Assign) {
                Assign inst = (Assign) i;
                if (inst.getLeftOperand().getDval() instanceof GPRegister) {
                    GPRegister destReg = (GPRegister) (inst.getLeftOperand().getDval());
                    if (inst.getRightOperand().getDval() != null) {
                        compiler.addInstruction(new LOAD(inst.getRightOperand().getDval(), destReg));
                        continue;
                    } else if (inst.getRightOperand() instanceof AbstractOpArith) {
                        AbstractOpArith rightOperand = (AbstractOpArith) inst.getRightOperand();
                        AbstractExpr leftSon = rightOperand.getLeftOperand();
                        AbstractExpr rightSon = rightOperand.getRightOperand();
                        if(leftSon.getDval() != null && rightSon.getDval() != null && (leftSon.getDval() == destReg || rightSon.getDval() == destReg)) {
                            if(leftSon.getDval() == destReg) {
                                compiler.setDval(rightSon.getDval());
                                compiler.setRegister(destReg);
                            } else if (rightSon.getDval() == destReg) {
                                compiler.setDval(leftSon.getDval());
                                compiler.setRegister(destReg);
                            }
                            rightOperand.codeGenBinary(compiler);
                            compiler.setRegister(Register.R2);
                            continue;
                        }
                    }
                }
            }
            i.codeGenInst(compiler);
        }
    }

    @Override
    public void decompile(IndentPrintStream s) {
        for (AbstractInst i : getList()) {
            i.decompileInst(s);
            s.println();
        }
    }
}
