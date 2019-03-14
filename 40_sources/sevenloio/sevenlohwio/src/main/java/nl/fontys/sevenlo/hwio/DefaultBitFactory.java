package nl.fontys.sevenlo.hwio;

/**
 * See AbstractBitFactory.
 *
 * @author Pieter van den Hombergh (P dot vandenHombergh at fontys dot nl)
 */
public class DefaultBitFactory<X extends Number> implements AbstractBitFactory<X> {

    /**
     * Create an input bit at a specify position and connect it to the given bit aggregate.
     *
     * @param bag  associated port (Bit aggregate)
     * @param bitNr the position
     * @return new Input bit
     */
    @Override
    public Bit createInputBit( BitAggregate<X> bag, int bitNr ) {
        InBit result = new InBit( bitNr );
        bag.connect( result );
        return result;
    }

    /**
     * Create an output bit at a specified position.
     *
     * @param bag  the output to use
     * @param bitNr the position
     * @return new Output bit
     */
    @Override
    public Bit createOutputBit( BitAggregate<X> bag, int bitNr ) {
        return new OutBit( bag, bitNr );
    }
}
