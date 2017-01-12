package nl.fontys.sevenlo.widgets;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Properties;
import javax.swing.AbstractButton;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import nl.fontys.sevenlo.hwio.Bit;
import nl.fontys.sevenlo.utils.ResourceUtils;

/**
 * Utility factory.
 * @author Pieter van den Hombergh (p.vandenhombergh@fontys.nl)
 */
public class WidgetFactory {

    /** The icon set for the led. */
    private static ImageIcon onIcon =
            ResourceUtils.getIconResource("images/yellowled_on_small.png");
    /** offIcon, single icon. */
    private static ImageIcon offIcon =
            ResourceUtils.getIconResource("images/yellowled_off_small.png");

    /**
     * Label that paints its background.
     */
    static class JLLabel extends JLabel {

        /**
         * Create a label with a string and text placement parameter.
         * @param s string
         * @param i location of string
         */
        JLLabel(String s, int i) {
            super(s, i);
        }

        /**
         * Simple label, Filled in later.
         */
        JLLabel() {
            super();
        }

        @Override
        public void paintComponent(Graphics g) {
            Dimension s = getSize();
            Color oldC = g.getColor();
            g.setColor(getBackground());
            g.fillRect(0, 0, s.width, s.height);
            g.setColor(oldC);
            super.paintComponent(g);
        }
    }
    /** properties used by the factory. Eager initialising Singleton. */
    private final Properties properties;

    /**
     * Default ctor.
     */
    public WidgetFactory() {
        this(new Properties());
    }

    /**
     * Create WidgetFactory with specific properties.
     * @param p properties to use in construction
     */
    public WidgetFactory(Properties p) {
        properties = p;
    }

    /**
     * Create a check box that controls and outbit.
     * @param i bit number
     * @param b the bit to control
     * @return the requested JComponent
     */
    public JComponent createCheckbox(int i, final Bit b) {
        return this.createCheckbox(getPinText(i), b);
    }

    /**
     * Create a check box that controls and outbit.
     * @param text label text to checkbox
     * @param b the bit to control
     * @return the requested JComponent
     */
    public JComponent createCheckbox(String text, final Bit b) {

        JComponent wid;
        final AbstractButton ab = new JCheckBox();
        ab.setText(text);
        ab.setPreferredSize(new Dimension(20, 20));
        ab.addChangeListener(new ChangeListener() {

            private boolean isSelected = false;

            public void stateChanged(final ChangeEvent e) {
                boolean nowSelected = ab.getModel().isSelected();
                if (nowSelected != isSelected) {
                    isSelected = nowSelected;
                    b.set( isSelected );
                }
            }
        });
        wid = ab;
        return wid;
    }

    /**
     * Create a Label with a state full icon as a JComponent,
     * listening to a bit.
     * @param i the bit number
     * @param b the bit
     * @return the requested JComponent
     */
    public JComponent createLed(int i, Bit b) {
        return this.createLed(getPinText(i), b);
    }

    /**
     * Create a Label with a state full icon as a JComponent,
     * listening to a bit.
     * @param text the label text
     * @param b the bit
     * @return the requested JComponent
     */
    public JComponent createLed(String text, Bit b) {
        JComponent wid;
        final JLabel jlab = new JLLabel();
        BinaryIcon l = new BinaryIcon(jlab, offIcon.getImage(),
                onIcon.getImage());
        b.addListener(l);
        jlab.setIcon(l);
        jlab.setText(text);
        wid = jlab;
        return wid;
    }
    private static final NumberFormat NF = new DecimalFormat("00");

    /**
     * Get the appropriate text for a bit.
     *
     * @param pin bit number
     * @return the text from the properties.
     */
    private String getPinText(int pin) {
        String bitName= "b" + NF.format(pin);
        String result = bitName+": "+properties
                .getProperty("pin" + pin, "Port " + pin);
        return result;
    }

    /**
     * Get properties instance.
     * @return the properties.
     */
    public Properties getProperties() {
        return properties;
    }
}
