package fr.ensimag.deca.tree;

import java.io.PrintStream;
import fr.ensimag.deca.tools.IndentPrintStream;

/**
 * Visibility of a field.
 *
 * @author gl38
 * @date 01/01/2024
 */



public enum Visibility {
    PUBLIC {
        @Override
        public boolean isProtected() {
            return false;
        }

        @Override
        public String toString() {
            return "[visibility=PUBLIC]";
        }
    },
    PROTECTED {
        @Override
        public boolean isProtected() {
            return true;
        }

        @Override
        public String toString() {
            return "[visibility=PROTECTED]";
        }
    };

    public abstract boolean isProtected();

    public void decompile(IndentPrintStream s){
        if (isProtected()){
            s.print("protected ");
        }
    }

}
