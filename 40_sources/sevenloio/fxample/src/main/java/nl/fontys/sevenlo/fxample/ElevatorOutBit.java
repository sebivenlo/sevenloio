/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package nl.fontys.sevenlo.fxample;

import nl.fontys.sevenlo.hwio.OutBit;
import nl.fontys.sevenlo.hwio.Output;

/**
 * Outbit with type and number.
 *
 * @author hom
 */
public class ElevatorOutBit extends OutBit {

    private final int floor;
    private final OT type;

    /**
     * Create an identifiable outputbit.
     *
     * @param port  for the bit
     * @param bitNr position in port
     * @param type  role in elevator app
     * @param floor of the building
     */
    public ElevatorOutBit( Output port, int bitNr, OT type, int floor ) {
        super( port, bitNr );
        this.floor = floor;
        this.type = type;
    }

    /**
     * Get floor number.
     *
     * @return floor number.
     */
    public int getFloor() {
        return floor;
    }

    /**
     * Get type.
     *
     * @return outbit type.
     */
    public OT getType() {
        return type;
    }

    /**
     * some string representation.
     *
     * @return the string.
     */
    @Override
    public String toString() {
        return type.toString() + floor + " " + ( isSet() ? "active" : "inactive" );
    }

    @Override
    public String getName() {
        return type.toString() + ' ' + floor;//super.getName(); //To change body of generated methods, choose Tools | Templates.
    }

}
