
package mytunes.gui.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
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
 * This view handles the editing of a playlist.
 *
 * @author Alex, Asbjørn og Jan
 */
public class EditPlaylistViewController implements Initializable {

    @FXML
    private TextField txtfieldTitle;
    @FXML
    private Button btnSaveChanges;

    //Singleton variable to be able to use model information in this controller.
    MainModel model;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //Using Singleton method to be sure there aren't 2 instances running.
        model = MainModel.getInstance();
        
        textSetter();
    }

    /**
     * When pressed, edits the playlist name to the newly given name. Not having
     * anything entered in the text field resutls in an error.
     */
    @FXML
    private void handleButtonAction(ActionEvent event) throws BLLException {
        if (!txtfieldTitle.getText().isEmpty()) {
            model.editPlaylistInformation(
                    model.getChosenPlaylist().getPlaylistId(),
                    txtfieldTitle.getText());
            Stage stage = (Stage) btnSaveChanges.getScene().getWindow();
            stage.close();
        }
        else {
            Alert alert = new Alert(Alert.AlertType.WARNING, "The playlist has not been edited. Please give the playlist a name and try again.", ButtonType.OK);
            alert.showAndWait();
        }
    }

    /**
     * Sets the text in the text field to be the same as the current name of
     * the playlist.
     */
    private void textSetter() {
        txtfieldTitle.setText(model.getChosenPlaylist().getName());
    }

}
