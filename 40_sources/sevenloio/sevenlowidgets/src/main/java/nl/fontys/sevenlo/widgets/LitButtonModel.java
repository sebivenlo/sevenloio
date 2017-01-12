package nl.fontys.sevenlo.widgets;

import java.util.concurrent.CopyOnWriteArrayList;
import javax.swing.DefaultButtonModel;

/**
 * A lit button has a light with state (on/off).
 * This model adds listeners to state changes.
 * @author Pieter van den Hombergh (P dot vandenHombergh at fontys dot nl)
 * @version $Id$
 */
public class LitButtonModel extends DefaultButtonModel {

    private static final long serialVersionUID = 1L;
    private boolean lit = false;
    /** Listener collection. */
    private final CopyOnWriteArrayList<LightChangeListener>
            lightChangeListeners =
            new CopyOnWriteArrayList<LightChangeListener>();

    /**
     * Add a listener.
     * @param lcl LightChangeListener to add
     */
    public void addLightChangeListener(LightChangeListener lcl) {
        lightChangeListeners.addIfAbsent(lcl);
    }

    /**
     * Remove a listener.
     * @param lcl LightChangeListener to remove
     */
    public void removeLightChangeListener(LightChangeListener lcl) {
        lightChangeListeners.remove(lcl);
    }

    /**
     * Get the current illumination state of the Button.
     * @return lit or not.
     */
    public boolean isLit() {
        return lit;
    }

    /**
     * set light on or off.
     * @param l boolean on=true
     */
    public void setLit(boolean l) {
        this.lit = l;
        updateLightChangeListeners(lit);
    }

    /**
     * Upate Listeners.
     * @param l lit or not
     */
    protected void updateLightChangeListeners(boolean l) {
        for (LightChangeListener lcl : lightChangeListeners) {
            lcl.setLight(l);
        }
    }
}
