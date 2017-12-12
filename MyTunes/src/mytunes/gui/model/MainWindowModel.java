package mytunes.gui.model;

import java.util.ArrayList;
import java.util.List;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;
import mytunes.be.Genre;
import mytunes.be.Playlist;
import mytunes.be.Song;
import mytunes.bll.BLLException;
import mytunes.bll.BLLManager;
import mytunes.bll.MediaHandler;

/**
 * FXML Model class
 *
 * This class handles the data of the views.
 *
 * @author Alex, Asbjørn og Jan
 */
public class MainWindowModel {

    private static MainWindowModel instance;

    private BLLManager bllManager;
    private MediaHandler mediaHandler;
    private ObservableList<Playlist> playlists;
    private ObservableList<Genre> genres;
    private ObservableList<Song> shownSongs;
    private String selectedElement;
    private String currentAddMenu;
    private Song chosenSong;
    private Playlist chosenPlaylist;
    private boolean muted = false;
    private String songOrPlaylist;

    /**
     * Singleton method which makes sure that two MainWindowModels cannot be
     * created by two different classes that make use of the class.
     *
     * @return MainWindowModel
     */
    public static MainWindowModel getInstance() {
        if (instance == null) {
            instance = new MainWindowModel();
        }
        return instance;
    }

    public MainWindowModel() {
        try {
            bllManager = new BLLManager();
            mediaHandler = new MediaHandler();
            playlists = FXCollections.observableArrayList();
            genres = FXCollections.observableArrayList();
            shownSongs = FXCollections.observableArrayList();
            playlists.addAll(bllManager.getAllPlaylists());
            if (playlists.size() > 0) {
                setChosenPlaylist(playlists.get(0));
            }
        } catch (BLLException ex) {
            throw new RuntimeException(ex.getMessage(), ex.getCause());
        }
    }

    /**
     * Updates the list for the gui element which shows the playlists remember
     * to call the getPlaylists() and assign the observable list to the element
     * in which it has to be shown
     */
    public void addAllPlaylistsToGUI() {
        playlists.clear();
        try {
            playlists.addAll(bllManager.getAllPlaylists());
        } catch (BLLException ex) {
            Alert alert = new Alert(AlertType.WARNING, "Could not add Playlist: " + ex.getMessage() + ".", ButtonType.OK);
            alert.showAndWait();
        }
    }

    /**
     * gets the observablelist with the playlists
     *
     * @return a observablelist with Playlist objects
     */
    public ObservableList<Playlist> getPlaylists() {
        return playlists;
    }

    /**
     * gets the observablelist with the songs
     *
     * @return a observablelist with Playlist objects
     */
    public ObservableList<Song> getSongs() {
        //return mediaHandler.getSongs();
        return shownSongs;
    }

    /**
     * Get observable String
     *
     * @return
     */
    public SimpleStringProperty getArtist() {
        return mediaHandler.getArtist();
    }

    /**
     * Get observable String
     *
     * @return
     */
    public SimpleStringProperty getTitle() {
        return mediaHandler.getTitle();
    }

    /**
     * Get observable String
     *
     * @return
     */
    public SimpleStringProperty getAlbum() {
        return mediaHandler.getAlbum();
    }

    /**
     * Get observable String
     *
     * @return
     */
    public SimpleStringProperty getCurrentTime() {
        return mediaHandler.getCurrentTime();
    }

    /**
     * Get observable String
     *
     * @return
     */
    public SimpleStringProperty getDurationTime() {
        return mediaHandler.getDurationTime();
    }

    /**
     * Get observable Double
     *
     * @return
     */
    public SimpleDoubleProperty getProgress() {
        return mediaHandler.getProgress();
    }

    /**
     * set the songs from the given playlist in the observablelist. remember to
     * add the observablelist to the view with getSong()
     *
     * @param selectedItem the playlist from which to take the song
     */
    public void setSongs(Playlist selectedItem) {
//        try
//        {
        //mediaHandler.setSongs(selectedItem);
        if (selectedItem != null) {
            shownSongs.clear();
            shownSongs.addAll(selectedItem.getSongs());
        }

//        }
//        catch (BLLException ex)
//        {
//            Alert alert = new Alert(AlertType.WARNING, "Could not set Songs: " + ex.getMessage() + ".", ButtonType.OK);
//            alert.showAndWait();
//        }
    }

