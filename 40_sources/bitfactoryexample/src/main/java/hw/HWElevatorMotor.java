package hw;

import elevatorparts.Direction;
import elevatorparts.ElevatorMotor;
import nl.fontys.sevenlo.hwio.BitGroup;

/**
 *
 * @author Pieter van den Hombergh {@code <p.vandenhombergh@fontys.nl>}
 */
public class HWElevatorMotor implements ElevatorMotor {

    final BitGroup bits;
    private Direction direction;

    public HWElevatorMotor( BitGroup bits ) {
        this.bits = bits;
        move( Direction.STOP );
    }

    @Override
    public final void move( Direction dir ) {
        this.direction= dir;
        bits.set( dir.getBitPattern());
    }

    @Override
    public Direction getDirection() {
        return direction;
    }

}
