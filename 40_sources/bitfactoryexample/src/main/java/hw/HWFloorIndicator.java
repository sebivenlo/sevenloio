package hw;

import bitfactoryexample.ButtonSensor;
import elevatorparts.ElevatorMotor;
import elevatorparts.FloorIndicator;
import java.util.List;
import nl.fontys.sevenlo.hwio.BitGroup;
import nl.fontys.sevenlo.hwio.BitListener;

/**
 *
 * @author Pieter van den Hombergh {@code <p.vandenhombergh@fontys.nl>}
 */
public class HWFloorIndicator implements FloorIndicator, BitListener {

    final BitGroup bits;
    int indicatorValue = 0;
    final ElevatorMotor motor;

    public HWFloorIndicator( BitGroup bits, ElevatorMotor m ) {
        this.bits = bits;
        this.motor = m;
    }

    @Override
    public void setFloorsensors( List<ButtonSensor> sensors ) {
        for ( ButtonSensor sensor : sensors ) {
            sensor.addListener( this );
        }
    }

    @Override
    public void updateBit( Object bit, boolean newValue ) {
        ButtonSensor sensor = ( ButtonSensor ) bit;
        if ( newValue ) {

            indicatorValue = 1 << sensor.getFloor();
        } else {
            switch ( motor.getDirection() ) {
                case UP:
                    indicatorValue |= indicatorValue << 1;
                    break;
                case DOWN:
                    indicatorValue |= indicatorValue >> 1;
                    break;
            }
        }
    }
}
