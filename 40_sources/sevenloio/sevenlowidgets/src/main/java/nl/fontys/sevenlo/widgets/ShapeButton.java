package nl.fontys.sevenlo.widgets;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;

/**
 * A Button with a lit Shape and a icon.
 * @author Pieter van den Hombergh (P dot vandenHombergh at fontys dot nl)
 * @version $Id$
 */
public class ShapeButton extends JButton {

    private static final long serialVersionUID = 1L;
    /** Arrow is a Triangle. */
    private Shape shape;
    /** edge stroke.*/
    private BasicStroke stroke;

    /**
     * Get the highlight colour.
     * @return colour
     */
    public Color getHighlightColor() {
        return highlightColor;
    }

    /**
     * Sets the highlight colour.
     * @param hc colour
     */
    public void setHighlightColor(Color hc) {
        this.highlightColor = hc;
        repaint();
    }

    /**
     * Get the interior colour.
     * @return colour
     */
    public Color getInteriorColor() {
        return interiorColor;
    }

    /**
     * Sets the interior colour.
     * @param ic colour
     */
    public void setInteriorColor(Color ic) {
        this.interiorColor = ic;
        repaint();
    }

    /**
     * Gets the stroke (line of outline).
     * @return the stroke.
     */
    public BasicStroke getStroke() {
        return stroke;
    }

    /**
     * Sets the stroke (line of outline).
     * @param st the stroke.
     */
    public void setStroke(BasicStroke st) {
        this.stroke = st;
        repaint();
    }

    /**
     * Gets the stroke (line of outline) colour.
     * @return the stroke.
     */
    public Color getStrokeColor() {
        return strokeColor;
    }

    /**
     * Sets the stroke (line of outline) colour.
     * @param  sc the stroke colour.
     */
    public void setStrokeColor(Color sc) {
        this.strokeColor = sc;
        repaint();
    }

    /** interior fill paint. */
    private Paint interiorPaint;
    /** on and off colours. */
    private Color interiorColorOn = Color.RED;
    /** Interior Colour for off state.  */
    private Color interiorColorOff = new Color(128, 128, 128);
    /** highlights in on and off state. */
    private Color highlightColorOn = Color.WHITE;
    /** highlight in off state. */
    private Color highlightColorOff = Color.LIGHT_GRAY;
    /** interior colour used in shape painting. */
    private Color interiorColor;
    /** highlight colour. */
    private Color highlightColor;
    /** used in shape painting. */
    private Color strokeColor;
    /**
     * Hints (smooth or not) for rendering.
     */
    private RenderingHints hints =
            new RenderingHints(RenderingHints.KEY_ANTIALIASING,
            RenderingHints.VALUE_ANTIALIAS_ON);
    /**
     * Symbol to be put on top.
     */
    //protected ImageIcon icn = null;

    /**
     * Paint the component with shape and symbol.
     * @param g graphics context
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        evaluateStateAttributes();
        drawShapeShadow(g2d);

        float pressedShift = Math.min(3.0f,3.0f * getHeight() / 100);

        // save transformation
        AffineTransform oldTransform = g2d.getTransform();

        if (getModel().isPressed()) {
            g2d.translate(pressedShift, pressedShift);
        } else {
            g2d.translate(-pressedShift, -pressedShift);
        }

        // draw in (un)pressed transform
        drawShape(g2d);
        drawSymbol(g2d, this);

        // restore transform
        g2d.setTransform(oldTransform);
    }

    /**
     * Creates a Button pointing down.
     */
    public ShapeButton() {
        this(false);
    }

    /**
     * Creates a up/down pointing arrow.
     * @param pointUp or down
     */
    public ShapeButton(boolean pointUp) {
        this(new ArrowShape(pointUp?ArrowPointing.UP:ArrowPointing.DOWN));
    }

    /**
     * Create ShapeButton with icon.
     * @param i image to be put on top.
     */

    public ShapeButton(ImageIcon i){
        super(i);
    }

