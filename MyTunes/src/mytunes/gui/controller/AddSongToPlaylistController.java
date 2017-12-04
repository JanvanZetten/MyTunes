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
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import mytunes.be.Playlist;
import mytunes.bll.BLLException;
import mytunes.gui.model.MainWindowModel;

/**
 * FXML Controller class
 *
 * @author janvanzetten
 */
public class AddSongToPlaylistController implements Initializable {

    @FXML
    private ListView<Playlist> listviewPlaylist;
    
    MainWindowModel model;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        model = MainWindowModel.getInstance();
        model.setCurrentAddMenu("playlist");
        listviewPlaylist.setItems(model.getPlaylists());
        
    }    

    /**
     * for the button for adding a song to playlist
     * @param event
     * @throws BLLException 
     */
    @FXML
    private void btnAddToPLaylistAction(ActionEvent event) throws BLLException {
        model.addSongToPlaylist(listviewPlaylist.getSelectionModel().getSelectedItem(), model.getChosenSong());
        Stage stage = (Stage) listviewPlaylist.getScene().getWindow();
        stage.close();
    }

    /**
     * Cancel button, closes the window
     * @param event 
     */
    @FXML
    private void btnCancelAction(ActionEvent event) {
        Stage stage = (Stage) listviewPlaylist.getScene().getWindow();
        stage.close();
    }
    
}
