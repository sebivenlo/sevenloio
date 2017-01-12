package nl.fontys.sevenlo.hwio;

import javafx.beans.property.SimpleBooleanProperty;

/**
 *
 * @author Pieter van den Hombergh {@code <p.vandenhombergh@fontys.nl>}
 */
public class InBitBooleanProperty extends SimpleBooleanProperty implements BitOps {

    @Override
    public boolean isSet() {
        return get();
    }
}
