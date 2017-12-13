package mytunes.gui.controller;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import static java.nio.file.StandardCopyOption.*;
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
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import mytunes.be.Genre;
import mytunes.bll.BLLException;
import mytunes.dal.AudioMedia;
import mytunes.dal.DALException;
import mytunes.gui.model.MainWindowModel;

/**
 * FXML Controller class
 *
 * This view handles the addition of a new song into the database.
 *
 * @author Alex, Asbjørn og Jan
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
    private TextField txtfieldFileLocation;
    @FXML
    private ComboBox<Genre> cmboboxGenre;
    @FXML
    private ComboBox<String> cmboboxYear;
    @FXML
    private Button btnSaveChanges;

    //Variables used for the file chooser.
    private Path to;
    private Path from;
    private File selectedFile;

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

            model.setCurrentAddMenu("song");
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

        if ((!txtfieldArtist.getText().isEmpty())
                && (!txtfieldTitle.getText().isEmpty())
                && (!txtfieldAlbum.getText().isEmpty())
                && (yearInInt != 0)
                && (cmboboxGenre.getSelectionModel().getSelectedItem() != null)
                && (!txtfieldFileLocation.getText().isEmpty())
                && (txtfieldFileLocation.getText().equals(selectedFile.getName()))) {
            model.createSong(
                    txtfieldArtist.getText(),
                    txtfieldTitle.getText(),
                    txtfieldAlbum.getText(),
                    yearInInt,
                    cmboboxGenre.getSelectionModel().getSelectedItem(),
                    "music/" + selectedFile.getName());

            if (!from.toString().equals(to.toString())) {
                Files.copy(from, to, REPLACE_EXISTING);
            }

            Stage stage = (Stage) btnSaveChanges.getScene().getWindow();
            stage.close();
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING, "The song has not been created. Please fill out all the fields and try again.", ButtonType.OK);
            alert.showAndWait();
        }
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
     * Handles and opens a file searcher so a file path can be found.
     */
    @FXML
    private void handleFileLocationSearcher() throws IOException {
        ExtensionFilter filter = new ExtensionFilter("Audio Files", "*.mp3", "*.wav", "*.flac", "*.aiff");
        FileChooser fc = new FileChooser();
        fc.getExtensionFilters().add(filter);
        String currentDir = System.getProperty("user.dir") + File.separator;
        File dir = new File(currentDir);
        fc.setInitialDirectory(dir);
        fc.setTitle("Attach a file");
        selectedFile = fc.showOpenDialog(null);

        if (selectedFile != null) {
            from = Paths.get(selectedFile.toURI());
            to = Paths.get(dir + "/music/" + selectedFile.getName());
            txtfieldFileLocation.setText(selectedFile.getName());
            try {
                AudioMedia am = new AudioMedia(from.toFile());

                txtfieldArtist.setText(am.getArtist().trim());
                txtfieldTitle.setText(am.getTitle().trim());
                txtfieldAlbum.setText(am.getAlbum().trim());

                int count = 0;
                for (Genre genre : genreOL) {
                    if (genre.getGenre().equalsIgnoreCase(am.getGenre().trim())) {
                        cmboboxGenre.getSelectionModel().select(genre);
                        count = 1;
                        break;
                    }
                }
                if (count == 0) {
                    if (am.getGenre().trim().isEmpty()) {
                        cmboboxGenre.getSelectionModel().selectFirst();
                    } else {
                        txtfieldNewGenre.setText(am.getGenre().trim());
                    }
                }

                if (am.getYear() == -1) {
                    cmboboxYear.getSelectionModel().selectFirst();
                } else if (cmboboxYear.getItems().contains(String.valueOf(am.getYear()))) {
                    cmboboxYear.getSelectionModel().select(String.valueOf(am.getYear()));
                }
            } catch (DALException ex) {
                txtfieldArtist.setText("");
                txtfieldTitle.setText("");
                txtfieldAlbum.setText("");
                txtfieldNewGenre.setText("");
                cmboboxGenre.getSelectionModel().selectFirst();
                cmboboxYear.getSelectionModel().selectFirst();
            }
        }
    }
}