    /**
     * Create a button with specific shape.
     * @param shaper shape to use.
     */
    public ShapeButton(ShapeMaker shaper) {
        this.shapeMaker = shaper;
        setPreferredSize(new Dimension(100, 100));
        stroke = new BasicStroke(3.0f);
        setModel(new LitButtonModel());
    }

    /**
     * Test if light on.
     * @return true if light is on
     */
    public boolean isOn() {
        return ((LitButtonModel) getModel()).isLit();
    }

    /**
     * Set light on or off.
     * @param on will light the lamp.
     */
    public void setOn(boolean on) {
        ((LitButtonModel) getModel()).setLit(on);
    }

    /**
     * This recomputes the shape.
     *
     */
    @Override
    public void doLayout() {
        super.doLayout();
        shape = computeShape(this);
    }

    /**
     * Returns the current HighLight off color.
     * @return the highlight off color
     */
    public Color getHighlightColorOff() {
        return highlightColorOff;
    }

    /**
     * Determine the hightlight off color.
     * @param c highlightColorOff the color
     */
    public void setHighlightColorOff(Color c) {
        this.highlightColorOff = c;
        repaint();
    }

    /**
     * Returns the current HighLight on color.
     * @return the highlight on color
     */
    public Color getHighlightColorOn() {
        return highlightColorOn;
    }

    /**
     * Determine the high light on color.
     * @param c highlightColorOn the color
     */
    public void setHighlightColorOn(Color c) {
        this.highlightColorOn = c;
        repaint();
    }

    /**
     * Returns the current Shape fill off color.
     * @return the fille off color
     */
    public Color getInteriorColorOff() {
        return interiorColorOff;
    }

    /**
     * Set the current  fill off color.
     * @param c  the fill off  color
     */
    public void setInteriorColorOff(Color c) {
        this.interiorColorOff = c;
        repaint();
    }

    /**
     * Returns the current Shape fill on color.
     * @return the fille on color
     */
    public Color getInteriorColorOn() {
        return interiorColorOn;
    }

    /**
     * Set the current  fill on color.
     * @param c  the fill on  color
     */
    public void setInteriorColorOn(Color c) {
        this.interiorColorOn = c;
        repaint();
    }

    /**
     * Compute the attributes dependent on state.
     * A vertical gradient is used, starting at 1/3 from the top
     * and ending at 1/3 from the bottom.
     */
    protected void evaluateStateAttributes() {
        int w = getWidth();
        int h = getHeight();

        if (isOn()) {
            interiorColor = interiorColorOn;
            highlightColor = highlightColorOn;
        } else {
            interiorColor = interiorColorOff;
            highlightColor = highlightColorOff;

        }
        interiorPaint = new GradientPaint(w / 2, h / 3,
                highlightColor, w / 2, (2 * h) / 3, interiorColor.darker(),
                    true);

        strokeColor = isOn() ? Color.RED : Color.RED.darker();

    }

    /**
     * Draw the symbol on the component with the graphics context.
     * @param g2d graphics context
     * @param c component to draw on.
     */
    protected void drawSymbol(Graphics2D g2d, Component c) {
        symbolPainter.drawSymbol(g2d, c);
    }

    /**
     * Compute a shap that should with (in?) rectangle bounds.
     * Default returns rectangle offset 1/3 from edges.
     * @param c the component to draw on.
     * @return the computed shape.
     */
    protected Shape computeShape(Component c) {
        return shapeMaker.computeShape(c);

    }

