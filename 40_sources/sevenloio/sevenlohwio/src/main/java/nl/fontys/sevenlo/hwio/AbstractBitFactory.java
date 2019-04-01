package nl.fontys.sevenlo.hwio;

/**
 * Factory to create Bit for an application.
 * GoF Factory Pattern, participant Abstract Factory.
 * The factory creates a input of output for an application. The bits produced
 * are associated with (connected to) a port at a bit position.
 *
 * @author Pieter van den Hombergh (p.vandenHombergh at fontys dot nl)
 * @param <X> the type (width in bits) of the io used; choose between Integer or Long.
 */
public interface AbstractBitFactory<X extends Number> {

    /**
     * Create an INputBit.
     * @param bag the bit aggregate to use
     * @param bitNr nth bit position counted from the right.
     * @return new input bit
     */
    Bit createInputBit(BitAggregate<X> bag, int bitNr);

    /**
     * Create an output bit.
     *
     * @param bag  the big aggregate output to use
     * @param bitNr nth bit position counted from the right.
     * @return new output bit
     */
    Bit createOutputBit(BitAggregate<X> bag, int bitNr );
}
