package nl.fontys.sevenlo.hwio;

/**
 * This interface provides abstract write access to hardware, allowing selected
 * bit updates.
 *
 * The unit of transfer is an integer (32 bit). It is up to the user to use
 * smaller aggregates such as byte (8 bit) or short (16 bit).
 *
 * @author Pieter van den Hombergh (P.vandenHombergh at fontys.nl)
 */
public interface Output {

    /**
     * Write with an update to only those bits that have a 1 (one) in the
     * associated position in mask. A hint to the implementor is to use a
     * <b>shadow</b> value that is a local copy of the last value written. This
     * facilitates the implementation on hardware that does not support read
     * back of values written, which is a very common case.
     *
     * @param mask the mask selects the bit written (1=written)
     * @param value of which the bit values indicated by mask are effectively
     * copied the output
     */
    void writeMasked( int mask, int value );

    /**
     * Nonblocking read-back of the last value written. Most implementations
     * would return a <b>shadow</b> value.
     *
     * @see Output#writeMasked(int, int)
     * @return the last value that was written
     */
    int lastWritten();
}
