/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mytunes.gui.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Stage;

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
    private void addSongAction() throws IOException {
        Stage newStage = new Stage();
        newStage.initModality(Modality.APPLICATION_MODAL);
        FXMLLoader fxLoader = new FXMLLoader(getClass().getResource("/mytunes/gui/view/AddSongView.fxml"));
        Parent root = fxLoader.load();
        Scene scene = new Scene(root);
        newStage.setScene(scene);
        newStage.show();
    }

    @FXML
    private void addPlaylistAction(ActionEvent event) {
    }

    @FXML
    private void deletePlaylistAction(ActionEvent event) {
    }
    
}
