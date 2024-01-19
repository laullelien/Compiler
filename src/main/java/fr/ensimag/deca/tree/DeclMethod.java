package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.IMAProgram;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.*;

public class DeclMethod extends AbstractDeclMethod{
    public DeclMethod(AbstractIdentifier methodReturnType, AbstractIdentifier methodName, ListDeclParam methodParameters, AbstractMethodBody methodBody) {
        super(methodReturnType, methodName, methodParameters, methodBody);
    }

    @Override
    protected void codeGenMethod(DecacCompiler compiler, String className) {
        //enregister le programme que l'on a utilisé jusque la
        IMAProgram untilNowProg = compiler.getProgram();
        // nouveau programme que l'on utilise seulement pour cette methode, pour les TSTO, ADDSP et enregistrement de registres
        IMAProgram varProg = new IMAProgram();
        IMAProgram methodProg = new IMAProgram();

        compiler.codegenHelper.reset();
        compiler.resetMaxReg();

        getMethodBody().setLocalOperand();

        compiler.setProgram(varProg);
        getMethodBody().codeGenDecl(compiler);

        compiler.setProgram(methodProg);
        getMethodBody().codeGenInst(compiler);

        //code qui se situe en première position. A lire a l'envers car on ajoute au début
        int nbDeclVar = getMethodBody().getVarNb();
        int maxReg = compiler.getMaxReg();

        if(nbDeclVar != 0) {
            varProg.addFirst(new ADDSP(nbDeclVar));
        }
        varProg.addFirst(new BOV(new Label("stack_full")));
        varProg.addFirst(new TSTO(nbDeclVar + compiler.codegenHelper.getMaxPushDepth() + maxReg - 1));
        varProg.addFirstLabel(new Label("code." + className + "." + getMethodName().getName().getName()));

        for(int i = maxReg; i >= 2; i--) {
            methodProg.addFirst(new PUSH(Register.getR(i)));
        }

        // on n'a pas croisé de return
        if(!getMethodReturnType().getType().isVoid()) {
            compiler.addInstruction(new BRA(new Label("return_error")));
        }

        compiler.getProgram().addLabel(new Label("fin." + className + "." + getMethodName().getName().getName()));
        for(int i = 2; i <= maxReg; i++) {
            compiler.addInstruction(new POP(Register.getR(i)));
        }
        compiler.addInstruction(new RTS());

        untilNowProg.append(varProg);
        untilNowProg.append(methodProg);
        compiler.setProgram(untilNowProg);
    }
}
