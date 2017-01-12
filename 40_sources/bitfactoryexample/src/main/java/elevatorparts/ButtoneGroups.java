
package elevatorparts;

import bitfactoryexample.BST;
import bitfactoryexample.ButtonSensor;
import java.util.List;

/**
 *
 * @author Pieter van den Hombergh {@code <p.vandenhombergh@fontys.nl>}
 */
public interface ButtoneGroups {
    int floorCount();
    List<ButtonSensor> getByType(BST type);
}
