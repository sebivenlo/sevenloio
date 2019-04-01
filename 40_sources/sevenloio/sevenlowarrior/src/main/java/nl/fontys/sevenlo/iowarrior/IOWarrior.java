package nl.fontys.sevenlo.iowarrior;

import com.codemercs.iow.IowKit;
import java.util.Arrays;
import static java.util.stream.Collectors.joining;
import nl.fontys.sevenlo.hwio.AbstractBitFactory;
import nl.fontys.sevenlo.hwio.BitAggregate;
import nl.fontys.sevenlo.hwio.Bit;
import nl.fontys.sevenlo.hwio.BitOps;
import nl.fontys.sevenlo.hwio.DefaultBitFactory;

/**
 * BitAggregate implementation for an IOWarrior.
 *
 * This class supports input and output on the same device. Only iowarrior pipe
 * 0 is used. (Simple io). This implementation supports IOWarrior40 only; Which
 * bits are input and which are output is determined by the <i>inputMask</i>.
 *
 * <p>
 * A 1 in the inputMask makes the bit at that position input, a 0 makes the bit
 * output. See the 1 and 0 as mnemonic for
 * <b>I</b>nput and <b>O</b>utput.</p>
 *
 * <p>
 * At connect time all inputbits are set high (1) (high impedance plus pullup in
 * the hardware), all outputbits are set to low (0).</p>
 * <p>
 * <strong>Note:</strong> The use of the factory methods to create the bits is
 * postponed to the time that the connect() method is called. Before that, no
 * bits exists in the aggregate.
 * </p>
 *
 * @see nl.fontys.sevenlo.hwio.BitAggregate
 * @author Pieter van den Hombergh (P.vandenHombergh at fontys.nl)
 */
public final class IOWarrior implements BitAggregate<Integer> {

    //@GuardedBy("readLock")
    /**
     * reference to handle from connector.
     */
    private final long handle;
    /**
     * input to distinguish between in (1) and out (0).
     */
    private int inputMask;
    /**
     * helps in serialized access to lastRead .
     */
    private final Object readLock = new Object();
    /**
     * helps in serialized access to shadow.
     */
    private final Object writeLock = new Object();
    //@GuardedBy("writeLock")
    /**
     * the last value sent to the hardware output.
     */
    private volatile int shadow = 0;
    //@GuardedBy("writeLock")
    /**
     * The last read (cached) value in the poll loop.
     */
    private volatile int lastRead = 0;
    /**
     * The connected bits.
     */
    private final Bit[] bit;
    /**
     * THe number of bits supported per warrior.
     */
    public static final int SUPPORTED_BITS = 32;
    private int reportCounter = 0;
    private final AbstractBitFactory fact;

    /**
     * Create an IoWarrior connection instance and uses a BitFactory to create
     * the IO Bits.
     *
     * @param hnd  handle to identify device to library
     * @param im   inputMask
     * @param fact BitFactory
     */
    public IOWarrior( final long hnd, final int im,
            final AbstractBitFactory fact ) {
        this.handle = hnd;
        this.inputMask = im;
        this.shadow = inputMask;
        bit = new Bit[ SUPPORTED_BITS ];
        this.fact = fact;
    }

    /**
     * Create an IoWarrior connection instance with default Bit instances.
     *
     * @param hnd handle to identify device to library
     * @param im  inputMask
     */
    public IOWarrior( final long hnd, final int im ) {
        this( hnd, im, new DefaultBitFactory() );
    }

    /**
     * Read the iowarrior bits.
     *
     * This is a blocking read. The IOWarrior library will only return if there
     * were any changes in the inputs. For event driven operation, this should
     * be called on a separate input thread.
     *
     * @return result which is 32 bits of new value.
     */
    @Override
    public int read() {

        int[] rdata = IowKit.read( handle, 0, WARRIOR_BUF_SIZE );
        int result = 0;
        if ( rdata.length > 0 ) {
            // discard highest byte
            for ( int i = rdata.length - 1; i > 0; i-- ) {
                result <<= BYTE_BIT_SHIFT;
                result |= rdata[ i ] & LOWBYTE;
            }
        }
        // save for readFast
        synchronized ( this.readLock ) {
            lastRead = result;
        }
        reportCounter++;
        return lastRead;
    }

    /**
     * Writes the output with all input bits set high. The work is delegated to
     * writeMasked with all bits to 1.
     *
     * @param value to write.
     */
    public void write( int value ) {
        writeMasked( ~0, value );
    }

