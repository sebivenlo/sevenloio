package nl.fontys.sevenlo.fxample;

import java.util.function.Supplier;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Circle;
import nl.fontys.sevenlo.hwio.BitListener;
import nl.fontys.sevenlo.hwio.InBit;

/**
 *
 * @author Pieter van den Hombergh {@code <p.vandenhombergh@fontys.nl>}
 */
class ElevatorLed extends HBox implements BitListener {

    private final String lblText;
    private final Label label;
    private final Node graphic;
    private static Supplier<Node> defaultGraphicProvider = () -> {
        Circle c = new Circle( 10.0 );
        return c;
    };

    ElevatorLed( String lblText, Supplier<Node> graphics ) {
        this.lblText = lblText;
        this.graphic = graphics.get();
        label = new Label( lblText, this.graphic );
    }

    ElevatorLed( String lblText ) {
        this( lblText, defaultGraphicProvider );
    }

    public final ElevatorLed init() {
        label.prefHeightProperty().bind( this.heightProperty() );
        getChildren().add( label );
        graphic.getStyleClass().add( "led-off" );
        return this;
    }

    public ElevatorLed attach( InBit bit ) {
        bit.addListener( this );
        return this;
    }

    public ElevatorLed disconnect( InBit bit ) {
        bit.removeListener( this );
        return this;
    }

    @Override
    public void updateBit( Object bit, boolean newValue ) {
        ObservableList<String> styles = graphic.getStyleClass();
        if ( newValue ) {
            graphic.getStyleClass().remove( "led-off" );
            graphic.getStyleClass().add( "led-on" );
        } else {
            graphic.getStyleClass().remove( "led-on" );
            graphic.getStyleClass().add( "led-off" );
        }
    }
}
