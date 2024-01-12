package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.DecacInternalError;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.DVal;
import org.apache.commons.lang.Validate;

import java.io.PrintStream;

/**
 * Expression, i.e. anything that has a value.
 *
 * @author gl38
 * @date 01/01/2024
 */
public abstract class AbstractExpr extends AbstractInst {
    private Type type;

    /**
     * @return true if the expression does not correspond to any concrete token
     * in the source code (and should be decompiled to the empty string).
     */
    boolean isImplicit() {
        return false;
    }

    protected boolean isTerminal() { return false; }

    /**
     * Get the type decoration associated to this expression (i.e. the type computed by contextual verification).
     */
    public Type getType() {
        return type;
    }

    protected void setType(Type type) {
        Validate.notNull(type);
        this.type = type;
    }

    private DVal dval ;
    private DVal negDval;

    public DVal getDval() {
        return dval;
        // TODO refactor codeGenPrint de AbstractPrint qui dépend encore de cet attribut
        // qui est encore nul ici
        // throw new DecacInternalError("getDval() should not be called from AbstractExpr");
    }

    public DVal getNegativeDval() {
        return negDval;
    }


    @Override
    protected void checkDecoration() {
        if (getType() == null) {
            throw new DecacInternalError("Expression " + decompile() + " has no Type decoration");
        }
    }

    public void verifyExprPrint(DecacCompiler compiler,
                           EnvironmentExp localEnv, ClassDefinition currentClass)
            throws ContextualError {
        // regle 3.31
         Type returnType = this.verifyExpr(compiler, localEnv, currentClass);
         if(!(returnType.isString() || returnType.isInt() || returnType.isFloat())) {
             throw new ContextualError("Le type d'un parametre de print ne respecte pas la règle 3.30", this.getLocation());
         }
    }

    /**
     * Verify the expression for contextual error.
     * <p>
     * implements non-terminals "expr" and "lvalue"
     * of [SyntaxeContextuelle] in pass 3
     *
     * @param compiler     (contains the "env_types" attribute)
     * @param localEnv     Environment in which the expression should be checked
     *                     (corresponds to the "env_exp" attribute)
     * @param currentClass Definition of the class containing the expression
     *                     (corresponds to the "class" attribute)
     *                     is null in the main bloc.
     * @return the Type of the expression
     * (corresponds to the "type" attribute)
     */
    public abstract Type verifyExpr(DecacCompiler compiler,
                                    EnvironmentExp localEnv, ClassDefinition currentClass)
            throws ContextualError;

    /**
     * Verify the expression in right hand-side of (implicit) assignments
     * <p>
     * implements non-terminal "rvalue" of [SyntaxeContextuelle] in pass 3
     *
     * @param compiler     contains the "env_types" attribute
     * @param localEnv     corresponds to the "env_exp" attribute
     * @param currentClass corresponds to the "class" attribute
     * @param expectedType corresponds to the "type1" attribute
     * @return this with an additional ConvFloat if needed...
     */
    public AbstractExpr verifyRValue(DecacCompiler compiler,
                                     EnvironmentExp localEnv, ClassDefinition currentClass,
                                     Type expectedType)
            throws ContextualError {
        Type assignedType = this.verifyExpr(compiler, localEnv, currentClass);
        setType(assignedType);
        // règle 3.28
        if (!(compiler.environmentType.assignCompatible(expectedType, assignedType))) {
            throw new ContextualError("La règle 3.28 n'est pas respectée : le type n'est pas compatible pour l'affectation", this.getLocation());
        }
        // ConvFloat à ajouter si besoin
        return this;
    }


    @Override
    protected void verifyInst(DecacCompiler compiler, EnvironmentExp localEnv,
                              ClassDefinition currentClass, Type returnType)
            throws ContextualError {
        // regle 3.20
        verifyExpr(compiler, localEnv, currentClass);
    }

    /**
     * Verify the expression as a condition, i.e. check that the type is
     * boolean.
     *
     * @param localEnv     Environment in which the condition should be checked.
     * @param currentClass Definition of the class containing the expression, or null in
     *                     the main program.
     */
    void verifyCondition(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        Type returnType = this.verifyExpr(compiler, localEnv, currentClass);
        if(!returnType.isBoolean()) {
            throw new ContextualError("Le paramètre de l'instruction n'est pas de type boolean : règle (3.29)", this.getLocation());
        }
    }

    /**
     * Generate code to print the expression
     *
     * @param compiler
     */
    protected void codeGenPrint(DecacCompiler compiler) {
        throw new UnsupportedOperationException("not yet implemented");
    }

    protected void codeGenPrintX(DecacCompiler compiler) {
        throw new UnsupportedOperationException("not yet implemented");
    }

    @Override
    protected void codeGenInst(DecacCompiler compiler) {
        throw new UnsupportedOperationException("not yet implemented");
    }

    @Override
    protected void decompileInst(IndentPrintStream s) {
        decompile(s);
        s.print(";");
    }

    @Override
    protected void prettyPrintType(PrintStream s, String prefix) {
        Type t = getType();
        if (t != null) {
            s.print(prefix);
            s.print("type: ");
            s.print(t);
            s.println();
        }
    }
}
