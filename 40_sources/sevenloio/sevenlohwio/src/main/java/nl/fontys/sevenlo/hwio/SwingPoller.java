package nl.fontys.sevenlo.hwio;

import javax.swing.SwingUtilities;

/**
 * SwingPoller reads from the intReader and does bit setting and resulting
 * listener processing on the swing gui thread.
 *
 * SwingPoller reads the input on the poller thread provided by in IOWarrior
 * class. This input is then inspected on differences. If there are differences
 * the new and old values are put into a runnable which is run on the Swing
 * event handling thread.
 *
 * Assumptions are that the BitOps array's content is not changed, so no
 * defensive copy is made.
 *
 * @author Pieter van den Hombergh P dot vandenHombergh at fontys dot nl
 */
public class SwingPoller implements Poller {

    //private final Input reader;
    //@GuardedBy("this");
    private volatile int lastValue = 0;
    private final BitAggregate<Integer> bitA;

    /**
     * Create the poller for an int reader.
     *
     * @param b bit aggregate
     */
    public SwingPoller( BitAggregate b ) {
        if ( null == b ) {
            throw new NullPointerException( "Real aggregates please" );
        }
        lastValue = 0;
        bitA = b;
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
            SwingUtilities.invokeLater( myTask );
        }
    }

    /**
     * Task to do the listener updates (bit[].set(b)) onn the swing ui thread.
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
