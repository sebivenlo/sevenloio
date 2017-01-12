package fx3;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.DoublePredicate;
import javafx.animation.AnimationTimer;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.css.Styleable;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;

/**
 *
 * @author Pieter van den Hombergh {@code <p.vandenhombergh@fontys.nl>}
 */
public class FXMLController implements Initializable {

    @FXML
    Pane shaft;
    @FXML

    GridPane grid;

    @FXML
    Slider doorslider;
    @FXML
    Slider cageslider;
    @FXML
    Pane cage;
    @FXML
    Pane doorFrame;
    @FXML
    Pane rpanel;

    @FXML
    Pane lpanel;

    @FXML
    Button open;
    @FXML
    Button close;
    @FXML
    Label obstructed;

    @FXML
    HBox floorindicator;

    @Override
    public void initialize( URL url, ResourceBundle rb ) {
        // TODO
        ReadOnlyDoubleProperty cageWidth = shaft.widthProperty();
        DoubleBinding doorPanelWidth = bindDoorToDoorFrameWidth();
        cage.layoutYProperty().bind( cageslider.valueProperty() );
        DoubleBinding leftPos = doorPanelWidth.multiply( motorValue ).subtract( doorPanelWidth );
        DoubleBinding rightPos = cageWidth.subtract( motorValue.multiply( doorPanelWidth ) );
        lpanel.layoutXProperty().bind( leftPos );
        rpanel.layoutXProperty().bind( rightPos );
        motorValue.addListener( styleUpdater( motorValue, close, "on", d -> d <= 0.0 ) );
        motorValue.addListener( styleUpdater( motorValue, open, "on", d -> d >= 1.0 ) );
        open.setOnAction( this::doorOpenAction );
        close.setOnAction( this::doorCloseAction );
        doorFrame.hoverProperty().addListener( new ChangeListener<Boolean>() {
            @Override
            public void changed( ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue ) {
                if ( newValue ) {
                    obstructed.getStyleClass().remove( "obstructed" );
                    if ( motorDelta > 0 ) {
                        motorDelta = -motorDelta;
                    }
                } else {
                    obstructed.getStyleClass().add( "obstructed" );
                }
            }
        } );
        doorslider.valueProperty().bindBidirectional( motorValue );
        motorValue.set( 0.1 );
        motorValue.set( 0.0 );
        floorindicator.getChildren().add( new Label( "-" ) );
        double height = cageslider.maxProperty().get();
        for ( int i = 0; i < 4; i++ ) {
            Label l = new Label( "" + i );
            final double z = height - i * height / 4;
            cageslider.valueProperty().addListener( styleUpdater( cageslider.valueProperty(), l, "indicator", d -> d >= z && d <= z + 15.0 ) );
            floorindicator.getChildren().addAll( l, new Label( "-" ) );
            l.getStyleClass().add( "indicatorlabel" );
        }
        Rectangle clip = new Rectangle();
        clip.heightProperty().bind( shaft.heightProperty() );
        clip.widthProperty().bind( shaft.widthProperty() );
        shaft.setClip( clip );

//        ColumnConstraints c1 = new ColumnConstraints();
//        c1.setPercentWidth( 100 );
//        ColumnConstraints c2 = new ColumnConstraints();
//        c1.setPercentWidth( 100 );
//        grid.getColumnConstraints().addAll( c1, c2 );
//        RowConstraints r1 = new RowConstraints();
//        r1.setPercentHeight( 20 );
//        RowConstraints r2 = new RowConstraints();
//        r1.setPercentHeight( 100 );
//        RowConstraints r3 = new RowConstraints();
//        r1.setPercentHeight( 100 );
//        RowConstraints r4 = new RowConstraints();
//        r1.setPercentHeight( 100 );
//        RowConstraints r5 = new RowConstraints();
//        r1.setPercentHeight( 100 );
//        grid.getRowConstraints().addAll( r1, r2, r3, r4, r5 );
    }

    DoubleBinding bindDoorToDoorFrameWidth() {
        DoubleBinding doorPanelWidth = shaft.widthProperty().divide( 2.0 );
        DoubleBinding doorFrameWidth = shaft.widthProperty().add( 0 );
        rpanel.prefWidthProperty().bind( doorPanelWidth );
        rpanel.maxWidthProperty().bind( doorPanelWidth );
        rpanel.minWidthProperty().bind( doorPanelWidth );
        lpanel.prefWidthProperty().bind( doorPanelWidth );
        lpanel.maxWidthProperty().bind( doorPanelWidth );
        lpanel.minWidthProperty().bind( doorPanelWidth );
        doorslider.prefWidthProperty().bind( doorFrameWidth );
        return doorPanelWidth;
    }

    ChangeListener<Number> styleUpdater( DoubleProperty motorValue, Styleable styleable, String styleClass, DoublePredicate pred ) {
        return new ChangeListener<Number>() {
            boolean ov = false;

            @Override
            public void changed( ObservableValue<? extends Number> observable, Number oldValue, Number newValue ) {
                boolean nv = pred.test( motorValue.get() );
                if ( nv != ov ) {
                    ov = nv;
                    if ( ov ) {
                        styleable.getStyleClass().add( styleClass );
                    } else {
                        styleable.getStyleClass().remove( styleClass );
                    }

                }
            }
        };
    }

    final double motorSpeed = 0.03125 / 2;
    private double motorDelta = 0;
    final DoubleProperty motorValue = new SimpleDoubleProperty();
    final DoorMotor doorMotor = new DoorMotor();

    void doorOpenAction( ActionEvent e ) {
        if ( motorValue.get() > 0.0 ) {
            motorDelta = -motorSpeed;
            doorMotor.start();
        }
    }

    void doorCloseAction( ActionEvent e ) {
        if ( motorValue.get() < 1.0 ) {
            motorDelta = motorSpeed;
            doorMotor.start();
        }
    }

    class DoorMotor extends AnimationTimer {

        @Override
        public void handle( long now ) {
            double v = motorValue.get();
            v += motorDelta;
            motorValue.set( v );
            if ( v <= 0.0 || v >= 1.0 ) {
                this.stop();
            }
        }

    }
}
