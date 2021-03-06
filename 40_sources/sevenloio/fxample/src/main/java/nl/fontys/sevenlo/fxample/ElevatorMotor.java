package nl.fontys.sevenlo.fxample;

import nl.fontys.sevenlo.hwio.BitGroup;

/**
 *
 * @author Pieter van den Hombergh {@code <p.vandenhombergh@fontys.nl>}
 */
public class ElevatorMotor {

    enum Command {
        STOP( 0b00 ), DOWN( 0b01 ), UP( 0b10 );
        final int bitPattern;

        Command( int bitPattern ) {
            this.bitPattern = bitPattern;
        }
    };
    final BitGroup bg;

    /**
     * Create a motor using a bit group.
     * @param bg bit group to use
     */
    ElevatorMotor( BitGroup bg ) {
        this.bg = bg;
    }

    void go( ElevatorMotor.Command dir ) {
        bg.set( dir.bitPattern );
        System.out.println( "go " + dir + " on " + bg + " pat " + dir.bitPattern );
    }

}
