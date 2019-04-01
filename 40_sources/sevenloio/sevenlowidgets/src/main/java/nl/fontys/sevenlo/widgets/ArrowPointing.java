package nl.fontys.sevenlo.widgets;

/**
 * Arrows can point in four directions.
 * @author Pieter van den Hombergh (P dot vandenHombergh at fontys dot nl)
 * @version $Id$
 * 8 directions to point in.
 */
public enum ArrowPointing {

    /** Point NORTH. */
    UP(0.0),
    /** Point SOUTH. */
    DOWN(Math.PI),
    /** Point WEST. */
    LEFT(Math.PI / 2),
    /** Point EAST. */
    RIGHT(3 * Math.PI / 2),
    /** Point NORTHEAST. */
    UPRIGHT(Math.PI / 4),
    /** Point SOUTHHEAST. */
    DOWNRIGHT(3 * Math.PI / 4),
    /** Point NORTHWEST. */
    UPLEFT(7 * Math.PI / 4),
    /** Point SOUTHWEST. */
    DOWNLEFT(5 * Math.PI / 4);
    /**
     * The direction of point in radians ccw. Up = 0.0.
     */
    private final double angle;

    /** Construct a an arrow with an attitude. */
    ArrowPointing(double a) {
        angle = a;
    }

    /**
     * Get the angle the arrow is pointing to.
     * @return the angle.
     */
    public double getAngle() {
        return angle;
    }

}
