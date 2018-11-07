package nl.fontys.sevenlo.hwio;

import java.util.Arrays;

/**
 * Output bit group.
 * Purpose of a bitgroup to transfer more bits in one call, in particular if
 * the combination of the bits controls one device.<br>
 *
 * Uses an Output to do the actual io.<br>
 *
 * This variant is optimized for use cases where the bits in the group are
 * not contiguous in the output port.<br>
 *
 * The bits in passed in the constructor or in the factory method
 * <code>createFromBits</code> are sorted before use.
 *
 * The integer that is passed in on set and is returned on get has its bits
 * packed, that is the bits are adjacent in the order determined by the
 * bit numbers passed at construction time. Rationale: a bit group logically
 * represents only a few bits, typically two, that are easier and more
 * independently handled for the action output connected if they
 * are allowed to present the values 0, 1, 2 and 3 or 00 01 10 and  11 in binary.
 *
 * @author Pieter van den Hombergh (P dot vandenHombergh at fontys dot nl)
 */
public class NonContiguousOutBitGroup implements BitGroup {
    private final Output output;
    private final int size;
    private final int offset;
    /** mask before shifting. */
    private final int pmask;
    /** mask used in output operation. */
    private final int mask;
    /** masks used in per bit selection. */
    private final int[] masks;
    /** last value written (0 based). */
    private volatile int value;
    /**
     * Create a OutbitGroup of size starting at bit offset.
     * @param o output to use.
     * @param bitNr list of bits to use in group.
     */
    public NonContiguousOutBitGroup( Output o, int... bitNr) {
        int [] bits = Arrays.copyOf(bitNr, bitNr.length );
        Arrays.sort( bits );
        this.output = o;
        this.masks = new int[bits.length];
        this.size = bits.length;
        this.offset = bits[0];
        this.pmask = ( 1 << size ) -1;
        int vmask = 0;
        int m=0;
        for (int b: bits) {
            masks[m] = 1<< b;
            vmask |= masks[m];
            m++;
        }
        mask = vmask;
    }

    /**
     * Factory to create a bit group from a set of bits.
     * The bits to be combined into a group must share the same
     * Output port.
     * @param bits the bits to be combined.
     * @return the create bit group.
     * @throws IllegalArgumentException if the bits have different writers.
     */
    public static NonContiguousOutBitGroup createFromBits(OutBit ... bits) {
        int [] bitNumbers = new int[bits.length];
        Output writer = bits[0].getWriter();
        for (int b=0; b< bits.length; b++){
            if ( bits[b].getWriter() != writer ) {
                throw new
                  IllegalArgumentException("All bits should have same writer");
            }
            bitNumbers[b] = bits[b].getBitNr();
        }
        return new NonContiguousOutBitGroup(writer, bitNumbers);
    }

    /**
     * See BitGroup get documentation.
     * @return the current value
     */
    @Override
    public int get() {
        int result = 0;
        for (int i= 0; i < masks.length; i++){
            result <<=1;
            if ( (output.lastWritten() & masks[i]) !=0 ) {
                result |= 1;
            }
        }
        return result;
    }


    /**
     * See BitGroup get documentation.
     * @param v new value
     * @return the previous value
     */
    @Override
    public int set( int v ) {
        int ov = value;
        value = v & pmask;
        int distributed =0;
        for (int b=0; b < masks.length && v !=0; b++) {
            distributed |= ((v&1) !=0 )?masks[b]:0;// * ( v & 1 );
            v >>>= 1;
        }
        output.writeMasked( mask, distributed );
        return ov;
    }

    /**
     * See BitGroup.get documentation.
     * @return the size
     */
    @Override
    public int size() {
        return size;
    }

    /**
     * See BitGroup get documentation.
     * @return the offset (position from the right)
     */
    @Override
    public int offset() {
        return offset;
    }

    /**
     * See BitGroup.get documentation.
     * Return the input mask.
     * @return  the mask
     */
    @Override
    public int getMask() {
        return this.mask;
    }
}
