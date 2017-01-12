/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package nl.fontys.sevenlo.hwio;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Pieter van den Hombergh (P dot vandenHombergh at fontys dot nl)
 */
public class OutBitGroupTest {
    /**
     * Test the calculation of the mask.
     */
    @Test
    public void testGetMask() {
        Output mo = new MockOutput();
        // three bits offset 4;
        BitGroup bg = new OutBitGroup(mo, 3, 4);
        assertEquals("3 1 bits shifted left 4 ", 7<<4, bg.getMask());
    }

    /**
     * Test of get method, of class OutBitGroup.
     */
    @Test
    public void testGet() {
        Output mo = new MockOutput();
        // three bits offset 1;
        BitGroup bg = new OutBitGroup(mo, 3, 1);
        bg.set( 5 );
        assertEquals("should get back 5", 5, bg.get() );
    }

    /**
     * Test of set method, of class OutBitGroup.
     */
    @Test
    public void testSet() {
    }

    /**
     * Test of size method, of class OutBitGroup.
     */
    @Test
    public void testSize() {
        System.out.println("size");
        Output mo = new MockOutput();
        OutBitGroup instance =  new OutBitGroup( mo, 4, 2);
        int expResult = 4;
        int result = instance.size();
        assertEquals("should be size 4", expResult, result);
    }

    /**
     * Test of offset method, of class OutBitGroup.
     */
    @Test
    public void testOffset() {
        System.out.println("offset");
        Output mo = new MockOutput();
        OutBitGroup instance = new OutBitGroup( mo, 3, 3);
        int expResult = 3;
        int result = instance.offset();
        assertEquals(expResult, result);
    }
    /**
     * Test mock.
     */
    static class MockOutput implements Output{
        int v;
        public void writeMasked(int mask, int value) {
            v = (v & ~mask) | (mask & value);
        }

        public int lastWritten() {
            return v;
        }
    }
}