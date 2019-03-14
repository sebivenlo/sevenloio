package nl.fontys.sevenlo.fxample;

import nl.fontys.sevenlo.hwio.InBit;

/**
 * Button Sensor.
 *
 * @author hom
 */
public class ButtonSensor extends InBit {

    private final BST type;
    private final int floor;
    private static int serialSource=0;
    private final int serialNr=++serialSource;

    /**
     * Create a ButtonsSensor for a bit with type and id.
     *
     * @param bitNr bit number
     * @param t button sensor type
     * @param f floor id
     */
    public ButtonSensor( int bitNr, BST t, int f ) {
        super( bitNr );
        type = t;
        floor = f;
    }

    @Override
    public String toString() {
        return type + " floor " + floor+ " #"+serialNr;
    }

    /**
     * Get floor info.
     *
     * @return floor info.
     */
    public int getFloor() {
        return floor;
    }

    /**
     * Return the button type.
     *
     * @return the type.
     */
    public BST getType() {
        return type;
    }

    @Override
    public String getName() {
        return type.toString()+' '+floor;
    }
    
}
