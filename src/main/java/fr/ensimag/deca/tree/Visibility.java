package fr.ensimag.deca.tree;

import java.io.PrintStream;

/**
 * Visibility of a field.
 *
 * @author gl38
 * @date 01/01/2024
 */


import java.io.PrintStream;

public enum Visibility {
    PUBLIC {
        @Override
        public boolean isProtected() {
            return false;
        }

        @Override
        public void prettyPrint(PrintStream s, String prefix, boolean last) {
            s.print(prefix);
            s.print("visibility: ");
            s.print(this);
            s.println();

        }

        @Override
        public String toString() {
            return "public";
        }
    },
    PROTECTED {
        @Override
        public boolean isProtected() {
            return true;
        }

        @Override
        public void prettyPrint(PrintStream s, String prefix, boolean last) {
            s.print(prefix);
            s.print("visibility: ");
            s.print(this);
            s.println();
        }

        @Override
        public String toString() {
            return "protected";
        }
    };

    public abstract boolean isProtected();

    public abstract void prettyPrint(PrintStream s, String prefix, boolean last);
}
