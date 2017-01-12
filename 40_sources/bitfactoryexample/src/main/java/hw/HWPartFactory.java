package hw;

import bitfactoryexample.BST;
import bitfactoryexample.ButtonSensor;
import bitfactoryexample.ElevatorBitFactory;
import elevatorparts.DoorMotor;
import elevatorparts.ElevatorMotor;
import elevatorparts.FloorIndicator;
import elevatorparts.PartFactory;
import java.util.Iterator;
import java.util.Objects;
import nl.fontys.sevenlo.hwio.BitGroup;

/**
 *
 * @author Pieter van den Hombergh {@code <p.vandenhombergh@fontys.nl>}
 */
public class HWPartFactory implements PartFactory {

    final ElevatorBitFactory bf;
    @Override
    public int floorcount() {
        return 4;
    }

    public HWPartFactory( ElevatorBitFactory bf ) {
        this.bf = bf;
    }

    @Override
    public DoorMotor createDoorMotor() {
        return new HWDoorMotor( bf.createDoorMotorBitGroup() );
    }

    @Override
    public FloorIndicator createFloorIndicator( ElevatorMotor m ) {
        Objects.requireNonNull( m );
        BitGroup bits = bf.createIndicatorBitGroup();
        return new HWFloorIndicator( bits, m );
    }

    @Override
    public ElevatorMotor createElevatorMotor() {
        return new HWElevatorMotor(bf.createMotorBitGroup());
    }

    @Override
    public Iterator<ButtonSensor> byType( BST type ) {
        return bf.byType( type );
    }
}
