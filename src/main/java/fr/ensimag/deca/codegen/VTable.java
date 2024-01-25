package fr.ensimag.deca.codegen;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.*;
import fr.ensimag.ima.pseudocode.instructions.LEA;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.STORE;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class VTable {

    /** Tableau des étiquettes des méthodes */
    private List<Label> labelsTable;
    private List<String> declaredMethod;

    /** Adresse de la VTable dans la mémoire, relative à GB */
    private int addressGB;

    private VTable parentVTable;

    private String className;

    public VTable(String className) {
        this.className = className;
        labelsTable = new LinkedList<>();
        this.declaredMethod = new ArrayList<>();
    }

    public DAddr getDAddr() {
        return new RegisterOffset(addressGB, Register.GB);
    }

    /**
     * Ajoute tous les labels hérités de la classe mère dans la VTable
     * @param parentVTable
     */
    public void createInheritedLabel(VTable parentVTable) {
        this.parentVTable = parentVTable;
        // Par défaut, le tableau des étiquettes contient Object.equals
        if (parentVTable != null) {
            labelsTable.addAll(parentVTable.labelsTable);
            declaredMethod.addAll(parentVTable.declaredMethod);
        }
    }

    /**
     * Ajoute un label pour une méthode dans la VTable
     * @param methodName Nom de la méthode
     */
    public void createLabel(String methodName) {
        Label label = new Label("code." + className + "." + methodName);
        int labelIndex = declaredMethod.indexOf(methodName);
        if (labelIndex >= 0) {
            // la méthode est redéfini
            labelsTable.remove(labelIndex);
            labelsTable.add(labelIndex, label);
        }
        else {
            declaredMethod.add(methodName);
            labelsTable.add(label);
        }
    }

    public int indexOf(String methodName) {
        return declaredMethod.indexOf(methodName);
    }

    /**
     * Génére les instructions assembleur de la Virtual Table
     * @param compiler
     * @param offset Dernier emplacement utilisé depuis GB
     * @return
     */
    public int codeGenVTable(DecacCompiler compiler, int offset) {
        addressGB = offset + 1;
        compiler.addComment("Code de la table des méthodes de " + className);
        if (parentVTable != null) {
            compiler.addInstruction(new LEA(new RegisterOffset(parentVTable.addressGB, Register.GB), Register.R0));
            compiler.addInstruction(new STORE(Register.R0, new RegisterOffset(addressGB, Register.GB)));
        }
        else {
            // la seule classe qui a un VTable nul est Object
            compiler.addInstruction(new LOAD(new NullOperand(), Register.R0));
            compiler.addInstruction(new STORE(Register.R0, new RegisterOffset(addressGB, Register.GB)));
        }
        int i = 1;
        for (Label label : labelsTable) {
            compiler.addInstruction(new LOAD(new LabelOperand(label), Register.R0));
            compiler.addInstruction(new STORE(Register.R0, new RegisterOffset(addressGB + i, Register.GB)));
            i++;
        }
        return i;
    }
}
