package nl.fontys.sevenlo.fxample;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Properties;
import nl.fontys.sevenlo.hwio.AbstractBitFactory;
import nl.fontys.sevenlo.hwio.Bit;
import nl.fontys.sevenlo.hwio.BitAggregate;
import nl.fontys.sevenlo.hwio.InBit;
import nl.fontys.sevenlo.hwio.OutBit;
import static nl.fontys.sevenlo.utils.ResourceUtils.*;

/**
 * Factory implementation. This factory contains a connection to its output
 * bits.
 *
 * @author Pieter van den Hombergh {@code <p.vandenhombergh@fontys.nl>}
 */
class ElevatorBitFactory implements AbstractBitFactory<Integer> {

    protected final Properties props;
    private final EnumMap<OT, ArrayList<OutBit>> outMap;
    private final EnumMap<BST, ArrayList<InBit>> inMap;
    private final int inputMask;

    /**
     * Create factory with properties.
     *
     * @param props to be used in factory algorithm.
     */
    ElevatorBitFactory( Properties props ) {
        this.inputMask = getInputMaskFromProperties( props, 32 );
        this.props = props;
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
     * Create a bit according the properties given in factory constructor and
     * depending on bit
     * number.
     *
     * @param bag   bit aggregate to use for bits
     * @param bitNr the number
     *
     * @return a bit with no listeners attached.
     */
    public Bit createBit( BitAggregate<Integer> bag, int bitNr ) {
        if ( ( ( 1 << bitNr ) & getInputMask() ) != 0 ) {
            return createInputBit( bag, bitNr );
        } else {
            return createOutputBit( bag, bitNr );

        }
    }

    /**
     * Get the used input mask.
     * @return the mask.
     */
    protected int getInputMask() {
        return this.inputMask;
    }

    class BitBuilder {

        final int bitNr;
        final int nrInRole;
        final String pinName;
        final String bitdescription;
        final String[] bitparts;
        final String io;
        final String role;
        BitAggregate<Integer> bag;

        BitBuilder( BitAggregate<Integer> aBag, int bitNr ) {
            this.bag = aBag;
            this.bitNr = bitNr;
            pinName = "pin" + bitNr;
            bitdescription = props.getProperty( pinName );
            bitparts = bitdescription.split( "," );
            io = bitparts[ 0 ];
            role = bitparts[ 1 ];
            if ( bitparts.length > 2 ) {
                nrInRole = Integer.parseInt( bitparts[ 2 ] );
            } else {
                nrInRole = 0;
            }
        }

        Bit buildInputBit() {
            Bit result;
            BST t = BST.valueOf( BST.class, role.toUpperCase() );
            ButtonSensor ibit = new ButtonSensor( bitNr, t, nrInRole );
            ibit.addListener( ( Object bit, boolean newValue ) -> System.out.println( bit
                    .toString() ) );
            result = ibit;
            System.out.println( "created inbit " + ibit + " on " + bag );
            this.bag.connect( result );
            result.addListener( ( obs, newValue ) -> System.out.println( obs.toString() ) );
            inMap.get( t ).add( ibit );
            return result;
        }

        Bit buildOutputBit() {
            Bit result;
            // outputswitch ( io ) {
            //case "out":
            OT type = OT.valueOf( role.toUpperCase() );
            OutBit obit = new ElevatorOutBit( bag, bitNr, type, nrInRole );
            outMap.get( type ).add( nrInRole, obit );
            result = obit;
            System.out.println( "created outbit " + obit + " on " + bag );
            return result;
        }

        String buildName() {

            return role.toUpperCase() + " " + nrInRole;
        }
    }

    @Override
    public Bit createInputBit( BitAggregate bag, int bitNr ) {
        Bit result = new BitBuilder( bag, bitNr ).buildInputBit();
        return result;
    }

    @Override
    public Bit createOutputBit( BitAggregate bag, int bitNr ) {
        Bit result = new BitBuilder( bag, bitNr ).buildOutputBit();
        return result;
    }

    public List<InBit> getInbitsByType( BST type ) {
        return inMap.get( type );
    }

    public List<OutBit> getOutbitsByType( OT type ) {
        return outMap.get( type );
    }

    static class InvertingFactory extends ElevatorBitFactory {

        private final BitAggregate<Integer> bag;

        InvertingFactory( BitAggregate<Integer> bag, Properties props ) {
            super( props );
            this.bag = bag;
        }

        @Override
        protected int getInputMask() {
            return bag.getInputMask();
        }

        @Override
        public Bit createOutputBit( BitAggregate aBag, int bitNr ) {
            final String name = new BitBuilder( aBag, bitNr ).buildName();
            return new OutBit( aBag, bitNr ) {
                @Override
                public String getName() {
                    return name;
                }
            };
        }

        @Override
        public Bit createInputBit( BitAggregate aBag, int bitNr ) {
            final String name = new BitBuilder( aBag, bitNr ).buildName();
            Bit result = new InBit( bitNr ) {
                @Override
                public String getName() {
                    return name;
                }

            };
            result.addListener( ( obs, newValue ) -> System.out.println( obs.toString() ) );

            aBag.connect( result );
            return result;
        }
    }
}