    /**
     * Play song.
     */
    public void playMedia() {
        try {
            mediaHandler.playMedia();
        } catch (BLLException ex) {
            Alert alert = new Alert(AlertType.WARNING, "Could not play Media: " + ex.getMessage() + ".", ButtonType.OK);
            alert.showAndWait();
        }
    }

    /**
     * Pause song.
     */
    public void pauseMedia() {
        mediaHandler.pauseMedia();
    }

    /**
     * Change to previous song in list.
     */
    public void previousMedia() {
        try {
            mediaHandler.previousMedia();
        } catch (BLLException ex) {
            Alert alert = new Alert(AlertType.WARNING, "Could not load Media: " + ex.getMessage() + ".", ButtonType.OK);
            alert.showAndWait();
        }
    }

    /**
     * Change to next song in list.
     */
    public void nextMedia() {
        try {
            mediaHandler.nextMedia();
        } catch (BLLException ex) {
            Alert alert = new Alert(AlertType.WARNING, "Could not load Media: " + ex.getMessage() + ".", ButtonType.OK);
            alert.showAndWait();
        }
    }

    /**
     * Switch song to index.
     */
    public void switchSong(int index) {
        try {
            mediaHandler.switchSong(index);
        } catch (BLLException ex) {
            Alert alert = new Alert(AlertType.WARNING, "Could not load Media: " + ex.getMessage() + ".", ButtonType.OK);
            alert.showAndWait();
        }
    }

    /**
     * Switch looping playlist.
     *
     * @return
     */
    public boolean switchLooping() {
        return mediaHandler.switchLooping();
    }

    /**
     * Switch shuffling playlist.
     *
     * @return
     */
    public boolean switchShuffling() {
        return mediaHandler.switchShuffling();
    }

    /**
     * links the volumeslider to the mediaplayers volume
     *
     * @param volumeSlider the Slider who have to adjust the volume
     */
    public void volumeSliderSetup(Slider volumeSlider) {
        volumeSlider.setValue(mediaHandler.getVolume() * volumeSlider.getMax());
        volumeSlider.valueProperty().addListener(new InvalidationListener() {
            @Override
            public void invalidated(Observable observable) {
                mediaHandler.setVolume(volumeSlider.getValue() / volumeSlider.getMax());
                if (volumeSlider.getValue() == 0) {
                }
            }
        });

    }

    /**
     * Sets selectedElement to get what element is currently attempted to be
     * removed.
     *
     * @param SelectedElement
     */
    public void selectedDeletedElements(String SelectedElement) {
        selectedElement = SelectedElement;
    }

    /**
     * Sends information about a currently attempted song or playlist to be
     * removed.
     *
     * @return
     */
    public String getSelectedElement() {
        return selectedElement;
    }

    /**
     * Takes the current list of songs shown and filters it for songs who
     * contains the text in the title or in the artist
     *
     * @param text the text which should be found in the songs title or artist
     * for it to be shown
     */
    public void filterSongList(String text) {
        List<Song> songsList = new ArrayList<>();
        songsList.addAll(shownSongs);
        shownSongs.clear();
        for (Song song : songsList) {
            if (song.getTitle().toLowerCase().contains(text.toLowerCase()) || song.getArtist().toLowerCase().contains(text.toLowerCase())) {
                shownSongs.add(song);
            }
        }
    }

    /**
     * Creates a playlist with information sent by AddPlaylistView.
     *
     * @param text
     * @throws mytunes.bll.BLLException
     */
    public void createPlaylist(String text) throws BLLException {
        bllManager.addPlaylist(text);
    }

    /**
     * First clears the list of genres to stop two lists being fused and then
     * adds all genres for a getter to function.
     *
     * @throws BLLException
     */
    public void getAllGenres() throws BLLException {
        genres.clear();
        genres.addAll(bllManager.getAllGenres());
    }

    /**
     * Returns all genres currently active.
     *
     * @return
     */
    public ObservableList<Genre> getGenres() {
        return genres;
    }

