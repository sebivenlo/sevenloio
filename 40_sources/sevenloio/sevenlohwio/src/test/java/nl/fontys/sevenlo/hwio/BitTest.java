package nl.fontys.sevenlo.hwio;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Pieter van den Hombergh {@code <p.vandenhombergh@fontys.nl>}
 */
public class BitTest {

    int invocations = 0;
    Bit instance = new Bit() {

        boolean value = false;

        @Override
        public boolean isSet() {
            return value;
        }

        @Override
        public void set( boolean b ) {
            value = b;
            updateListeners( b, value );
        }
    };

    @Test
    public void testRemoveListener() {

        BitListener bl = new BitListener() {
            @Override
            public void updateBit( Object bit, boolean newValue ) {
                System.out.println( "bit = " + bit + " newvalue = " + newValue );
                invocations++;
            }
        };
        instance.addListener( bl );
        int pre = invocations;

        instance.set();
        assertEquals( pre + 1, invocations );

        pre = invocations;
        instance.clear();
        assertEquals( pre + 1, invocations );
        instance.removeListener( bl );

        pre = invocations;
        instance.set();
        assertEquals( pre, invocations );

    }

}
