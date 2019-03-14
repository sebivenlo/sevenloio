package bitfactoryexample;

import static bitfactoryexample.BST.*;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import nl.fontys.sevenlo.hwio.AbstractBitFactory;
import nl.fontys.sevenlo.hwio.Bit;
import nl.fontys.sevenlo.hwio.BitGroup;
import nl.fontys.sevenlo.hwio.BitListener;
import nl.fontys.sevenlo.hwio.Input;
import nl.fontys.sevenlo.hwio.NonContiguousOutBitGroup;
import nl.fontys.sevenlo.hwio.OutBit;
import nl.fontys.sevenlo.hwio.Output;

/**
 * Factory example. Shows how input bits can be created of a subtype and
 * connected to a listener.
 *
 * This version connects the listener to both input and outputs.
 *
 * @author hom
 */
public class ElevatorBitFactory implements AbstractBitFactory<Number>, BitListener<Bit> {

    private Properties props;
    private final int pinCount;
    /**
     * Storage for the created outbits.
     */
    private final EnumMap<OT, ArrayList<ElevatorOutBit>> outMap;
    /**
     * Storage for the created inbits.
     */
    private final EnumMap<BST, ArrayList<ButtonSensor>> inMap;

    /**
     * At construction time, pass the listener to all input bits.
     *
     * @param props Property description of io.
     */
    public ElevatorBitFactory( Properties props ) {
        this.props = props;
        String pinCountS = props.getProperty( "pinCount", "0" );
        this.pinCount = Integer.parseInt( pinCountS );
        System.out.println( "found pinCount " + pinCount );
        outMap = new EnumMap<>( OT.class );
        inMap = new EnumMap<>( BST.class );
        for ( OT ot : OT.values() ) {
            outMap.put( ot, new ArrayList<>() );
        }
        for ( BST bst : BST.values() ) {
            inMap.put( bst, new ArrayList<>() );
        }
    }

    /**
     * See AbstractBitFactory doc. Creates the input bits for the elevator.
     *
     * @param in    input port
     * @param bitNr bit number
     *
     * @return the Bit
     */
    @Override
    public Bit createInputBit( Input in, int bitNr ) {
        String pinName = "pin" + bitNr;
        String bitdescription = props.getProperty( pinName );
        String[] bitparts = bitdescription.split( "," );
        String io = bitparts[ 0 ];
        String role = bitparts[ 1 ];
        int nrInRole = 0;
        if ( bitparts.length > 2 ) {
            nrInRole = Integer.parseInt( bitparts[ 2 ] );
        }
        BST type = BST.valueOf( BST.class, role.toUpperCase() );
        ButtonSensor bit = new ButtonSensor( bitNr, type, nrInRole );
        inMap.get( type ).add( bit );
        bit.addListener( this );
        return bit;
    }

    /**
     * Creates output bits for the elevator.
     *
     * @param port  output
     * @param bitNr bit number.
     *
     * @return the created Bit
     *
     */
    @Override
    public final Bit createOutputBit( Output port, int bitNr ) {
        String pinName = "pin" + bitNr;
        String bitdescription = props.getProperty( pinName );
        String[] bitparts = bitdescription.split( "," );
        String io = bitparts[ 0 ];
        String role = bitparts[ 1 ];
        int nrInRole = 0;
        if ( bitparts.length > 2 ) {
            nrInRole = Integer.parseInt( bitparts[ 2 ] );
        }
        OT type = OT.valueOf( role.toUpperCase() );
        ElevatorOutBit bit = new ElevatorOutBit( port, bitNr, type, nrInRole );
        outMap.get( type ).add( bit );
        bit.addListener( this );
        return bit;
    }

    /**
     * Two bits for motor.
     *
     *
     * @return the motor bit group.
     */
    public BitGroup createMotorBitGroup() {
        return NonContiguousOutBitGroup.createFromBits( getOutBit( OT.MOTORDOWN ), getOutBit( OT.MOTORUP ) );
    }

    /**
     * Two bits for door motor.
     *
     * @return the motor bit group.
     */
    public BitGroup createDoorMotorBitGroup() {
        ElevatorOutBit b1 = getOutBit( OT.DOORMOTORCLOSE );
        ElevatorOutBit b2 = getOutBit( OT.DOORMOTOROPEN );
        return NonContiguousOutBitGroup.createFromBits( b1, b2 );
    }

    /**
     * Four bits for indicator bits.
     *
     * @return bit group
     */
    public BitGroup createIndicatorBitGroup() {
        OutBit[] bits = outMap.get( OT.FLOORINDICATOR ).toArray( new OutBit[ 0 ] );
        return NonContiguousOutBitGroup.createFromBits( bits );
    }

