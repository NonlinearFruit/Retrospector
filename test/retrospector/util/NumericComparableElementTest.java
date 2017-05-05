/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package retrospector.util;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author nonfrt
 */
public class NumericComparableElementTest {
    
    public NumericComparableElementTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of getPriority method, of class NumericComparableElement.
     */
    @Test
    public void testGetPriority() {
        System.out.println("getPriority");
        NumericComparableElement instance = null;
        Integer expResult = null;
        Integer result = instance.getPriority();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of compareToInstanceOfSameType method, of class NumericComparableElement.
     */
    @Test
    public void testCompareToInstanceOfSameType() {
        System.out.println("compareToInstanceOfSameType");
        NumericComparableElement other = null;
        NumericComparableElement instance = null;
        int expResult = 0;
        int result = instance.compareToInstanceOfSameType(other);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of add method, of class NumericComparableElement.
     */
    @Test
    public void testAdd() {
        System.out.println("add");
        char c = ' ';
        NumericComparableElement instance = null;
        instance.add(c);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
