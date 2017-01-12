package nl.fontys.sevenlo.widgets;

import java.awt.Dimension;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.Shape;
import javax.swing.ImageIcon;

/**
 * A Button with a lit Symbol.
 *
 * The implementation uses some magic numbers to compute the appearance
 * of the pressed and released state. The design is made on a 100x100
 * pixel grid.
 *
 * @author Pieter van den Hombergh (P dot vandenHombergh at fontys dot nl)
 * @version $Id$
 */
public class LitSymbolButton extends ShapeButton {
    private static final long serialVersionUID=1;

    /** Arrow is a Triangle. */
    public LitSymbolButton() {
        this(null);
    }

    /**
     * Create a Button with a symbol.
     * @param sym icon
     */
    public LitSymbolButton(ImageIcon sym) {
        super(sym);
        setPreferredSize(new Dimension(100, 100));
    }

   /**
    * Compute a shape relative to the given bounds.
    * @param bounds a rectangle
    * @return the shape
    */

    protected Shape computeShape(Rectangle bounds) {
        Polygon s;
        int[] x = new int[4];
        int[] y = new int[4];
        int w = getWidth();
        int h = getHeight();
        int tw = (1 + 2 * Math.min(w, h)) / 3;
        int th = tw;
        boolean isPressed = getModel().isPressed();
        int pox = isPressed ? (w + 25) / 50 : 0;
        int poy = isPressed ? (w + 25) / 50 : 0;

        x[0] = pox + (w - tw) / 2;
        y[0] = poy + (h - th) / 2;
        x[1] = x[0] + tw;
        y[1] = y[0];
        x[2] = x[1];
        y[2] = y[1] + th;
        y[3] = y[2];
        x[3] = x[0];

        s = new Polygon(x, y, x.length);
        return s;
    }
}
