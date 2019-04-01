package nl.fontys.sevenlo.hwio;

/**
 * Group of numbered bits.
 *
 * The Bits can be input or output in any mix. Returns the bitops at location i.
 * Bits are numbered from right to left. Right most bit has number 0. The
 * (constant) inputMask is used te distinguish between input (1) and output (0).
 *
 * @param <X> backing type, typically int or long, expressed as Integer or Long.
 * @author Pieter van den Hombergh (P.vandenHombergh at fontys dot nl)
 */
public interface BitAggregate<X extends Number> extends IO {

    /**
     * Get Bit a position i.
     *
     * @param i bit nr
     * @return the bitops
     */
    BitOps getBit( int i );

    /**
     * Get number of bits in the aggregate.
     *
     * @return the number of bits in the aggregate
     */
    int size();

    /**
     * Get the input mask.
     *
     * @return the actual input mask.
     */
    X getInputMask();

    /**
     * Return he last read value read. The method is non blocking. The value can
     * be stale.
     *
     * @return the last value seen on the port.
     */
    X lastRead();

    /**
     * Connect the bit listeners to the bits.
     *
     * This method is there to prevent an implementation issue. Without it the
     * implementor of this interface maybe forced to leak <b>this</b> in some
     * constructor. The user of the aggregate must call this method before the
     * aggregate is to be used and be fully functional.
     *
     * A typical BitAggregate will use an AbstractBitFactory implementation to
     * create the bits and then connect the listeners in this method call. This
     * ensures that the listener connections does not have to be made during
     * construction time, when it is not yet safe to do so.
     *
     * @throws IllegalStateException when not ready for connect.
     */
    void connect() throws IllegalStateException;

    /**
     * Connect a bit at a time. Connect the bits after the aggregate is created.
     *
     * @param bit to be connected.
     * @throws IllegalArgumentException when the bit is not compatible with the
     * input mask of the aggregate.
     */
    void connect( Bit bit );

    /**
     * Get this aggregate as String.
     *
     * @return the string representation .
     */
    default String asString() {
        StringBuilder result = new StringBuilder( "[\n" );
        for ( int i = 0, n = this.size(); i < n; i++ ) {
            result.append( "\t" ).append( this.getBit( i ) ).append( "\n" );
        }
        result.append( "]" );
        return result.toString();
    }
}
