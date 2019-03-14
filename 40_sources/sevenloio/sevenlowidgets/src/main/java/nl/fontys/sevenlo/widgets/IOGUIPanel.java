package nl.fontys.sevenlo.widgets;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.util.Properties;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.WindowConstants;
import nl.fontys.sevenlo.hwio.Bit;
import nl.fontys.sevenlo.hwio.BitAggregate;

/**
 * Test GUI for the IOWarrior.
 *
 * A test case for the hwio io package. The application can be used with and
 * without an iowarrior.
 *
 *
 * @author Pieter van den Hombergh (P dot vandenHombergh at fontys dot nl)
 * @version $Id$
 */
public class IOGUIPanel extends JFrame {

    private static final long serialVersionUID = 1;
    /**
     * The which frame
     */

    /**
     * the input mask, read from the command line.
     */
    private int inputMask = 0xffff;
    /**
     * the bits provided by the IOWarrior as an aggregate.
     */
    private BitAggregate<Integer> bitAggregate;
    /**
     * properties for the Gui.
     */
    /**
     * The widgets to represent the bits.
     */
    private JComponent[] widget;
    private JComponent panel;
    private JTextArea lab;

    /**
     * Create a named frame for a bit aggregate.
     *
     * @param l label
     * @param des description
     * @param ba bit aggregate to use.
     * @param prop properties for the GUI
     */
    public IOGUIPanel( String l, String des, BitAggregate<Integer> ba,
            Properties prop ) {
        super( l );
        if ( null == ba ) {
            throw new NullPointerException( "The real stuff please" );
        }

        bitAggregate = ba;
        bitAggregate.connect();
        panel = createWidgets( prop );
        lab = new JTextArea( des );
    }

    /**
     * Panel with only label.
     *
     * @param l label
     * @param ba bitaggregate
     * @param prop properties
     */
    public IOGUIPanel( String l, BitAggregate<Integer> ba, Properties prop ) {
        this( l, l, ba, prop );
    }

    /**
     * show the GUI.
     */
    public void startTheShow() {
        this.add( panel, BorderLayout.CENTER );
        this.add( lab, BorderLayout.NORTH );
        this.setDefaultCloseOperation( WindowConstants.EXIT_ON_CLOSE );
        this.pack();
        setVisible( true );
    }
    /**
     * The graphic resources to use.
     */

    private final Color evenColor = new Color( 224, 255, 255 );
    private final Color oddColor = new Color( 238, 238, 238 );

    /**
     * create the widgets.
     *
     * @param prop properties (labels) to with pins.
     */
    private JComponent createWidgets( Properties prop ) {
        JPanel panel = new JPanel();
        //p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        panel.setLayout( new GridLayout( 0, 1 ) );
        widget = new JComponent[ Integer.SIZE ]; //warrior.size()];
        int mask = 1;
        WidgetFactory widgetFactory = new WidgetFactory( prop );
        for ( int i = 0; i < widget.length; i++ ) {
            JComponent wid = ( ( mask & ( ( Integer ) bitAggregate
                    .getInputMask() ) ) != 0 )
                                     ? widgetFactory.createLed( i,
                                    ( Bit ) bitAggregate.getBit( i ) )
                                     : widgetFactory.createCheckbox( i,
                                    ( Bit ) bitAggregate.getBit( i ) );

            panel.add( wid );
            Color bgColor = ( 0 == ( i % 2 ) ) ? evenColor : oddColor;
            //lab.setBackground(bgColor);
            wid.setBackground( bgColor );
            mask <<= 1;
        }
        return panel;
    }

}
