/**
 * <h1>Java bitwise Hardware IO package.</h1>
 *
 * The package <strong>Hardware IO</strong> defines a number of interfaces and
 * some implementations of these interfaces to provide bitwise input and
 * output.<br>
 *
 * Both input and output support a Listener interface. This interface can be
 * used to monitor the individual output bits but is more useful to the input
 * part, to allow the software to react to input changes by means of an
 * <b>Observer</b> type pattern, in many cases combined with an <b>Adapter</b>.
 * <br> The class SimpleBitAggregate can be considered as the Façade to this
 * package or as an example. <br>
 * It provides access to the important aspects: a group of individual Input and
 * Output bits which an Observer/Listener architecture.
 * <br>
 * As an example see the nl.fontys.sevenlo.iowarrior.WarriorTestGUI in the
 * separate library project sevenlowarrior, which uses the BitAggregate and
 * provides a rudimentary GUI.
 * <br>
 *
 * <div> <div class='caption'>
 * A class diagram with the main classes and the extension points.<br></div>
 * <img src='doc-files/hwio.svg' alt='class diagram' > <br>
 * </div>
 * The classes and interfaces with the white background are the extension points
 * for a typical implementation. To use it
 * <ul>
 * <li>Provide an implementation for the AbstractBitFactory,</li>
 * <li>create an application specific InBit</li>
 * <li>and make your application implement the BitListener interface.</li>
 * </ul>
 *
 * @author Pieter van den Hombergh (p.vandenhombergh@fontys.nl)
 *
 */
package nl.fontys.sevenlo.hwio;
