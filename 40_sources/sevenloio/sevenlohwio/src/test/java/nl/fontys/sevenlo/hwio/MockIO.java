/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.fontys.sevenlo.hwio;

/**
 *
 * @author Pieter van den Hombergh (P dot vandenHombergh at fontys dot nl)
 */
public class MockIO implements BitAggregate<Integer> {

    public static final int SUPPORTED_BITS = 32;
    int shadow;
    BitOps[] bit = null;
    final int inputMask;
    AbstractBitFactory bitFac = new DefaultBitFactory();

    MockIO( int inputMask ) {
        bit = new Bit[ 32 ];
        this.inputMask = inputMask;
    }

    public void write( int newValue ) {
        int ov = shadow; // fetch old value
        shadow = newValue;
        System.out.println( "wrote " + shadow );
        BitUpdater.updateIntegerBits( this, ov, newValue );
    }

    @Override
    public void writeMasked( int mask, int value ) {
        write( ( shadow & ~mask ) | ( value & mask ) );
    }

    @Override
    public int read() {
        return shadow;
    }

    public BitOps get( int i ) {
        return bit[ i ];

    }

    public int size() {
        return SUPPORTED_BITS;
    }

    public Integer getInputMask() {
        return inputMask;
    }

    @Override
    public BitOps getBit( int i ) {
        return bit[ i ];
    }

    public Integer lastRead() {
        return shadow;
    }

    public int lastWritten() {
        return shadow;
    }

    @Override
    public void connect() throws IllegalStateException {
        int im = this.inputMask;
        for ( int i = 0; i < bit.length; i++ ) {
            if ( ( 1 & im ) != 0 ) {
                bit[ i ] = bitFac.createInputBit( this, i );
            } else {
                bit[ i ] = bitFac.createOutputBit( this, i );
            }
            im >>= 1;
        }
    }

}
