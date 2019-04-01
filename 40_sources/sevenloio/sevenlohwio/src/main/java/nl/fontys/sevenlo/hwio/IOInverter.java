package nl.fontys.sevenlo.hwio;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Inverter of IO bits: one sides input is the other output. This adapter class
 * is handy for testing to e.g. simulate IO with a separate GUI module.
 *
 * Normal usage: Create the IOInverter with the same input mask as you would for
 * the normal BitAggregate. Then take the insideAggregate with the appropriate
 * method to replace the normal BitAggregate in your application and take the
 * outside aggregate as the IO for your simulated hardware.
 *
 * @author Pieter van den Hombergh (p.vandenhombergh@fontys.nl)
 */
public class IOInverter implements BitAggregate<Integer> {

    private PassThroughAggregate insideAggregate;
    private PassThroughAggregate outsideAggregate;
    private final Object leftLock = new Object();
    private final Object rightLock = new Object();

    /**
     * Create Inverter with BitFactory.
     *
     * @param inputMask mask
     */
    public IOInverter( int inputMask ) {
        insideAggregate = new PassThroughAggregate( inputMask, true );
        outsideAggregate = new PassThroughAggregate( ~inputMask, false );
        insideAggregate.setOther( outsideAggregate );
        outsideAggregate.setOther( insideAggregate );
        insideAggregate.setPoller( new FXPoller( outsideAggregate ) );
        outsideAggregate.setPoller( new FXPoller( insideAggregate ) );
    }

    @Override
    public BitOps getBit( int i ) {
        return insideAggregate.getBit( i );
    }

    @Override
    public int size() {
        return insideAggregate.size();
    }

    @Override
    public Integer getInputMask() {
        return insideAggregate.getInputMask();
    }

    @Override
    public Integer lastRead() {
        return insideAggregate.lastRead();
    }

    @Override
    public void connect() {
        insideAggregate.connect();
    }

    @Override
    public String toString() {
        return insideAggregate.toString();
    }

    @Override
    public void connect( Bit aBit ) {
        insideAggregate.connect( aBit );
    }

    @Override
    public void writeMasked( int mask, int value ) {
        insideAggregate.writeMasked( mask, value );
    }

    @Override
    public int read() {
        return insideAggregate.read();
    }

    @Override
    public int lastWritten() {
        return insideAggregate.lastWritten();
    }

    /**
     * Set the opposing aggregate.
     *
     * @param o the other
     */
    void setOther( PassThroughAggregate o ) {
        insideAggregate.setOther( o );
    }

    /**
     * @author Pieter van den Hombergh (p.vandenhombergh@fontys.nl)
     */
    private class PassThroughAggregate extends SimpleBitAggregate implements IO {

        private final Object myLock;
        private final Object otherLock;
        private PassThroughAggregate other;
        private int lastWritten = 0;
        private int lastRead = 0;
        private AbstractBitFactory fac;
        private FXPoller poller;

        /**
         * Create PassThroughAggregate with BitFactory.
         *
         * @param im inputMask
         * @param fac BitFactory
         */
        PassThroughAggregate( int im, boolean left ) {
            super( im );

            if ( left ) {
                myLock = leftLock;
                otherLock = rightLock;

            } else {
                myLock = rightLock;
                otherLock = leftLock;

            }
        }

        /**
         * Sets the poller.
         * @param aPoller to set
         */
        void setPoller( FXPoller aPoller ) {
            this.poller = aPoller;
        }

        /**
         * Set connection to other.
         *
         * @param o other
         */

        public void setOther( PassThroughAggregate o ) {
            other = o;
        }

        /**
         * Write combination of mask and value.
         *
         * @param mask
         * @param value
         */
        @Override
        public void writeMasked( int mask, int value ) {
            lastWritten = ( lastWritten & ~mask ) | ( mask & value );
            //The field below is set before this lock is used.
            synchronized ( otherLock ) {
                otherLock.notifyAll();
            }
//            if ( null != poller ) {
//                Platform.runLater( () -> poller.pollOnce() );
//            }
        }

        /**
         * Read from other.
         *
         * @return the value read.
         */
        @Override
        public int read() {
            synchronized ( myLock ) {
                try {
                    myLock.wait();
                } catch ( InterruptedException ex ) {
                    Logger.getLogger( IOInverter.class
                            .getName() ).log( Level.SEVERE, null, ex );
                }
            }
            return other.lastWritten();
        }

        @Override
        public int lastWritten() {
            return this.lastWritten;
        }

    }

    /**
     * Get the inside aggregate. This has the same setup as a normal aggregate.
     *
     * @return BitAggregate
     */
    public BitAggregate<Integer> getInside() {
        return insideAggregate;
    }

    /**
     * Get the outside aggregate. This aggregate can be used to connect to the
     * hardware simulation.
     *
     * @return the outside aggregate
     */
    public BitAggregate<Integer> getOutside() {
        return outsideAggregate;
    }
}
