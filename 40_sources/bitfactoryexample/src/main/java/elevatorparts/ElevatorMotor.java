package elevatorparts;

/**
 *
 * @author Pieter van den Hombergh {@code <p.vandenhombergh@fontys.nl>}
 */
public interface ElevatorMotor {

    void move(Direction dir);
    Direction getDirection();
}
