package elevatorparts;

import bitfactoryexample.ButtonSensor;
import java.util.List;

/**
 *
 * @author Pieter van den Hombergh {@code <p.vandenhombergh@fontys.nl>}
 */
public interface FloorIndicator {
    void setFloorsensors(List<ButtonSensor> sensors);
}
