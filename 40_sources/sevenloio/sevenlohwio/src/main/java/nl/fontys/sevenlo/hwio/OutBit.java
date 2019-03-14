package nl.fontys.sevenlo.hwio;

import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Output Bitwise with value read-back.
 *
 * @author Pieter van den Hombergh (P.vandenHombergh at fontys dot nl)
 */
public class OutBit extends Bit {

    /**
     * Mask for isolated bit updates.
     */
    private final int mask;
    /**
     * The writer for the hardware.
     */
    private final Output writer;
    /**
     * The last written value.
     */
    private boolean value;

    /**
     * Get the output port.
     *
     * @return the port.
     */
    Output getWriter() {
        return writer;
    }

    /**
     * Outbit on port on bitnr position.
     *
     * @param port output to use.
     * @param bitNr position of bit.
     */
    public OutBit( Output port, int bitNr ) {
        super(bitNr);
        writer = port;
        mask = 1 << bitNr;
    }

    /**
     * Set th bit to value.
     *
     * @param b new value
     */
    @Override
    public void set( boolean b ) {
        value = b;
        writer.writeMasked( mask, b ? ~0 : 0 );
        updateListeners( this, value );
    }

    /**
     * Get boolean bit value.
     *
     * @return the boolean bit value.
     */
    @Override
    public boolean isSet() {
        return ( writer.lastWritten() & mask ) != 0;
    }

    /**
     * Get a string representation.
     *
     * @return the string.
     */
    @Override
    public String getName() {
        return getClass().getSimpleName() + bitNr;
    }

}
