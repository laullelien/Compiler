package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;

/**
 * Left-hand side value of an assignment.
 * 
 * @author gl38
 * @date 01/01/2024
 */
public abstract class AbstractLValue extends AbstractExpr {
    // Peut être override par les classes qui étendent AbstractLValue (règle 3.64)
    // public abstract Type verifyLValue(DecacCompiler compiler, EnvironmentExp localEnv,
    //                           ClassDefinition currentClass) throws ContextualError;

    // pas sûr qu'il faut faire ça
    private AbstractIdentifier varName;

    public Type verifyLValueIdent(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass)
            throws ContextualError {
        // règle 3.69
        Type resType = varName.verifyExpr(compiler, localEnv, currentClass);
        return resType;
    }

    public void codeGenIdent(DecacCompiler compiler) {
        varName.codeGenInst(compiler);
    }
}
