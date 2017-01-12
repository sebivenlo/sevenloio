package elevatorparts;

import javafx.beans.property.BooleanProperty;

/**
 *
 * @author Pieter van den Hombergh {@code <p.vandenhombergh@fontys.nl>}
 */
public interface Door {
    void doorOpenCmd();
    void doorCloseCmd();
    BooleanProperty closedProperty();
    BooleanProperty openedProperty();
    BooleanProperty obstryctedProperty();
}
