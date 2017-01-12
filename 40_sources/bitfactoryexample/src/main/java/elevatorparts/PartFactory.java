/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package elevatorparts;

import bitfactoryexample.BST;
import bitfactoryexample.ButtonSensor;
import java.util.Iterator;

/**
 *
 * @author Pieter van den Hombergh {@code <p.vandenhombergh@fontys.nl>}
 */
public interface PartFactory {

    int floorcount();
    DoorMotor createDoorMotor();

    FloorIndicator createFloorIndicator(ElevatorMotor m);

    ElevatorMotor createElevatorMotor();
    
    Iterator<ButtonSensor> byType(BST type);
}
