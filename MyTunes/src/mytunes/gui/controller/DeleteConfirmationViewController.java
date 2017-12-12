package mytunes.gui.controller;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import mytunes.bll.BLLException;

import mytunes.gui.model.MainWindowModel;

/**
 * FXML Controller class
 *
 * This view is used when attempting to delete a song or playlist. If Yes is
 * pressed, it uses the method in the model that initializes the deletion of the
 * selected song or playlist. If No is pressed, the window simply closes and
 * nothing happens.
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

        btnYes.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent ke) {
                if (ke.getCode().equals(KeyCode.ENTER)) {
                    try {
                        handleButtonActionDelete();
                    } catch (BLLException ex) {
                        Logger.getLogger(DeleteConfirmationViewController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        });
    }

    /**
     * Calls the method that initializes the deletion and then closes this view.
     */
    @FXML
    private void handleButtonActionDelete() throws BLLException {
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

    @FXML
    public void buttonPressed(KeyEvent e) throws BLLException {
        if (e.getCode().toString().equals("ENTER")) {
            handleButtonActionDelete();
        }
    }

}
