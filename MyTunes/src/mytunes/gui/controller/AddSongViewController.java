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
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import mytunes.gui.model.MainWindowModel;

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
    private ObservableList<String> yearOL = FXCollections.observableArrayList();
    @FXML
    private Button btnAddGenre;
    private int yearInInt;
    MainWindowModel model;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        model = MainWindowModel.getInstance();
        yearGenerator();
        genreGetter();
        cmboboxYear.setItems(yearGenerator());
    }

    /**
     * Takes the current year from the calendar and adds all years down to 1700
     * to the cmboboxYear.
     */
    private ObservableList<String> yearGenerator() {
        int yearCounter = Calendar.getInstance().get(Calendar.YEAR);
        yearOL.add("Unknown");
        for (int i = yearCounter; i >= 1700; i--) {
            yearOL.add(i + "");
        }
        return yearOL;
    }
    
    private void genreGetter() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void handleButtonAction() {

        stringToInt(cmboboxYear.getSelectionModel().getSelectedItem());
        cmboboxYear.getSelectionModel().getSelectedItem();
        model.createSong(txtfieldArtist.getText(), txtfieldTitle.getText(), 
        txtfieldAlbum.getText(), yearInInt, Genre genre, txtfieldFileLocation.getText());
    }

    private int stringToInt(String s) {
        try {
            Integer.valueOf(s);
            return yearInInt;
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    @FXML
    private void handleAddSongAction(ActionEvent event) {
    }

    @FXML
    private void handleAddGenreAction(ActionEvent event) {
        model.addGenre();
    }

    

}
