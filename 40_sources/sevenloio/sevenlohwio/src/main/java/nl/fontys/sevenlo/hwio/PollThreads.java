package nl.fontys.sevenlo.hwio;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A PollThread can be used to read data from a device with a blocking read
 * operation.
 *
 * Typical application is to use one poll thread per blocking device. For reads
 * that do not block such as reading from a 'real' parallel port, more port
 * reads can share the same poll thread.
 *
 * @author Pieter van den Hombergh (p.vandenhombergh@fontys.nl)
 */
public final class PollThreads {

    private PollThreads() {
    }
    /**
     * PollThreads are numbered for identification.
     */
    private static int pollThreadId = 0;
    /**
     * Collection of PollThreads created.
     */
    private static ArrayList<Thread> pollThreads = new ArrayList<Thread>();

    /**
     * create a pollThread. This thread depends on the blocking property of the
     * read operation of the IOWarrior. This ensures that a read only commences
     * after a bit change in the inputs.
     *
     * @param poller the poller to invoke on the pollThread loop.
     * @return a new PollThread for the reader.
     */
    public static Thread createPollThread( final Poller poller ) {
        // create poller thread
        Thread pThread = new Thread( "PollThread" + pollThreadId++ ) {

            private volatile int counter = 0;

            @Override
            public void run() {
                while ( !Thread.interrupted() ) {
                    poller.pollOnce();
                    counter++;
                }
            }

            @Override
            public void start() {
                super.start();
                System.out.println( "PollThread " + getName() + " started" );
            }
        };
        pollThreads.add( pThread );
        return pThread;
    }

    /**
     * Closed and free device.
     */
    private void close() {
        for ( Thread t : pollThreads ) {
            try {
                t.interrupt();
                t.join();
            } catch ( InterruptedException ex ) {
                Logger.getLogger( PollThreads.class
                        .getName() ).log( Level.SEVERE, null, ex );
            }
        }
    }
}
