package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.*;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.ADD;
import fr.ensimag.ima.pseudocode.instructions.LEA;
import fr.ensimag.ima.pseudocode.instructions.LOAD;

import java.io.PrintStream;

public class Selection extends AbstractLValue {

    private AbstractExpr thisExpr;
    private AbstractIdentifier identifier;

    public Selection(AbstractExpr thisExpr, AbstractIdentifier identifier){
        this.thisExpr = thisExpr;
        this.identifier = identifier;
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass) throws ContextualError {
        Type exprType = thisExpr.verifyExpr(compiler, localEnv, currentClass);
        TypeDefinition exprClassTypeDefinition = compiler.environmentType.defOfType(exprType.getName());
        if (exprClassTypeDefinition == null) {
            throw new ContextualError("Cela ne respecte pas 3.65 et 3.66. La classe n'existe pas, cela n'est pas sensé se produire.", getLocation());
        }
        if (!exprClassTypeDefinition.isClass()) {
            throw new ContextualError("Cela ne respecte pas 3.65 et 3.66. Le type de l'expression n'est pas une classe.", getLocation());
        }
        EnvironmentExp envClass = ((ClassType) exprType).getDefinition().getMembers();
        FieldDefinition fieldIdent = (FieldDefinition) (this.identifier.verifyField(compiler, envClass));
        if (fieldIdent.getVisibility() == Visibility.PROTECTED) {
            if (currentClass == null){
                throw new ContextualError("Appel d'un champ protected dans le main : la règle 3.66 n'est pas respectée.", getLocation());
            }
            if (!currentClass.getType().isSubType(exprType)) {
                throw new ContextualError("Cela ne respecte pas 3.66. Le type de l'expression n'est pas un sous-type du type de la classe actuelle.", getLocation());
            }
            if (!(currentClass.getType().isSubType(fieldIdent.getContainingClass().getType()))) {
                throw new ContextualError("Cela ne respecte pas 3.66. Le type de la classe actuelle n'est pas un sous-type du type du field.", getLocation());
            }
        }
        setType(fieldIdent.getType());
        return fieldIdent.getType();
    }


    @Override
    protected void codeGenInst(DecacCompiler compiler){
        compiler.addComment("Debut selection");
        thisExpr.codeGenInst(compiler);
        compiler.addInstruction(new LOAD(new RegisterOffset(identifier.getFieldDefinition().getIndex(), compiler.getRegister()), compiler.getRegister()));
        compiler.addComment("Fin Selection");
    }


    @Override
    public void decompile(IndentPrintStream s) {
        thisExpr.decompile(s);
        s.print(".");
        identifier.decompile(s);
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        thisExpr.prettyPrint(s, prefix, false);
        identifier.prettyPrint(s,prefix, true);
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        thisExpr.iter(f);
        identifier.iter(f);
    }

    public void codeGenAdress(DecacCompiler compiler) {
        compiler.addComment("Debut calcul adresse field");
        thisExpr.codeGenInst(compiler);
        compiler.addInstruction(new LEA(new RegisterOffset(identifier.getFieldDefinition().getIndex(), compiler.getRegister()), compiler.getRegister()));
        compiler.addComment("Fin calcul adresse field");
    }
}
