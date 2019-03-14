package nl.fontys.sevenlo.hwio;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Unit test for swingpoller.
 * @author Pieter van den Hombergh (p.vandenhombergh@fontys.nl)
 */
public class SwingPollerTest {

    BitAggregate ba;
    SwingPoller poller;
    MockListener ls; // one to serve them all...

    final int inputMask = ~0;

    public SwingPollerTest() {
    }

    @BeforeClass
	public static void setUpClass() throws Exception {
    }

    @AfterClass
	public static void tearDownClass() throws Exception {
    }

    @Before
	public void setUp() {
        DefaultBitFactory bf = new DefaultBitFactory();
        IO io = new MockIO(inputMask);
        ((BitAggregate)io).connect();
        ba = new SimpleBitAggregate(bf,io,io,inputMask);
        ba.connect();
        ls = new MockListener();
        connectBits(ba, ls );
        poller = new SwingPoller(ba);

    }

    @After
	public void tearDown() {
        ba = null;
	//        bit = null;
    }
    /**
     * Mock implementation for BitListener.
     */
    class MockListener implements BitListener {

        boolean value = false;
        int invocations = 0;
        int lastUpdaterId;

        public void updateBit(Bit b, boolean value) {
            Bit mb = (Bit) b;
            //lastUpdaterId = mb.id;
            invocations++;
            System.out.println(" invocation " + invocations +
			       "  update for " + b +
			       " class " + b.getClass().getSimpleName() +
			       " set to " + value);
        }
        public String toString(){
            return getClass().getSimpleName() + " invoked " + invocations
                    + " times";
        }
    }

    void connectBits(BitAggregate ba, BitListener l) {
        for (int i = 0,n=ba.size(); i < n; i++) {
	    ((Bit)ba.getBit(i)).addListener(l);
        }
    }

    @Test
	public void testSwingPoller() {
        int oldV,newV;
        Runnable r = new SwingPoller.UpdateTask( ba, oldV=0, newV=1);
        r.run();
        r = new SwingPoller.UpdateTask(ba,oldV=2, newV=1);
        r.run();
        r = new SwingPoller.UpdateTask(ba,oldV=1, newV=~2);
        r.run();


        assertEquals(" 3 runs, 31 updates ", 31, ls.invocations);
        System.out.println(""+ls);
    }
}