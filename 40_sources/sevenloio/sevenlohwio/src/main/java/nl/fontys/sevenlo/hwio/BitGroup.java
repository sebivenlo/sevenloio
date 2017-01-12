package nl.fontys.sevenlo.hwio;

/**
 * Bit operations in groups. Similar to the C bit field. The bits in the group
 * are contiguous from the programmers point of view, but need not be so at the
 * hardware side. A bit group is typically used when more then one bit control
 * the same hardware and the values of the whole group has to meet some
 * invariants that cannot be met when this bits are transported to the output
 * individually and not as a group.
 *
 * @author Pieter van den Hombergh (P dot vandenHombergh at fontys dot nl)
 */
public interface BitGroup {

    /**
     * Get the current value.
     *
     * @return the current value
     */
    int get();

    /**
     * Set to a new value.
     *
     * @param v new value.
     * @return previous value
     */
    int set( int v );

    /**
     * Get the size.
     *
     * @return number of bits in this group.
     */
    int size();

    /**
     * Get to number of the lowest (right most) bit in the group. Return the
     * position of the right most bit in the group. Same as the left shift
     * amount.
     *
     * @return the bit number of the rightmost bit.
     */
    int offset();

    /**
     * Get the bit isolating mask.
     *
     * @return the mask.
     */
    int getMask();
}
