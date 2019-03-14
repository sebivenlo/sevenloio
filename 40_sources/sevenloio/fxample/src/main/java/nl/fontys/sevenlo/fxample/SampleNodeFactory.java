package nl.fontys.sevenlo.fxample;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import nl.fontys.sevenlo.hwio.Bit;
import nl.fontys.sevenlo.hwio.BitAggregate;
import nl.fontys.sevenlo.hwio.InBit;
import nl.fontys.sevenlo.hwio.OutBit;

/**
 * Factory for JavaFX widgets. Since Node is the top dog in JavaFX GUI elements,
 * it is a factory of those. This factory creates buttons and labels and not
 * much else.
 *
 * @author Pieter van den Hombergh {@code <p.vandenhombergh@fontys.nl>}
 */
public class SampleNodeFactory implements AbstractNodeFactory {

    final BitAggregate bag;
    static final double WIDGETHEIGHT = 22.0;

    /**
     * Create a factory that uses a bit-aggregate. All controls created by this
     * factory are connected to the bit given bit aggregate, either as input or
     * as output.
     *
     * @param bag the bit aggregate to use.
     */
    public SampleNodeFactory( BitAggregate bag ) {
        this.bag = bag;
    }

    /**
     * Create a node controlling or reading a bit.
     *
     * @param bit the bit to attach to
     *
     * @return the connected Node.
     */
    @Override
    public Node createNode( Bit bit ) {
        Node result = null;
        if ( bit instanceof InBit ) { // attach to led for this sample
            result = createElevatorLed( ( InBit ) bit, bag );
        } else if ( bit instanceof OutBit ) {
            result = createButtonWidget( ( OutBit ) bit );
        } else {
            result = new Label( "unknown control type" );
        }
        return result;
    }

    /**
     * Create a button plus label.
     *
     * @param eb          the bit manipulated by this button.
     * @param description the description of the bit
     *
     * @return the Node.
     */
    private static Node createButtonWidget( final OutBit eb ) {
        Button btn = new Button( "o" );
        String description = eb.getName();
        Label lbl = new Label( description );
        lbl.setLabelFor( btn );
        HBox box = new HBox();
        lbl.prefHeightProperty().bind( box.heightProperty() );
        btn.setPrefWidth( 20.0 );//prefWidthProperty().bind( box.widthProperty().multiply( 0.25 ) );
        box.getChildren().addAll( btn, lbl );
        box.setPrefHeight( WIDGETHEIGHT );
        btn.pressedProperty().addListener( ( obs, ov, nv ) -> eb.set( nv ) );
        btn.pressedProperty().addListener( ( obs, ov, nv ) -> System.out.println( eb ) );
        return box;
    }

    /**
     * Create an output led with a label, which shows the state of the connected
     * input.
     * The important side effect is that the bit, the led and the input are
     * connected in the listener sense. A change in the (hardware) input should
     * be reflected in the led, part of the returned node.
     *
     * @param ib  input bit to monitor
     * @param bag the aggregate to use with this bit.
     *
     * @return the Node.
     */
    private static Node createElevatorLed( InBit ib, BitAggregate bag ) {
        ElevatorLed led = new ElevatorLed( ib.getName() );
        led.attach( ib );
        led.prefHeight( WIDGETHEIGHT );
        return led.init();//new Label("Pietje Puk");
    }

}
