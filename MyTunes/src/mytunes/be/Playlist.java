/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mytunes.be;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author janvanzetten
 */
public class Playlist implements Serializable
{
    private final int playlistId;
    private List<Song> songs;
    private String name;

    /**
     * Constructer for a playlist
     *
     * @param name the name you want to give the playlist as a String
     */
    public Playlist(int playlistId, String name)
    {
        this.playlistId = playlistId;
        this.name = name;
        songs = new ArrayList<>();
    }

    /**
     * give a playlist a new name
     *
     * @param newName the new name as a string
     */
    public void renamePlaylist(String newName)
    {
        name = newName;
    }

    /**
     * add a song object to the playlist
     *
     * @param song
     */
    public void addSongToPlaylist(Song song)
    {
        songs.add(song);
    }

    /**
     * add a song object to the playlist
     *
     * @param song
     */
    public void addAllSongToPlaylist(List<Song> song)
    {
        songs.addAll(song);
    }

    /**
     * Returns all the songs from the playlist as a list
     *
     * @return a list of songs
     */
    public List<Song> getSongs()
    {
        return songs;
    }

    /**
     * gets the id of the playlist
     *
     * @return a int with the id
     */
    public int getPlaylistId()
    {
        return playlistId;
    }

    /**
     * gets the name of the playlist
     *
     * @return a string with the name
     */
    public String getName()
    {
        return name;
    }

    @Override
    public String toString()
    {
        return name;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj == null)
        {
            return false;
        }
        if (obj instanceof Playlist)
        {
            return this.playlistId == ((Playlist) obj).getPlaylistId();
        }
        return super.equals(obj); //To change body of generated methods, choose Tools | Templates.
    }
}
