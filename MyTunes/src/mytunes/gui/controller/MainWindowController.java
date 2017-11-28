/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mytunes.gui.controller;

import java.io.File;
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
public class MainWindowController implements Initializable
{

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

    MainWindowModel model;

    @Override
    public void initialize(URL url, ResourceBundle rb)
    {

        model = new MainWindowModel();

        //add the playlists to the view
        model.addAllPlaylistsToGUI();
        listViewPlaylists.setItems(model.getPlaylists());

        //add the songs to the view
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

        tblviewMaster.setItems(model.getSongs());

        setSongsOnTableview(model.getAllSongsPlaylist());

    }

    @FXML
    private void playSongAction(ActionEvent event)
    {
        model.playMedia();
    }

    @FXML
    private void pauseSongAction(ActionEvent event)
    {
        model.pauseMedia();
    }

    @FXML
    private void previusSongAction(ActionEvent event)
    {
        model.previousMedia();
    }

    @FXML
    private void nextSongAction(ActionEvent event)
    {
        model.nextMedia();
    }

    @FXML
    private void repeatSongsAction(ActionEvent event)
    {
    }

    @FXML
    private void shuffleSongsAction(ActionEvent event)
    {
    }

    /**
     * Clicking the "Add" button under the songlist causes a modal window that
     * assists the user in adding music to the library to appear.
     */
    @FXML
    private void addSongAction(ActionEvent event) throws IOException
    {
        Stage newStage = new Stage();
        newStage.initModality(Modality.APPLICATION_MODAL);
        FXMLLoader fxLoader = new FXMLLoader(getClass().getResource("/mytunes/gui/view/AddSongView.fxml"));
        Parent root = fxLoader.load();
        Scene scene = new Scene(root);
        newStage.setScene(scene);
        newStage.show();
    }

    /**
     * Clicking the "Add" button under the playlists causes a modal window that
     * assists the user in making a new playlist to appear.
     */
    @FXML
    private void addPlaylistAction(ActionEvent event) throws IOException
    {
        Stage newStage = new Stage();
        newStage.initModality(Modality.APPLICATION_MODAL);
        FXMLLoader fxLoader = new FXMLLoader(getClass().getResource("/mytunes/gui/view/AddPlaylistView.fxml"));
        Parent root = fxLoader.load();
        Scene scene = new Scene(root);
        newStage.setScene(scene);
        newStage.show();
    }

    /**
     * Clicking the "Delete" button under the playlists causes a confirmation
     * window to appear.
     */
    @FXML
    private void deletePlaylistAction(ActionEvent event) throws IOException
    {
        Stage newStage = new Stage();
        newStage.initModality(Modality.APPLICATION_MODAL);
        FXMLLoader fxLoader = new FXMLLoader(getClass().getResource("/mytunes/gui/view/DeleteConfirmationView.fxml"));
        Parent root = fxLoader.load();
        Scene scene = new Scene(root);
        newStage.setScene(scene);
        newStage.show();
    }

    /**
     * Clicking the "Delete" button under the songlist causes a confirmation
     * window to appear.
     */
    @FXML
    private void deleteSongAction(ActionEvent event) throws IOException
    {
        Stage newStage = new Stage();
        newStage.initModality(Modality.APPLICATION_MODAL);
        FXMLLoader fxLoader = new FXMLLoader(getClass().getResource("/mytunes/gui/view/DeleteConfirmationView.fxml"));
        Parent root = fxLoader.load();
        Scene scene = new Scene(root);
        newStage.setScene(scene);
        newStage.show();
    }

    /**
     * loads the clikced playlist to the song view
     *
     * @param event
     */
    @FXML
    private void clickedPlaylist(MouseEvent event)
    {
        if (listViewPlaylists.getSelectionModel().getSelectedItem() != null)
        {
            setSongsOnTableview(listViewPlaylists.getSelectionModel().getSelectedItem());
        }
    }

    /**
     * sets the songs from the given playlist in the table view and updates the
     * labels to match with the playlist
     *
     * @param playlist the playlist to show
     */
    private void setSongsOnTableview(Playlist playlist)
    {
        model.setSongs(playlist);
        lblChosenPlaylist.setText(playlist.getName());
        lblPlaylistInfo.setText(playlist.getSongs().size() + " song in this playlist");
    }

}
