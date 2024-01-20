package fr.ensimag.deca.context;

import fr.ensimag.deca.tools.SymbolTable;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.ImmediateFloat;

/**
 *
 * @author Ensimag
 * @date 01/01/2024
 */
public class ObjectType extends Type {

    public ObjectType(SymbolTable.Symbol name) {
        super(name);
    }

    @Override
    public boolean isObject() {
        return true;
    }

    @Override
    public boolean sameType(Type otherType) {
        return this.getName().equals(otherType.getName());
    }


}
