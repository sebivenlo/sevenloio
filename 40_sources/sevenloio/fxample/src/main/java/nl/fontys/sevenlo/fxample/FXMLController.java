package nl.fontys.sevenlo.fxample;

import java.net.URL;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.function.Function;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import static nl.fontys.sevenlo.fxample.BST.ALARMBUTTON;
import static nl.fontys.sevenlo.fxample.BST.DOORCLOSEBUTTON;
import static nl.fontys.sevenlo.fxample.BST.DOOROPENBUTTON;
import static nl.fontys.sevenlo.fxample.ElevatorMotor.Command.DOWN;
import static nl.fontys.sevenlo.fxample.ElevatorMotor.Command.STOP;
import static nl.fontys.sevenlo.fxample.ElevatorMotor.Command.UP;
import nl.fontys.sevenlo.hwio.BitAggregate;
import nl.fontys.sevenlo.hwio.BitGroup;
import nl.fontys.sevenlo.hwio.FXPoller;
import nl.fontys.sevenlo.hwio.PollThreads;
import nl.fontys.sevenlo.hwio.Poller;
import nl.fontys.sevenlo.hwio.IOInverter;
import nl.fontys.sevenlo.hwio.InBit;
import nl.fontys.sevenlo.hwio.OutBitGroup;
import nl.fontys.sevenlo.iowarrior.IOWarrior;
import nl.fontys.sevenlo.iowarrior.IOWarriorConnector;
import nl.fontys.sevenlo.utils.ResourceUtils;

/**
 * enumerate the warriors.
 *
 * for each warrior create and connect input bits.
 * for each inputbit create led
 * a led contains its inbit
 *
 * for each outbit create button
 * the button contains its outbit
 *
 *
 * Each bit is either input our output.
 *
 * @author Pieter van den Hombergh {@code <p.vandenhombergh@fontys.nl>}
 */
public class FXMLController implements Initializable {

    @FXML
    AnchorPane root;

    @Override
    public void initialize( URL url, ResourceBundle rb ) {
        HBox hbox = new HBox();
        root.getChildren().add( hbox );
        Properties prop = new Properties();
        prop = ResourceUtils.loadPropertiesFormFile( prop,
                "elevator.properties" );
        final int inputMask = ResourceUtils.getInputMaskFromProperties( prop, 32 );
        IOWarriorConnector iowc = IOWarriorConnector.getInstance();
        int warriorCount = iowc.getWarriorCount();
        System.out.println( "warriorCount = " + warriorCount );
        int hcount;
        ElevatorBitFactory[] bitFactory;
        Function<Integer, BitAggregate<Integer>> bags;
        if ( warriorCount > 0 ) {
            hcount = warriorCount;
        } else {
            hcount = 1;
        }
        bitFactory = createFactories( hcount, prop );
        IOInverter[] ioi;
        if ( warriorCount > 0 ) {
            bags = ( w ) -> {
                long handle = iowc.getHandle( w );
                IOWarrior iow = new IOWarrior( handle, inputMask );
                return iow;
            };
        } else {
            ioi = new IOInverter[ hcount ];
            for ( int z = 0; z < hcount; z++ ) {
                ioi[ z ] = createMockIO( inputMask, bitFactory, prop, hbox );
            }
            bags = ( w ) -> ioi[ w ].getInside();
        }

        BitAggregate<Integer>[] hardware = new BitAggregate[ hcount ];
        connectAndStart( hcount, bags, hardware, bitFactory, hbox );
        buildSimpleElevator( warriorCount, bitFactory );
    }

    void buildSimpleElevator( int warriorCount, ElevatorBitFactory[] bitFactory ) {
        if ( warriorCount > 0 ) {
            for ( int w = 0; w < warriorCount; w++ ) {
                final ElevatorPartsFactory pfac = new ElevatorPartsFactory(bitFactory[w] );
                final ElevatorMotor m = pfac.getElevatorMotor();
                ElevatorBitFactory f = bitFactory[ w ];
                connectButton(f, m, DOORCLOSEBUTTON, UP);
                connectButton(f, m, DOOROPENBUTTON, DOWN);
                connectButton(f, m, ALARMBUTTON, STOP);
            }
        }
    }

    void connectButton( ElevatorBitFactory f, final ElevatorMotor m, BST buttonType, ElevatorMotor.Command cmd ) {
        InBit doorCloseBtn
                = f.getInbitsByType( buttonType ).get( 0 );
        doorCloseBtn.addListener( ( bo, v ) -> m.go( cmd ) );
    }

    void connectAndStart( int hcount, Function<Integer, BitAggregate<Integer>> bags, BitAggregate<Integer>[] hardware, ElevatorBitFactory[] bitFactory, HBox hbox ) {
        for ( int w = 0; w < hcount; w++ ) {
            BitAggregate bag = bags.apply( w );
            hardware[ w ] = bag;
            ControlPanel panel = new ControlPanel( bag, w, bitFactory[ w ] );
            hbox.getChildren().add( panel );
            Poller poller = new FXPoller( bag );
            Thread pollThread = PollThreads.createPollThread( poller );
            pollThread.start();
        }
        //start( hardware );
    }

    ElevatorBitFactory[] createFactories( int hcount, Properties prop ) {
        ElevatorBitFactory[] bitFactory;
        bitFactory = new ElevatorBitFactory[ hcount ];
        for ( int w = 0; w < hcount; w++ ) {
            bitFactory[ w ] = new ElevatorBitFactory( prop );
        }
        return bitFactory;
    }

    private static IOInverter createMockIO( final int inputMask, ElevatorBitFactory[] bitFactory,
            Properties prop, HBox hbox ) {
        IOInverter ioi = new IOInverter( inputMask );
        BitAggregate<Integer> outside = ioi.getOutside(); // elevator

        ControlPanel panel = new ControlPanel( outside, 0,
                new ElevatorBitFactory.InvertingFactory( outside, prop ) );
        panel.getStyleClass().add( "fake" );
        hbox.getChildren().add( panel );
        Poller poller = new FXPoller( outside );
        Thread pollThread = PollThreads.createPollThread( poller );
        pollThread.start();
        return ioi;
    }

    /**
     * Method to start the hardware polling loop.
     *
     * @param hardware the hardware to poll.
     */
    private void start( BitAggregate<Integer>[] hardware ) {
        for ( BitAggregate<Integer> hw : hardware ) {
            Poller poller = new FXPoller( hw );
            Thread pollThread = PollThreads.createPollThread( poller );
            pollThread.start();
        }
    }
}
