
package mytunes.gui.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import mytunes.bll.BLLException;

import mytunes.gui.model.MainWindowModel;

/**
 * FXML Controller class
 * 
 * This view is used when attempting to delete a song or playlist. If Yes is 
 * pressed, it uses the method in the model that initializes the deletion of 
 * the selected song or playlist. If No is pressed, the window simply closes 
 * and nothing happens.
 *
 * @author Alex, Asbjørn og Jan
 */
public class DeleteConfirmationViewController implements Initializable {

    @FXML
    private Label lblDeletedElement;
    @FXML
    private Button btnYes;
    @FXML
    private Button btnNo;
    
    //Singleton variable to be able to use model information in this controller.
    MainWindowModel model;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        //Using Singleton method to be sure there aren't 2 instances running.
        model = MainWindowModel.getInstance();
        
        lblDeletedElement.setText(model.getSelectedElement() + "?");
    }

    /**
     * Calls the method that initializes the deletion and then closes this view.
     */
    @FXML
    private void handleButtonActionDelete(ActionEvent event) throws BLLException {
        model.setCurrentElementToBeDeleted(model.getSongOrPlaylist());
        Stage stage = (Stage) btnYes.getScene().getWindow();
        stage.close();
    }

    /**
     * Closes this view and does nothing else.
     */
    @FXML
    private void handleButtonActionBack(ActionEvent event) {
        Stage stage = (Stage) btnNo.getScene().getWindow();
        stage.close();
    }

}
