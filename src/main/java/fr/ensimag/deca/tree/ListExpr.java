package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.IndentPrintStream;

/**
 * List of expressions (eg list of parameters).
 *
 * @author gl38
 * @date 01/01/2024
 */
public class ListExpr extends TreeList<AbstractExpr> {

    public void verifyListExpr(DecacCompiler compiler, EnvironmentExp localEnv,
                               ClassDefinition currentClass)
            throws ContextualError{
        for (AbstractExpr e : this.getList()){
            Type typeExpr = e.verifyExpr(compiler, localEnv, currentClass);
            if (!(typeExpr.equals(compiler.environmentType.STRING)
                    || typeExpr.equals(compiler.environmentType.INT)
                    || typeExpr.equals(compiler.environmentType.FLOAT) )){
                throw new ContextualError("L'argument du print n'est ni un float, ni un int, ni un string", this.getLocation());
            };
        }
    };
    @Override
    public void decompile(IndentPrintStream s) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
