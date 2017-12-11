/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
import javafx.stage.Stage;
import mytunes.be.Genre;
import mytunes.bll.BLLException;
import mytunes.gui.model.MainWindowModel;

/**
 * FXML Controller class
 *
 * @author Alex, Asbjørn og Jan
 */
public class EditSongViewController implements Initializable
{

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

    MainWindowModel model;
    private ObservableList<String> yearOL = FXCollections.observableArrayList();
    private ObservableList<Genre> genreOL = FXCollections.observableArrayList();
    private int yearInInt;
    private File selectedFile;
    private Path from;
    private Path to;
    private boolean newFileSelected;
    private String lastPart;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        try
        {
            model = MainWindowModel.getInstance();
            cmboboxYear.setItems(yearGenerator());
            genreGetter();
            textSetter();
            File preset = new File(txtfieldFileLocation.getText());
            selectedFile = preset;
            directory();

        }
        catch (BLLException ex)
        {
            Logger.getLogger(EditSongViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Handles the button that sends the edited song information to the database
     * and then closes the window.
     */
    @FXML
    private void handleEditSongAction() throws BLLException, IOException
    {
        stringToInt(cmboboxYear.getSelectionModel().getSelectedItem());
        cmboboxYear.getSelectionModel().getSelectedItem();

        System.out.println(selectedFile);

        if ((!txtfieldArtist.getText().isEmpty())
                && (!txtfieldTitle.getText().isEmpty())
                && (!txtfieldAlbum.getText().isEmpty())
                && (yearInInt != 0)
                && (cmboboxGenre.getSelectionModel().getSelectedItem() != null)
                && (!txtfieldFileLocation.getText().isEmpty())
                && (txtfieldFileLocation.getText().equals(selectedFile.getName()))
                || txtfieldFileLocation.getText().equals(lastPart))
        {

            model.editSongInformation(model.getChosenSong().getSongId(),
                    txtfieldArtist.getText(),
                    txtfieldTitle.getText(),
                    txtfieldAlbum.getText(),
                    yearInInt,
                    cmboboxGenre.getSelectionModel().getSelectedItem(),
                    directory());
            if (newFileSelected == true)
            {
                if (!from.toString().equals(to.toString()))
                {
                    Files.copy(from, to, REPLACE_EXISTING);
                }
            }
            Stage stage = (Stage) btnSaveChanges.getScene().getWindow();
            stage.close();

        }
        else
        {
            Alert alert = new Alert(Alert.AlertType.WARNING, "The song has not been edited. Please fill out all the fields and try again.", ButtonType.OK);
            alert.showAndWait();
        }

    }

    /**
     * Handles and opens a file searcher so a file path can be found.
     */
    @FXML
    private void handleFileLocationSearcher() throws IOException
    {
        FileChooser fc = new FileChooser();
        String currentDir = System.getProperty("user.dir") + File.separator;
        File dir = new File(currentDir);
        fc.setInitialDirectory(dir);
        fc.setTitle("Attach a file");
        selectedFile = fc.showOpenDialog(null);

        if (selectedFile != null)
        {
            from = Paths.get(selectedFile.toURI());
            to = Paths.get(dir + "/music/" + selectedFile.getName());
            txtfieldFileLocation.setText(selectedFile.getName());
            newFileSelected = true;
        }
    }

    /**
     * If no new file has been selected through the FileChooser, then the
     * program needs to cut down the file path so that only the name of the file
     * is selected. If another file is selected through the FileChooser, then it
     * proceeds as normal.
     */
    private String directory()
    {
        if (newFileSelected == false)
        {
            File dir = new File(selectedFile + "");
            String[] splitDir = dir.toString().split("\\\\");
            lastPart = splitDir[splitDir.length - 1];
            txtfieldFileLocation.setText(lastPart);
            return lastPart;
        }
        else if (newFileSelected == true)
        {
            return "music/" + selectedFile;
        }
        return "Nothing";
    }

    /**
     * Handles the button that adds a new genre to the database.
     */
    @FXML
    private void handleAddGenreAction(ActionEvent event) throws BLLException
    {
        model.addGenre(txtfieldNewGenre.getText());
        genreGetter();
        cmboboxGenre.getSelectionModel().selectLast();
    }

    /**
     * Converts the year strings into int for database use.
     */
    private void stringToInt(String s)
    {
        if (s.equals("Unknown"))
        {
            yearInInt = -1;
        }
        else
        {
            try
            {
                yearInInt = Integer.parseInt(s);
            }
            catch (NumberFormatException e)
            {
            }
        }
    }

    /**
     * Takes the current year from the calendar and adds all years down to 1700
     * to the cmboboxYear.
     */
    private ObservableList<String> yearGenerator()
    {
        int yearCounter = Calendar.getInstance().get(Calendar.YEAR);
        yearOL.add("Unknown");
        for (int i = yearCounter; i >= 1700; i--)
        {
            yearOL.add(i + "");
        }
        return yearOL;
    }

    /**
     * Gets and sets all the genres that are available into a combo box.
     */
    private void genreGetter() throws BLLException
    {
        model.getAllGenres();
        genreOL = model.getGenres();
        cmboboxGenre.setItems(genreOL);
    }

    /**
     * Presets the information about the song into the text fields.
     */
    private void textSetter()
    {
        txtfieldTitle.setText(model.getChosenSong().getTitle());
        txtfieldArtist.setText(model.getChosenSong().getArtist());
        txtfieldAlbum.setText(model.getChosenSong().getAlbum());
        cmboboxGenre.setValue(model.getChosenSong().getGenre());
        if (model.getChosenSong().getYear() == -1)
        {
            cmboboxYear.setValue("Unknown");
        }
        else
        {
            cmboboxYear.setValue(model.getChosenSong().getYear() + "");
        }
        txtfieldFileLocation.setText(model.getChosenSong().getPath());
    }

}
