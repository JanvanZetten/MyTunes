
package mytunes.gui.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import mytunes.bll.BLLException;

import mytunes.gui.model.MainModel;

/**
 * FXML Controller class
 * 
 * This view handles the addition of a new playlist into the database.
 *
 * @author Alex, Asbjørn og Jan
 */
public class AddPlaylistViewController implements Initializable {

    @FXML
    private TextField txtfieldTitle;
    @FXML
    private Button btnCreatePlaylist;
    
    //Singleton variable to be able to use model information in this controller.
    MainModel model;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //Using Singleton method to be sure there aren't 2 instances running.
        model = MainModel.getInstance();
    }

    /**
     * Sets the name of the playlist to the user submitted name. If no text is
     * entered into the text field, an error will appear.
     */
    @FXML
    public void handleButtonAction() throws BLLException {
        if (!txtfieldTitle.getText().isEmpty()) {
            model.createPlaylist(txtfieldTitle.getText());

            Stage stage = (Stage) btnCreatePlaylist.getScene().getWindow();
            stage.close();
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING, "The playlist has not been created. Please give it a name.", ButtonType.OK);
            alert.showAndWait();
        }
    }

}
