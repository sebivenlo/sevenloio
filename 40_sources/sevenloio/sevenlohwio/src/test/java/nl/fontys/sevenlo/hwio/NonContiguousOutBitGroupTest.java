/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package nl.fontys.sevenlo.hwio;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Pieter van den Hombergh (P dot vandenHombergh at fontys dot nl)
 */
public class NonContiguousOutBitGroupTest {

    /**
     * Test the calculation of the mask.
     */
    @Test
    public void testGetMask() {
        Output mo = new MockOutput();
        // three bits offset 4;
        BitGroup bg = new NonContiguousOutBitGroup(mo, 3, 4, 7);
        assertEquals(" 1001 1000 ", 0x98, bg.getMask());
    }

    /**
     * Test of get method, of class OutBitGroup.
     */
    @Test
    public void testGet() {
        Output mo = new MockOutput();
        // three bits offset 1;
        BitGroup bg = new NonContiguousOutBitGroup(mo, 0, 1, 2);
        bg.set(5);
        assertEquals("should get back 5", 5, bg.get());
    }

    /**
     * Test of set method, of class OutBitGroup.
     */
    @Test
    public void testSet() {
        Output mo = new MockOutput();
        NonContiguousOutBitGroup instance = new NonContiguousOutBitGroup(mo, 1,
                2, 3, 5);
        instance.set(2 | 8);
        assertEquals("value written should be ", (2 << 1) | (8 << 2), mo.
                lastWritten());
        instance.set(8);
        assertEquals("value written should be ", (8 << 2), mo.lastWritten());
    }

    /**
     * Test of size method, of class OutBitGroup.
     */
    @Test
    public void testSize() {
        System.out.println("size");
        Output mo = new MockOutput();
        NonContiguousOutBitGroup instance = new NonContiguousOutBitGroup(mo, 1,
                3, 2, 4);
        int expResult = 4;
        int result = instance.size();
        assertEquals("should be size 4", expResult, result);
    }

    @Test
    public void testCreateFromBits() {
        System.out.println("create from bits");
        Output o = new MockOutput();
        OutBit b12 = new OutBit(o, 12);
        OutBit b6 = new OutBit(o, 6);
        NonContiguousOutBitGroup group 
                = NonContiguousOutBitGroup.createFromBits( b12, b6);
        group.set(3);
        int expResult = 1 << 6 | 1 << 12;
        assertEquals(expResult, o.lastWritten());
        Output o2 = new MockOutput();
        OutBit b52 = new OutBit(o2, 52 - 32); // in other port 
        NonContiguousOutBitGroup forbiddenFruit;
        try {
            forbiddenFruit = NonContiguousOutBitGroup.createFromBits(b12, b6,
                    b52);
            fail("Constructor should not pass");
        } catch (IllegalArgumentException iae) {
            assertTrue("The world is intact", true);
        }

    }

    /**
     * Test of offset method, of class OutBitGroup.
     */
    @Test
    public void testOffset() {
        System.out.println("offset");
        Output mo = new MockOutput();
        NonContiguousOutBitGroup instance = new NonContiguousOutBitGroup(mo, 3,
                4);
        int expResult = 3;
        int result = instance.offset();
        assertEquals(expResult, result);
    }

    /**
     * Test mock.
     */
    static class MockOutput implements Output {

        int v;

        public void writeMasked(int mask, int value) {
            v = (v & ~mask) | (mask & value);
        }

        public int lastWritten() {
            return v;
        }
    }
}