/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package retrospector.util;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * @author nonfrt
 */
public class OrderComparatorTest {

    /**
     * Test of compare method, of class OrderComparator.
     */
    @Test
    public void testCompare() {
        System.out.println("compare");
        
        
        String o1 = "E0";
        String o2 = "E01 ";
        OrderComparator instance = new OrderComparator();
        int result = instance.compare(o1, o2);
        assertTrue(result<0);
        
        // In a previous comparator, E01 came before E0, which is bad
        o1 = "E0";
        o2 = "E1 ";
        instance = new OrderComparator();
        result = instance.compare(o1, o2);
        assertTrue(result<0);
        
        // A previous version also ignored white space, which is bad
        o1 = "E0";
        o2 = "   E1 ";
        instance = new OrderComparator();
        result = instance.compare(o1, o2);
        assertTrue(result>0);
        
        // Numbers before Letters
        o1 = "A";
        o2 = "1";
        instance = new OrderComparator();
        result = instance.compare(o1, o2);
        assertTrue(result>0);
        
        // Symbols before Numbers
        o1 = "1";
        o2 = "%";
        instance = new OrderComparator();
        result = instance.compare(o1, o2);
        assertTrue(result>0);
        
        // Symbols before Letters
        o1 = "z";
        o2 = "%";
        instance = new OrderComparator();
        result = instance.compare(o1, o2);
        assertTrue(result>0);
        
        // Consecutive digits should be handled as 1 character
//        o1 = "E00088A";
//        o2 = "E088B";
//        instance = new OrderComparator();
//        result = instance.compare(o1, o2);
//        assertTrue(result<0);
        
        // Alphabetical order, not ASCIIbetical. So `a` before `B`
//        o1 = "a";
//        o2 = "B";
//        instance = new OrderComparator();
//        result = instance.compare(o1, o2);
//        assertTrue(result<0);
        
        o1 = "A";
        o2 = "a";
        instance = new OrderComparator();
        result = instance.compare(o1, o2);
        assertTrue(result<0);
        
        o1 = "1234567";
        o2 = "0999999";
        instance = new OrderComparator();
        result = instance.compare(o1, o2);
        assertTrue(result>0);
        
        o1 = "A";
        o2 = "a";
        instance = new OrderComparator();
        result = instance.compare(o1, o2);
        assertTrue(result<0);
        
        o1 = "A";
        o2 = "0B";
        instance = new OrderComparator();
        result = instance.compare(o1, o2);
        assertTrue(result>0);
        
        // A string should be equal to itself
        o1 = "A123b#$@";
        instance = new OrderComparator();
        result = instance.compare(o1, o1);
        assertEquals(0, result);
        
        // The shorter is first among equals
        o1 = "E3";
        o2 = "E03";
        instance = new OrderComparator();
        result = instance.compare(o1, o2);
        assertTrue(result<0);
        
        // The shorter is first among equals
        o1 = "E003";
        o2 = "E03";
        instance = new OrderComparator();
        result = instance.compare(o1, o2);
        assertTrue(result>0);
        
        // Be able to handle large numbers
        o1 = "20170307215859";
        o2 = "20160307215859";
        instance = new OrderComparator();
        result = instance.compare(o1, o2);
        assertTrue(result>0);
        
    }
    
}

