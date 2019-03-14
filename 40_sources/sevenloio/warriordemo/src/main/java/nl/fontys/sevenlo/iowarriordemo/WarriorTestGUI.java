package nl.fontys.sevenlo.iowarriordemo;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
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

/**
 * Test GUI for the IOWarrior. Accepts property files on the command line. These
 * properties are "attached" to the iowarrior(s). The io warriors are ordered in
 * ascending order by serial number, so the order of the property file on the
 * command line matters if more then one file is used.
 *
 * @author Pieter van den Hombergh (p.vandenhombergh@fontys.nl)
 */
public final class WarriorTestGUI {

    /**
     * Private default ctor to make checkstyle happy.
     */
    private WarriorTestGUI() {
    }

    /**
     * Startup.
     *
     * @param args command line arguments
     */
    public static void main( String[] args ) {
        int im = 0xFFFF;
        String propFilename;
        Properties[] properties;
        System.out.println( "args.length=" + args.length );
        if ( 0 < args.length ) {
            properties = new Properties[ args.length ];
            for ( int p = 0; p < args.length; p++ ) {
                // take first arg as prop file name
                propFilename = args[ p ];
                properties[ p ] = new Properties();
                // load properties file
                System.out
                        .println( "trying to load properties " + propFilename );
                properties[ p ] = ResourceUtils.loadPropertiesFormFile(
                        properties[ p ], propFilename );
            }
        } else {
            properties = new Properties[ 1 ];
            properties[ 0 ] = new Properties();
            propFilename = "WarriorTestGUI.properties";
            properties[ 0 ] = ResourceUtils.loadPropertiesFormFile(
                    properties[ 0 ], propFilename );
        }
        
        IOWarriorConnector iowc = IOWarriorConnector.getInstance();
        List<BitAggregate<Integer>> irw = null;
        String[] labelText;
        int warriorCount = iowc.getWarriorCount();
        System.out.println( "Warrior count " + warriorCount );
        int p = 0;
        int initX = 100, initY = 100;
        if ( 0 != warriorCount ) {
            labelText = new String[ warriorCount ];
            irw = new ArrayList<>( warriorCount );
            for ( int h = 0; h < warriorCount; h++ ) {
                
                long handle = iowc.getHandle( h );
                
                im = ResourceUtils.getInputMaskFromProperties( properties[ h ],
                        32 );
                System.out.println( "input mask=" + Integer.toHexString( im ) );
                BitAggregate<Integer> warrior = new IOWarrior( handle, im );
                irw.add( warrior );
                
                labelText[ h ] = "Real IO Warrior connected serial "
                        + IOWarriorConnector.getInstance().getSerialNr( handle )
                        + " io mask=" + im;
                IOGUIPanel g = new IOGUIPanel( labelText[ h ],
                        warrior, properties[ h ] );
                g.startTheShow();
                g.setLocation( initX, initY );
                initX += g.getWidth();

                // make sure that last properties are used for all remaining
                if ( p < properties.length - 1 ) {
                    p++;
                }
            }
        } else {
            IO rwSim = new ReaderWriterSim();
            irw = new ArrayList<>( 1 );
            labelText = new String[ 1 ];
            im = ResourceUtils.getInputMaskFromProperties( properties[ 0 ], 32 );
            BitAggregate<Integer> warrior = new SimpleBitAggregate( rwSim, im );
            labelText[ 0 ] = "No IO warrior, io is faked, inputmask="
                    + Integer.toHexString( im );
            System.out.println( "No warriors found using simulator" );
            IOGUIPanel g = new IOGUIPanel( labelText[ 0 ], warrior,
                    properties[ 0 ] );
            g.startTheShow();
        }
        for ( int h = 0; h < irw.size(); h++ ) {
            Poller poller = new SwingPoller( irw.get( h ) );
            Thread pollThread = PollThreads.createPollThread( poller );
            pollThread.start();
        }
    }

    /**
     * IO Mockup if no Warrior is present. Uses Java Monitor/semaphore to
     * coordinate writing to reading. Write enables
     *
     * @author Pieter van den Hombergh (p.vandenhombergh@fontys.nl)
     */
    private static class ReaderWriterSim implements IO {

        /**
         * Current value.
         */
        private int value = 0;
        /**
         * Data available flag.
         */
        private volatile boolean newData = false;
        /**
         * Synchronization object.
         */
        private final Object myLock = new Object();
        
        public ReaderWriterSim() {
        }
        
        @Override
        public int read() {
            System.out.println( "sim read try " );
            synchronized ( myLock ) {
                try {
                    while ( !newData ) {
                        myLock.wait();
                    }
                } catch ( InterruptedException ex ) {
                    Logger.getLogger( WarriorTestGUI.class.getName() ).
                            log( Level.SEVERE, null, ex );
                }
            }
            System.out.println( "sim read " + Integer.toHexString( value ) );
            int result = value;
            newData = false;
            return result;
        }
        
        private void write( int newValue ) {
            synchronized ( myLock ) {
                value = Integer.rotateLeft( newValue, 16 );
                newData = true;
                myLock.notifyAll();
            }
            System.out.println( "sim written " + Integer.toHexString( value ) );
        }
        
        @Override
        public void writeMasked( int mask, int newValue ) {
            synchronized ( myLock ) {
                write( ( value & ~mask ) | ( newValue & mask ) );
            }
        }
        
        @Override
        public int lastWritten() {
            return value;
        }
    }
    
}
