package mytunes.gui.controller;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import javafx.beans.binding.Bindings;
import java.util.logging.Logger;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
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
import mytunes.gui.model.MainModel;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.shape.Rectangle;
import mytunes.bll.BLLException;
import mytunes.gui.model.MediaControlModel;

/**
 * FXML Controller class
 *
 * This view handles the main menu of the program.
 *
 * @author Alex, Asbjørn og Jan
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
    private TableColumn<Song, String> tblviewTime;
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
    @FXML
    private ImageView imageviewMute;
    @FXML
    private Button BtnRepeat;
    @FXML
    private Button Btnshuffle;
    @FXML
    private ImageView imageviewPlayPause;
    @FXML
    private Slider musicSlider;

    //Singleton variable to be able to use model information in this controller.
    MainModel model;

    MediaControlModel mediaControlModel;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        //Using Singleton method to be sure there aren't 2 instances running.
        model = MainModel.getInstance();

        mediaControlModel = new MediaControlModel();

        //add the playlists to the view
        model.addAllPlaylistsToGUI();

        //set observables
        refreshAndSetElements();
        listViewPlaylists.getSelectionModel().selectFirst();
        model.setChosenPlaylist(listViewPlaylists.getSelectionModel().getSelectedItem());
        lblSongArtistTopBar.textProperty().bind(Bindings.convert(mediaControlModel.getArtist()));
        lblSongTitleTopBar.textProperty().bind(Bindings.convert(mediaControlModel.getTitle()));
        lblSongAlbumTopBar.textProperty().bind(Bindings.convert(mediaControlModel.getAlbum()));
        lblCurrentTime.textProperty().bind(Bindings.convert(mediaControlModel.getCurrentTime()));
        lblTotalTimeSong.textProperty().bind(Bindings.convert(mediaControlModel.getDurationTime()));
        progressBar.progressProperty().bind(mediaControlModel.getProgress().add(0.01));

        //add the songs to the view
        tblviewSong.setCellValueFactory(
                new PropertyValueFactory("title"));
        tblviewArtist.setCellValueFactory(
                new PropertyValueFactory("artist"));
        tblviewAlbum.setCellValueFactory(
                new PropertyValueFactory("album"));
        tblviewGenre.setCellValueFactory(
                new PropertyValueFactory("genre"));
        tblviewYear.setCellValueFactory((TableColumn.CellDataFeatures<Song, String> param) -> {
            if (param.getValue().getYear() == -1) {
                return new ReadOnlyObjectWrapper<>("Unknown");
            } else {
                return new ReadOnlyObjectWrapper<>(param.getValue().getYear() + "");
            }
        });
        tblviewTime.setCellValueFactory(
                new PropertyValueFactory("duration"));

        //volumeSlider
        mediaControlModel.volumeSliderSetup(volumeSlider);
        //musicSlider
        mediaControlModel.musicSliderSetup(musicSlider);

        //Sets the context menus for playlists and songs.
        contextSongMenuHandler();
        contextPlaylistMenuHandler();

        mediaControlModel.setListOfSongsForPlaying(model.getSongs());

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
    private void refreshAndSetElements() {
        tblviewMaster.setItems(model.getSongs());
        listViewPlaylists.setItems(model.getPlaylists());
        model.addAllPlaylistsToGUI();
        if (model.getChosenPlaylist() != null) {
            setSongsOnTableview(model.getChosenPlaylist());
        }
    }

    /**
     * Sets the songs from the given playlist in the table view and updates the
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
    }

    /**
     * Clicking the "Add" button under the songlist causes a modal window that
     * assists the user in adding music to the library to appear.
     */
    @FXML
    private void addSongAction(ActionEvent event) throws IOException {
        startModalWindow("AddSongView");
        model.refreshFromDatabase();
    }

    /**
     * Clicking the "Add" button under the playlists causes a modal window that
     * assists the user in making a new playlist to appear.
     */
    @FXML
    private void addPlaylistAction(ActionEvent event) throws IOException {
        startModalWindow("AddPlaylistView");
        model.refreshFromDatabase();
    }

    /**
     * Clicking the "Delete" button under the songlist causes a confirmation
     * window to appear.
     */
    @FXML
    private void deleteSongAction() throws IOException, BLLException {
        if (tblviewMaster.getSelectionModel().getSelectedItem() != null) {
            model.setSongOrPlaylist("Song");
            String selectedTitle = model.getChosenSong().getTitle();
            String selectedArtist = model.getChosenSong().getArtist();
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
            if (listViewPlaylists.getSelectionModel().getSelectedItem().getPlaylistId() == 1) {
                Alert alert = new Alert(Alert.AlertType.WARNING, "This playlist cannot be deleted.", ButtonType.OK);
                alert.showAndWait();
            } else {
                model.selectedDeletedElements(listViewPlaylists.getSelectionModel().getSelectedItem().getName());
                model.setSongOrPlaylist("Playlist");

                startModalWindow("DeleteConfirmationView");
            }
        }
    }

    /**
     * Opens the window for adding a song to a playlist.
     */
    private void addSongToPlaylist() throws IOException {
        model.setChosenSong(tblviewMaster.getSelectionModel().getSelectedItem());
        startModalWindow("addSongToPlaylist");
    }

    /**
     * Filters the song list for matches in title or artist and then sets the
     * button to clear the filter afterwards.
     */
    @FXML
    private void FilterButtonAction() {
        if (!textfieldFilter.getText().trim().equals("") && btnFilter.getText().equals("Filter")) {
            model.filterSongList(textfieldFilter.getText().trim());
            btnFilter.setText("Clear");
        } else if (btnFilter.getText().equals("Clear")) {
            setSongsOnTableview(model.getChosenPlaylist());
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
        if (!mediaControlModel.isMuted()) {
            File file = new File("src/mytunes/gui/view/pictures/mutedspeaker.png");
            imageviewMute.setImage(new Image(file.toURI().toString()));
            volumeSlider.adjustValue(0);
            mediaControlModel.setMuted(true);
        } else if (mediaControlModel.isMuted()) {
            File file = new File("src/mytunes/gui/view/pictures/speaker.png");
            imageviewMute.setImage(new Image(file.toURI().toString()));
            volumeSlider.adjustValue(100);
            mediaControlModel.setMuted(false);
        }
    }

    /**
     * Sets the volume of the playing song.
     */
    @FXML
    private void sliderDragAction(MouseEvent event) {
        if (volumeSlider.getValue() != volumeSlider.getMin()) {
            File file = new File("src/mytunes/gui/view/pictures/speaker.png");
            imageviewMute.setImage(new Image(file.toURI().toString()));
            mediaControlModel.setMuted(false);
        } else {
            File file = new File("src/mytunes/gui/view/pictures/mutedspeaker.png");
            imageviewMute.setImage(new Image(file.toURI().toString()));
            mediaControlModel.setMuted(true);
        }
    }

    /**
     * Pauses the song on button press.
     */
    private void pauseSongAction(ActionEvent event) {
        mediaControlModel.pauseMedia();
    }

    /**
     * Plays the previous song on button press.
     */
    @FXML
    private void previusSongAction(ActionEvent event) {
        mediaControlModel.previousMedia();
    }

    /**
     * Plays the next song on button press.
     */
    @FXML
    private void nextSongAction(ActionEvent event) {
        mediaControlModel.nextMedia();
    }

    /**
     * Repeats the current song on button press.
     */
    @FXML
    private void repeatSongsAction(ActionEvent event) {
        BtnRepeat.setStyle("-fx-background-color: orange;}");
        if (mediaControlModel.switchLooping()) {
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
        if (mediaControlModel.switchShuffling()) {
            Btnshuffle.setStyle("-fx-background-color: orange;}");
        } else {
            Btnshuffle.setStyle("-fx-background-color: #3E606F;}");
        }

    }

    /**
     * Loads the clikced playlist to the song view.
     */
    @FXML
    private void clickedPlaylist(MouseEvent event) {
        if (listViewPlaylists.getSelectionModel().getSelectedItem() != null) {
            setSongsOnTableview(listViewPlaylists.getSelectionModel().getSelectedItem());
            updateSelected();
        }
    }

    /**
     * Updates the ID of the selected playlist and song so that you can track
     * what items you want to delete. The playlist ID is by default -1 as this
     * is the ID of "My Library".
     */
    private void updateSelected() {

        if (listViewPlaylists.getSelectionModel().getSelectedItem() != null) {
            model.setChosenPlaylist(listViewPlaylists.getSelectionModel().getSelectedItem());
        }
        if (tblviewMaster.getSelectionModel().getSelectedItem() != null) {
            model.setChosenSong(tblviewMaster.getSelectionModel().getSelectedItem());
        }
    }

    /**
     * Creates and attaches contect menus to the song list which adds options,
     * all with their own method calls attached.
     */
    private void contextSongMenuHandler() {
        //Plays the selected song.
        MenuItem item1 = new MenuItem("Play");
        item1.setOnAction((ActionEvent e)
                -> {
            mediaControlModel.setListOfSongsForPlaying(model.getSongs());
            mediaControlModel.switchSong(tblviewMaster.getSelectionModel().getSelectedIndex());
            if (!mediaControlModel.isPlaying()) {
                playSong();
            }
        });

        //Edits the selected song and presets text fields with current information.
        MenuItem item2 = new MenuItem("Edit song");
        item2.setOnAction((ActionEvent e)
                -> {
            try {
                editWindow();

            } catch (IOException ex) {
                Logger.getLogger(MainWindowController.class.getName()).log(Level.SEVERE, null, ex);
                System.out.println(ex.getMessage());
            }
        });

        //Adds the selected song to the queue.
        MenuItem item3 = new MenuItem("Add to playlist");
        item3.setOnAction((ActionEvent e)
                -> {
            try {
                addSongToPlaylist();
            } catch (IOException ex) {
                Alert alert = new Alert(Alert.AlertType.WARNING, "Could not add to Playlist: " + ex.getMessage() + ".", ButtonType.OK);
                alert.showAndWait();
            }
        });

        //Deletes the selected song.
        MenuItem item4 = new MenuItem("Delete song");
        item4.setOnAction((ActionEvent e)
                -> {
            try {
                deleteSongAction();
            } catch (IOException | BLLException ex) {
                Alert alert = new Alert(Alert.AlertType.WARNING, "Could not delete song: " + ex.getMessage() + ".", ButtonType.OK);
                alert.showAndWait();
            }
        });

        //Sets the created MenuItems into the context menu for the table.
        final ContextMenu contextMenu = new ContextMenu(item1, item2, item3, item4);
        contextMenu.setMaxSize(50, 50);
        tblviewMaster.setContextMenu(contextMenu);
    }

    /**
     * starts the edit window and refreshes the view
     *
     * @throws IOException
     */
    private void editWindow() throws IOException {
        if (tblviewMaster.getSelectionModel().getSelectedItem() != null) {
            model.setChosenSong(tblviewMaster.getSelectionModel().getSelectedItem());
            startModalWindow("EditSongView");
        }
    }

    /**
     * Creates and attaches contect menus to the playlist which adds options,
     * all with their own method calls attached.
     */
    private void contextPlaylistMenuHandler() {
        //Edits the selected playlist and presets the name in the window.
        MenuItem item1 = new MenuItem("Edit playlist");
        item1.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                if (listViewPlaylists.getSelectionModel().getSelectedItem().getPlaylistId() == 1) {
                    Alert alert = new Alert(Alert.AlertType.WARNING, "This playlist cannot be edited.", ButtonType.OK);
                    alert.showAndWait();
                } else {
                    try {
                        model.setChosenPlaylist(listViewPlaylists.getSelectionModel().getSelectedItem());

                        startModalWindow("EditPlaylistView");
                    } catch (IOException ex) {
                        Logger.getLogger(MainWindowController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        });

        //Deletes the selected playlist.
        MenuItem item2 = new MenuItem("Delete playlist");
        item2.setOnAction((ActionEvent e)
                -> {
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
     * Keylistener listens for keys and performs the assosiated tasks.
     *
     * @param event the keyevent
     * @throws IOException
     */
    @FXML
    private void keyReleasedTable(KeyEvent event) {
        KeyCode key = event.getCode();

        if (null != key) {
            switch (key) {
                case LEFT:
                    try {
                        addSongToPlaylist();
                    } catch (IOException ex) {
                        Alert alert = new Alert(Alert.AlertType.WARNING, "Could not add Song to playlist.", ButtonType.OK);
                        alert.showAndWait();
                    }
                    break;
                case UP:
                    int indeks = -1;
                    indeks = model.moveSong(1, tblviewMaster.getSelectionModel().getSelectedItem(), listViewPlaylists.getSelectionModel().getSelectedItem());
                    if (indeks != -1) {
                        tblviewMaster.getSelectionModel().select(indeks);
                    }
                    break;
                case DOWN:
                    int indeks2 = -1;
                    indeks2 = model.moveSong(-1, tblviewMaster.getSelectionModel().getSelectedItem(), listViewPlaylists.getSelectionModel().getSelectedItem());
                    if (indeks2 != -1) {
                        tblviewMaster.getSelectionModel().select(indeks2);
                    }
                    break;
                case DELETE:
                    try {
                        deleteSongAction();
                    } catch (IOException | BLLException ex) {
                        Alert alert = new Alert(Alert.AlertType.WARNING, "Cant delete.\n" + ex.getMessage(), ButtonType.OK);
                        alert.showAndWait();
                    }
                    break;
                case PLAY:
                    playSong();
                    break;
                case PAUSE:
                    mediaControlModel.pauseMedia();
                    break;
                case FAST_FWD:
                    mediaControlModel.nextMedia();
                    break;
                case REWIND:
                    mediaControlModel.previousMedia();
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * Handels all the stuff for when the mouse is cliked on the tableview
     * @param event the mouse event
     */
    @FXML
    private void tableviewMouseClicked(MouseEvent event) {
        updateSelected();
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
                mediaControlModel.setListOfSongsForPlaying(model.getSongs());
                mediaControlModel.switchSong(tblviewMaster.getSelectionModel().getSelectedIndex());
                if (!mediaControlModel.isPlaying()) {
                    playSong();
                }
            }
        }
    }

    /**
     * Plays the song and handels the button image
     */
    private void playSong() {
        if (mediaControlModel.isPlaying()) {
            mediaControlModel.pauseMedia();
            if (!mediaControlModel.isPlaying()) {
                File file = new File("src/mytunes/gui/view/pictures/play.png");
                imageviewPlayPause.setImage(new Image(file.toURI().toString()));
            }
        } else {
            mediaControlModel.playMedia();
            if (mediaControlModel.isPlaying()) {
                File file = new File("src/mytunes/gui/view/pictures/pause.png");
                imageviewPlayPause.setImage(new Image(file.toURI().toString()));
            }
        }
    }
}
