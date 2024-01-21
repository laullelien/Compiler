package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.*;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.deca.tools.SymbolTable;
import fr.ensimag.ima.pseudocode.*;
import fr.ensimag.ima.pseudocode.instructions.*;

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
        nameSuperClass.setDefinition(typeParentClass);
        if (typeParentClass == null) {
            throw new ContextualError("La superclass n'est pas dans l'environnement. La règle (1.3) n'est pas respectée.", this.getLocation());
        }
        if(!(typeParentClass.isClass())) {
            throw new ContextualError("La superclass n'est pas un identificateur de classe. La règle (1.3) n'est pas respectée.", this.getLocation());
        }
        ClassType typeNewClass = new ClassType(this.name.getName(), this.getLocation(), (ClassDefinition) typeParentClass);
        try{
            compiler.environmentType.declare(this.name.getName(), typeNewClass.getDefinition());
        }
        catch(EnvironmentType.DoubleDefException e){
            throw new ContextualError("Il y a double définition de classe. La règle (1.3) n'est pas respectée", this.getLocation());
        }
        this.name.setType(typeNewClass);
        this.name.setDefinition(typeNewClass.getDefinition());
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
        try {
            envExpMethods.declare(envExpFields);
            envExpMethods.stackEnvironment(this.name.getClassDefinition().getMembers().getParentEnvironment());
            this.name.getClassDefinition().getMembers().stackEnvironment(envExpMethods);
        }
        catch(EnvironmentExp.DoubleDefException e){
            throw new ContextualError("Des attributs et des méthodes ont le même nom. La règle (2.3) n'est pas respectée.", this.getLocation());
        }
        compiler.environmentType.stackOneElement(this.name.getName(), this.name.getClassDefinition());
        this.name.setDefinition(this.name.getClassDefinition());
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
    protected void codeGenClassVTable(DecacCompiler compiler) {
        compiler.listVTable.codeGenVTable(compiler, name.getName().getName(), nameSuperClass.getName().getName(), methods);
    }

    @Override
    protected void codeGenClassBody(DecacCompiler compiler) {
        codeGenInit(compiler);
        for(AbstractDeclMethod method: methods.getList()) {
            method.codeGenMethod(compiler, this.name.getName().getName());
        }
    }


    protected void codeGenInit(DecacCompiler compiler) {
        compiler.addLabel(new Label("init." + name.getName()));

        //object adress
        compiler.addInstruction(new LOAD(new RegisterOffset(-2, Register.LB), Register.R0));

        //init own fields to 0
        for(AbstractDeclField field : fields.getList()) {
            if(field.getFieldName().getType().isInt() || field.getFieldName().getType().isBoolean()) {
                compiler.addInstruction(new LOAD(0, Register.R1));
                compiler.addInstruction(new STORE(Register.R1, new RegisterOffset(((field.getFieldName().getFieldDefinition())).getIndex(), Register.R0)));
            }
            if(field.getFieldName().getType().isFloat()) {
                compiler.addInstruction(new LOAD(new ImmediateFloat(0), Register.R1));
                compiler.addInstruction(new STORE(Register.R1, new RegisterOffset(((field.getFieldName().getFieldDefinition())).getIndex(), Register.R0)));
            }
            if(field.getFieldName().getType().isClass()) {
                compiler.addInstruction(new LOAD(new NullOperand(), Register.R1));
                compiler.addInstruction(new STORE(Register.R1, new RegisterOffset(((field.getFieldName().getFieldDefinition())).getIndex(), Register.R0)));
            }
        }

        //init inherited fields
        if(nameSuperClass.getName() != compiler.environmentType.OBJECT.getName()) {
            compiler.addInstruction(new TSTO(3));
            compiler.addInstruction(new BOV(new Label("stack_full")));
            compiler.addInstruction(new PUSH(Register.R0));
            compiler.addInstruction(new BSR(new Label("init." + nameSuperClass.getName().getName())));
        }

        compiler.addInstruction(new SUBSP(1));

        //init own fields
        for(AbstractDeclField field: fields.getList()) {
            field.codeGenInit(compiler);
        }

        compiler.addInstruction(new RTS());
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
