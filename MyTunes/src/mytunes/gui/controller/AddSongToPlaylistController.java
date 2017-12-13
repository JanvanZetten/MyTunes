
package mytunes.gui.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import mytunes.be.Playlist;
import mytunes.bll.BLLException;
import mytunes.gui.model.MainModel;

/**
 * FXML Controller class
 * 
 * This view handles the ability to add a song to a playlist.
 *
 * @author Alex, Asbjørn og Jan
 */
public class AddSongToPlaylistController implements Initializable {

    @FXML
    private ListView<Playlist> listviewPlaylist;

    //Singleton variable to be able to use model information in this controller.
    MainModel model;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //Using Singleton method to be sure there aren't 2 instances running.
        model = MainModel.getInstance();
        
        //Sets the playlist options and removes My Library from the list.
        ObservableList<Playlist> playlists = FXCollections.observableArrayList();
        playlists.addAll(model.getPlaylists());
        listviewPlaylist.setItems(playlists);
        listviewPlaylist.getItems().remove(0);
    }

    /**
     * For the button for adding a song to playlist
     */
    @FXML
    private void btnAddToPLaylistAction(ActionEvent event) throws BLLException {

        model.addSongToPlaylist(listviewPlaylist.getSelectionModel().getSelectedItem(), model.getChosenSong());
        Stage stage = (Stage) listviewPlaylist.getScene().getWindow();
        stage.close();
    }

    /**
     * Cancel button, closes the window and does nothing more.
     */
    @FXML
    private void btnCancelAction(ActionEvent event) {
        Stage stage = (Stage) listviewPlaylist.getScene().getWindow();
        stage.close();
    }

    /**
     * If the mouse is double clicked, it adds the song to the clicked playlist.
     * @param event
     * @throws BLLException 
     */
    @FXML
    private void handleMouseClickedAction(MouseEvent event) throws BLLException {
        if (event.getButton().equals(MouseButton.PRIMARY)
                && event.getClickCount() == 2) {
            model.addSongToPlaylist(listviewPlaylist.getSelectionModel().getSelectedItem(), model.getChosenSong());
            Stage stage = (Stage) listviewPlaylist.getScene().getWindow();
            stage.close();
        }
    }
}
