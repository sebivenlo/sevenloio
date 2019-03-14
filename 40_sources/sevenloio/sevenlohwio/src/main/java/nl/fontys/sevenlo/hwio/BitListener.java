package nl.fontys.sevenlo.hwio;

/**
 * Bit Listener as part of the listener (modified observer) pattern.
 *
 * @author Pieter van den Hombergh.
 */
public interface BitListener<B extends Bit>  {
    /**
     * Inform the implementing class of bit changes.
     * @param bit Value carrying object
     * @param newValue the new value, causing the invocation of this method.
     */
    void updateBit( B bit, boolean newValue );
}
