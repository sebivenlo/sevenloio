package nl.fontys.sevenlo.hwio;

/**
 * Façade class to hide package complexity. This implementation provides four
 * constructors which can be used for the most common cases.
 *
 * The BitAggregate allows access to all input and output bits.
 * <br>
 * <h2>Usage output</h2>
 * For a application layer the SimpleBitAggregate provides the functionality of
 * individually settable output bits. The application cloud/should get the
 * instances of the bits and connect the appropriate output logic.
 * <pre class='brush:java'>
 * SimpleBitAggregate ba = ...<br>
 * Bit lampA = ba.getBit(LAMPABIT);<br>
 * . <br>
 * . <br>
 * lampA.set(); // turn lampA on <br>
 * </pre>
 * <br>
 * <h2>Usage input</h2>
 * Input would typically use a Listener model. The logic should implement the
 * BitListener interface and connect that Listener to the appropriate bits.
 * <pre class='brush:java'>
 * BitListener buttonLogic = new ....
 * ba.addListener(buttonLogic);
 * </pre>
 *
 *
 *
 * @author Pieter van den Hombergh (p.vandenhombergh@fontys.nl)
 */
public class SimpleBitAggregate implements BitAggregate<Integer> {

    /**
     * The number of bits in the aggregate.
     */
    public static final int SUPPORTED_BITS = Integer.SIZE;

    private final int inputMask;
    private final Bit[] bit;
    private int shadow;
    private int lastRead; // cached value, non blocking
    private Output output;
    private Input input;
    private AbstractBitFactory bitFactory;

    /**
     * Constructor with do it yourselves bitFactory, output and input and
     * Bitmask parameters. Use this version if you want to roll your own bits.
     *
     * @param bitFactory the factory to create input and output bits
     * @param out        the writer implementor
     * @param in         the reader implementor
     * @param ipm        the inputmask
     */
    public SimpleBitAggregate( AbstractBitFactory bitFactory, Output out,
            Input in, int ipm ) {
        this.inputMask = ipm;
        this.bitFactory = bitFactory;
        bit = new Bit[ SUPPORTED_BITS ];
        output = out;
        input = in;
    }

    /**
     * Constructor using the default bit factory provided in the package. Use
     * this version if you reader and writer are implemented in different
     * objects (or classes).
     *
     * @param out the writer implementor
     * @param in  the reader implementor
     * @param ipm the input mask
     */
    public SimpleBitAggregate( Output out, Input in, int ipm ) {
        this( new DefaultBitFactory(), out, in, ipm );
    }

    /**
     * Constructor for the case that you have a combined IO object.
     *
     * Typical example is the IOWarrior which provides Bit IO in one object.
     *
     * @param io  the writer and reader implementing object
     * @param ipm inputMask
     */
    public SimpleBitAggregate( IO io, int ipm ) {
        this( io, io, ipm );
    }

    /**
     * Constructor for test purposes.
     *
     * @param ipm input mask
     */
    public SimpleBitAggregate( int ipm ) {
        this( new IO() {
            private int v = 0;

            @Override
            public void writeMasked( int mask, int value ) {
                v = ( v & ~mask ) | ( mask & value );
            }

            @Override
            public int lastWritten() {
                return v;
            }

            @Override
            public int read() {
                return lastWritten();
            }
        }, ipm );
    }

    /**
     * Access to the Bit through its number.
     *
     * @param i the bit number
     *
     * @return the requested bit
     */
    @Override
    public BitOps getBit( int i ) {
        return bit[ i ];
    }

    /**
     * Get the size (count of bits) of the aggregate.
     *
     * @return number of bits in the aggregate.
     */
    @Override
    public int size() {
        return bit.length;
    }

    /**
     * Get the input mask, which determines which bits are input or output.
     *
     * @return input mask
     */
    @Override
    public Integer getInputMask() {
        return inputMask;
    }

    /**
     * Write a value using the writer.
     *
     * @param mask  determines which bit affect the output.
     * @param value determines the values of the affected bits.
     */
    @Override
    public void writeMasked( int mask, int value ) {
        output.writeMasked( mask, value );
    }

    /**
     * Blocking read operation.
     *
     * This read will wait for new input.
     *
     * Do not use it in a GUI thread. Use a @see SwingPoller in that case if you
     * want to poll the input in a GUI application.
     *
     * @return new value
     */
    @Override
    public int read() {
        int result = input.read();
        return result;
    }

    /**
     * Get the last value written.
     *
     * @return the shadow value.
     */
    @Override
    public int lastWritten() {
        return output.lastWritten();
    }

    /**
     * Non blocking read.
     *
     * @return the last value produced by a blocking read. Note taht this value
     *         may be stale.
     */
    @Override
    public Integer lastRead() {
        return lastRead;
    }

    /**
     * Connect a listener to a bit.
     *
     * @param bitNr the bit number to connect to.
     * @param l     the listener to add
     */
    public void addBitListener( int bitNr, BitListener l ) {
        BitOps bo = getBit( bitNr );
        if ( bo instanceof Bit ) {
            Bit bit = ( Bit ) bo;
            bit.addListener( l );
        }
    }

    /**
     * Remove a listener to a bit.
     *
     * @param bitNr the bit number to remove from.
     * @param l     the listener to remove
     */
    public void removeBitListener( int bitNr, BitListener l ) {
        BitOps bo = getBit( bitNr );
        if ( bo instanceof Bit ) {
            Bit bit = ( Bit ) bo;
            bit.removeListener( l );
        }
    }

    @Override
    public void connect() {
        for ( int i = 0; i < bit.length; i++ ) {
            if ( ( ( 1 << i ) & inputMask ) != 0 ) {
                bit[ i ] = ( Bit ) bitFactory.createInputBit( this, i );
            } else {
                bit[ i ] = ( Bit ) bitFactory.createOutputBit( this, i );
            }
        }
    }

    @Override
    public String toString() {
        return "SimpleBitAggregate{" + Integer.toHexString( inputMask) + '}';
    }

    /**
     * Connect a bit to this aggregate.
     * The argument is is tested for compatibility to its position, according to
     * the inputmask for
     * the aggregate.
     *
     * @param aBit to connect
     */
    @Override
    public void connect( Bit aBit ) {
        int pos = aBit.getBitNr();
        boolean isInput = ( inputMask & ( 1 << pos ) ) != 0;
        if ( !isInput && aBit instanceof OutBit ) {
            if ( null != bit[ pos ] ) {
                aBit.addAllListener( bit[pos].getListeners() );
            }
            bit[ pos ] = aBit;
        } else if ( isInput && aBit instanceof InBit ) {
            bit[ pos ] = aBit;
        } else {
            throw new IllegalArgumentException( "type of " + aBit
                    + " not according to bitmask or position" );
        }
    }
}
