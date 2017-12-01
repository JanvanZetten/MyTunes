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

import mytunes.gui.model.MainWindowModel;

/**
 * FXML Controller class
 *
 * @author Alex
 */
public class CannotAddViewController implements Initializable {

    @FXML
    private Button btnBack;
    @FXML
    private Label lblCategory;
    
    MainWindowModel model;

    /**
     * Initializes the controller class and sets the label to the correct category.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        model = MainWindowModel.getInstance();
        lblCategory.setText("The " + model.getCurrentAddMenu() + " cannot be created.");
    }    

    /**
     * Closes the window on button press.
     */
    @FXML
    private void handleBackButtonAction(ActionEvent event) {
        Stage stage = (Stage) btnBack.getScene().getWindow();
        stage.close();
    }
    
}
