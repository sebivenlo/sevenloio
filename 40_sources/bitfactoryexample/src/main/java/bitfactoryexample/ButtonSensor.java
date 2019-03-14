package bitfactoryexample;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import nl.fontys.sevenlo.hwio.InBit;

/**
 * Button Sensor.
 * This button sensor has type, floor and cage number information.
 *
 * @author hom
 */
public class ButtonSensor extends InBit {

    private final BST type;
    private final int floor;
    private int cageNr=0;
    private final BooleanProperty status = new SimpleBooleanProperty();

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
        connectProperty();
    }

    /**
     * Attach this as listener to update the boolean property about the change.
     * (Object adapter)
     */
    private void connectProperty(){
        this.addListener(( Bit itsMe, boolean newValue ) -> {
            status.set(newValue);
        });
    }
    
    @Override
    public String toString() {
        return type + " floor " + floor;
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

    public int getCageNr() {
        return cageNr;
    }

    public ButtonSensor atCage( int cageNr ) {
        this.cageNr = cageNr;
        return this;
    }
}
