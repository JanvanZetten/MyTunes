/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mytunes.gui.model;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Slider;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import mytunes.be.Playlist;
import mytunes.be.Song;
import mytunes.bll.BLLException;
import mytunes.bll.BLLManager;

/**
 *
 * @author Alex
 */
public class MainWindowModel
{
    private static MainWindowModel instance;
    private BLLManager bllManager;
    private ObservableList<Playlist> playlists;
    private ObservableList<Song> songs;
    Media sound;
    MediaPlayer mediaPlayer;
    int currentIndex = -1;
    private Double currentVolume = 1.0;
    private String selectedElement;

    /**
     * Singleton method which makes sure that two MainWindowModels cannot be
     * created by two different classes that make use of the class.
     * @return MainWindowModel
     */
    public static MainWindowModel getInstance()
    {
        if (instance == null)
        {
            instance = new MainWindowModel();
        }
        return instance;
    }

    public MainWindowModel()
    {
        try
        {
            bllManager = new BLLManager();
            playlists = FXCollections.observableArrayList();
            songs = FXCollections.observableArrayList();
            playlists.addAll(bllManager.getAllPlaylists());
            songs.addAll(bllManager.getAllSongs());
        }
        catch (BLLException ex)
        {
            throw new RuntimeException(ex.getMessage(), ex.getCause());
        }
    }

    /**
     * Updates the list for the gui element which shows the playlists remember
     * to call the getPlaylists() and assign the observable list to the element
     * in which it has to be shown
     */
    public void addAllPlaylistsToGUI()
    {
        playlists.clear();
        try
        {
            playlists.addAll(bllManager.getAllPlaylists());
        }
        catch (BLLException ex)
        {
            Logger.getLogger(MainWindowModel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * gets the observablelist with the playlists
     *
     * @return a observablelist with Playlist objects
     */
    public ObservableList<Playlist> getPlaylists()
    {
        return playlists;
    }

    /**
     * gets the observablelist with the songs
     *
     * @return a observablelist with Playlist objects
     */
    public ObservableList<Song> getSongs()
    {
        return songs;
    }

    /**
     * set the songs from the given playlist in the observablelist. remember to
     * add the observablelist to the view with getSong()
     *
     * @param selectedItem the playlist from which to take the song
     */
    public void setSongs(Playlist selectedItem)
    {
        songs.clear();
        songs.addAll(selectedItem.getSongs());

        if (songs.size() > 0)
        {
            currentIndex = 0;
        }
        else
        {
            currentIndex = -1;
        }
        switchSong();
    }

    /**
     * gets the playlist with all the songs
     *
     * @return playlist with all the known songs
     */
    public Playlist getAllSongsPlaylist()
    {
        Playlist playlist = new Playlist(-1, "MyLibrary");
        try
        {
            playlist.addAllSongToPlaylist(bllManager.getAllSongs());
            return playlist;
        }
        catch (BLLException ex)
        {
            throw new RuntimeException("Could not read all songs.");
        }
    }

    /**
     * Play song.
     */
    public void playMedia()
    {
        if (currentIndex != -1)
        {
            mediaPlayer.play();
        }
    }

    /**
     * Pause song.
     */
    public void pauseMedia()
    {
        if (currentIndex != -1)
        {
            mediaPlayer.pause();
        }
    }

    /**
     * Change to previous song in list.
     */
    public void previousMedia()
    {
        if (currentIndex - 1 < 0)
        {
            currentIndex = songs.size();
        }
        else
        {
            currentIndex--;
        }
        switchSong();
    }

    /**
     * Change to next song in list.
     */
    public void nextMedia()
    {
        if (currentIndex + 1 >= songs.size())
        {
            currentIndex = 0;
        }
        else
        {
            currentIndex++;
        }
        switchSong();
    }

    /**
     * Switch song to current index.
     */
    private void switchSong()
    {
        if (currentIndex != -1)
        {
            sound = new Media(new File(songs.get(currentIndex).getpath()).toURI().toString());
            mediaPlayer = new MediaPlayer(sound);
            mediaPlayer.setVolume(currentVolume);
        }

    }

    /**
     * links the volumeslider to the mediaplayers volume
     * @param volumeSlider the Slider who have to adjust the volume
     */
    public void volumeSliderSetup(Slider volumeSlider)
    {
        volumeSlider.setValue(mediaPlayer.getVolume() * volumeSlider.getMax());
        volumeSlider.valueProperty().addListener(new InvalidationListener()
        {
            @Override
            public void invalidated(Observable observable)
            {
                mediaPlayer.setVolume(volumeSlider.getValue() / volumeSlider.getMax());
                currentVolume = (volumeSlider.getValue() / volumeSlider.getMax());
            }
        });

    }

    public String selectedDeletedElements(String SelectedElement)
    {
        selectedElement = SelectedElement;
        return selectedElement;
    }

    public String getSelectedElement()
    {
        return selectedElement;
    }

}
