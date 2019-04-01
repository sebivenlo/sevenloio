package nl.fontys.sevenlo.widgets;

/**
 * Turns the lights off.
 * @author Pieter van den Hombergh (P dot vandenHombergh at fontys dot nl)
 * @version $Id$
 */
public interface LightChangeListener {
    /**
     * Turn the light on or off.
     * @param on the new value
     */
    void setLight(boolean on);
}
