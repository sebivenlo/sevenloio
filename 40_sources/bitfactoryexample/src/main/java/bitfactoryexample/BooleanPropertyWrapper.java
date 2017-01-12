package bitfactoryexample;

import javafx.beans.property.SimpleBooleanProperty;
import nl.fontys.sevenlo.hwio.BitListener;

/**
 *
 * @author Pieter van den Hombergh {@code <p.vandenhombergh@fontys.nl>}
 */
public class BooleanPropertyWrapper extends SimpleBooleanProperty implements BitListener {

    public final ButtonSensor wrappedSensor;

    public BooleanPropertyWrapper( ButtonSensor wrappedSensor ) {
        this.wrappedSensor = wrappedSensor;
        connect();
    }

    private void connect() {
        wrappedSensor.addListener( this );
    }

    @Override
    public void updateBit( Object bit, boolean newValue ) {
        set( newValue );
    }

    public int getFloor() {
        return wrappedSensor.getFloor();
    }

    public BST getType() {
        return wrappedSensor.getType();
    }

    @Override
    public String getName() {
        return wrappedSensor.getName();
    }
    
    
}
