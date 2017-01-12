package nl.fontys.sebivenlo.mavenwithimage;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

public class FXMLController implements Initializable {

    @FXML
    private HBox hbox;

    @FXML
    private Label label;

    @FXML
    private void handleButtonAction( ActionEvent event ) {
        System.out.println( "You clicked me!" );
        label.setText( "Hello World!" );
    }

    @Override
    public void initialize( URL url, ResourceBundle rb ) {
        Image im = new Image( "/images/Flower.jpg", true );
        ImageView iv = new ImageView( im );
        hbox.getChildren().add( iv );
    }
}
