package nl.fontys.sevenlo.hwio;

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
     * @param o output to use.
     * @param sz size or the number of bits in the group.
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
}
