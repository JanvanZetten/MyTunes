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
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import mytunes.bll.BLLException;
import mytunes.gui.model.MainWindowModel;

/**
 * FXML Controller class
 *
 * @author Alex, Asbjørn og Jan
 */
public class EditPlaylistViewController implements Initializable {

    @FXML
    private TextField txtfieldTitle;
    @FXML
    private Button btnSaveChanges;
    
    MainWindowModel model;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        model = MainWindowModel.getInstance();
        textSetter();
    }    

    @FXML
    private void handleButtonAction(ActionEvent event) throws BLLException {
        model.editPlaylistInformation(
                model.getChosenPlaylist().getPlaylistId(), 
                txtfieldTitle.getText());
        Stage stage = (Stage) btnSaveChanges.getScene().getWindow();
        stage.close();
    }

    private void textSetter() {
        txtfieldTitle.setText(model.getChosenPlaylist().getName());
    }
    
}
