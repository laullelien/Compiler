package fr.ensimag.deca.context;

import fr.ensimag.deca.DecacCompiler;
import java.util.HashMap;
import java.util.Map;
import fr.ensimag.deca.tools.SymbolTable.Symbol;
import fr.ensimag.deca.tree.AbstractIdentifier;
import fr.ensimag.deca.tree.Identifier;
import fr.ensimag.deca.tree.Location;

// A FAIRE: étendre cette classe pour traiter la partie "avec objet" de Déca
/**
 * Environment containing types. Initially contains predefined identifiers, more
 * classes can be added with declareClass().
 *
 * @author gl38
 * @date 01/01/2024
 */
public class EnvironmentType {

    public EnvironmentType(DecacCompiler compiler) {
        
        envTypes = new HashMap<Symbol, TypeDefinition>();
        
        Symbol intSymb = compiler.createSymbol("int");
        INT = new IntType(intSymb);
        envTypes.put(intSymb, new TypeDefinition(INT, Location.BUILTIN));

        Symbol floatSymb = compiler.createSymbol("float");
        FLOAT = new FloatType(floatSymb);
        envTypes.put(floatSymb, new TypeDefinition(FLOAT, Location.BUILTIN));

        Symbol voidSymb = compiler.createSymbol("void");
        VOID = new VoidType(voidSymb);
        envTypes.put(voidSymb, new TypeDefinition(VOID, Location.BUILTIN));

        Symbol booleanSymb = compiler.createSymbol("boolean");
        BOOLEAN = new BooleanType(booleanSymb);
        envTypes.put(booleanSymb, new TypeDefinition(BOOLEAN, Location.BUILTIN));

        Symbol stringSymb = compiler.createSymbol("string");
        STRING = new StringType(stringSymb);
        // not added to envTypes, it's not visible for the user.

        Symbol objectSymbol = compiler.createSymbol("Object");
        Symbol equalSymbol = compiler.createSymbol("equals");
        OBJECT = new ClassType(objectSymbol);
        Signature equalsSignature = new Signature();
        equalsSignature.add(OBJECT);
        MethodDefinition equalsMethodDefinition = new MethodDefinition(BOOLEAN, Location.BUILTIN, equalsSignature, 0);
        ClassDefinition objectDefinition = new ClassDefinition(OBJECT, Location.BUILTIN, equalSymbol,equalsMethodDefinition);
        envTypes.put(objectSymbol, objectDefinition);
        this.objectClassIdentifier = new Identifier(objectSymbol);
    }

    public static class DoubleDefException extends Exception {
        private static final long serialVersionUID = -2733379901827316441L;
    }

    private final Map<Symbol, TypeDefinition> envTypes;

    public TypeDefinition defOfType(Symbol s) {
        return envTypes.get(s);
    }

    public boolean castCompatible(Type initialType, Type finalType){
        if (!(initialType.isVoid())) {
            return  (assignCompatible(initialType, finalType) || assignCompatible(finalType, initialType));
        }
        return false;
    }

    public boolean assignCompatible(Type t1, Type t2) {
        // gestion des sous-classes à implémenter
        if (t1.isFloat() && t2.isInt() || t1 == t2) {
            return true;
        }
        return false;
    }
    public void declare(Symbol classSymbol, ClassDefinition classDefinition) throws DoubleDefException {
        if (envTypes.containsKey(classSymbol)) {
            throw new DoubleDefException();
        }
        envTypes.put(classSymbol, classDefinition);
    }

    public void stackOneElement(Symbol symbol, ClassDefinition def){
        this.envTypes.put(symbol, def);
    }

    public final VoidType    VOID;
    public final IntType     INT;
    public final FloatType   FLOAT;
    public final StringType  STRING;
    public final BooleanType BOOLEAN;
    public final ClassType OBJECT;
    public final AbstractIdentifier objectClassIdentifier;
}
