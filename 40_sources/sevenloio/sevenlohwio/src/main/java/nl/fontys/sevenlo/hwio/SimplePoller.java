/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package nl.fontys.sevenlo.hwio;

/**
 * Use for Polls in a continuous loop. This poller implementation can be used in
 * combination with for instance a BitAggregate with a blocking
 * <code>read()</code>. The poller immediately invokes the updater. If you want
 * to use a poller in combination with a GUI, where bit changes should be
 * reflected on the GIU, consider to use a SwingPoller instead.
 *
 * @author Pieter van den Hombergh (P dot vandenHombergh at fontys dot nl)
 */
public class SimplePoller implements Poller {

    private int oldValue;
    private final BitAggregate<Integer> bitAggregate;

    /**
     * Create a poller for a bit aggregate with input mask set.
     *
     * @param ba bit aggregate
     */
    public SimplePoller( BitAggregate<Integer> ba ) {
        bitAggregate = ba;
        oldValue = 0;
    }

    /**
     * Poll the input and trigger all listeners in the aggregate. It is Okay and
     * even desirable for the BitAggregate.read() to do a blocking read.
     */
    @Override
    public void pollOnce() {
        int newValue = bitAggregate.read();
        BitUpdater.updateIntegerBits( bitAggregate, oldValue, newValue );
        oldValue = newValue;
    }
}
