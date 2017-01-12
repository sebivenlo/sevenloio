/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package nl.fontys.sevenlo.hwio;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Pieter van den Hombergh (p.vandenhombergh@fontys.nl)
 */
public class SimplePollerTest {

    public SimplePollerTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Listener Mock.
     */
    static class MyListener implements BitListener {
        int count=0;

        public void updateBit(Object b, boolean newValue) {
            count++;
        }

    }
    /**
     * Test of pollOnce method, of class SimplePoller.
     */
    @Test
    public void testPollOnce() {
        DefaultBitFactory bf = new DefaultBitFactory();
        int inputMask = 0xffff;
        IO io = new MockIO(inputMask);
        ((BitAggregate) io).connect();
        BitAggregate ba = new SimpleBitAggregate(bf, io,io, inputMask);
        ba.connect();
        MyListener bl0 = new MyListener();
        MyListener bl1 = new MyListener();
        MyListener bl2 = new MyListener();
        ((Bit)ba.getBit(0)).addListener(bl0);
        ((Bit)ba.getBit(1)).addListener(bl1);
        ((Bit)ba.getBit(2)).addListener(bl2);

        io.writeMasked(~0, 0); // set all bits to 0
        Poller p = new SimplePoller( ba );
        p.pollOnce();
        assertEquals(0,bl0.count);
        assertEquals(0,bl1.count);
        // toggle b0
        io.writeMasked(~0, 1); // set all bits to 0, 0->1
        p.pollOnce();
        assertEquals(1,bl0.count);
        assertEquals(0,bl1.count);
        io.writeMasked(~0, 3); // set all bits to 0, 1..0->1
        p.pollOnce();
        assertEquals(1,bl0.count);
        assertEquals(1,bl1.count);
        io.writeMasked(~0, 0); // set all bits to 0, 1..0->0
        p.pollOnce();
        assertEquals(2,bl0.count);
        assertEquals(2,bl1.count);
        // bit 2 should stay silent
        assertEquals(0,bl2.count);

    }

}