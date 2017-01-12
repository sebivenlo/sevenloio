package nl.fontys.sevenlo.hwio;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Pieter van den Hombergh {@code <p.vandenhombergh@fontys.nl>}
 */
public class InBitTest {

    InBit ib = new InBit( 42 );

    @Test
    public void testBitNr() {
        assertEquals( 42, ib.getBitNr() );
    }

}
