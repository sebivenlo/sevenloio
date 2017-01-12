/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.fontys.sevenlo.widgets;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import nl.fontys.sevenlo.utils.ResourceUtils;

/**
 * Widget demo/test program.
 * Tests various instances of shape and symbol buttons.
 * @author Pieter van den Hombergh (p.vandenhombergh@fontys.nl)
 */
public final class WidgetsMain {

    /**
     * Make check style happy.
     */
    private WidgetsMain(){}
    /**
     * A show case method.
     * @param args unused.
     */
    public static void main(String[] args) {
        ClassLoader cl = WidgetsMain.class.getClassLoader();
        final JFrame f = new JFrame();
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        final ImageIcon sym =
                ResourceUtils.getIconResource("images/opensymbol.png");
        final ImageIcon symclose =
                ResourceUtils.getIconResource("images/closesymbol.png");
        final ImageIcon key
                = ResourceUtils.getIconResource("images/key.png");
        final ArrowButton bup = new ArrowButton(ArrowPointing.UP);
        final ShapeButton bdown =
                new ArrowButton(ArrowPointing.DOWN);
        final ShapeButton bblue = ShapeButton.createRectangleSymbolButton(sym);
        final ShapeButton bgreen =
                ShapeButton.createRectangleSymbolButton(symclose);
        final ShapeButton byellow =
                ShapeButton.createRectangleSymbolButton(key);
        final ShapeButton rb = ShapeButton.createRoundDigitButton(0);
        final ShapeButton rb2 = ShapeButton.createRoundDigitButton(7);
        final ShapeButton tb = new ShapeButton(true);
        final ShapeButton star3 =
                new ShapeButton(new ShapeButton.StarShapeMaker(3));
        final ShapeButton star6 =
                new ShapeButton(new ShapeButton.StarShapeMaker(5));
        final ShapeButton pol =
                new ShapeButton(new ShapeButton.PolygonShapeMaker(5));
        final ShapeButton polArrow =
                new ShapeButton(new ShapeButton
                .PolygonShapeMaker(3).setStartAngle(0.0));
        final ShapeButton dialButton =
                new ShapeButton(new DialNeedle(null).setRotation(0.0));
        dialButton.setPreferredSize(new Dimension(300, 300));
        bblue.setPreferredSize(new Dimension(25, 25));
        bblue.setInteriorColorOn(new Color(0, 0, 255));
        bgreen.setPreferredSize(new Dimension(25, 25));
        bgreen.setInteriorColorOn(new Color(0, 255, 0));
        byellow.setInteriorColorOn(new Color(255, 255, 0));
        byellow.setPreferredSize(new Dimension(25, 25));
        rb.setInteriorColorOn(new Color(255, 128, 0));
        rb2.setInteriorColorOn(new Color(255, 128, 0));
        rb2.setPreferredSize(new Dimension(25, 25));
        byellow.setBackground(Color.BLACK);
        byellow.setContentAreaFilled(false);
        bup.setContentAreaFilled(false);
        tb.setPreferredSize(new Dimension(20, 20));
        bgreen.setOn(true);
        byellow.setOn(true);
        f.getContentPane().setLayout(new FlowLayout());
        f.getContentPane().setBackground(Color.black);
        f.add(bdown);
        f.add(bup);
        f.add(bblue);
        f.add(bgreen);
        f.add(byellow);
        f.add(tb);
        f.add(rb);
        f.add(rb2);
        f.add(star3);
        f.add(star6);
        f.add(pol);
        f.add(polArrow);
        f.add(dialButton);

        bdown.addActionListener(new ActionListener() {
            private int state = 0;
            public void actionPerformed(ActionEvent e) {
                bdown.setOn((state % 2) == 0);
                state++;
            }
        });

        bblue.addActionListener(new ActionListener() {
            private int state = 0;
            public void actionPerformed(ActionEvent e) {
                bblue.setOn((state % 2) == 0);
                state++;
            }
        });

        bup.addActionListener(new ActionListener() {
            private int state = 0;
            public void actionPerformed(ActionEvent e) {
                bup.setOn((state % 4) == 0);
                int values = ArrowPointing.values().length;
                bup.setPointing(ArrowPointing.values()[(state) % values]);
                state++;
            }
        });
        byellow.addActionListener(new ActionListener() {
            private int state = 0;
            public void actionPerformed(ActionEvent e) {
                byellow.setOn((state % 2) == 0);
                state++;
            }
        });
        tb.addActionListener(new ActionListener() {
            private int state = 0;
            public void actionPerformed(ActionEvent e) {
                tb.setOn((state % 2) == 0);
                state++;
            }
        });
       rb.addActionListener(new ActionListener() {
            private int state = 0;
            public void actionPerformed(ActionEvent e) {
                rb.setOn((state % 2) == 0);
                state++;
            }
        });

        rb2.addActionListener(new ActionListener() {
            private int state = 0;
            public void actionPerformed(ActionEvent e) {
                rb2.setOn((state % 2) == 0);
                state++;
            }
        });
        polArrow.addActionListener(new ActionListener() {
            private double startAngle = 0.0;
            public void actionPerformed(ActionEvent e) {
                startAngle += (Math.PI / 10);
                if (startAngle > 2 * Math.PI) {
                    startAngle -= 2 * Math.PI;
                }
                polArrow.setShapeMaker(new ShapeButton
                        .PolygonShapeMaker().setStartAngle(startAngle));
            }
        });
        dialButton.addActionListener(new ActionListener() {
            private double startAngle = 0.0;
            private double delta = (Math.PI / 50);
            public void actionPerformed(ActionEvent e) {
                startAngle += delta;
                if (Math.abs(startAngle) > Math.PI / 2) {
                    delta = -delta;
                }
                dialButton.setShapeMaker(
                        new DialNeedle(dialButton).setRotation(startAngle)
                        );
            }
        });
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                f.pack();
                f.setVisible(true);
            }
        });
    }
}
