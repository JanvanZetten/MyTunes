/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mytunes.gui.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import mytunes.be.Playlist;
import mytunes.be.Song;
import mytunes.bll.BLLManager;

/**
 *
 * @author Alex
 */
public class MainWindowModel {
    
    private BLLManager bllManager;
    private ObservableList<Playlist> playlists;
    private ObservableList<Song> songs;
    

    public MainWindowModel() {
        bllManager = new BLLManager();
        playlists = FXCollections.observableArrayList();
        songs = FXCollections.observableArrayList();
        bllManager.loadPlaylistAllSongs();
    }
    
    
    
    /**
     * Updates the list for the gui element which shows the playlists
     * remember to call the getPlaylists() and assign the observable list to the element in which it has to be shown
     */
    public void addAllPlaylistsToGUI(){
        playlists.clear();
        playlists.addAll(bllManager.getAllPlaylists());
    }
    
    /**
     * gets the observablelist with the playlists
     * @return a observablelist with Playlist objects
     */
    public ObservableList<Playlist> getPlaylists(){
        return playlists;
    }
    
    /**
     * gets the observablelist with the songs
     * @return a observablelist with Playlist objects
     */
    public ObservableList<Song> getSongs(){
        return songs;
    }

    /**
     * set the songs from the given playlist in the observablelist. 
     * remember to add the observablelist to the view with getSong()
     * @param selectedItem the playlist from which to take the song
     */
    public void setSongs(Playlist selectedItem) {
        songs.clear();
        songs.addAll(selectedItem.getSongs());
        }

    /**
     * gets the playlist with all the songs
     * @return playlist with all the known songs
     */
    public Playlist getAllSongsPlaylist() {
        return bllManager.getAllSongsPlaylist();
    }

    
    
    
}
