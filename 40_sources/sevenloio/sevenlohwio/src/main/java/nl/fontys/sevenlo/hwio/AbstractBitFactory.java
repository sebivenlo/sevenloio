package nl.fontys.sevenlo.hwio;

/**
 * Factory to create Bit for an application.
 * GoF Factory Pattern, participant Abstract Factory.
 * The factory creates a input of output for an application. The bits produced
 * are associated with (connected to) a port at a bit position.
 *
 * @author Pieter van den Hombergh (p.vandenHombergh at fontys dot nl)
 */
public interface AbstractBitFactory {

    /**
     * Create an INputBit.
     * @param port the output to use
     * @param bitNr nth bit position counted from the right.
     * @return new input bit
     */
    BitOps createInputBit(Input port, int bitNr);

    /**
     * Create an output bit.
     *
     * @param port the output to use
     * @param bitNr nth bit position counted from the right.
     * @return new output bit
     */
    BitOps createOutputBit(Output port, int bitNr );
}
