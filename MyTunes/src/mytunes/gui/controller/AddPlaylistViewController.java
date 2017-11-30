/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mytunes.gui.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import mytunes.bll.BLLException;

import mytunes.gui.model.MainWindowModel;

/**
 * FXML Controller class
 *
 * @author Alex
 */
public class AddPlaylistViewController implements Initializable {

    @FXML
    private TextField txtfieldTitle;
    @FXML
    private Button btnCreatePlaylist;
    
    MainWindowModel model;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        model = MainWindowModel.getInstance();
    }    
    
    @FXML
    public void handleButtonAction() throws BLLException {
        model.createPlaylist(txtfieldTitle.getText());
        
        Stage stage = (Stage) btnCreatePlaylist.getScene().getWindow();
        stage.close();
    }
    
}
