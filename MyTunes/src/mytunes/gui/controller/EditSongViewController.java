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
import javafx.stage.Window;
import mytunes.be.Genre;
import mytunes.be.Song;
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
    private int currentSongId;

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
        } catch (BLLException ex) {
            Logger.getLogger(EditSongViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void getAndSetAllSongInformation() {
//        model.getAllSongInformation();
    }

    @FXML
    private void handleEditSongView(ActionEvent event) throws BLLException {
        stringToInt(cmboboxYear.getSelectionModel().getSelectedItem());
        cmboboxYear.getSelectionModel().getSelectedItem();
        
        model.editSongInformation(1, 
                                  txtfieldArtist.getText(),
                                  txtfieldTitle.getText(),
                                  txtfieldAlbum.getText(),
                                  yearInInt,
                                  cmboboxGenre.getSelectionModel().getSelectedItem(),
                                  txtfieldFileLocation.getText());
    }
    
    @FXML
    private void handleFileLocationSearcher(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Upload song");
        fileChooser.showOpenDialog(fileChooserStage);
    }

    @FXML
    private void handleAddGenreAction(ActionEvent event) throws BLLException {
        model.addGenre(txtfieldNewGenre.getText());
        genreGetter();
        cmboboxGenre.getSelectionModel().selectLast();
    }
    
    private int stringToInt(String s) {
        try {
            yearInInt = Integer.parseInt(s);
            return yearInInt;
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private ObservableList<String> yearGenerator() {
        int yearCounter = Calendar.getInstance().get(Calendar.YEAR);
        yearOL.add("Unknown");
        for (int i = yearCounter; i >= 1700; i--) {
            yearOL.add(i + "");
        }
        return yearOL;
    }

    private void genreGetter() throws BLLException {
        model.getAllGenres();
        genreOL = model.getGenres();
        cmboboxGenre.setItems(genreOL);
    }

}
