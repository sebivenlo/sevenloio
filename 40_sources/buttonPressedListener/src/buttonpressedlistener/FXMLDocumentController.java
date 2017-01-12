package buttonpressedlistener;


import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;

/**
 *
 * @author Pieter van den Hombergh {@code <p.vandenhombergh@fontys.nl>}
 */
public class FXMLDocumentController implements Initializable {
    
    @FXML
    Button button;
    @FXML
    CheckBox cb;
    
    @FXML
    void handleButtonAction( ActionEvent event ) {
        System.out.println( "You clicked me!" );
    }
    
    @Override
    public void initialize( URL url, ResourceBundle rb ) {
        // TODO
        cb.selectedProperty().bind(button.pressedProperty() );
    }    
    
}
