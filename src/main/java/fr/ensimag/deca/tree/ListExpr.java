package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;

import java.util.Iterator;

/**
 * List of expressions (eg list of parameters).
 *
 * @author gl38
 * @date 01/01/2024
 */
public class ListExpr extends TreeList<AbstractExpr> {

    public void verifyListExpr(DecacCompiler compiler, EnvironmentExp localEnv,
                                    ClassDefinition currentClass)
            throws ContextualError {
        throw new ContextualError("not yet implemented", this.getLocation());
    }

    public void verifyListExprPrint(DecacCompiler compiler, EnvironmentExp localEnv,
                               ClassDefinition currentClass)
            throws ContextualError {
        // regle (3.30)
        for (AbstractExpr e : this.getList()) {
            e.verifyExprPrint(compiler, localEnv, currentClass);
        }
    }

    @Override
    public void decompile(IndentPrintStream s) {
        Iterator<AbstractExpr> iterator = iterator();

        while (iterator.hasNext()) {
            AbstractExpr expr = iterator.next();
            expr.decompile(s);
            if (iterator.hasNext()) {
                s.print(',');
            }
        }
    }
}