    private int findOutputByName( String name ) {
        int result = -1;
        for ( int i = 0; i < this.pinCount; i++ ) {
            String propName = "pin" + i;
            if ( null != this.props.getProperty( propName ) ) {
                result = i;
                break;
            }
        }
        return result;
    }

    /**
     * Get OutBit by type and number (floor).
     *
     * @param type   the OT type
     * @param number the floor number
     *
     * @return the requested outbit
     *
     * @throws ArrayIndexOutOfBoundsException when bit not available.
     */
    public ElevatorOutBit getOutBit( OT type, int number ) {
        return outMap.get( type ).get( number );
    }

    /**
     * Get OutBit of Type at index 0.
     *
     * @param type of the bit
     *
     * @return the requested bit.
     */
    public ElevatorOutBit getOutBit( OT type ) {
        return getOutBit( type, 0 );
    }

    public Iterator<ButtonSensor> byType( BST type ) {
        return inMap.get( type ).iterator();
    }

    /**
     * <<<<<<< Get the input bits by type. @param bt button sensor type
     *
     * @r
     *
     * eturn the list of all the button sensors created by this class.
     */
    public List<ButtonSensor> getBitListByType( BST bt ) {
        return inMap.get( bt );
    }

    void onUpCall( Object bit, boolean newValue ) {

    }

    void onDownCall( Object bit, boolean newValue ) {

    }

    void onTargetCall( Object bit, boolean newValue ) {

    }

    void onFloorSensor( Object bit, boolean newValue ) {

    }

    public void demoConnect() {
        this.getBitListByType( UPBUTTON ).forEach( ( b ) -> {
            b.addListener( this::onUpCall );
        } );
        for ( ButtonSensor b : this.getBitListByType( DOWNBUTTON ) ) {
            b.addListener( this::onDownCall );
        }
        for ( ButtonSensor b : this.getBitListByType( TARGETBUTTON ) ) {
            b.addListener( this::onTargetCall );
        }
        for ( ButtonSensor b : this.getBitListByType( FLOORSENSOR ) ) {
            b.addListener( this::onFloorSensor );
        }
    }

    /**
     * Get InBit by type and number (floor).
     *
     * @param type   the OT type
     * @param number the floor number
     *
     * @return the requested inbit
     *
     * @throws ArrayIndexOutOfBoundsException when bit not available.
     */
    public ButtonSensor getInBit( BST type, int number ) {
        return inMap.get( type ).get( number );
    }

    /**
     * Get InBit of Type at index 0.
     *
     * @param type of the bit
     *
     * @return the requested bit.
     */
    public ButtonSensor getInBit( BST type ) {
        return getInBit( type, 0 );
    }

    /**
     * Update the bit to newvalue.
     *
     * @param bit      the bit to update.
     * @param newValue the news.
     */
    @Override
    public void updateBit( Object bit, boolean newValue ) {
        System.out.println( "updater " + bit + " = " + newValue );
        OutBit ob = null;

        if ( bit instanceof ButtonSensor ) {
            ButtonSensor bs = ( ButtonSensor ) bit;
            int floor = bs.getFloor();
            switch ( bs.getType() ) {
                case DOOROPENBUTTON:
                    System.out.println( "open button" );
                    ob = getOutBit( OT.DOORMOTOROPEN );
                    System.out.println( "outbit=" + ob );
                    break;
                case DOORCLOSEBUTTON:
                    ob = getOutBit( OT.DOORMOTORCLOSE );
                    break;
                case TARGETBUTTON:
                    ob = getOutBit( OT.FLOORINDICATOR, floor );
                    break;
                case NURSEBUTTON:
                    ob = getOutBit( OT.MOTORUP );
                    break;
                case OBSTRUCTIONSENSOR:
                    ob = getOutBit( OT.MOTORDOWN );
                    break;
                case UPBUTTON:
                    ob = getOutBit( OT.UPLED );
                    break;
                case DOWNBUTTON:
                    ob = getOutBit( OT.DOWNLED );
                    break;
                case FLOORSENSOR:
                    OutBit md = getOutBit( OT.MOTORDOWN );
                    OutBit mu = getOutBit( OT.MOTORUP );
                    if ( floor == 0 ) {
                        md.set( false );
                        mu.set( true );
                    } else if ( floor == 3 ) {
                        mu.set( false );
                        md.set( true );
                    }
                    break;
                default:
                    break;
            }
            if ( null != ob ) {
                System.out.println( "set output " + ob + " to " + newValue );
                ob.set( newValue );
            }
        }
    }

    @Override
    public int getInputMask() {
        throw new UnsupportedOperationException( "Not supported yet." ); //To change body of generated methods, choose Tools | Templates.
    }
}
