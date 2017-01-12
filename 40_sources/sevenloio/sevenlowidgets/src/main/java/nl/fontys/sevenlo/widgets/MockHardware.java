package nl.fontys.sevenlo.widgets;


import java.util.Properties;
import nl.fontys.sevenlo.hwio.AbstractBitFactory;
import nl.fontys.sevenlo.hwio.BitAggregate;
import nl.fontys.sevenlo.hwio.DefaultBitFactory;
import nl.fontys.sevenlo.hwio.IO;
import nl.fontys.sevenlo.hwio.IOInverter;
import nl.fontys.sevenlo.hwio.PollThreads;
import nl.fontys.sevenlo.hwio.Poller;
import nl.fontys.sevenlo.hwio.SimpleBitAggregate;
import nl.fontys.sevenlo.hwio.SwingPoller;
import nl.fontys.sevenlo.utils.ResourceUtils;

/**
 * Simulates a Bit Aggregate Hardware port with a GUI Panel.
 * This class simulates the 'inside' and 'outside' of a physical hardware port
 * with a specific number of bits.
 * The bit configuration of the hardware is read from a properties file
 * that can be passed as an argument to this class main method.
 * @author Pieter van den Hombergh (P dot vandenHombergh at fontys dot nl)
 * @version $Id$
 */
public class MockHardware {
    private final BitAggregate insideAggregate;
    private final BitAggregate outsideAggregate;
    private final IOInverter inverter;

    /**
     * Startup. Test for io demo.
     * @param args command line arguments
     */
    public static void main(String[] args) {
        Properties prop = new Properties();
        if (args.length > 0) {
            prop = ResourceUtils.loadPropertiesFormFile(prop, args[0]);
        }

        int im = Integer.decode(prop.getProperty("inputMask", "0xffff"));
        MockHardware mh = new MockHardware( prop );
        BitAggregate inside = mh.getInsideAggregate();
        IOGUIPanel p2 =
        new IOGUIPanel("Inside", "Hi, I am a Fakeapplication.\n"
                +" My buttons are connected to the output,\n"
                +"my leds to the input",
                inside, prop );
        p2.startTheShow();
        Poller poller = new SwingPoller(inside);
        Thread pollThread = PollThreads.createPollThread(poller);
        pollThread.start();
        mh.startMock();

    }

    /**
     * Create a Mock with a properties set and a bit factory.
     * @param prop properties
     * @param bfac bit factory
     * @param im inputMask
     */
    public MockHardware( Properties prop, AbstractBitFactory bfac, int im ) {
        inverter = new IOInverter(im);
        IO outside = inverter.getOutside();
        IO inside = inverter.getInside();
        outsideAggregate = new SimpleBitAggregate(outside, ~im);
        insideAggregate = new SimpleBitAggregate(bfac, inside, inside, im);
        IOGUIPanel p1 =
        new IOGUIPanel("Outside","Hi, I am faking the hardware.\n"
                + " My buttons are your inputs,\n my leds are your outputs",
                outsideAggregate, prop );
        p1.startTheShow();
    }

    /**
     * Startup the mock.
     */
    public void startMock() {
        Poller poller = new SwingPoller(outsideAggregate);
        Thread pollThread = PollThreads.createPollThread(poller);
        pollThread.start();
    }

    /**
     * Create a MockHardware with the default BitFactory.
     * @param prop properties
     */
    public MockHardware(Properties prop){
        this(prop, new DefaultBitFactory(),0xFFFF0000);
    }

    /**
     * Gets the inside aggregate.
     * @return the BitAggragegate modelling the 'inside'
     */
    public BitAggregate getInsideAggregate(){
        return insideAggregate;

    }

    /**
     * Gets the outside aggregate.
     * @return the BitAggragegate modelling the 'outside'.
     */
    public BitAggregate getOutsideAggregate(){
        return outsideAggregate;
    }
}
// EOF