    /**
     * Draw the shadow for the shape.
     * This implementation draws with a dark gray 50% translucent stroke.
     * The strok width = 10% or the height.
     * @param g2d graphics context.
     */
    protected void drawShapeShadow(Graphics2D g2d) {
        Stroke oldStroke = g2d.getStroke();
        Stroke shadowStroke = new BasicStroke(
                Math.min(8.0f,8.0F * getHeight() / 100),
                BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
        g2d.setRenderingHints(hints);
        g2d.setStroke(shadowStroke);
        g2d.setColor(new Color(20, 20, 20, 128));
        g2d.draw(shape);
        g2d.setStroke(oldStroke);
    }

    /**
     * Draw the distinguished shape.
     * @param g2d THe graphics context.
     */
    protected void drawShape(Graphics2D g2d) {
        int w = getWidth();
        int h = getHeight();
        int y2 = 5 * h / 6;
        if (isOn()) {
            interiorColor = interiorColorOn;
            highlightColor = highlightColorOn;
        } else {
            interiorColor = interiorColorOff;
            highlightColor = highlightColorOff;
        }

        Stroke oldStroke = g2d.getStroke();
        stroke = new BasicStroke(Math.min(3.0f,3.0F * getHeight() / 100),
                    BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
        interiorPaint = new GradientPaint(w / 2, h / 3, highlightColor,
                    w / 2, y2, interiorColor.darker(), true);
        Paint oldPaint = g2d.getPaint();
        g2d.setPaint(interiorPaint);
        g2d.fill(shape);
        g2d.setPaint(oldPaint);

        g2d.setStroke(stroke);
        g2d.setColor(interiorColorOn.darker());
        g2d.setRenderingHints(hints);
        g2d.draw(shape);
        g2d.setStroke(oldStroke);
    }
    private ShapeMaker shapeMaker = new RectangleShape();

    /**
     * Interface used to compute shape.
     */
    public interface ShapeMaker {

        /**
         * Compute(produce) the shape for component c.
         * @param c component used to determine extends.
         * @return the computed shape
         */
        Shape computeShape(Component c);
    }

    /**
     * Get the shape producing object.
     * @return the shapeMaker.
     */
    public ShapeMaker getShapeMaker() {
        return shapeMaker;
    }

    /**
     * Set the shape producer.
     * @param sm shapeMaker the maker of the shape
     */
    public final void setShapeMaker(ShapeMaker sm) {
        if (sm != null) {
            this.shapeMaker = sm;
            doLayout();
        }
    }

    /**
     * Implementation example for round shape.
     */
    public static class RoundShape implements ShapeMaker {

        /**
         * Create the required shape.
         * @param c for this component.
         * @return the shape.
         */
        @Override
        public Shape computeShape(Component c) {
            Rectangle bounds = c.getBounds();
            int w = 3 * Math.min(bounds.width, bounds.height) / 4;
            int x = (bounds.width - w) / 2;
            int y = (bounds.height - w) / 2;
            Ellipse2D result = new Ellipse2D.Float(x, y, w, w);
            return result;
        }
    }

    /**
     * Implementation example for rectangular shape.
     */
    public static class RectangleShape implements ShapeMaker {

        /**
         * Create the required shape.
         * @param c for this component.
         * @return the shape.
         */
        public Shape computeShape(Component c) {
            Rectangle bounds = c.getBounds();
            return new Rectangle(bounds.width / 6, bounds.height / 6,
                    2 * bounds.width / 3, 2 * bounds.height / 3);
        }
    }

    /**
     * Creates and Arrow like shape.
     */
    public static class ArrowShape implements ShapeMaker {

        private ArrowPointing direction;
        /**
         * Create an arrowshape pointing in a direction.
         * @param p the direction to point
         */
        public ArrowShape(ArrowPointing p) {
            direction = p;
        }

        /**
         * Create the required shape.
         * @param c for this component.
         * @return the shape.
         */
        public Shape computeShape(Component c) {
            Rectangle bounds = c.getBounds();
            Polygon s;
            int[] x = new int[7];
            int[] y = new int[7];
            int w = bounds.width;
            int h = bounds.height;
            int asize = (1 + 2 * Math.min(w, h)) / 3;
            int th = (17 * asize) / 20;
            int tailw = (4*asize) / 20;
            int taill = (6*asize) / 20;
            x[0] = (w) / 2;
            y[0] = (w - asize) / 2;
            x[1] = x[0] + asize / 2;
            y[1] = y[0] + th;
            x[2] = x[0] + tailw;
            y[2] = y[1] - (3 * asize) / 20;
            x[3] = x[2];
            y[3] = y[2] + taill;
            x[4] = x[2] - 2*tailw;
            y[4] = y[3];
            x[5] = x[4];
            y[5] = y[2];
            x[6] = x[0] - asize / 2;
            y[6] = y[1];
            s = new Polygon(x, y, x.length);
            AffineTransform at;
            switch (direction) {
                case UP:
                    return s;
                default:
                    at = AffineTransform.getRotateInstance(direction.getAngle(),
                            w/2, h/2);
                    return at.createTransformedShape(s);
                case DOWN:
                    at = AffineTransform.getQuadrantRotateInstance(2, w/2,
                            h/2);
                    return at.createTransformedShape(s);
                case LEFT:
                    at = AffineTransform.getQuadrantRotateInstance(1, w/2,
                            h/2);
                    return at.createTransformedShape(s);
                case RIGHT:
                    at = AffineTransform.getQuadrantRotateInstance(3, w/2,
                            h/2);
                    return at.createTransformedShape(s);
            }
        }
    }

    /**
     *
     * Regular polygon painting shape. First point points north.
     *
     */
    public static class PolygonShapeMaker implements ShapeMaker {

        private final int points;
        private double startAngle = 0.0;

        /**
         * Get the starting angle of the first point.
         * @return the angle
         */
        public double getStartAngle() {
            return startAngle;
        }

        /**
         * Sets the start angle.
         * @param ang startAngle
         * @return this for setting chaining.
         */
        public PolygonShapeMaker setStartAngle(double ang) {
            this.startAngle = ang;
            return this;
        }

        /**
         * Create the required shape.
         * @param c for this component.
         * @return the shape.
         */
        public Shape computeShape(Component c) {
            Polygon s = new Polygon();
            int h = c.getHeight();
            int w = c.getWidth();
            int centerX = w / 2;
            int centerY = h / 2;
            int radius = Math.min(h, w) / 3;
            double angleStep = 2 * Math.PI / (points);
            double angle = startAngle;
            for (int i = 0; i < (points); i++) {
                int px;
                int py;
                py = (int) (centerY - radius * Math.cos(angle));
                px = (int) (centerX + radius * Math.sin(angle));
                s.addPoint(px, py);
                angle += angleStep;
            }
            return s;
        }

        /**
         * Make a regular equilateral polygon with n points.
         * @param n points
         */
        public PolygonShapeMaker(int n) {
            super();
            this.points = n;
        }

        /**
         * Create a triangle.
         */
        public PolygonShapeMaker() {
            this(3);
        }
    }

    /**
     * Simple n pointed star shape.
     */
    static class StarShapeMaker implements ShapeButton.ShapeMaker {

        private final int points;
        private double innerPhase = 0.0;
        private double outerPhase = 0.0;

        /**
         * Computer the shape.
         * @param c the component the shape is computed (scaled) for.
         * @return the shape.
         */
        @Override
        public Shape computeShape(Component c) {
            Polygon s = new Polygon();
            int h = c.getHeight();
            int w = c.getWidth();
            int centerX = w / 2;
            int centerY = h / 2;
            int outerRadius = 3 * Math.min(h, w) / 9;
            int innerRadius = Math.min(h, w) / 10;
            int radius;
            double angleStep = Math.PI / (points);
            double angle = 0;
            double phase = 0;
            for (int i = 0; i < (2 * points); i++) {
                if (i % 2 == 0) {
                    radius = outerRadius;
                    phase = outerPhase;
                } else {
                    radius = innerRadius;
                    phase = innerPhase;
                }
                int px, py;
                double a = angle + phase;
                py = (int) (centerY - radius * Math.cos(a));
                px = (int) (centerX + radius * Math.sin(a));
                s.addPoint(px, py);
                angle += angleStep;
            }
            return s;

        }

        /**
         * Create a n pointed star.
         * @param n points
         */
        StarShapeMaker(int n) {
            this.points = n;
        }

        /**
         * Create a 3 pointer star.
         */
        StarShapeMaker() {
            this(3);
        }
    }

    /**
     * Paint an icon on the Component.
     */
    public interface SymbolPainter {

        /**
         * Draw a symbol on (top of )  a component.
         * @param g2d the graphics context
         * @param c the component to use
         */
        void drawSymbol(Graphics2D g2d, Component c);
    }
    private SymbolPainter symbolPainter = new IconPainter();

    /**
     * Getter for  symbol painter.
     * @return the painter
     */
    public SymbolPainter getSymbolPainter() {
        return symbolPainter;
    }

    /**
     * Set the symbol painter.
     * @param sp symbolPainter the painter
     */
    public void setSymbolPainter(SymbolPainter sp) {
        this.symbolPainter = sp;
    }

    /**
     * String painting as symbol painter class.
     * Could be used to draw any font provided shape such as 'wingdings' as
     * symbols.
     */
    public static class StringPainter implements SymbolPainter {

        private final String string;

        /**
         * initialise with a string.
         * @param s the symbol string.
         */
        public StringPainter(String s) {
            string = s;
        }

        /**
         * Draw my icon on the button.
         * @param g2d the graphics context
         * @param c component to draw on
         */
        @Override
        public void drawSymbol(Graphics2D g2d, Component c) {
            Font f = c.getFont().deriveFont(Font.BOLD, 0.6f * c.getHeight());
            int x, y;
            String s = "" + string;
            g2d.setFont(f);
            FontMetrics fm = c.getFontMetrics(f);
            Rectangle2D r = fm.getStringBounds(s, g2d);
            x = (int) ((c.getWidth() - r.getWidth()) / 2);
            y = (int) ((c.getHeight() - r.getHeight()) / 2);
            g2d.setColor(Color.BLACK);
            g2d.drawString(s, x, y + fm.getAscent());
        }
    }

    /**
     * Paints an Icon as symbol.
     */
    public static class IconPainter implements SymbolPainter {

        private Icon icn;

        /**
         * Create a default (null) painter.
         */
        public IconPainter(){}
        /**
         * Create a a icon painter with the icon set.
         * @param ii the icon to use
         */
        public IconPainter(Icon ii) {
            icn = ii;
        }

        /**
         * Draw my icn on the button.
         * @param g2d the graphics context
         * @param c component to draw on
         */
        public void drawSymbol(Graphics2D g2d, Component c) {
            if (icn != null) {
                int x = (c.getWidth() - icn.getIconWidth()) / 2;
                int y = (c.getHeight() - icn.getIconHeight()) / 2;
                icn.paintIcon(c, g2d, x, y);
            }
        }
    }

    /**
     * Factory method for round digit button.
     * @param d the digit
     * @return round shape button
     */
    public static ShapeButton createRoundDigitButton(int d) {
        return changeToRoundDigitButton(new ShapeButton(), d);
    }

    /**
     * Factory method for rectangular symbol button.
     * @param icn the icon
     * @return rectangular shape button
     */
    public static ShapeButton createRectangleSymbolButton(ImageIcon icn) {
        return changeToSymbolButton(new ShapeButton(new RectangleShape()),
                    icn);
    }

    /**
     * Factory for pointing button.
     * @param pointing the dierction
     * @return arrow shape button
     */
    public static ShapeButton createArrowButton(ArrowPointing pointing) {
        return changeToArrowButton(new ShapeButton(), pointing);
    }

    /**
     * Change shape to round shape.
     * @param sb the shape button
     * @param d the digit
     * @return modified shape button
     */
    public static ShapeButton changeToRoundDigitButton(ShapeButton sb, int d) {
        sb.setShapeMaker(new ShapeButton.RoundShape());
        sb.setSymbolPainter(new StringPainter("" + d));
        return sb;
    }

    /**
     * Change shape to rect shape.
     * @param sb the shape button
     * @param icn the icon
     * @return modified shape button
     */
    public static ShapeButton changeToSymbolButton(ShapeButton sb,
            ImageIcon icn) {
        sb.setSymbolPainter(new IconPainter(icn));
        return sb;
    }

    /**
     * Change shape to ArrowButton.
     * @param sb the shape button
     * @param pointing the direction
     * @return modified shape button
     */
    public static ShapeButton changeToArrowButton(ShapeButton sb,
            ArrowPointing pointing) {
        sb.setShapeMaker(new ArrowShape(pointing));
        return sb;
    }
}
