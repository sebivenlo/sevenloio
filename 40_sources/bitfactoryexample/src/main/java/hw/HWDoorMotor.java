/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hw;

import elevatorparts.DoorDirection;
import elevatorparts.DoorMotor;
import nl.fontys.sevenlo.hwio.BitGroup;

/**
 *
 * @author Pieter van den Hombergh {@code <p.vandenhombergh@fontys.nl>}
 */
public class HWDoorMotor implements DoorMotor {
    final BitGroup bits;
    private DoorDirection direction;
    public HWDoorMotor( BitGroup bits ) {
        this.bits = bits;
    }

    @Override
    public void move( DoorDirection cmd ) {
        direction=cmd;
        bits.set( direction.getBitPattern());
    }

    @Override
    public DoorDirection getDirection() {
        return direction;
   }

}
