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

    public List<Playlist> getAllPlaylists() {
        return dalManager.getAllPlaylists();
    }

    public void loadPlaylistAllSongs() {
        dalManager.loadPlaylistAllSongs();
    }

    public Playlist getAllSongsPlaylist() {
        return dalManager.getAllSongsPlaylist();
    }
    
}
