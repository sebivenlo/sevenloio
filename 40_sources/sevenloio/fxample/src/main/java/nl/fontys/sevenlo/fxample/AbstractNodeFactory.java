
package nl.fontys.sevenlo.fxample;

import javafx.scene.Node;
import nl.fontys.sevenlo.hwio.Bit;

/**
 *
 * @author Pieter van den Hombergh {@code <p.vandenhombergh@fontys.nl>}
 */
public interface AbstractNodeFactory {
    /**
     * Creates a JavaFX Node appropriate to the bit.
     * Typically a button is created for an output bit and a led label is created for an input.
     * @param bit connected to this node
     * @return the node
     */
    Node createNode(Bit bit);
    
}
