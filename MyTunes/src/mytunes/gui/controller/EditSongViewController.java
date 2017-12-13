
package mytunes.gui.controller;

import java.io.IOException;
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
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import mytunes.be.Genre;
import mytunes.bll.BLLException;
import mytunes.gui.model.MainWindowModel;

/**
 * FXML Controller class
 * 
 * This view handles the editing of a song.
 *
 * @author Alex, Asbjørn og Jan
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
    private TextField txtfieldFileLocation;
    @FXML
    private ComboBox<Genre> cmboboxGenre;
    @FXML
    private ComboBox<String> cmboboxYear;
    @FXML
    private Button btnSaveChanges;
    
    //Variables for year and genre comboboxes.
    private ObservableList<String> yearOL = FXCollections.observableArrayList();
    private ObservableList<Genre> genreOL = FXCollections.observableArrayList();
    private int yearInInt;
    
    //Singleton variable to be able to use model information in this controller.
    MainWindowModel model;
    

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            //Using Singleton method to be sure there aren't 2 instances running.
            model = MainWindowModel.getInstance();
            
            cmboboxYear.setItems(yearGenerator());
            genreGetter();
            textSetter();
        } catch (BLLException ex) {
            Logger.getLogger(EditSongViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Handles the button that sends the edited song information to the database
     * and then closes the window.
     */
    @FXML
    private void handleEditSongAction() throws BLLException, IOException {
        stringToInt(cmboboxYear.getSelectionModel().getSelectedItem());
        cmboboxYear.getSelectionModel().getSelectedItem();

        if ((!txtfieldArtist.getText().isEmpty())
                && (!txtfieldTitle.getText().isEmpty())
                && (!txtfieldAlbum.getText().isEmpty())
                && (yearInInt != 0)
                && (cmboboxGenre.getSelectionModel().getSelectedItem() != null)
                && (!txtfieldFileLocation.getText().isEmpty())) {

            model.getChosenSong().setArtist(txtfieldArtist.getText());
            model.getChosenSong().setTitle(txtfieldTitle.getText());
            model.getChosenSong().setAlbum(txtfieldAlbum.getText());
            model.getChosenSong().setYear(yearInInt);
            model.getChosenSong().setGenre(cmboboxGenre.getSelectionModel().getSelectedItem());
            model.editSongInformation(model.getChosenSong());
            
            Stage stage = (Stage) btnSaveChanges.getScene().getWindow();
            stage.close();

        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING, "The song has not been edited. Please fill out all the fields and try again.", ButtonType.OK);
            alert.showAndWait();
        }

    }

    /**
     * Handles the button that adds a new genre to the database. If the genre
     * already exists, it auto-fills the combo box instead.
     */
    @FXML
    private String handleAddGenreAction(ActionEvent event) throws BLLException {
        for (int i = 0; i < genreOL.size(); i++) {
                if (cmboboxGenre.getItems().get(i).toString().trim().equalsIgnoreCase(txtfieldNewGenre.getText())) {
                    cmboboxGenre.getSelectionModel().select(i);
                    System.out.println("The genre " + txtfieldNewGenre.getText() + " attempted to add at index " + i + " already exists.");
                    return "Nothing";
                }
            }

            model.addGenre(txtfieldNewGenre.getText());
            genreGetter();
            cmboboxGenre.getSelectionModel().selectLast();
            return "Nothing";
    }

    /**
     * Converts the year strings into int for database use.
     */
    private void stringToInt(String s) {
        if (s.equals("Unknown")) {
            yearInInt = -1;
        } else {
            try {
                yearInInt = Integer.parseInt(s);
            } catch (NumberFormatException e) {
            }
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
        if (model.getChosenSong().getYear() == -1) {
            cmboboxYear.setValue("Unknown");
        } else {
            cmboboxYear.setValue(model.getChosenSong().getYear() + "");
        }
        txtfieldFileLocation.setText(model.getChosenSong().getPath());
    }

}
