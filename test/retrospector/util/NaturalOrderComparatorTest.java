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
public class NaturalOrderComparatorTest {
    
    public NaturalOrderComparatorTest() {
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
     * Test of compare method, of class NaturalOrderComparator.
     */
    @Test
    public void testCompare() {
        System.out.println("compare");
        Object o1 = "E0";
        Object o2 = "E01 ";
        NaturalOrderComparator instance = new NaturalOrderComparator();
        int expResult = -1;
        int result = instance.compare(o1, o2);
        assertEquals(expResult, result);
        
        o1 = "E0";
        o2 = "E1 ";
        instance = new NaturalOrderComparator();
        expResult = -1;
        result = instance.compare(o1, o2);
        assertEquals(expResult, result);
        
        o1 = "E0";
        o2 = "   E1 ";
        instance = new NaturalOrderComparator();
        expResult = 1;
        result = instance.compare(o1, o2);
        assertEquals(expResult, result);
        
        o1 = "A";
        o2 = "1";
        instance = new NaturalOrderComparator();
        expResult = 1;
        result = instance.compare(o1, o2);
        assertEquals(expResult, result);
        
        o1 = "E00099";
        o2 = "E088";
        instance = new NaturalOrderComparator();
        expResult = 1;
        result = instance.compare(o1, o2);
        assertEquals(expResult, result);
        
        o1 = "a";
        o2 = "B";
        instance = new NaturalOrderComparator();
        expResult = -1;
        result = instance.compare(o1, o2);
        assertEquals(expResult, result);
        
        o1 = "A";
        o2 = "a";
        instance = new NaturalOrderComparator();
        expResult = -1;
        result = instance.compare(o1, o2);
        assertEquals(expResult, result);
        
        o1 = "A";
        o2 = "0B";
        instance = new NaturalOrderComparator();
        expResult = 1;
        result = instance.compare(o1, o2);
        assertEquals(expResult, result);
        
        o1 = "A123b#$@";
        instance = new NaturalOrderComparator();
        expResult = 0;
        result = instance.compare(o1, o1);
        assertEquals(expResult, result);
        
        
    }
    
}
