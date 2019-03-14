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
public class InputBitTest {

    private MockIO mio;
    private Bit bit;

    public InputBitTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setup() {
        mio = new MockIO(0xff);
        ((BitAggregate)mio).connect();
        bit = mio.get(0);
    }

    @After
    public void teardown() {
        bit = null;
        mio = null;
    }

    @Test
    public void testBitSet1() {
        mio.write(0);
        assertFalse(bit.isSet());
        mio.write(1);
        assertTrue(bit.isSet());

    }

    @Test
    public void testListener() {
        /**
         * Stub listener.
         */
        class BitListenerStub implements BitListener {

            boolean v = false;

            public void updateBit(Bit b, boolean newValue) {
                v = newValue;
            }
        }

        BitListenerStub bls = new BitListenerStub();
        bit.addListener(bls);
        mio.write(1);
        assertTrue(bls.v);
        mio.write(0);
        assertFalse(bls.v);

    }
}
