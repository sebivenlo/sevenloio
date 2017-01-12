package nl.fontys.sevenlo.hwio;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Pieter van den Hombergh {@code <p.vandenhombergh@fontys.nl>}
 */
public class BitOpsTest {

    BitOps b = new BitOps() {
        boolean value = false;

        @Override
        public boolean isSet() {
            return value;
        }

        @Override
        public void set( boolean b ) {
            value = b;
        }
    };

    @Test
    public void testSomeMethod() {

        assertFalse( b.isSet() );
        b.set();
        assertTrue( b.isSet() );
        b.clear();
        assertFalse( b.isSet() );
    }

}
