package nl.fontys.sevenlo.hwio;

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
    void addListener( BitListener bl );

    /**
     * Remove Listener.
     *
     * @param bl the listener
     */
    void removeListener( BitListener bl );

}
