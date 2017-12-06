/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mytunes.gui.controller;

import com.google.common.io.Files;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Calendar;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import mytunes.be.Genre;
import mytunes.bll.BLLException;
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
    private ComboBox<Genre> cmboboxGenre;
    @FXML
    private ComboBox<String> cmboboxYear;
    @FXML
    private Button btnSaveChanges;
    @FXML
    private Button btnAddGenre;

    private Path to;
    private Path from;
    private File selectedFile;
    private ObservableList<String> yearOL = FXCollections.observableArrayList();
    private ObservableList<Genre> genreOL = FXCollections.observableArrayList();

    private int yearInInt;
    MainWindowModel model;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            model = MainWindowModel.getInstance();
            model.setCurrentAddMenu("song");
            yearGenerator();
            cmboboxYear.setItems(yearGenerator());
            genreGetter();
        } catch (BLLException ex) {
            Logger.getLogger(AddSongViewController.class.getName()).log(Level.SEVERE, null, ex);
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
     * Handles the funtion to add songs to the database. The if statements make
     * sure no fields are left empty. Also handles the function to convert year
     * strings into int so they can be used by the database. Closes the window
     * when used. If all fields haven't been filled, it will instead open an
     * area telling the user to fill out all fields.
     */
    @FXML
    private void handleAddSongAction(ActionEvent event) throws BLLException, IOException {
        stringToInt(cmboboxYear.getSelectionModel().getSelectedItem());
        cmboboxYear.getSelectionModel().getSelectedItem();

        if (!txtfieldArtist.getText().isEmpty()) {
            if (!txtfieldTitle.getText().isEmpty()) {
                if (!txtfieldAlbum.getText().isEmpty()) {
                    if (yearInInt != 0) {
                        if (cmboboxGenre.getSelectionModel().getSelectedItem() != null) {
                            if (!txtfieldFileLocation.getText().isEmpty()) {
                                if (txtfieldFileLocation.getText().equals(selectedFile.toString())) {
                                    model.createSong(
                                            txtfieldArtist.getText(),
                                            txtfieldTitle.getText(),
                                            txtfieldAlbum.getText(),
                                            yearInInt,
                                            cmboboxGenre.getSelectionModel().getSelectedItem(),
                                            txtfieldFileLocation.getText());
//                                    if (!from.toString().equals(to.toString())) {
                                    Files.copy(from.toFile(), to.toFile());
//                                    }

                                    Stage stage = (Stage) btnSaveChanges.getScene().getWindow();
                                    stage.close();
                                }
                            }
                        }
                    }
                }
            }
        } else {
            Stage newStage = new Stage();
            newStage.initModality(Modality.APPLICATION_MODAL);
            FXMLLoader fxLoader = new FXMLLoader(getClass().getResource("/mytunes/gui/view/CannotAddView.fxml"));
            Parent root = fxLoader.load();
            Scene scene = new Scene(root);
            newStage.setScene(scene);
            newStage.show();
        }

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
     * Handles the button that adds a new genre to the database.
     */
    @FXML
    private void handleAddGenreAction(ActionEvent event) throws BLLException {
        model.addGenre(txtfieldNewGenre.getText());
        genreGetter();
        cmboboxGenre.getSelectionModel().selectLast();
    }

    /**
     * Handles and opens a file searcher so a file path can be found.
     */
    @FXML
    private void handleFileLocationSearcher() throws IOException {
        FileChooser fc = new FileChooser();
        fc.setTitle("Attach a file");
        selectedFile = fc.showOpenDialog(null);

        if (selectedFile != null) {
            from = Paths.get(selectedFile.toURI());
            
            File file = new File("./MyTunes/Mytunes/");
            
            to = Paths.get(file + "/" + selectedFile.getName());
            txtfieldFileLocation.setText(selectedFile.toString());
            System.out.println(file);
            System.out.println(to);
        }
    }
}
