package fr.ensimag.ima.pseudocode;

import java.util.Objects;

/**
 * Immediate operand containing a float value.
 * 
 * @author Ensimag
 * @date 01/01/2024
 */
public class ImmediateFloat extends DVal {
    private float value;

    public ImmediateFloat(float value) {
        super();
        this.value = value;
    }

    public float getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "#" + Float.toHexString(value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ImmediateFloat that = (ImmediateFloat) o;
        return Float.compare(that.value, value) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
