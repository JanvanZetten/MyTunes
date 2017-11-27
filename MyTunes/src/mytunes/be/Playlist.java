/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mytunes.be;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author janvanzetten
 */
public class Playlist {
    List<Song> songs;
    String name;

    
    /**
     * Constructer for a playlist
     * @param name the name you want to give the playlist as a String
     */
    public Playlist(String name) {
        this.name = name;
        songs  = new ArrayList<>();
    }
    
    /**
     * give a playlist a new name
     * @param newName the new name as a string
     */
    public void renamePlaylist(String newName){
        name = newName;
    }
    
    /**
     * add a song object to the playlist
     * @param song 
     */
    public void addSongToPlaylist(Song song){
        songs.add(song);
    }

    /**
     * Returns all the songs from the playlist as a list
     * @return a list of songs
     */
    public List<Song> getSongs() {
        return songs;
    }

    /**
     * gets the name of the playlist
     * @return a string with the name
     */
    public String getName() {
        return name;
    }
}