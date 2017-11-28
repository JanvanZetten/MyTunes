/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mytunes.gui.model;

import java.io.File;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import mytunes.be.Playlist;
import mytunes.be.Song;
import mytunes.bll.BLLManager;

/**
 *
 * @author Alex
 */
public class MainWindowModel
{

    private BLLManager bllManager;
    private ObservableList<Playlist> playlists;
    private ObservableList<Song> songs;
    Media sound;
    MediaPlayer mediaPlayer;
    int currentIndex = -1;

    public MainWindowModel()
    {
        bllManager = new BLLManager();
        playlists = FXCollections.observableArrayList();
        songs = FXCollections.observableArrayList();
        bllManager.loadPlaylistAllSongs();
    }

    /**
     * Updates the list for the gui element which shows the playlists remember
     * to call the getPlaylists() and assign the observable list to the element
     * in which it has to be shown
     */
    public void addAllPlaylistsToGUI()
    {
        playlists.clear();
        playlists.addAll(bllManager.getAllPlaylists());
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

    public Playlist getAllSongsPlaylist()
    {
        return bllManager.getAllSongsPlaylist();
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
        }
    }

}
