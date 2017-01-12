package nl.fontys.sevenlo.hwio;

/**
 * Utility class to go with the bit-aggregate interface. Updates all INPUT bits
 * (according to aggregate input mask) to the new value.
 *
 * @author Pieter van den Hombergh (P dot vandenHombergh at fontys dot nl)
 */
public interface BitUpdater {

    /**
     * Utility method to update the bits in an aggregate.
     *
     * @param bitAg the bit aggregate
     * @param oldValue the old value
     * @param newValue the new value to update to.
     */
    public static void updateIntegerBits( final BitAggregate<Integer> bitAg,
            final int oldValue,
            final int newValue ) {
        // diff is used to look at the bits of interest.
        // Also only considers inputbits.
        int diff = ( newValue ^ oldValue ) & bitAg.getInputMask();
        // start at lowest bits. Optimizes for large output bit sections at
        // the low end.
        int i = Integer.numberOfTrailingZeros( diff );
        long mask = 1 << i;
        int bitsSize = bitAg.size();
        while ( diff != 0 && i < bitsSize ) {
            if ( ( diff & mask ) != 0 ) {
                bitAg.getBit( i ).set( ( mask & newValue ) != 0 );
            }
            diff &= ~mask; // kick diff bit
            mask <<= 1;
            i++;
        }
    }

    /**
     * Utility method to update the bits in an aggregate.
     *
     * @param bitAg the bit aggregate
     * @param oldValue the old value
     * @param newValue the new value to update to.
     */
    public static void updateLongBits( final BitAggregate<Long> bitAg,
            final long oldValue,
            final long newValue ) {
        // diff is used to look at the bits of interest.
        // Alos only considers inputbits.
        long diff = ( newValue ^ oldValue ) & bitAg.getInputMask();
        // start at lowest bits. Optimizes for large output bit sections at
        // the low end.
        int i = Long.numberOfTrailingZeros( diff );
        long mask = 1 << i;
        int bitsSize = bitAg.size();
        while ( diff != 0 && i < bitsSize ) {
            if ( ( diff & mask ) != 0 ) {
                bitAg.getBit( i ).set( ( mask & newValue ) != 0 );
            }
            diff &= ~mask; // kick diff bit
            mask <<= 1;
            i++;
        }
    }

}
