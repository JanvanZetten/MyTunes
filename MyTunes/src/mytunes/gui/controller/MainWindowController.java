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
import javafx.scene.control.Label;

/**
 *
 * @author janvanzetten
 */
public class MainWindowController implements Initializable {
    
    
    @FXML
    private Label lblSongTitleTopBar;
    @FXML
    private Label lblSongAlbumTopBar;
    @FXML
    private Label lblSongArtistTopBar;
    @FXML
    private Label lblChosenPlaylist;
    @FXML
    private Label lblPlaylistInfo;
    @FXML
    private Label lblChosenPlaylist1;
    @FXML
    private Label lblCurrentTime;
    @FXML
    private Label lblTotalTimeSong;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void playSongAction(ActionEvent event) {
    }

    @FXML
    private void pauseSongAction(ActionEvent event) {
    }

    @FXML
    private void previusSongAction(ActionEvent event) {
    }

    @FXML
    private void nextSongAction(ActionEvent event) {
    }

    @FXML
    private void repeatSongsAction(ActionEvent event) {
    }

    @FXML
    private void shuffleSongsAction(ActionEvent event) {
    }

    @FXML
    private void addSongAction(ActionEvent event) {
    }

    @FXML
    private void addPlaylistAction(ActionEvent event) {
    }

    @FXML
    private void deletePlaylistAction(ActionEvent event) {
    }
    
}
