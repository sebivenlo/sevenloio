package nl.fontys.sevenlo.hwio;

/**
 * Do one read and check step.
 * @author Pieter van den Hombergh (P.vandenHombergh at fontys.nl)
 */
public interface Poller {

    /**
     * Do one read action and update inputbits for changes.
     * Call this method in the runnable of a pollThread.
     */
    void pollOnce();
}
