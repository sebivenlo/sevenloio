package nl.fontys.sevenlo.hwio;

import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Bit operations with BitListener support. Bits are typically of type input or
 * output, in some cases both. This abstract class provides all operations that
 * are common to all kind of IO bits. The implementation only supports true (=1)
 * and false(=0) as states; tri-state is not supported.
 *
 * @author Pieter van den Hombergh (P.vandenHombergh at fontys dot nl)
 */
public abstract class Bit implements BitOps, BitSubject {
    //@GuardedBy("listeners")

    private static int bitSerialNumberSequence = 0;
    /**
     * The serial number each bit from the factory receives.
     */
    protected final int bitSerialNumber = ++bitSerialNumberSequence;

    /**
     * Returns the serial number of a bit. Generation of bits is maintained
     * here.
     *
     * @return the serial number.
     */
    protected int getBitSerialNumber() {
        return bitSerialNumber;
    }
    private final CopyOnWriteArrayList<BitListener> listeners
            = new CopyOnWriteArrayList<>();

    /**
     * set to true.
     */
    public void set() {
        set( true );
    }

    /**
     * Set to false.
     */
    public void clear() {
        set( false );
    }

    /**
     * Add Listener.
     *
     * @param bl the listener
     */
    @Override
    public void addListener( BitListener bl ) {
        listeners.addIfAbsent( bl );
    }

    /**
     * Remove Listener.
     *
     * @param bl the listener
     */
    @Override
    public void removeListener( BitListener bl ) {
        listeners.remove( bl );
    }

    /**
     * Get the bitname for use in toString. this.toString uses this string to
     * compose a meaning full string for the bit and its subclasses. It is
     * intended that this method is overwritten.
     *
     * @return a name
     */
    public String getName() {
        return getClass().getSimpleName();
    }

    /**
     * Update my listeners. Typical usage is to pass this as the Object in a
     * subclass, which then can be inspected by the callee to get more info then
     * just the bit value.
     *
     * @param bo bit object
     * @param newValue newest value
     */
    //@SuppressWarnings("unchecked")
    protected void updateListeners( Object bo, boolean newValue ) {
        listeners.forEach( ( bl ) -> {
            bl.updateBit( bo, newValue );
        } );
    }

    /**
     * Return simple class name plus value.
     *
     * @return string representation
     */
    @Override
    public String toString() {
        return getName()
                + " [serial #" + getBitSerialNumber() + "]=" + isSet();
    }
}
