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

    public List<Playlist> getAllPlaylists() {
        List<Playlist> playlists = new ArrayList<>();
        Playlist playlist = new Playlist("Test Playlist with no content");
        playlist.addSongToPlaylist(new Song("test title", "Jan van Zetten", "/noWhere"));
        playlists.add(playlist);
        return playlists;
    }

    
    
}
