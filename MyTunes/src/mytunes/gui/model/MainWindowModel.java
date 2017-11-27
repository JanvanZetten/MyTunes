/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mytunes.gui.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import mytunes.be.Playlist;
import mytunes.bll.BLLManager;

/**
 *
 * @author Alex
 */
public class MainWindowModel {
    
    private BLLManager BLLManager = new BLLManager();
    private ObservableList<Playlist> playlists = FXCollections.observableArrayList();
    
    
    
    /**
     * Updates the list for the gui element which shows the playlists
     * remember to call the getPlaylists() and assign the observable list to the element in which it has to be shown
     */
    public void addAllPlaylistsToGUI(){
        playlists.clear();
        playlists.addAll(BLLManager.getAllPlaylists());
    }
    
    /**
     * gets the observablelist with the playlists
     * @return a observablelist with Playlist objects
     */
    public ObservableList<Playlist> getPlaylists(){
        return playlists;
    }
    
    
}
