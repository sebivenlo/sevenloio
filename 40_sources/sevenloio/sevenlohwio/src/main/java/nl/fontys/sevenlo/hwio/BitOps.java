package nl.fontys.sevenlo.hwio;

/**
 * The Operations of a Bit.
 * @author Pieter van den Hombergh (p.vandenhombergh@fontys.nl)
 */
public interface BitOps {

    /**
     * Test current value.
     * @return the current value
     */
    boolean isSet();

    /**
     * Set to value.
     * @param b new value
     */
    void set(boolean b);

}
