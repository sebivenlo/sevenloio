package elevatorparts;

/**
 *
 * @author Pieter van den Hombergh {@code <p.vandenhombergh@fontys.nl>}
 */
public interface DoorMotor {
    
    void move(DoorDirection cmd);
    DoorDirection getDirection();
}
