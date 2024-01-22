package fr.ensimag.deca.codegen;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Impossible de faire les tests comme les champs sont privés.
 * Recherche de fix en cours
 */
public class TestVTable {

    final String className = "maClasse";
    final String superClassName = "maSuperClass";
    final String methodName = "maFonction";
    final String labelName = "code.maClasse.maFonction";

    @Mock
    VTable vTableMere;

    @BeforeEach
    public void setup() throws NoSuchFieldException, IllegalAccessException {
        MockitoAnnotations.initMocks(this);
        Field labelsTable = vTableMere.getClass().getDeclaredField("labelsTable");
        labelsTable.setAccessible(true); // Suppress Java language access checking
        labelsTable.set(vTableMere, new LinkedList<>(Arrays.asList(methodName, "monAutreFunc")));
        Field declaredMethod = vTableMere.getClass().getDeclaredField("declaredMethod");
        declaredMethod.setAccessible(true); // Suppress Java language access checking
        declaredMethod.set(vTableMere, new LinkedList<>(Arrays.asList("code.maSuperClass.maFonction", "code.maSuperClass.monAutreFunc")));

    }

    //@Test
    //public void testCreateLabel() throws NoSuchFieldException, IllegalAccessException {
        //VTable v = new VTable(className);
        //v.createLabel(methodName);
        //Field labelsTable = v.getClass().getDeclaredField("labelsTable");
        //labelsTable.setAccessible(true); // Suppress Java language access checking
        //assertEquals(labelName, labelsTable.get(0).toString());
    //}

    //@Test
    //public void testCreateLabelHerite() {
        //VTable vMere = new VTable(superClassName);
        //VTable v =
    //}

}
