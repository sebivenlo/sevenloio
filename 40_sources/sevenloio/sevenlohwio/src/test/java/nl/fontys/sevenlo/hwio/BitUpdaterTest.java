/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.fontys.sevenlo.hwio;

import java.util.ArrayList;
import java.util.List;
import org.junit.Assert;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
import static nl.fontys.sevenlo.hwio.BitUpdater.updateIntegerBits;
import static nl.fontys.sevenlo.hwio.BitUpdater.updateLongBits;

/**
 * Test class for bit updater.
 * @author Pieter van den Hombergh (p.vandenhombergh at fontys dot nl )
 *
 */
public class BitUpdaterTest {

    BitAggregate<Long> aglong = null;
    BitAggregate<Long> agint = null;

    @Before
    public void setUp() {
        aglong = new MockBitAggregate( 64, 0xffff_0000_ffff_0000L );
        agint = new MockBitAggregate( 32, 0xffff_0000L );

    }

    /**
     * Test of updateIntegerBits method, of class BitUpdater.
     */
    @Test
    public void testUpdateLongBits() {
        System.out.println( "aglong = " + aglong.size() );
        long lastRead = 0;
        long newValue = 0x2_0002;
        CountingBit b1 = ( CountingBit ) aglong.getBit( 1 );
        CountingBit b17 = ( CountingBit ) aglong.getBit( 17 );
        System.out.println( "b1 = " + b1 );
        updateLongBits( aglong, ( int ) lastRead, ( int ) newValue );
        Assert.assertEquals( 0, b1.count );
        Assert.assertFalse( b1.isSet() );
        Assert.assertEquals( 1, b17.count );
        Assert.assertTrue( b17.isSet() );

        lastRead = newValue;
        // same values
        updateLongBits( aglong, lastRead, newValue );
        Assert.assertEquals( 0, b1.count );
        Assert.assertEquals( 1, b17.count );
        lastRead = 0x4_0002; // bit to zero, other up
        updateLongBits( aglong, lastRead, newValue );
        Assert.assertEquals( 0, b1.count );
        Assert.assertEquals( 2, b17.count );
        // flip all bits
        b17.count = 0;
        updateLongBits( aglong, 0, -1 );
        Assert.assertEquals( 0, b1.count );
        Assert.assertEquals( 1, b17.count );
        updateLongBits( aglong, -1, 0 );
        Assert.assertEquals( 0, b1.count );
        Assert.assertEquals( 2, b17.count );

    }

    /**
     * Test of updateLongBits method, of class BitUpdater.
     */
    @Test
    public void testUpdateIntegerBits() {

        System.out.println( "aglong = " + aglong.size() );
        int lastRead = 0;
        int newValue = 0xa_0002;
        CountingBit b1 = ( CountingBit ) aglong.getBit( 1 );
        CountingBit b17 = ( CountingBit ) aglong.getBit( 17 );
        System.out.println( "b1 = " + b1 );
        updateLongBits( aglong, ( int ) lastRead, ( int ) newValue );
        Assert.assertEquals( 0, b1.count );
        Assert.assertFalse( b1.isSet() );
        Assert.assertEquals( 1, b17.count );
        Assert.assertTrue( b17.isSet() );

        lastRead = newValue;
        // same values
        updateLongBits( aglong, lastRead, newValue );
        Assert.assertEquals( 0, b1.count );
        Assert.assertEquals( 1, b17.count );
        lastRead = 0xc_0002; // bit to zero, other up
        updateLongBits( aglong, lastRead, newValue );
        Assert.assertEquals( 0, b1.count );
        Assert.assertEquals( 2, b17.count );
        // flip all bits
        b17.count = 0;
        updateLongBits( aglong, 0, -1 );
        Assert.assertEquals( 0, b1.count );
        Assert.assertEquals( 1, b17.count );
        updateLongBits( aglong, -1, 0 );
        Assert.assertEquals( 0, b1.count );
        Assert.assertEquals( 2, b17.count );

    }

    private static class MockBitAggregate implements BitAggregate<Long> {

        final int size;
        final long mask;
        final List<Bit> bits = new ArrayList<>();

        public MockBitAggregate( int s, long mask ) {
            size = s;
            this.mask = mask;
            for ( int i = 0; i < size; i++ ) {
                bits.add( new CountingBit() );
            }
        }

        @Override
        public BitOps getBit( int i ) {
            return bits.get( i );
        }

        @Override
        public int size() {
            return this.size;
        }

        @Override
        public Long getInputMask() {
            return this.mask;
        }

        @Override
        public Long lastRead() {
            throw new UnsupportedOperationException( "Not supported yet." ); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void connect() throws IllegalStateException {
            
        }

        @Override
        public void writeMasked( int mask, int value ) {
            throw new UnsupportedOperationException( "Not supported yet." ); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public int lastWritten() {
            throw new UnsupportedOperationException( "Not supported yet." ); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public int read() {
            throw new UnsupportedOperationException( "Not supported yet." ); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void connect( Bit bit ) {
            throw new UnsupportedOperationException( "Not supported yet." ); //To change body of generated methods, choose Tools | Templates.
        }
    }

    static class CountingBit extends Bit {

        int count = 0;
        boolean value = false;

        public CountingBit() {
            super( 0 );
        }

        @Override
        public boolean isSet() {
            return value;
        }

        @Override
        public void set( boolean b ) {
            value = b;
            System.out.println( "b = " + b );
            count++;
        }

    }
}
