/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
 * @author Alex
 */
public class DeleteConfirmationViewController implements Initializable {

    @FXML
    private Label lblDeletedElement;
    @FXML
    private Button btnYes;
    @FXML
    private Button btnNo;
    MainWindowModel model;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        model = MainWindowModel.getInstance();
        lblDeletedElement.setText(model.getSelectedElement() + "?");
    }    

    @FXML
    private void handleButtonActionDelete(ActionEvent event) throws BLLException {
        model.setCurrentElementToBeDeleted(model.getSongOrPlaylist());
        Stage stage = (Stage) btnYes.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void handleButtonActionBack(ActionEvent event) {
        Stage stage = (Stage) btnNo.getScene().getWindow();
        stage.close();
    }
    
}
