package fr.ensimag.deca.context;

import fr.ensimag.deca.tree.Location;
import fr.ensimag.ima.pseudocode.DAddr;
import fr.ensimag.ima.pseudocode.DVal;

/**
 * Definition associated to identifier in expressions.
 *
 * @author gl38
 * @date 01/01/2024
 */
public abstract class ExpDefinition extends Definition {

    public void setOperand(DVal operand) {
        this.operand = operand;
    }

    public DVal getOperand() {
        return operand;
    }
    private DVal operand;

    public ExpDefinition(Type type, Location location) {
        super(type, location);
    }

}
