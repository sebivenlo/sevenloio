package bitfactoryexample;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import nl.fontys.sevenlo.hwio.BitAggregate;
import nl.fontys.sevenlo.hwio.IO;
import nl.fontys.sevenlo.hwio.PollThreads;
import nl.fontys.sevenlo.hwio.Poller;
import nl.fontys.sevenlo.hwio.SimpleBitAggregate;
import nl.fontys.sevenlo.hwio.SwingPoller;
import nl.fontys.sevenlo.iowarrior.IOWarrior;
import nl.fontys.sevenlo.iowarrior.IOWarriorConnector;
import nl.fontys.sevenlo.utils.ResourceUtils;
import nl.fontys.sevenlo.widgets.IOGUIPanel;
import nl.fontys.sevenlo.widgets.MockHardware;

/**
 * Main program and creator of the of Demo. A Creates the factory instances and
 * hands them to the warrior constructor, then starts the marriage ceremony by
 * invoking connect on the warrior.
 *
 * @author hom
 */
public class ElevatorMain {

    /**
     * Startup for elevator.
     *
     * @param args the command line arguments
     */
    @SuppressWarnings( "ResultOfObjectAllocationIgnored" )
    public static void main( String[] args ) {
        new ElevatorMain().start();
    }
    private final List<BitAggregate<Integer>> hardware; // warrior if available
    private final ElevatorBitFactory[] factory;

    /**
     * The startup class.
     */
    ElevatorMain() {

        Properties prop = new Properties();
        prop = ResourceUtils.loadPropertiesFormFile( prop,
                "elevator.properties" );
        int inputMask = ResourceUtils.getInputMaskFromProperties( prop, 32 );
        IOWarriorConnector iowc = IOWarriorConnector.getInstance();
        int warriorCount = iowc.getWarriorCount();
        if ( warriorCount > 0 ) {
            factory = new ElevatorBitFactory[ warriorCount ];
            hardware = new ArrayList<>( warriorCount );//BitAggregate<Integer>[])new BitAggregate[ warriorCount ];

            for ( int i = 0; i < warriorCount; i++ ) {
                factory[ i ] = new ElevatorBitFactory( prop );
                long handle = iowc.getHandle( i );
                hardware.add( new IOWarrior( handle, inputMask,
                        factory[ i ] ) );
                hardware.get( i ).connect();
                IOGUIPanel iowp = new IOGUIPanel( "Warrior "
                        + ( ( IOWarrior ) hardware.get( i ) ).getSerialNumber(),
                        hardware.get( i ),
                        prop );
                iowp.startTheShow();
            }
        } else {
            warriorCount = 2;
            hardware = new ArrayList<>( warriorCount );
            factory = new ElevatorBitFactory[ warriorCount ];

            for ( int i = 0; i < warriorCount; i++ ) {
                factory[ i ] = new ElevatorBitFactory( prop );
                MockHardware mock = new MockHardware( prop, factory[ i ],
                        inputMask );
                IO io = mock.getInsideAggregate();
                hardware.add( new SimpleBitAggregate( factory[ i ], io, io,
                        inputMask ) );
                hardware.get( i ).connect();
                mock.startMock();
                IOGUIPanel iowp = new IOGUIPanel( "Warrior", hardware.get( i ),
                        prop );
                iowp.startTheShow();
            }
        }
        for ( ElevatorBitFactory fact : factory) {
            fact.demoConnect();
        }
    }

    /**
     * Start method. Should be invoke to receive hardware events.
     */
    private void start() {
        for ( BitAggregate<Integer> hardware1 : hardware ) {
            Poller poller = new SwingPoller( hardware1 );
            Thread pollThread = PollThreads.createPollThread( poller );
            pollThread.start();
        }
    }
}
