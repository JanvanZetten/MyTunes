/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mytunes.gui.controller;

import java.net.URL;
import java.util.Calendar;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;
import mytunes.be.Genre;
import mytunes.bll.BLLException;
import mytunes.gui.model.MainWindowModel;

/**
 * FXML Controller class
 *
 * @author Alex
 */
public class EditSongViewController implements Initializable {

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
    private ComboBox<Genre> cmboboxGenre;
    @FXML
    private ComboBox<String> cmboboxYear;
    @FXML
    private Button btnSaveChanges;
    private Window fileChooserStage;

    MainWindowModel model;
    private ObservableList<String> yearOL = FXCollections.observableArrayList();
    private ObservableList<Genre> genreOL = FXCollections.observableArrayList();
    private int yearInInt;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            model = MainWindowModel.getInstance();
            yearGenerator();
            cmboboxYear.setItems(yearGenerator());
            genreGetter();
            textSetter();
        } catch (BLLException ex) {
            Logger.getLogger(EditSongViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Handles the button that sends the edited song information to the 
     * database and then closes the window.
     */
    @FXML
    private void handleEditSongAction() throws BLLException {
        stringToInt(cmboboxYear.getSelectionModel().getSelectedItem());
        cmboboxYear.getSelectionModel().getSelectedItem();
        
        model.editSongInformation(model.getCurrentSongId(),
                                  txtfieldArtist.getText(),
                                  txtfieldTitle.getText(),
                                  txtfieldAlbum.getText(),
                                  yearInInt,
                                  cmboboxGenre.getSelectionModel().getSelectedItem(),
                                  txtfieldFileLocation.getText());
        Stage stage = (Stage) btnSaveChanges.getScene().getWindow();
        stage.close();
    }
    
    /**
     * Handles and opens a file searcher so a file path can be found.
     */
    @FXML
    private void handleFileLocationSearcher(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Upload song");
        fileChooser.showOpenDialog(fileChooserStage);
    }

    /**
     * Handles the button that adds a new genre to the database.
     */
    @FXML
    private void handleAddGenreAction(ActionEvent event) throws BLLException {
        model.addGenre(txtfieldNewGenre.getText());
        genreGetter();
        cmboboxGenre.getSelectionModel().selectLast();
    }
    
    /**
     * Converts the year strings into int for database use.
     */
    private int stringToInt(String s) {
        try {
            yearInInt = Integer.parseInt(s);
            return yearInInt;
        } catch (NumberFormatException e) {
            return 0;
        }
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

    /**
     * Gets and sets all the genres that are available into a combo box.
     */
    private void genreGetter() throws BLLException {
        model.getAllGenres();
        genreOL = model.getGenres();
        cmboboxGenre.setItems(genreOL);
    }

    /**
     * Presets the information about the song into the text fields.
     */
    private void textSetter() {
        txtfieldTitle.setText(model.getChosenSong().getTitle());
        txtfieldArtist.setText(model.getChosenSong().getArtist());
        txtfieldAlbum.setText(model.getChosenSong().getAlbum());
        cmboboxGenre.setValue(model.getChosenSong().getGenre());
        cmboboxYear.setValue(model.getChosenSong().getYear() + "");
        txtfieldFileLocation.setText(model.getChosenSong().getpath());
    }

}
