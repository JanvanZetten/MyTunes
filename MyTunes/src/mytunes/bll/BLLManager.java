/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mytunes.bll;

import java.util.List;
import mytunes.be.Playlist;
import mytunes.dal.DALManager;

/**
 *
 * @author janvanzetten
 */
public class BLLManager {
    DALManager dalManager = new DALManager();

    /**
     * gets all the playlists
     * @return a list with all the playlists
     */
    public List<Playlist> getAllPlaylists() {
        return dalManager.getAllPlaylists();
    }

    /**
     * loads a playlist callad All Songs
     */
    public void loadPlaylistAllSongs() {
        dalManager.loadPlaylistAllSongs();
    }

    
    /**
     * get the All songs playlist
     * @return the playlist with all the songs
     */
    public Playlist getAllSongsPlaylist() {
        return dalManager.getAllSongsPlaylist();
    }
    
}
