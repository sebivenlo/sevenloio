package nl.fontys.sevenlo.widgets;

import java.awt.Component;
import java.awt.Polygon;
import java.awt.Shape;
import java.awt.geom.AffineTransform;

/**
 * Lots of magic numbers here. Come from drawing with inkscape and then
 * converting the svg into java shape creating primitives.
 *
 * @author Pieter van den Hombergh (P dot vandenHombergh at fontys dot nl)
 * @version $Id$
 */
public class DialNeedle implements ShapeButton.ShapeMaker {

    private int centerX = 0, centerY = 0;
    private double angle;

    /**
     * Compute the shape of the Needle.
     * The 'magic' numbers used stem from a design on a 1000x1000 pixel grid.
     *
     * @param c the component for its size.
     * @return the shape.
     */
    @Override
    public Shape computeShape(Component c) {
        int w = c.getWidth();
        int h = c.getHeight();

        centerX = c.getWidth() * 500 / 1000;
        centerY = c.getHeight() * 880 / 1000;

        Polygon s = new Polygon();

        s.addPoint(w * 500 / 1000, h * 32 / 1000);
        s.addPoint(w * 480 / 1000, h * 972 / 1000);
        s.addPoint(w * 500 / 1000, h * 912 / 1000);
        s.addPoint(w * 520 / 1000, h * 972 / 1000);

        AffineTransform at = AffineTransform.getRotateInstance(angle, centerX,
                centerY);

        return at.createTransformedShape(s);
    }

    /**
     * Produce a needle for a component.
     * The needle rotates around a magic center.
     * @param c the component to fit with a needle.
     */
    public DialNeedle(Component c) {
        if (c != null) {
            centerX = c.getWidth() * 500 / 1000;
            centerY = c.getHeight() * 880 / 1000;
        }
    }

    /**
     * Set the rotation angle of the needle.
     * @param theta the rotation angle.
     * @return the needle in its new posture.
     */
    public DialNeedle setRotation(double theta) {
        angle = theta;
        return this;
    }
}
