package nl.fontys.sevenlo.hwio;

/**
 * See AbstractBitFactory.
 *
 * @author Pieter van den Hombergh (P dot vandenHombergh at fontys dot nl)
 */
public class DefaultBitFactory implements AbstractBitFactory {

    /**
     * Create an input bit at a specify position.
     *
     * @param port associated port
     * @param bitNr the position
     * @return new Input bit
     */
    @Override
    public Bit createInputBit( Input port, int bitNr ) {
        return new InBit( bitNr );
    }

    /**
     * Create an output bit at a specified position.
     *
     * @param port the output to use
     * @param bitNr the position
     * @return new Output bit
     */
    @Override
    public Bit createOutputBit( Output port, int bitNr ) {
        return new OutBit( port, bitNr );
    }
}
