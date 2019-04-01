package nl.fontys.sevenlo.fxample;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import nl.fontys.sevenlo.hwio.Bit;
import nl.fontys.sevenlo.hwio.BitAggregate;

/**
 *
 * @author Pieter van den Hombergh {@code <p.vandenhombergh@fontys.nl>}
 */
public class ControlPanel extends VBox {

    static List<Node> createControls( BitAggregate bag, int w, ElevatorBitFactory bitFactory ) {
        List<Node> controls = new ArrayList( 33 );
        controls.add( new Label( bag.toString() ) );
        AbstractNodeFactory nf = new SampleNodeFactory( bag );
        for ( int p = 0; p < bag.size(); p++ ) {
            Bit eb = bitFactory.createBit( bag, p );
            controls.add( nf.createNode( eb ) );
        }
        return controls;
    }

    /**
     * Create a control panel for a bag.
     * @param bag bits for io
     * @param w control panel number
     * @param bitFactory to create the bits.
     */
    public ControlPanel( BitAggregate<Integer> bag, int w, ElevatorBitFactory bitFactory ) {
        List<Node> controls = createControls( bag, w, bitFactory );
        Collections.reverse( controls );
        this.getChildren().addAll( controls );
    }

}
