
package elevatorparts;

import javafx.beans.property.BooleanProperty;

/**
 *
 * @author Pieter van den Hombergh {@code <p.vandenhombergh@fontys.nl>}
 */
public interface Cage {
    int floorCount();
    void motorCmd(Direction dir);
    BooleanProperty floorReached(int f);
}
