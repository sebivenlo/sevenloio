package nl.fontys.sevenlo.hwio;

import java.util.Arrays;
import java.util.List;

/**
 * Output bit group. Uses an Output to do the actual I/O.
 *
 * @author Pieter van den Hombergh (P dot vandenHombergh at fontys dot nl)
 */
public class OutBitGroup implements BitGroup {

    private final Output output;
    private final int size;
    private final int offset;
    /**
     * mask before shifting.
     */
    private final int pmask;
    /**
     * mask used in output operation.
     */
    private final int mask;
    /**
     * last value written (0 based).
     */
    private volatile int value;

    /**
     * Create a OutbitGroup of size starting at bit offset.
     *
     * @param o    output to use.
     * @param sz   size or the number of bits in the group.
     * @param offs the bit number of the rightmost bit.
     */
    public OutBitGroup( Output o, int sz, int offs ) {
        this.output = o;
        this.size = sz;
        this.offset = offs;
        this.pmask = ( 1 << size ) - 1;
        mask = pmask << offset;
    }

    /**
     * See BitGroup get documentation.
     *
     * @return the current value
     */
    @Override
    public int get() {
        return value;
    }

    /**
     * See BitGroup get documentation.
     *
     * @param v new value
     *
     * @return the previous value
     */
    @Override
    public int set( int v ) {
        int ov = value;
        value = v & pmask;
        output.writeMasked( mask, value << offset );
        return ov;
    }

    /**
     * See BitGroup.get documentation.
     *
     * @return the size
     */
    @Override
    public int size() {
        return size;
    }

    /**
     * See BitGroup get documentation.
     *
     * @return the offset (position from the right)
     */
    @Override
    public int offset() {
        return offset;
    }

    /**
     * See BitGroup.get documentation. Return the input mask.
     *
     * @return the mask
     */
    @Override
    public int getMask() {
        return this.mask;
    }

    /**
     * Create a OutBitGroup from a contiguous set of bits.
     * he output bits must share the same output port.
     *
     * @param bits to combine into a group
     *
     * @return the combined groups.
     * @throws IllegalArgumentException if 1. The array is empty, 2 the outputs
     *                                  (writers) of the
     *                                  bist differ or 3 the
     *                                  bit are not contiguous.
     */
    public static OutBitGroup from( OutBit... bits ) {
        if ( bits.length < 1 ) {
            throw new IllegalArgumentException( "Set cannot be empty" );
        }
        OutBit[] sorted = Arrays.copyOf( bits, bits.length );
        Arrays.sort( sorted, ( a, b ) -> Integer.compare( a.bitNr, b.bitNr ) );
        System.out.println( Arrays.toString( sorted ) );
        Output writer = sorted[ 0 ].getWriter();
        for ( int b = 1; b < sorted.length; b++ ) {
            if ( bits[ b ].getWriter() != writer ) {
                throw new IllegalArgumentException( "All bits should have same writer" );
            }
            if ( bits[ b - 1 ].bitNr + 1 != ( bits[ b ].bitNr ) ) {
                throw new IllegalArgumentException( "Bits should be contiguous" );
            }
        }
        return new OutBitGroup( writer, sorted.length, sorted[ 0 ].bitNr );
    }

    /**
     * Create a OutBitGroup from a contiguous set of bits.
     * he output bits must share the same output port.
     *
     * @param bits to combine into a group
     *
     * @return the combined groups.
     * @throws IllegalArgumentException if 1. The array is empty, 2 the outputs
     *                                  (writers) of the
     *                                  bist differ or 3 the
     *                                  bit are not contiguous.
     */
    public static OutBitGroup from( List<? extends OutBit> bits ) {
        return from( bits.toArray( new OutBit[ 0 ] ) );
    }

    @Override
    public String toString() {
        return "OutBitGroup{" + "output=" + output + ", size=" + size + ", offset=" + offset + '}';
    }

}
