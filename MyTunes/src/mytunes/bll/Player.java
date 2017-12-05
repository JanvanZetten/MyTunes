/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mytunes.bll;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import mytunes.be.Playlist;
import mytunes.be.Song;

/**
 *
 * @author Asbamz
 */
public interface Player
{

    /**
     * gets the observablelist with the songs
     *
     * @return a observablelist with Playlist objects
     */
    public ObservableList<Song> getSongs();

    /**
     * Get observable String
     * @return
     */
    public SimpleStringProperty getArtist();

    /**
     * Get observable String
     * @return
     */
    public SimpleStringProperty getTitle();

    /**
     * Get observable String
     * @return
     */
    public SimpleStringProperty getAlbum();

    /**
     * Get observable String
     * @return
     */
    public SimpleStringProperty getCurrentTime();

    /**
     * Get observable String
     * @return
     */
    public SimpleStringProperty getDurationTime();

    /**
     * Get observable Double
     * @return
     */
    public SimpleDoubleProperty getProgress();

    /**
     * set the songs from the given playlist in the observablelist. remember to
     * add the observablelist to the view with getSong()
     *
     * @param selectedItem the playlist from which to take the song
     * @throws mytunes.bll.BLLException
     */
    public void setSongs(Playlist selectedItem) throws BLLException;

    /**
     * Play song.
     */
    public void playMedia();

    /**
     * Pause song.
     */
    public void pauseMedia();

    /**
     * Change to previous song in list.
     * @throws mytunes.bll.BLLException
     */
    public void previousMedia() throws BLLException;

    /**
     * Change to next song in list.
     * @throws mytunes.bll.BLLException
     */
    public void nextMedia() throws BLLException;

    /**
     * Get current volume of player.
     * @return current volume as double.
     */
    public double getVolume();

    /**
     * Set volume of player.
     * @param value new volume as double.
     */
    public void setVolume(double value);

    /**
     * Switch to wanted song on index.
     * @param index of song.
     * @throws BLLException if index does not run.
     */
    public void switchSong(int index) throws BLLException;

    /**
     * Switch looping playlist.
     */
    public boolean switchLooping();

    /**
     * Switch shuffling playlist.
     */
    public boolean switchShuffling();
}
