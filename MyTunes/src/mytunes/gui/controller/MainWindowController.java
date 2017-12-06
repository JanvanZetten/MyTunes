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
import java.util.logging.Level;
import javafx.beans.binding.Bindings;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Slider;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import mytunes.be.Playlist;
import mytunes.be.Song;
import mytunes.gui.model.MainWindowModel;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.shape.Rectangle;
import mytunes.be.Genre;
import mytunes.bll.BLLException;

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
    @FXML
    private Slider volumeSlider;
    @FXML
    private TextField textfieldFilter;
    @FXML
    private Button btnFilter;
    @FXML
    private ProgressBar progressBar;
    @FXML
    private Rectangle topBar;

    MainWindowModel model;
    @FXML
    private ImageView imageviewMute;
    @FXML
    private Button BtnRepeat;
    @FXML
    private Button Btnshuffle;
    @FXML
    private ImageView imageviewPlayPause;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        //Using Singleton method to be sure there aren't 2 instances running.
        model = MainWindowModel.getInstance();

        //add the playlists to the view
        model.addAllPlaylistsToGUI();

        //set observables
        setTableItems();
        lblSongArtistTopBar.textProperty().bind(Bindings.convert(model.getArtist()));
        lblSongTitleTopBar.textProperty().bind(Bindings.convert(model.getTitle()));
        lblSongAlbumTopBar.textProperty().bind(Bindings.convert(model.getAlbum()));
        lblCurrentTime.textProperty().bind(Bindings.convert(model.getCurrentTime()));
        lblTotalTimeSong.textProperty().bind(Bindings.convert(model.getDurationTime()));
        progressBar.progressProperty().bind(model.getProgress());

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

        //volumeSlider
        model.volumeSliderSetup(volumeSlider);

        //Sets the context menus for playlists and songs.
        contextSongMenuHandler();
        contextPlaylistMenuHandler();
    }

    /**
     * Added because I could not get scene in initialize().
     */
    public void afterInitialize() {
        topBar.widthProperty().bind(topBar.getScene().widthProperty());
    }

    /**
     * Updates the table and is used after changes are made so the program
     * updates live.
     */
    private void setTableItems() {
        tblviewMaster.setItems(model.getSongs());
        listViewPlaylists.setItems(model.getPlaylists());
        model.addAllPlaylistsToGUI();
        setSongsOnTableview(model.getAllSongsPlaylist());
    }

    /**
     * sets the songs from the given playlist in the table view and updates the
     * labels to match with the playlist
     *
     * @param playlist the playlist to show
     */
    private void setSongsOnTableview(Playlist playlist) {
        model.setSongs(playlist);
        lblChosenPlaylist.setText(playlist.getName());
        if (playlist.getSongs().size() > 1) {
            lblPlaylistInfo.setText(playlist.getSongs().size() + " songs in this playlist");
        } else if (playlist.getSongs().size() == 1) {
            lblPlaylistInfo.setText(playlist.getSongs().size() + " song in this playlist");
        } else if (playlist.getSongs().size() == 0) {
            lblPlaylistInfo.setText("No songs in this playlist");
        } else {
            lblPlaylistInfo.setText("");
        }
    }

    /**
     * Starts a new window by sending in the name of the view in the parameters.
     */
    private void startModalWindow(String windowView) throws IOException {
        Stage newStage = new Stage();
        newStage.initModality(Modality.APPLICATION_MODAL);
        FXMLLoader fxLoader = new FXMLLoader(getClass().getResource("/mytunes/gui/view/" + windowView + ".fxml"));
        Parent root = fxLoader.load();
        Scene scene = new Scene(root);
        newStage.setScene(scene);
        newStage.showAndWait();
        setTableItems();
    }

    /**
     * Clicking the "Add" button under the songlist causes a modal window that
     * assists the user in adding music to the library to appear.
     */
    @FXML
    private void addSongAction(ActionEvent event) throws IOException {
        startModalWindow("AddSongView");
    }

    /**
     * Clicking the "Add" button under the playlists causes a modal window that
     * assists the user in making a new playlist to appear.
     */
    @FXML
    private void addPlaylistAction(ActionEvent event) throws IOException {
        startModalWindow("AddPlaylistView");
    }

    /**
     * Clicking the "Delete" button under the songlist causes a confirmation
     * window to appear.
     */
    @FXML
    private void deleteSongAction() throws IOException, BLLException {
        if (tblviewMaster.getSelectionModel().getSelectedItem() != null) {
            model.setSongOrPlaylist("Song");
            String selectedTitle = model.getCurrentSongTitle();
            String selectedArtist = model.getCurrentSongArtist();
            model.selectedDeletedElements(selectedTitle + " by " + selectedArtist);

            startModalWindow("DeleteConfirmationView");
        }
    }

    /**
     * Clicking the "Delete" button under the playlists causes a confirmation
     * window to appear. The all song playlist cannot be deleted.
     */
    @FXML
    private void deletePlaylistAction() throws IOException, BLLException {
        if (listViewPlaylists.getSelectionModel().getSelectedItem() != null) {
            if (listViewPlaylists.getSelectionModel().getSelectedItem().getName() == "My Library") {
                startModalWindow("CannotDeleteView");
            } else {
                model.selectedDeletedElements(listViewPlaylists.getSelectionModel().getSelectedItem().getName());
                model.setSongOrPlaylist("Playlist");

                startModalWindow("DeleteConfirmationView");
            }
        }
    }

    /**
     * Opens the window for adding a song to a playlist
     *
     * @throws IOException
     */
    private void addSongToPlaylist() throws IOException {
        model.setChosenSong(tblviewMaster.getSelectionModel().getSelectedItem());
        startModalWindow("addSongToPlaylist");
    }

    /**
     * TO DO
     */
    @FXML
    private void FilterButtonAction(ActionEvent event) {
        if (!textfieldFilter.getText().trim().equals("") && btnFilter.getText().equals("Filter")) {
            model.filterSongList(textfieldFilter.getText().trim());
            btnFilter.setText("Clear");
        } else if (btnFilter.getText().equals("Clear")) {
            setSongsOnTableview(model.getAllSongsPlaylist());
            listViewPlaylists.getSelectionModel().select(0);
            btnFilter.setText("Filter");
        }

    }

    /**
     * Plays the song on button press.
     */
    @FXML
    private void playSongAction(ActionEvent event) {
        playSong();
    }

    /**
     * Mutes media.
     */
    @FXML
    private void muteSongsAction(ActionEvent event) {
        if (!model.isMuted()) {
            File file = new File("src/mytunes/gui/view/pictures/mutedspeaker.png");
            imageviewMute.setImage(new Image(file.toURI().toString()));
            volumeSlider.adjustValue(0);
            model.setMuted(true);
        } else if (model.isMuted()) {
            File file = new File("src/mytunes/gui/view/pictures/speaker.png");
            imageviewMute.setImage(new Image(file.toURI().toString()));
            volumeSlider.adjustValue(100);
            model.setMuted(false);
        }
    }

    /**
     * TO DO
     */
    @FXML
    private void sliderDragAction(MouseEvent event) {
        File file = new File("src/mytunes/gui/view/pictures/speaker.png");
        imageviewMute.setImage(new Image(file.toURI().toString()));
        model.setMuted(false);
    }

    /**
     * Pauses the song on button press.
     */
    private void pauseSongAction(ActionEvent event) {
        model.pauseMedia();
    }

    /**
     * Plays the previous song on button press.
     */
    @FXML
    private void previusSongAction(ActionEvent event) {
        model.previousMedia();
    }

    /**
     * Plays the next song on button press.
     */
    @FXML
    private void nextSongAction(ActionEvent event) {
        model.nextMedia();
    }

    /**
     * Repeats the current song on button press.
     */
    @FXML
    private void repeatSongsAction(ActionEvent event) {
        BtnRepeat.setStyle("-fx-background-color: orange;}");
        if (model.switchLooping()) {
            BtnRepeat.setStyle("-fx-background-color: orange;}");
        } else {
            BtnRepeat.setStyle("-fx-background-color: #3E606F;}");
        }
    }

    /**
     * Selects a random song as the next song on button press.
     */
    @FXML
    private void shuffleSongsAction(ActionEvent event) {
        Btnshuffle.setStyle("-fx-background-color: orange;}");
        if (model.switchShuffling()) {
            Btnshuffle.setStyle("-fx-background-color: orange;}");
        } else {
            Btnshuffle.setStyle("-fx-background-color: #3E606F;}");
        }

    }

    /**
     * loads the clikced playlist to the song view
     *
     * @param event
     */
    @FXML
    private void clickedPlaylist(MouseEvent event) {
        if (listViewPlaylists.getSelectionModel().getSelectedItem() != null) {
            setSongsOnTableview(listViewPlaylists.getSelectionModel().getSelectedItem());
            updateIdSelected();
        }
    }

    /**
     * Updates the ID of the selected playlist and song so that you can track
     * what items you want to delete. The playlist ID is by default -1 as this
     * is the ID of "My Library".
     */
    private void updateIdSelected() {
        int currentPlaylistId = -1;
        int currentSongId = 0;
        if (listViewPlaylists.getSelectionModel().getSelectedItem() != null) {
            currentPlaylistId = listViewPlaylists.getSelectionModel().getSelectedItem().getPlaylistId();
        }
        if (tblviewMaster.getSelectionModel().getSelectedItem() != null) {
            currentSongId = tblviewMaster.getSelectionModel().getSelectedItem().getSongId();
        }
        model.setCurrentIds(currentSongId, currentPlaylistId);
    }

    /**
     * Creates and attaches contect menus to the song list which adds options,
     * all with their own method calls attached.
     */
    private void contextSongMenuHandler() {
        //Plays the selected song.
        MenuItem item1 = new MenuItem("Play");
        item1.setOnAction((ActionEvent e) -> {
            model.switchSong(tblviewMaster.getSelectionModel().getSelectedIndex());
            if (!model.isPlaying()) {
                playSong();
            }
        });

        //Edits the selected song and presets text fields with current information.
        MenuItem item2 = new MenuItem("Edit song");
        item2.setOnAction((ActionEvent e) -> {
            try {
                model.setCurrentSongInformation(
                        tblviewMaster.getSelectionModel().getSelectedItem().getSongId(),
                        tblviewMaster.getSelectionModel().getSelectedItem().getTitle(),
                        tblviewMaster.getSelectionModel().getSelectedItem().getArtist(),
                        tblviewMaster.getSelectionModel().getSelectedItem().getAlbum(),
                        tblviewMaster.getSelectionModel().getSelectedItem().getYear(),
                        tblviewMaster.getSelectionModel().getSelectedItem().getGenre(),
                        tblviewMaster.getSelectionModel().getSelectedItem().getpath());

                startModalWindow("EditSongView");
            } catch (IOException ex) {
                Logger.getLogger(MainWindowController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

        //Adds the selected song to the queue.
        MenuItem item3 = new MenuItem("Add to queue");
        item3.setOnAction((ActionEvent e) -> {
            System.out.println("Needs to be implemented");
        });

        //Deletes the selected song.
        MenuItem item4 = new MenuItem("Delete song");
        item4.setOnAction((ActionEvent e) -> {
            try {
                deleteSongAction();
            } catch (IOException | BLLException ex) {
                Logger.getLogger(MainWindowController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

        //Sets the created MenuItems into the context menu for the table.
        final ContextMenu contextMenu = new ContextMenu(item1, item2, item3, item4);
        contextMenu.setMaxSize(50, 50);
        tblviewMaster.setContextMenu(contextMenu);
    }

    /**
     * Creates and attaches contect menus to the playlist which adds options,
     * all with their own method calls attached.
     */
    private void contextPlaylistMenuHandler() {
        //Edits the selected playlist and presets the name in the window.
        MenuItem item1 = new MenuItem("Edit playlist");
        item1.setOnAction((ActionEvent e) -> {
            try {
                model.setCurrentPlaylistInformation(
                        listViewPlaylists.getSelectionModel().getSelectedItem().getPlaylistId(),
                        listViewPlaylists.getSelectionModel().getSelectedItem().getName());

                startModalWindow("EditPlaylistView");
            } catch (IOException ex) {
                Logger.getLogger(MainWindowController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

        //Deletes the selected playlist.
        MenuItem item2 = new MenuItem("Delete playlist");
        item2.setOnAction((ActionEvent e) -> {
            try {
                deletePlaylistAction();
            } catch (IOException | BLLException ex) {
                Logger.getLogger(MainWindowController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

        final ContextMenu contextMenu = new ContextMenu(item1, item2);
        contextMenu.setMaxSize(50, 50);

        listViewPlaylists.setContextMenu(contextMenu);
    }

    /**
     * Keylistener
     *
     * @param event
     * @throws IOException
     */
    @FXML
    private void keyReleasedTable(KeyEvent event) throws IOException, BLLException {
        KeyCode key = event.getCode();

        if (null != key) {
            switch (key) {
                case LEFT:
                    addSongToPlaylist();
                    setTableItems();

                    break;
                case UP:
                    int indeks = model.moveSong(1, tblviewMaster.getSelectionModel().getSelectedItem(), listViewPlaylists.getSelectionModel().getSelectedItem());
                    if (indeks != -1) {
                        tblviewMaster.getSelectionModel().select(indeks);
                    }
                    break;
                case DOWN:
                    int indeks2 = model.moveSong(-1, tblviewMaster.getSelectionModel().getSelectedItem(), listViewPlaylists.getSelectionModel().getSelectedItem());
                    if (indeks2 != -1) {
                        tblviewMaster.getSelectionModel().select(indeks2);
                    }
                    break;
                case DELETE:
                    deleteSongAction();
                    break;
                default:
                    break;
            }
        }
    }

    @FXML
    private void tableviewMouseClicked(MouseEvent event) {
        updateIdSelected();
        doubleClickTblview(event);
    }

    /**
     * cheks for double click on mouseevent and runs the switch song stuff
     *
     * @param event
     */
    private void doubleClickTblview(MouseEvent event) {
        if (event.getButton().equals(MouseButton.PRIMARY)) {
            if (event.getClickCount() == 2) {
                model.switchSong(tblviewMaster.getSelectionModel().getSelectedIndex());
                if (!model.isPlaying()) {
                    playSong();
                }
            }
        }
    }

    /**
     * Plays the song and handels the button image
     */
    private void playSong() {
        if (model.isPlaying()) {
            model.pauseMedia();
            File file = new File("src/mytunes/gui/view/pictures/play.png");
            imageviewPlayPause.setImage(new Image(file.toURI().toString()));
        } else {
            model.playMedia();
            File file = new File("src/mytunes/gui/view/pictures/pause.png");
            imageviewPlayPause.setImage(new Image(file.toURI().toString()));
        }
    }
}
