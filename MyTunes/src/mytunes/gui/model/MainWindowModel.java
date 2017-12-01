/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mytunes.gui.model;

import java.util.ArrayList;
import java.util.List;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Slider;
import mytunes.be.Genre;
import mytunes.be.Playlist;
import mytunes.be.Song;
import mytunes.bll.Player;
import mytunes.bll.AudioPlayer;
import mytunes.bll.BLLException;
import mytunes.bll.BLLManager;

/**
 *
 * @author Alex
 */
public class MainWindowModel {

    private static MainWindowModel instance;

    private BLLManager bllManager;
    private Player player;
    private ObservableList<Playlist> playlists;
    private ObservableList<Genre> genres;
    private String selectedElement;
    private String currentAddMenu;
    private Song chosenSong;
    
    //All variables below refer to the current selected song in the song list.
    private int currentSongId;
    private String currentSongTitle;
    private String currentSongArtist;
    private String currentSongAlbum;
    private int currentSongYear;
    private Genre currentSongGenre;
    private String currentSongPath;

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
            player = new AudioPlayer();
            playlists = FXCollections.observableArrayList();
            genres = FXCollections.observableArrayList();
            playlists.addAll(bllManager.getAllPlaylists());

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
            playlists.add(getAllSongsPlaylist());
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
        return player.getSongs();
    }

    /**
     * Get observable String
     *
     * @return
     */
    public SimpleStringProperty getArtist() {
        return player.getArtist();
    }

    /**
     * Get observable String
     *
     * @return
     */
    public SimpleStringProperty getTitle() {
        return player.getTitle();
    }

    /**
     * Get observable String
     *
     * @return
     */
    public SimpleStringProperty getAlbum() {
        return player.getAlbum();
    }

    /**
     * Get observable String
     *
     * @return
     */
    public SimpleStringProperty getCurrentTime() {
        return player.getCurrentTime();
    }

    /**
     * Get observable String
     *
     * @return
     */
    public SimpleStringProperty getDurationTime() {
        return player.getDurationTime();
    }

    /**
     * Get observable Double
     *
     * @return
     */
    public SimpleDoubleProperty getProgress() {
        return player.getProgress();
    }

    /**
     * set the songs from the given playlist in the observablelist. remember to
     * add the observablelist to the view with getSong()
     *
     * @param selectedItem the playlist from which to take the song
     */
    public void setSongs(Playlist selectedItem) {
        try {
            player.setSongs(selectedItem);
        } catch (BLLException ex) {
            Alert alert = new Alert(AlertType.WARNING, "Could not set Songs: " + ex.getMessage() + ".", ButtonType.OK);
            alert.showAndWait();
        }
    }

    /**
     * gets the playlist with all the songs
     *
     * @return playlist with all the known songs
     */
    public Playlist getAllSongsPlaylist() {
        Playlist playlist = new Playlist(-1, "My Library");
        try {
            playlist.addAllSongToPlaylist(bllManager.getAllSongs());
            return playlist;
        } catch (BLLException ex) {
            Alert alert = new Alert(AlertType.WARNING, "Could not get Library: " + ex.getMessage() + ".", ButtonType.OK);
            alert.showAndWait();
            return null;
        }
    }

    /**
     * Play song.
     */
    public void playMedia() {
        player.playMedia();
    }

    /**
     * Pause song.
     */
    public void pauseMedia() {
        player.pauseMedia();
    }

    /**
     * Change to previous song in list.
     */
    public void previousMedia() {
        try {
            player.previousMedia();
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
            player.nextMedia();
        } catch (BLLException ex) {
            Alert alert = new Alert(AlertType.WARNING, "Could not load Media: " + ex.getMessage() + ".", ButtonType.OK);
            alert.showAndWait();
        }
    }

    /**
     * Switch song to index.
     */
    private void switchSong(int index) {
        try {
            player.switchSong(index);
        } catch (BLLException ex) {
            Alert alert = new Alert(AlertType.WARNING, "Could not load Media: " + ex.getMessage() + ".", ButtonType.OK);
            alert.showAndWait();
        }
    }

    /**
     * Switch looping playlist.
     */
    public void switchLooping() {
        player.switchLooping();
    }

    /**
     * Switch shuffling playlist.
     */
    public void switchShuffling() {
        player.switchShuffling();
    }

    /**
     * links the volumeslider to the mediaplayers volume
     *
     * @param volumeSlider the Slider who have to adjust the volume
     */
    public void volumeSliderSetup(Slider volumeSlider) {
        volumeSlider.setValue(player.getVolume() * volumeSlider.getMax());
        volumeSlider.valueProperty().addListener(new InvalidationListener() {
            @Override
            public void invalidated(Observable observable) {
                player.setVolume(volumeSlider.getValue() / volumeSlider.getMax());
            }
        });

    }

    /**
     * Sets selectedElement to get what element is currently attempted to be
     * removed.
     */
    public void selectedDeletedElements(String SelectedElement) {
        selectedElement = SelectedElement;
    }

    /**
     * Sends information about a currently attempted song or playlist to be
     * removed.
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
        songsList.addAll(player.getSongs());
        player.getSongs().clear();
        for (Song song : songsList) {
            if (song.getTitle().toLowerCase().contains(text.toLowerCase()) || song.getArtist().toLowerCase().contains(text.toLowerCase())) {
                player.getSongs().add(song);
            }
        }
    }

    /**
     * Creates a playlist with information sent by AddPlaylistView.
     */
    public void createPlaylist(String text) throws BLLException {
        bllManager.addPlaylist(text);
    }

    public void getDeleteConfirmation() {

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
     */
    public ObservableList<Genre> getGenres() {
        return genres;
    }

    /**
     * Sends information to BLL about the song that is being created in
     * AddSongView.
     */
    public void createSong(String artist, String title, String album, int year, Genre genre, String directory) throws BLLException {
        bllManager.addSong(artist, title, album, year, genre, directory);
    }

    /**
     * Creates a new genre from AddSongView.
     */
    public void addGenre(String genre) throws BLLException {
        bllManager.addGenre(genre);
    }

    /**
     * Saves what object you are curently trying to add to the database. Is used
     * in CannotAddView as a label.
     */
    public void setCurrentAddMenu(String menu) {
        currentAddMenu = menu;
    }

    /**
     * Getter for currentAddMenu, which shows what object you are trying to
     * create.
     */
    public String getCurrentAddMenu() {
        return currentAddMenu;
    }

    public void setChosenSong(Song selectedItem) {
        chosenSong = selectedItem;
    }

    public Song getChosenSong() {
        return chosenSong;
    }

    public void addSongToPlaylist(Playlist playlist, Song song) throws BLLException {
        bllManager.addSongToPlaylist(playlist, song);
        player.setSongs(playlist);
    }

    /**
     * moves the song
     *
     * @param i -1 if it has to go one down and 1 if it has to go one up
     * @param selectedItem
     * @param selectedItem0
     * @return the new indeks of the moved elment. -1 if failed
     */
    public int moveSong(int i, Song selectedItem, Playlist selectedItem0) {
        List<Song> songsholder = new ArrayList<>();
        songsholder.addAll(player.getSongs());

        if (true) {
            int index = songsholder.indexOf(selectedItem) + i;
            Song song = songsholder.get(index);
            songsholder.set(index, songsholder.get(index - i));
            songsholder.set(index - i, song);
            player.getSongs().clear();
            player.getSongs().addAll(songsholder);
            return index - i;
        }
        return -1;
    }

    public void editSongInformation(int songId, String artist, String title, String album, int year, Genre genre, String directory) throws BLLException {
        bllManager.updateSong(songId, artist, title, album, year, genre, directory);
    }

    public int getCurrentSongId(int songId) {
        return songId;
    }

    public String getCurrentSongTitle(String title) {
        return title;
    }

    public String getCurrentSongArtist(String artist) {
        return artist;
    }

    public String getCurrentSongAlbum(String album) {
        return album;
    }

    public int getCurrentSongYear(int year) {
        return year;
    }

    public Genre getCurrentSongGenre(Genre genre) {
        return genre;
    }

    public String getCurrentSongPath(String path) {
        return path;
    }
    
    

}
