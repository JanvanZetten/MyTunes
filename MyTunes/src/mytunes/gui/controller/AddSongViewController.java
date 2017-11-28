/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mytunes.gui.controller;

import java.net.URL;
import java.util.Calendar;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author janvanzetten
 */
public class AddSongViewController implements Initializable {

    @FXML
    private TextField txtfieldTitle;
    @FXML
    private TextField txtfieldArtist;
    @FXML
    private TextField txtfieldNewGenre;
    @FXML
    private TextField txtfieldAlbum;
    @FXML
    private Button btnFileLocation;
    @FXML
    private TextField txtfieldFileLocation;
    @FXML
    private ComboBox<?> cmboboxGenre;
    @FXML
    private ComboBox<String> cmboboxYear;
    @FXML
    private Button btnSaveChanges;
    private ObservableList<String> ol = FXCollections.observableArrayList();

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        yearGenerator();
        cmboboxYear.setItems(yearGenerator());
    }    

    /**
     * Takes the current year from the calendar and adds all years down to 1700
     * to the cmboboxYear.
     */
    private ObservableList<String> yearGenerator() {
        int yearCounter = Calendar.getInstance().get(Calendar.YEAR);
        ol.add("Unknown");
        for (int i = yearCounter; i >= 1700; i--) {
            ol.add(i + "");
        }
        return ol;
    }
    
}
