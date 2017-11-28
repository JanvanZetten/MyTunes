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
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import mytunes.be.Playlist;
import mytunes.be.Song;
import mytunes.gui.model.MainWindowModel;

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
    private Label lblCurrentTime;
    @FXML
    private Label lblTotalTimeSong;
    @FXML
    private ListView<Playlist> listViewPlaylists;
    
    MainWindowModel model;
    @FXML
    private TableView<Song> tblviewMaster;
    @FXML
    private TableColumn<Song, String> tblviewSong;
    @FXML
    private TableColumn<Song, String> tblviewArtist;
    @FXML
    private TableColumn<Song, String> tblviewAlbum;
    @FXML
    private TableColumn<Song, String> tblviewGenre;
    @FXML
    private TableColumn<Song, String> tblviewYear;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        //add the playlists to the view
        model = new MainWindowModel();
        model.addAllPlaylistsToGUI();
        listViewPlaylists.setItems(model.getPlaylists());
        
        
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
    private void addSongAction(ActionEvent event) throws IOException {
        Stage newStage = new Stage();
        newStage.initModality(Modality.APPLICATION_MODAL);
        FXMLLoader fxLoader = new FXMLLoader(getClass().getResource("/mytunes/gui/view/AddSongView.fxml"));
        Parent root = fxLoader.load();
        Scene scene = new Scene(root);
        newStage.setScene(scene);
        newStage.show();
    }

    @FXML
    private void addPlaylistAction(ActionEvent event) throws IOException {
        Stage newStage = new Stage();
        newStage.initModality(Modality.APPLICATION_MODAL);
        FXMLLoader fxLoader = new FXMLLoader(getClass().getResource("/mytunes/gui/view/AddPlaylistView.fxml"));
        Parent root = fxLoader.load();
        Scene scene = new Scene(root);
        newStage.setScene(scene);
        newStage.show();
    }

    @FXML
    private void deletePlaylistAction(ActionEvent event) {
    }

    @FXML
    private void deleteSongAction(ActionEvent event) {
    }

    @FXML
    private void clickedPlaylist(MouseEvent event) {
        tblviewSong.setCellValueFactory(
            new PropertyValueFactory("title"));
        tblviewArtist.setCellValueFactory(
            new PropertyValueFactory("artist"));
        tblviewAlbum.setCellValueFactory(
            new PropertyValueFactory("album"));
        tblviewGenre.setCellValueFactory(
            new PropertyValueFactory("genre"));
        tblviewYear.setCellValueFactory(
            new PropertyValueFactory("year"));
        
        tblviewMaster.setItems(model.setSongs(listViewPlaylists.getSelectionModel().getSelectedItem()));
        
        lblChosenPlaylist.setText(listViewPlaylists.getSelectionModel().getSelectedItem().getName());
    }
    
}
