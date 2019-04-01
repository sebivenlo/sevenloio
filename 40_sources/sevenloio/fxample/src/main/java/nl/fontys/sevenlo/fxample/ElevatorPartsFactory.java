package nl.fontys.sevenlo.fxample;

import nl.fontys.sevenlo.hwio.BitGroup;
import nl.fontys.sevenlo.hwio.OutBit;
import nl.fontys.sevenlo.hwio.OutBitGroup;

/**
 *
 * @author Pieter van den Hombergh {@code <p.vandenhombergh@fontys.nl>}
 */
public class ElevatorPartsFactory {

    final ElevatorBitFactory bfac;

    ElevatorPartsFactory( ElevatorBitFactory bfac ) {
        this.bfac = bfac;
    }

    private ElevatorMotor em = null;

    ElevatorMotor getElevatorMotor() {
        if ( em == null ) {
            OutBit mup = bfac.getOutbitsByType( OT.MOTORUP ).get( 0 );
            OutBit mdown = bfac.getOutbitsByType( OT.MOTORDOWN ).get( 0 );
            BitGroup bg = OutBitGroup.from( mdown, mup );
            em = new ElevatorMotor( bg );
        }
        return em;
    }
}
