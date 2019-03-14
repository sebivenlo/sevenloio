package nl.fontys.sevenlo.hwio;

import javafx.application.Platform;

/**
 * FXPoller reads from the intReader and does bit setting and resulting listener
 * processing on the java FX GUI thread.
 *
 * FXPoller reads the input on the poller thread provided by in IOWarrior class.
 * This input is then inspected on differences. If there are differences the new
 * and old values are put into a runnable which is run on the FX event handling
 * thread.
 *
 * Assumptions are that the BitOps array 's content is not changed, so no
 * defensive copy is made.
 *
 * @author Pieter van den Hombergh P dot vandenHombergh at fontys dot nl
 */
public class FXPoller implements Poller {

    //private final Input reader;
    //@GuardedBy("this");
    private volatile int lastValue = 0;
    private final BitAggregate<Integer> bitA;

    /**
     * Create the poller for an int reader.
     *
     * @param b bit aggregate
     */
    public FXPoller( BitAggregate<Integer> b ) {
        if ( null == b ) {
            throw new NullPointerException( "Real aggregates please" );
        }
        lastValue = 0;
        bitA = b;
        System.out.println( "created poller for " + bitA );
    }

    /**
     * To be called on the poll Thread.
     */
    @Override
    public void pollOnce() {
        // blocks in iowarrior until new value
        int newValue = bitA.read() & bitA.getInputMask();

        Runnable myTask = null;
        synchronized ( this ) {
            if ( newValue != lastValue ) {
                myTask = new UpdateTask( bitA, lastValue, newValue );
                lastValue = newValue;
            }
        }
        if ( myTask != null ) {
           // System.out.println( "submittted " + Integer.toHexString( newValue ) + " for " + bitA );
            Platform.runLater( myTask );
        }
    }

    /**
     * Task to do the listener updates (bit[].set(b)) on the swing UI thread.
     */
    // @Immutable
    public static class UpdateTask implements Runnable {

        private final int oldValue, newValue;
        private final BitAggregate<Integer> bitA;

        /**
         * Update the listeners using comparison between old and new value.
         *
         * @param bs bit aggregate
         * @param ov old value
         * @param nv new value
         */
        public UpdateTask( final BitAggregate<Integer> bs, int ov, int nv ) {
            if ( bs == null ) {
                throw new NullPointerException( "Null aggregate invalid" );
            }
            bitA = bs;
            oldValue = ov;
            newValue = nv;
        }

        /**
         * The run does the work and is invoked on the swing event thread.
         */
        @Override
        public void run() {
            BitUpdater.updateIntegerBits( bitA, oldValue, newValue );
        }
    }
}
