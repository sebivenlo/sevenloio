package nl.fontys.sevenlo.widgets;

/**
 *
 * @author Pieter van den Hombergh (P dot vandenHombergh at fontys dot nl)
 * @version $Id$
 */
public class ArrowButton extends ShapeButton {
    private static final long serialVersionUID = 1L;
    /**
     * Point direction.
     */
    private ArrowPointing direction = ArrowPointing.UP;

    /**
     * Get the direction this arrow is pointing in.
     * @return the direction.
     */
    public ArrowPointing getDirection() {
        return direction;
    }

    /**
     * Create and arrow pointing in specified direction.
     * @param pU the direction to point
     */
    public ArrowButton(ArrowPointing pU) {
        this.direction = pU;
        setShapeMaker(new ArrowShape(pU));
    }

    /**
     * Create an up pointing button.
     */
    public ArrowButton() {
        this(ArrowPointing.UP);
    }

    /**
     * Create arrow button using a shapemaker.
     * @param sm shape maker
     */
    public ArrowButton(ShapeMaker sm) {
        super(sm);
    }

    /**
     * Get the direction of the arrow.
     * @return the direction
     */
    public ArrowPointing getPointing() {
        return direction;
    }
    // Shape cache
    private ShapeMaker[] shapes = new ShapeMaker[ArrowPointing.values().length];

    /**
     * Set pointing dir.
     * @param pD pointDirection
     */
    public void setPointing(ArrowPointing pD) {
        this.direction = pD;
        if (shapes[direction.ordinal()] == null) {
            shapes[direction.ordinal()] = new ArrowShape(pD);
        }
        setShapeMaker(shapes[direction.ordinal()]);
    }
}
