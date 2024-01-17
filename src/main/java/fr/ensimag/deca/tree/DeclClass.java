package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.*;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.deca.tools.SymbolTable;

import java.io.PrintStream;

/**
 * Declaration of a class (<code>class name extends superClass {members}<code>).
 * 
 * @author gl38
 * @date 01/01/2024
 */
public class DeclClass extends AbstractDeclClass {
    private AbstractIdentifier name;
    private AbstractIdentifier nameSuperClass;
    private ListDeclMethod methods;
    private ListDeclField fields;
    public DeclClass(AbstractIdentifier name, AbstractIdentifier nameSuperClass, ListDeclMethod methods,
                             ListDeclField fields) {
        this.name = name;
        this.nameSuperClass = nameSuperClass;
        this.methods = methods;
        this.fields = fields;
    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.print("class ");
        this.name.decompile(s);
        s.print(" extends ");
        this.nameSuperClass.decompile(s);
        s.println(" {");
        s.indent();
        this.fields.decompile(s);
        this.methods.decompile(s);
        s.unindent();
        s.print("}");
        s.println("");
    }

    @Override
    protected void verifyClass(DecacCompiler compiler) throws ContextualError {
        TypeDefinition typeParentClass = compiler.environmentType.defOfType(this.nameSuperClass.getName());
        if (typeParentClass == null) {
            throw new ContextualError("La superclass n'est pas dans l'environnement. La règle (1.3) n'est pas respectée.", this.getLocation());
        }
        if(!(typeParentClass.isClass())) {
            throw new ContextualError("La superclass n'est pas un identificateur de classe. La règle (1.3) n'est pas respectée.", this.getLocation());
        }
        ClassType typeNewClass = new ClassType(this.name.getName(), this.getLocation(), (ClassDefinition) typeParentClass);
        ClassDefinition newClassDefinition = new ClassDefinition(typeNewClass, this.getLocation(), (ClassDefinition) typeParentClass);
        try{
            compiler.environmentType.declare(this.name.getName(), newClassDefinition);
        }
        catch(EnvironmentType.DoubleDefException e){
            throw new ContextualError("Il y a double définition de classe. La règle (1.3) n'est pas respectée", this.getLocation());
        }
        this.name.setType(typeNewClass);
        this.name.setDefinition(newClassDefinition);
    }

    @Override
    protected void verifyClassMembers(DecacCompiler compiler)
            throws ContextualError {
        EnvironmentExp envExpFields = fields.verifyListDeclField(compiler, this.nameSuperClass.getName(), this.name.getClassDefinition());
        EnvironmentExp envExpMethods = methods.verifyListDeclMethod(compiler, this.nameSuperClass.getName(), this.name.getClassDefinition());
        TypeDefinition superClassDefinition = compiler.environmentType.defOfType(this.nameSuperClass.getName());
        if (superClassDefinition == null) {
            throw new ContextualError("La classe utilisée n'a pas été déclarée, la règle (2.3) n'est pas respectée.", this.getLocation());
        }
        if (!(superClassDefinition.isClass())) {
            throw new ContextualError("L'identificateur de la super classe ne définit pas une classe, la règle (2.3) n'est pas respectée.", this.getLocation());
        }
        ClassDefinition newDef = new ClassDefinition(this.name.getClassDefinition().getType(), this.getLocation(), this.nameSuperClass.getClassDefinition());
        try {
            envExpMethods.declare(envExpFields);
            envExpMethods.stackEnvironment(this.name.getClassDefinition().getMembers().getParentEnvironment());
            newDef.getMembers().stackEnvironment(envExpMethods);
        }
        catch(EnvironmentExp.DoubleDefException e){
            throw new ContextualError("Des attributs et des méthodes ont le même nom. La règle (2.3) n'est pas respectée.", this.getLocation());
        }
        compiler.environmentType.stackOneClass(this.name.getName(), newDef);
        this.name.setDefinition(newDef);
    }
    
    @Override
    protected void verifyClassBody(DecacCompiler compiler) throws ContextualError {
        TypeDefinition superClassDefinition = compiler.environmentType.defOfType(this.nameSuperClass.getName());
        if (superClassDefinition == null) {
            throw new ContextualError("La classe utilisée n'a pas été déclarée, la règle (2.3) n'est pas respectée.", this.getLocation());
        }
        if (!(superClassDefinition.isClass())) {
            throw new ContextualError("L'identificateur de la super classe ne définit pas une classe, la règle (2.3) n'est pas respectée.", this.getLocation());
        }
        // règle 3.5
        fields.verifyListDeclFieldPass3(compiler, this.name.getClassDefinition().getMembers(), this.name.getClassDefinition());
        methods.verifyListDeclMethodPass3(compiler, this.name.getClassDefinition());
    }


    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        name.prettyPrint(s, prefix, false);
        nameSuperClass.prettyPrint(s, prefix, false);
        fields.prettyPrint(s, prefix, false);
        methods.prettyPrint(s, prefix, true);
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        name.iter(f);
        nameSuperClass.iter(f);
        fields.iter(f);
        methods.iter(f);
    }

}
