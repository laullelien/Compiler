package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.*;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.tools.IndentPrintStream;
import java.io.PrintStream;

import fr.ensimag.deca.tools.SymbolTable;
import org.apache.commons.lang.Validate;

/**
 * @author gl38
 * @date 01/01/2024
 */
public class DeclVar extends AbstractDeclVar {

    
    final private AbstractIdentifier type;
    final private AbstractIdentifier varName;
    final private AbstractInitialization initialization;

    public DeclVar(AbstractIdentifier type, AbstractIdentifier varName, AbstractInitialization initialization) {
        Validate.notNull(type);
        Validate.notNull(varName);
        Validate.notNull(initialization);
        this.type = type;
        this.varName = varName;
        this.initialization = initialization;
    }

    @Override
    protected void verifyDeclVar(DecacCompiler compiler,
            EnvironmentExp localEnv, ClassDefinition currentClass)
            throws ContextualError {
        if (type.getName().getName().equals("int")){
            type.setType(compiler.environmentType.INT);
        }
        if (type.getName().getName().equals("float")){
            type.setType(compiler.environmentType.FLOAT);
        }
        if (type.getName().getName().equals("void")){
            type.setType(compiler.environmentType.VOID);
        }
        if (type.getName().getName().equals("boolean")){
            type.setType(compiler.environmentType.BOOLEAN);
        }
        type.verifyType(compiler);
        // Pas sûr de quoi faire dans verifyInitialization
        // initialization.verifyInitialization(compiler, this.type.getType(), localEnv, currentClass);
        if (localEnv.getEnvironment().containsKey(this.varName.getName())){
            throw new ContextualError("Le symbole existe déjà : la règle (3.17) n'est pas respectée", this.getLocation());
        }
        localEnv.getEnvironment().put(this.varName.getName(), new VariableDefinition(this.type.getType(), this.getLocation()));
    }

    
    @Override
    public void decompile(IndentPrintStream s) {
        throw new UnsupportedOperationException("not yet implemented");
    }

    @Override
    protected
    void iterChildren(TreeFunction f) {
        type.iter(f);
        varName.iter(f);
        initialization.iter(f);
    }
    
    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        type.prettyPrint(s, prefix, false);
        varName.prettyPrint(s, prefix, false);
        initialization.prettyPrint(s, prefix, true);
    }
}
