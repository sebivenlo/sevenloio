package nl.fontys.sevenlo.hwio;

/**
 * This interface provides abstract read access to hardware.
 *
 * The unit of transfer is an integer. It is up to the user to
 * use smaller aggregates such as byte (8 bit) or short (16 bit).
 *
 * @author Pieter van den Hombergh (P.vandenHombergh at fontys dot nl)
 */
public interface Input {

    /**
     * Read a new value from this port.
     * @return the value read.
     */
    int read();

}
