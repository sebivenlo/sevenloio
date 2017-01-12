/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.fontys.sevenlo.hwio;

import org.junit.After;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Pieter van den Hombergh (p.vandenhombergh@fontys.nl)
 */
public class IOTest {

    private MockIO mio;

    public IOTest() {
    }

    @Before
    public void setUp() throws Exception {
        mio = new MockIO( 0xff );
        ( ( BitAggregate ) mio ).connect();
    }

    @After
    public void tearDown() throws Exception {
        mio = null;
    }

    // @Test
    // public void hello() {}
    @Test
    public void testWrite() {
        assertFalse( mio.get( 0 ).isSet() );
        mio.write( 0 );
        assertFalse( mio.get( 0 ).isSet() );
        mio.write( 1 );
        assertTrue( mio.get( 0 ).isSet() );

    }

    @Test
    public void testBitWrite() {
        mio.get( 8 ).set( true );
        System.out.println( "bit 8  " + mio.get( 8 ) );
        assertEquals( 0x100, mio.read() );
    }
}