    /**
     * Sends information to BLL about the song that is being created in
     * AddSongView.
     *
     * @param artist
     * @param title
     * @param album
     * @param year
     * @param genre
     * @param directory
     * @throws mytunes.bll.BLLException
     */
    public void createSong(String artist, String title, String album, int year, Genre genre, String directory) throws BLLException {
        bllManager.addSong(artist, title, album, year, genre, directory);
    }

    /**
     * Creates a new genre from AddSongView.
     *
     * @param genre
     * @throws mytunes.bll.BLLException
     */
    public void addGenre(String genre) throws BLLException {
        bllManager.addGenre(genre);
    }

    /**
     * Saves what object you are curently trying to add to the database. Is used
     * in CannotAddView as a label.
     *
     * @param menu
     */
    public void setCurrentAddMenu(String menu) {
        currentAddMenu = menu;
    }

    /**
     * Getter for currentAddMenu, which shows what object you are trying to
     * create.
     *
     * @return
     */
    public String getCurrentAddMenu() {
        return currentAddMenu;
    }

    /**
     * Sets the chosen song call this when a song element is clicked
     *
     * @param selectedSong a song object
     */
    public void setChosenSong(Song selectedSong) {
        chosenSong = selectedSong;
    }

    /**
     * returns the latest song given in setChosenSong()
     *
     * @return a song object
     */
    public Song getChosenSong() {
        return chosenSong;
    }

    /**
     * Adds a song to a playlist.
     *
     * @param playlist
     * @param song
     * @throws BLLException
     */
    public void addSongToPlaylist(Playlist playlist, Song song) throws BLLException {
        bllManager.addSongToPlaylist(playlist, song);
        playlist.addSongToPlaylist(song);
        shownSongs.clear();
        shownSongs.addAll(chosenPlaylist.getSongs());
    }

    /**
     * moves the song
     *
     * @param i -1 if it has to go one down and 1 if it has to go one up
     * @param selectedItem
     * @param selectedItem0
     * @return the new indeks of the moved elment. -1 if failed
     * @throws mytunes.bll.BLLException
     */
    public int moveSong(int i, Song selectedItem, Playlist selectedItem0) throws BLLException {
        List<Song> songsholder = new ArrayList<>();
        songsholder.addAll(shownSongs);

        int index = songsholder.indexOf(selectedItem);
        if (index - i >= 0 && index - i + 1 <= songsholder.size()) {
            Song song = songsholder.get(index);
            songsholder.set(index, songsholder.get(index - i));
            songsholder.set(index - i, song);
            shownSongs.clear();
            shownSongs.addAll(songsholder);
            bllManager.swapSongsInPlaylist(songsholder.get(index).getSongId(), songsholder.get(index - i).getSongId(), selectedItem0.getPlaylistId());
            return index;
        }
        return -1;
    }

    /**
     *
     * @param song
     * @throws BLLException
     */
    public void editSongInformation(Song song) throws BLLException {
        Song songfrombll = bllManager.updateSong(song.getSongId(), song.getArtist(), song.getTitle(), song.getAlbum(), song.getYear(), song.getGenre(), song.getPath());
        if (songfrombll != null) {
//            chosenPlaylist.getSongs().get(chosenPlaylist.getSongs().indexOf(song)).setAlbum(song.getAlbum());
//            chosenPlaylist.getSongs().get(chosenPlaylist.getSongs().indexOf(song)).setArtist(song.getArtist());
//            chosenPlaylist.getSongs().get(chosenPlaylist.getSongs().indexOf(song)).setTitle(song.getTitle());
//            chosenPlaylist.getSongs().get(chosenPlaylist.getSongs().indexOf(song)).setYear(song.getYear());
//            chosenPlaylist.getSongs().get(chosenPlaylist.getSongs().indexOf(song)).setGenre(song.getGenre());
            chosenPlaylist.getSongs().set(chosenPlaylist.getSongs().indexOf(song), songfrombll);
            
            shownSongs.clear();
            shownSongs.addAll(chosenPlaylist.getSongs());
        }

    }

    public void editPlaylistInformation(int PlaylistId, String text) throws BLLException {
        bllManager.updatePlaylist(PlaylistId, text);
    }

