package nl.fontys.sevenlo.widgets;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Image;
import javax.swing.Icon;
import nl.fontys.sevenlo.hwio.BitListener;

/**
 * State full Icon, shows off and on state of a boolean.
 * The implementation assumes the same size for both on and off images.
 * The BinaryIcon implements BitListener and invokes repaint on its Observing
 * component on a updateBit Listener call.
 * @author Pieter van den Hombergh (P dot vandenHombergh at fontys dot nl)
 * @version $Id$
 */
public class BinaryIcon implements Icon, BitListener {
    private final Image offStateIcon;
    private final Image onStateIcon;
    private boolean state = false;
    private final Component observer;
    /**
     * Create Binary Icon and take component carrying the icon and the 
     * off and on images.
     * @param iobs the component the images are drawn on.
     * @param off the off image
     * @param on the on image
     */
    public BinaryIcon(final Component iobs, final Image off, final Image on) {
        observer = iobs;
        offStateIcon = off;
        onStateIcon  = on;

    }
    /**
     * Paint the icon on a component at specific location.
     * @param c component to draw on.
     * @param g the graphics context
     * @param x the x location
     * @param y the y location
     */
    @Override
    public void paintIcon(Component c, Graphics g, int x, int y) {
            g.drawImage(state?onStateIcon:offStateIcon, x, y, c);
    }

    /**
     * Get the icon width.
     * @return the width of the off image on the observing component.
     */
    @Override
    public int getIconWidth() {
        return onStateIcon.getWidth(observer);
    }

    /**
     * Get the icon height.
     * @return the height of the off image on the observing component.
     */
    @Override
    public int getIconHeight() {
        return onStateIcon.getWidth(observer);
    }

    /**
     * Update the bit presentation.
     * @param bit object connected to this bit. Not used.
     * @param newValue of the bit
     */
    @Override
    public void updateBit(Object bit, boolean newValue) {
        state = newValue;
        observer.repaint();
    }
}