    /**
     * Specify the input mask to use. Use in tests only.
     *
     * @param ipm the input mask.
     */
    void setInputMask( int ipm ) {
        this.inputMask = ipm;
        writeMasked( ipm, ipm );
    }
    /**
     * The iowarrior has a command byte and 4 data bytes for a total of 5.
     */
    private static final int WARRIOR_BUF_SIZE = 5;
    /**
     * Isolate the low 8 bits of an int.
     */
    private static final int LOWBYTE = 255;
    /**
     * The amount of bits to shift for a byte.
     */
    private static final int BYTE_BIT_SHIFT = 8;

    /**
     * WriteMasked as defined in the api of intwriter. This version does all the
     * work and is threadsafe.
     *
     * @param mask  the bits that should be kept safe.
     * @param value the 'set' of value bits.
     */
    @Override
    public void writeMasked( int mask, int value ) {
        int[] writeBuffer = null;
        int newShadow;
        // calculate stack local stuff from shadow and new value
        // using mask
        synchronized ( this.writeLock ) {
            newShadow = ( shadow & ~mask ) | ( value & mask ) | inputMask;
            if ( newShadow != shadow ) {
                shadow = newShadow;
                writeBuffer = new int[ WARRIOR_BUF_SIZE ];

                writeBuffer[ 0 ] = 0; // command /pipe 0
            }
        }

        // writing to the hardware is done unsynchronized.
        // newShadow and writebuffer are local vars, thus thread safe.
        if ( writeBuffer != null ) {
            for ( int i = 1; i < writeBuffer.length; i++ ) {
                writeBuffer[ i ] = newShadow & LOWBYTE;
                newShadow >>= BYTE_BIT_SHIFT;
            }
            // assume that number of bytes written is correct.
            // so no test of result
            IowKit.write( handle, 0, writeBuffer );
        }
    }

    /**
     * Get a bit through its number.
     *
     * @param i the bit number
     *
     * @return the bit
     */
    @Override
    public BitOps getBit( int i ) {
        return bit[ i ];
    }

    /**
     * Return debug info.
     *
     * @return a description of the warrior and ist state.
     */
    @Override
    public String toString() {
        return "IOWarrior "
                + IOWarriorConnector.getInstance().getSerialNr( handle );//+Arrays.toString( bit);
//                + " im=" + inputMask + " read=" + lastRead
//                + " shadow=" + shadow;
    }

    /**
     * Get the string representation.
     * @return a string
     */
    @Override
    public String asString() {
        return this.toString()+", bits: [\n"
                + Arrays.stream( bit )
                        .map( b -> "\t" + b.toString() )
                        .collect( joining( ",\n" ) )
                + "\n]";
    }

    /**
     * Get the number of supported bits.
     *
     * @return the size
     */
    @Override
    public int size() {
        return SUPPORTED_BITS;
    }

    /**
     * Return the input mask.
     *
     * @return the input mask
     */
    @Override
    public Integer getInputMask() {
        return this.inputMask;
    }

    /**
     * Return the shadow value.
     *
     * @return the shadow value.
     */
    @Override
    public int lastWritten() {
        return shadow;
    }

    /**
     * Non blocking read of last received value. Note that the value may be
     * stale.
     *
     * @return the last value.
     */
    @Override
    public Integer lastRead() {
        synchronized ( this.readLock ) {
            return shadow;
        }
    }

    /**
     * Connect the bits to the aggregate. This implementation throws an
     * IllegalStateException when already connected.
     *
     * @throws IllegalStateException when already connected.
     */
    @Override
    public void connect() {
        for ( int i = 0; i < bit.length; i++ ) {
            if ( null == bit[ i ] ) {
                if ( ( inputMask & ( 1 << i ) ) != 0 ) {
                    bit[ i ] = fact.createInputBit( this, i );
                } else {
                    bit[ i ] = fact.createOutputBit( this, i );
                }
                System.out.println( "created bit[i] = " + bit[ i ] );
            }
        }

        write( shadow );
    }

    public String getSerialNumber() {
        return IOWarriorConnector.getSerialNumber( handle );
    }

    /**
     * Connect a bit at a time.
     * Connect the bits after the aggregate is created. If this bit replaces
     * another non-null bit, that bits listeners are attached to the aBit.
     *
     * @param aBit bit to be connected.
     *
     * @throws IllegalArgumentException when the bit is not compatible with the
     *                                  input mask of the
     *                                  aggregate.
     */
    @Override
    public void connect( Bit aBit ) {
        int pos = aBit.getBitNr();

        if ( null != bit[ pos ] ) {
            aBit.getListeners().addAll( bit[ pos ].getListeners() );
            bit[ pos ].removeAllListeners();
        }
        bit[ pos ] = aBit;
        System.out.println( "connected bit  = " + bit[ pos ] );
    }

}
