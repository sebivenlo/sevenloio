package nl.fontys.sevenlo.hwio;

/**
 * Input bit that supports Listeners and keeps copy of last value seen. The
 * implementation supports polling as well as listener style notification. The
 * implementation only notifies its listeners if the new bit value set differs
 * from the previous value.
 *
 * @author Pieter van den Hombergh (P.vandenHombergh at fontys dot nl)
 */
public class InBit extends Bit {

    /**
     * last value cache.
     */
    private boolean value = false;


    /**
     * Bit with id.
     *
     * @param bnr the bit number id.
     */
    public InBit( int bnr ) {
        super(bnr);
    }

    /**
     * Set the bit to boolean value.
     *
     * @param b new value.
     */
    @Override
    public void set( boolean b ) {
        if ( b != value ) {
            value = b;
            updateListeners( this, value );
        }
    }

    /**
     * Get up to date value.
     *
     * @return the last value.
     */
    @Override
    public boolean isSet() {
        return value;
    }

    /**
     * Get a string representation.
     *
     * @return the string.
     */
    public String getName() {
        return getClass().getSimpleName() + bitNr;
    }
}
