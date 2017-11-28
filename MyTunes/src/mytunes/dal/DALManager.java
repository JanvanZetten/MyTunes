/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mytunes.dal;

import java.util.ArrayList;
import java.util.List;
import mytunes.be.Playlist;
import mytunes.be.Song;

/**
 *
 * @author janvanzetten
 */
public class DALManager {
    List<Playlist> playlists = new ArrayList<>();

    public List<Playlist> getAllPlaylists() {
        Playlist playlist = new Playlist("Test Playlist with no content");
        playlist.addSongToPlaylist(new Song("test title", "Jan van Zetten", "/noWhere"));
        playlists.add(playlist);
        return playlists;
    }

    public void loadPlaylistAllSongs() {
        
        Playlist playlist = new Playlist("All my songs");
        playlist.addSongToPlaylist(new Song("test title", "Jan van Zetten", "/noWhere"));
        playlist.addSongToPlaylist(new Song("test title2", "En gruppe", "/noWhere"));
        
        playlists.add(playlist);
    }

    public Playlist getAllSongsPlaylist() {
        return playlists.get(0);
    }

    
    
}