    public void setCurrentElementToBeDeleted(String element) throws BLLException {
        if ("Song".equals(element)) {
            if (chosenPlaylist.getPlaylistId() == 1) {
                bllManager.deleteSong(chosenSong.getSongId());
                shownSongs.remove(chosenSong);
            } else {
                bllManager.deleteSongInPlaylist(chosenSong.getSongId(), chosenPlaylist.getPlaylistId());
                chosenPlaylist.getSongs().remove(chosenSong);
            }
        } else if ("Playlist".equals(element)) {
            bllManager.deletePlaylist(chosenPlaylist.getPlaylistId());
        }
    }

    /**
     * change the muted varibel
     *
     * @param mutedSetting true when muted false when unmuted
     */
    public void setMuted(boolean mutedSetting) {
        muted = mutedSetting;
    }

    /**
     * returns the muted varibel
     *
     * @return
     */
    public boolean isMuted() {
        return muted;
    }

    public void setSongOrPlaylist(String SongOrPlaylist) {
        songOrPlaylist = SongOrPlaylist;
    }

    public String getSongOrPlaylist() {
        return songOrPlaylist;
    }

    /**
     * a boolean for if the song is playing
     *
     * @return true if playing else false
     */
    public boolean isPlaying() {
        return mediaHandler.isPlaying();
    }

    /**
     * Sets the chosen playlist for use with methods based on the chossen
     * playlist
     *
     * @param chosenPlaylist
     */
    public void setChosenPlaylist(Playlist chosenPlaylist) {
        this.chosenPlaylist = chosenPlaylist;
    }

    /**
     * gets the latest chosen playlist
     *
     * @return be.Playlist object
     */
    public Playlist getChosenPlaylist() {
        return chosenPlaylist;
    }

    public void musicSliderSetup(Slider musicSlider) {
        musicSlider.valueProperty().bindBidirectional(mediaHandler.getProgress());

        musicSlider.valueProperty().addListener(new InvalidationListener() {
            @Override
            public void invalidated(Observable observable) {
                if (musicSlider.pressedProperty().get()) {
                    mediaHandler.setProgressing(false);
                }
            }
        });

        musicSlider.setOnMouseReleased(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                try {
                    mediaHandler.seek(musicSlider.getValue());
                } catch (BLLException ex) {
                    mediaHandler.stopMedia();
                }
                mediaHandler.setProgressing(true);
            }
        });
    }

    /**
     * Sets the currently shown songs for playing
     */
    public void setCurrentShownSongsForPlaying() {
        try {
            mediaHandler.setSongs(shownSongs);
        } catch (BLLException ex) {
            Alert alert = new Alert(AlertType.WARNING, "Could not set Songs: " + ex.getMessage() + ".", ButtonType.OK);
            alert.showAndWait();
        }

    }

    /**
     * Gets all information from the database. and assigns it to the diffrent
     * observables
     */
    public void refreshFromDatabase() {
        try {
            getAllGenres();
            playlists.clear();
            playlists.addAll(bllManager.getAllPlaylists());

            Playlist copyOfChosenPlaylist = chosenPlaylist;
            Song copyOfChosenSong = chosenSong;
            for (Playlist playlist : playlists) {
                if (playlist.getPlaylistId() == chosenPlaylist.getPlaylistId()) {
                    shownSongs.clear();
                    shownSongs.addAll(playlist.getSongs());
                    if (chosenSong != null) {
                        for (Song shownSong : shownSongs) {
                            if (shownSong.getSongId() == chosenSong.getSongId()) {
                                chosenSong = shownSong;
                            }
                        }
                    }
                    chosenPlaylist = playlist;
                }
            }
            if (chosenPlaylist.equals(copyOfChosenPlaylist)) {
                chosenPlaylist = playlists.get(0);
            }
            if (chosenSong != null) {
                if (chosenSong.equals(copyOfChosenSong)) {
                    chosenSong = chosenPlaylist.getSongs().get(0);
                }
            }
        } catch (BLLException ex) {
            Alert alert = new Alert(AlertType.WARNING, "Could not reload information,\n check connecetion to database", ButtonType.OK);
            alert.showAndWait();
        }

    }
}
