package nl.fontys.sevenlo.hwio;

import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Subject or Observable Bit.
 *
 * @author Pieter van den Hombergh (p.vandenhombergh@fontys.nl)
 */
public interface BitSubject {

    /**
     * Add Listener.
     *
     * @param bl the listener
     */
    default void addListener( BitListener bl ) {
        getListeners().addIfAbsent( bl );
    }

    /**
     * Remove Listener.
     *
     * @param bl the listener
     */
    default void removeListener( BitListener bl ) {
        getListeners().remove( bl );
    }

    /**
     * Get the collection for listeners.
     * @return the listeners 
     */
    CopyOnWriteArrayList<BitListener> getListeners();

    /**
     * Remove all listeners.
     */
    default void removeAllListeners() {
        getListeners().clear();
    }

    /**
     * Update my listeners. Typical usage is to pass this as the Object in a
     * subclass, which then can be inspected by the callee to get more info then
     * just the bit value.
     *
     * @param bo       bit object
     * @param newValue newest value
     */
    //@SuppressWarnings("unchecked")
    default void updateListeners( Bit bo, boolean newValue ) {
        for ( BitListener bl : getListeners() ) {
            bl.updateBit( bo, newValue );
        }
    }

}
