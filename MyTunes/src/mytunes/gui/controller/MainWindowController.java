/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mytunes.gui.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import javafx.beans.binding.Bindings;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import mytunes.be.Playlist;
import mytunes.be.Song;
import mytunes.gui.model.MainWindowModel;
import javafx.scene.input.KeyEvent;
import javafx.scene.shape.Rectangle;
import mytunes.be.Genre;
import mytunes.bll.BLLException;

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

    @Override
    public void initialize(URL url, ResourceBundle rb)
    {

        //Using Singleton method to be sure there aren't 2 instances running.
        model = MainWindowModel.getInstance();

        //add the playlists to the view
        model.addAllPlaylistsToGUI();

        //set observables
        listViewPlaylists.setItems(model.getPlaylists());
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

        setTableItems();

        //volumeSlider
        model.volumeSliderSetup(volumeSlider);

        contextSongMenuHandler();
        contextPlaylistMenuHandler();
    }

    /**
     * Plays the song on button press.
     */
    @FXML
    private void playSongAction(ActionEvent event)
    {
        model.playMedia();
    }

    /**
     * Pauses the song on button press.
     */
    @FXML
    private void pauseSongAction(ActionEvent event)
    {
        model.pauseMedia();
    }

    /**
     * Plays the previous song on button press.
     */
    @FXML
    private void previusSongAction(ActionEvent event)
    {
        model.previousMedia();
    }

    /**
     * Plays the next song on button press.
     */
    @FXML
    private void nextSongAction(ActionEvent event)
    {
        model.nextMedia();
    }

    /**
     * Repeats the current song on button press.
     */
    @FXML
    private void repeatSongsAction(ActionEvent event)
    {
        model.switchLooping();
    }

    /**
     * Selects a random song as the next song on button press.
     */
    @FXML
    private void shuffleSongsAction(ActionEvent event)
    {
        model.switchShuffling();
    }

    /**
     * Mutes media.
     */
    @FXML
    private void muteSongsAction(ActionEvent event)
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
        newStage.showAndWait();
        setTableItems();
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
     * window to appear. The all song playlist cannot be deleted.
     */
    @FXML
    private void deletePlaylistAction(ActionEvent event) throws IOException
    {
        if (listViewPlaylists.getSelectionModel().getSelectedItem() != null)
        {
            if (listViewPlaylists.getSelectionModel().getSelectedItem().getName() == "My Library")
            {
                Stage newStage = new Stage();
                newStage.initModality(Modality.APPLICATION_MODAL);
                FXMLLoader fxLoader = new FXMLLoader(getClass().getResource("/mytunes/gui/view/CannotDeleteView.fxml"));
                Parent root = fxLoader.load();
                Scene scene = new Scene(root);
                newStage.setScene(scene);
                newStage.show();
            }
            else
            {
                String selectedItem = listViewPlaylists.getSelectionModel().getSelectedItem().getName();
                model.selectedDeletedElements(selectedItem);

                Stage newStage = new Stage();
                newStage.initModality(Modality.APPLICATION_MODAL);
                FXMLLoader fxLoader = new FXMLLoader(getClass().getResource("/mytunes/gui/view/DeleteConfirmationView.fxml"));
                Parent root = fxLoader.load();
                Scene scene = new Scene(root);
                newStage.setScene(scene);
                newStage.show();
            }
        }
    }

    /**
     * Clicking the "Delete" button under the songlist causes a confirmation
     * window to appear.
     */
    @FXML
    private void deleteSongAction(ActionEvent event) throws IOException
    {
        deleteSongWindow();
    }

    /**
     * opens a popup window to delete a song
     */
    private void deleteSongWindow() throws IOException
    {
        if (tblviewMaster.getSelectionModel().getSelectedItem() != null)
        {
            String selectedTitle = tblviewMaster.getSelectionModel().getSelectedItem().getTitle();
            String selectedArtist = tblviewMaster.getSelectionModel().getSelectedItem().getArtist();
            model.selectedDeletedElements(selectedTitle + " by " + selectedArtist);

            Stage newStage = new Stage();
            newStage.initModality(Modality.APPLICATION_MODAL);
            FXMLLoader fxLoader = new FXMLLoader(getClass().getResource("/mytunes/gui/view/DeleteConfirmationView.fxml"));
            Parent root = fxLoader.load();
            Scene scene = new Scene(root);
            newStage.setScene(scene);
            newStage.show();
        }
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
        if (playlist.getSongs().size() > 1)
        {
            lblPlaylistInfo.setText(playlist.getSongs().size() + " songs in this playlist");
        }
        else if (playlist.getSongs().size() == 1)
        {
            lblPlaylistInfo.setText(playlist.getSongs().size() + " song in this playlist");
        }
        else if (playlist.getSongs().size() == 0)
        {
            lblPlaylistInfo.setText("No songs in this playlist");
        }
        else
        {
            lblPlaylistInfo.setText("");
        }

    }

    @FXML
    private void FilterButtonAction(ActionEvent event)
    {
        if (!textfieldFilter.getText().trim().equals("") && btnFilter.getText().equals("Filter"))
        {
            model.filterSongList(textfieldFilter.getText().trim());
            btnFilter.setText("Clear");
        }
        else if (btnFilter.getText().equals("Clear"))
        {
            setSongsOnTableview(model.getAllSongsPlaylist());
            listViewPlaylists.getSelectionModel().select(0);
            btnFilter.setText("Filter");
        }

    }

    /**
     * Creates and attaches contect menus to the song list which adds options
     * all with their own method calls attached.
     */
    private void contextSongMenuHandler()
    {
        MenuItem item1 = new MenuItem("Play");
        item1.setOnAction(new EventHandler<ActionEvent>()
        {
            public void handle(ActionEvent e)
            {
                model.playMedia();
            }
        });
        MenuItem item2 = new MenuItem("Edit song information");
        item2.setOnAction(new EventHandler<ActionEvent>()
        {
            public void handle(ActionEvent e)
            {
                try
                {
                    model.setCurrentSongInformation(
                            tblviewMaster.getSelectionModel().getSelectedItem().getSongId(),
                            tblviewMaster.getSelectionModel().getSelectedItem().getTitle(),
                            tblviewMaster.getSelectionModel().getSelectedItem().getArtist(),
                            tblviewMaster.getSelectionModel().getSelectedItem().getAlbum(),
                            tblviewMaster.getSelectionModel().getSelectedItem().getYear(),
                            tblviewMaster.getSelectionModel().getSelectedItem().getGenre(),
                            tblviewMaster.getSelectionModel().getSelectedItem().getpath());

                    Stage newStage = new Stage();
                    newStage.initModality(Modality.APPLICATION_MODAL);
                    FXMLLoader fxLoader = new FXMLLoader(getClass().getResource("/mytunes/gui/view/EditSongView.fxml"));
                    Parent root = fxLoader.load();
                    Scene scene = new Scene(root);
                    newStage.setScene(scene);
                    newStage.show();
                }
                catch (IOException ex)
                {
                    Logger.getLogger(MainWindowController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        MenuItem item3 = new MenuItem("Add to queue");
        item3.setOnAction(new EventHandler<ActionEvent>()
        {
            @Override
            public void handle(ActionEvent e)
            {
                System.out.println("Needs to be implemented");
            }
        });

        final ContextMenu contextMenu = new ContextMenu(item1, item2, item3);
        contextMenu.setMaxSize(50, 50);

        tblviewMaster.setContextMenu(contextMenu);
    }

    private void contextPlaylistMenuHandler()
    {
        MenuItem item1 = new MenuItem("Play");
        item1.setOnAction(new EventHandler<ActionEvent>()
        {
            public void handle(ActionEvent e)
            {
                System.out.println("Needs to be implemented");
            }
        });
        MenuItem item2 = new MenuItem("Edit song information");
        item2.setOnAction(new EventHandler<ActionEvent>()
        {
            public void handle(ActionEvent e)
            {
                System.out.println("Needs to be implemented");
            }
        });
        MenuItem item3 = new MenuItem("Add to queue");
        item3.setOnAction(new EventHandler<ActionEvent>()
        {
            @Override
            public void handle(ActionEvent e)
            {
                System.out.println("Needs to be implemented");
            }
        });

        final ContextMenu contextMenu = new ContextMenu(item1, item2, item3);
        contextMenu.setMaxSize(50, 50);

        listViewPlaylists.setContextMenu(contextMenu);
    }

    private void setTableItems()
    {
        tblviewMaster.setItems(model.getSongs());

        setSongsOnTableview(model.getAllSongsPlaylist());
    }

    /**
     * Keylistener
     *
     * @param event
     * @throws IOException
     */
    @FXML
    private void keyReleasedTable(KeyEvent event) throws IOException, BLLException
    {
        KeyCode key = event.getCode();

        if (null != key)
        {
            switch (key)
            {
                case LEFT:
                    addSongToPlaylist();
                    setTableItems();
                    break;
                case UP:
                    int indeks = model.moveSong(1, tblviewMaster.getSelectionModel().getSelectedItem(), listViewPlaylists.getSelectionModel().getSelectedItem());
                    if (indeks != -1)
                    {
                        tblviewMaster.getSelectionModel().select(indeks);
                    }
                    break;
                case DOWN:
                    int indeks2 = model.moveSong(-1, tblviewMaster.getSelectionModel().getSelectedItem(), listViewPlaylists.getSelectionModel().getSelectedItem());
                    if (indeks2 != -1)
                    {
                        tblviewMaster.getSelectionModel().select(indeks2);
                    }
                    break;
                case DELETE:
                    deleteSongWindow();
                    break;
                default:
                    break;
            }
        }

    }

    /**
     * Opens the window for adding a song to a playlist
     * @throws IOException
     */
    private void addSongToPlaylist() throws IOException
    {
        model.setChosenSong(tblviewMaster.getSelectionModel().getSelectedItem());
        Stage newStage = new Stage();
        newStage.initModality(Modality.APPLICATION_MODAL);
        FXMLLoader fxLoader = new FXMLLoader(getClass().getResource("/mytunes/gui/view/addSongToPlaylist.fxml"));
        Parent root = fxLoader.load();
        Scene scene = new Scene(root);
        newStage.setScene(scene);
        newStage.show();
    }
}
