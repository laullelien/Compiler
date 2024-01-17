package fr.ensimag.deca.codegen;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.tree.AbstractDeclMethod;
import fr.ensimag.deca.tree.ListDeclMethod;
import org.apache.commons.lang.Validate;

import java.util.HashMap;
import java.util.Map;

public class ListVTable {

     /** Dernier emplacement utilisé depuis GB */
    private int offset;

    private Map<String, VTable> vTableMap;

    public ListVTable() {
        vTableMap = new HashMap<>();
        // default offset
        offset = 0;
    }

    /**
     * Crée la Virtual Table d'une classe et l'ajoute à la liste
     * @param compiler
     * @param name Nom de la classe
     * @param parentName Nom de la classe mère, null si elle hérite d'aucune classe (Seulement valable pour Object)
     * @param methodList méthodes de la classe
     */
    public void codeGenVTable(DecacCompiler compiler, String name, String parentName, ListDeclMethod methodList) {
        Validate.notNull(name);
        Validate.isTrue(!name.isEmpty());
        VTable vTable = new VTable(name);
        if (parentName == null) {
            // nous avons Object
            Validate.isTrue(name.equals("Object"));
            vTable.createLabel("equals");
            vTable.createInheritedLabel(null);
        }
        else {
            Validate.notNull(methodList);
            vTable.createInheritedLabel(vTableMap.get(parentName));
            for (AbstractDeclMethod method : methodList.getList())
                vTable.createLabel(method.getMethodName().getName().getName());
        }
        offset += vTable.codeGenVTable(compiler, offset);
        vTableMap.put(name, vTable);
    }

}

