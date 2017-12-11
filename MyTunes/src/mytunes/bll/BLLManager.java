/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mytunes.bll;

import java.util.List;
import mytunes.be.Genre;
import mytunes.be.Playlist;
import mytunes.be.Song;
import mytunes.dal.DALException;
import mytunes.dal.DALManager;

/**
 *
 * @author Alex, Asbjørn og Jan
 */
public class BLLManager
{
    DALManager dalManager;

    public BLLManager() throws BLLException
    {
        try
        {
            dalManager = new DALManager();
        }
        catch (DALException ex)
        {
            throw new BLLException(ex.getMessage(), ex.getCause());
        }
    }

    /**
     * Get all Playlists in data.
     *
     * @return A list of Playlist objects.
     * @throws BLLException
     */
    public List<Playlist> getAllPlaylists() throws BLLException
    {
        try
        {
            return dalManager.getAllPlaylists();
        }
        catch (DALException ex)
        {
            throw new BLLException(ex.getMessage(), ex.getCause());
        }
    }

    /**
     * Get all Songs in data.
     *
     * @return A list of Song objects
     * @throws BLLException
     */
    public List<Song> getAllSongs() throws BLLException
    {
        try
        {
            return dalManager.getAllSongs();
        }
        catch (DALException ex)
        {
            throw new BLLException(ex.getMessage(), ex.getCause());
        }
    }

    /**
     * Get all genres in data.
     *
     * @return A list of Genre.
     * @throws BLLException
     */
    public List<Genre> getAllGenres() throws BLLException
    {
        try
        {
            return dalManager.getAllGenres();
        }
        catch (DALException ex)
        {
            throw new BLLException(ex.getMessage(), ex.getCause());
        }
    }

    /**
     * Add new Genre to data and return the new genre.
     *
     * @param genre new genre to add.
     * @return Genre object with new information.
     * @throws BLLException
     */
    public Genre addGenre(String genre) throws BLLException
    {
        try
        {
            return dalManager.addGenre(genre);
        }
        catch (DALException ex)
        {
            throw new BLLException(ex.getMessage(), ex.getCause());
        }
    }

    /**
     * Add new Song to data and return the new song.
     *
     * @param artist new artist of song.
     * @param title new title of song.
     * @param album new album of song.
     * @param year new year of song.
     * @param genre new genre of song.
     * @param directory new directory of song.
     * @return Song object with new information.
     * @throws BLLException
     */
    public Song addSong(String artist, String title, String album, int year, Genre genre, String directory) throws BLLException
    {
        try
        {
            return dalManager.addSong(artist, title, album, year, genre, directory);
        }
        catch (DALException ex)
        {
            throw new BLLException(ex.getMessage(), ex.getCause());
        }
    }

    /**
     * Add new Playlist to data and return the new playlist.
     *
     * @param name new name of playlist.
     * @return Playlist object with new information.
     * @throws BLLException
     */
    public Playlist addPlaylist(String name) throws BLLException
    {
        try
        {
            return dalManager.addPlaylist(name);
        }
        catch (DALException ex)
        {
            throw new BLLException(ex.getMessage(), ex.getCause());
        }
    }

    /**
     * Add a Song to a Playlist in data.
     *
     * @param playlist wanted to add to.
     * @param song wanted added.
     * @return true if song was added to playlist.
     * @throws BLLException
     */
    public boolean addSongToPlaylist(Playlist playlist, Song song) throws BLLException
    {
        try
        {
            return dalManager.addSongToPlaylist(playlist, song);
        }
        catch (DALException ex)
        {
            throw new BLLException(ex.getMessage(), ex.getCause());
        }
    }

    /**
     * Update Genre in data and return the updated genre.
     *
     * @param genreId genre wanted renamed.
     * @param genre new genre.
     * @return updated Genre object.
     * @throws BLLException
     */
    public Genre updateGenre(int genreId, String genre) throws BLLException
    {
        try
        {
            return dalManager.updateGenre(genreId, genre);
        }
        catch (DALException ex)
        {
            throw new BLLException(ex.getMessage(), ex.getCause());
        }
    }

    /**
     * Update Song in data and return the updated song.
     *
     * @param songId Id of song wanted updated.
     * @param artist new artist of song.
     * @param title new title of song.
     * @param album new album of song.
     * @param year new year of song.
     * @param genre new genre of song.
     * @param directory new directory of song.
     * @return Song object with new information.
     * @throws BLLException
     */
    public Song updateSong(int songId, String artist, String title, String album, int year, Genre genre, String directory) throws BLLException
    {
        try
        {
            return dalManager.updateSong(songId, artist, title, album, year, genre, directory);
        }
        catch (DALException ex)
        {
            throw new BLLException(ex.getMessage(), ex.getCause());
        }
    }

    /**
     * Update Playlist in data and return the updated playlist.
     *
     * @param playlistId Id of playlist wanted updated.
     * @param name new name of playlist.
     * @return Playlist object with new information.
     * @throws BLLException
     */
    public Playlist updatePlaylist(int playlistId, String name) throws BLLException
    {
        try
        {
            return dalManager.updatePlaylist(playlistId, name);
        }
        catch (DALException ex)
        {
            throw new BLLException(ex.getMessage(), ex.getCause());
        }
    }

    /**
     * Delete Genre from data and return true if operation succeeded.
     *
     * @param genreId genre wanted removed.
     * @return succession boolean.
     * @throws BLLException
     */
    public boolean deleteGenre(int genreId) throws BLLException
    {
        try
        {
            return dalManager.deleteGenre(genreId);
        }
        catch (DALException ex)
        {
            throw new BLLException(ex.getMessage(), ex.getCause());
        }
    }

    /**
     * Delete Song from data and return true if operation succeeded.
     *
     * @param songId song wanted removed.
     * @return succession boolean.
     * @throws BLLException
     */
    public boolean deleteSong(int songId) throws BLLException
    {
        try
        {
            return dalManager.deleteSong(songId);
        }
        catch (DALException ex)
        {
            throw new BLLException(ex.getMessage(), ex.getCause());
        }
    }

    /**
     * Delete Playlist from data and return true if operation succeeded.
     *
     * @param playlistId playlist wanted removed.
     * @return succession boolean.
     * @throws BLLException
     */
    public boolean deletePlaylist(int playlistId) throws BLLException
    {
        try
        {
            return dalManager.deletePlaylist(playlistId);
        }
        catch (DALException ex)
        {
            throw new BLLException(ex.getMessage(), ex.getCause());
        }
    }

    /**
     * Delete Song from Playlist in data and return true if operation succeeded.
     *
     * @param songId song wanted removed from playlist.
     * @param playlistId playlist wanted song removed from.
     * @return succession boolean.
     * @throws BLLException
     */
    public boolean deleteSongInPlaylist(int songId, int playlistId) throws BLLException
    {
        try
        {
            return dalManager.deleteSongInPlaylist(songId, playlistId);
        }
        catch (DALException ex)
        {
            throw new BLLException(ex.getMessage(), ex.getCause());
        }
    }

    /**
     * swab the placement of two songs in a playlist in the database
     * @param firstSongId
     * @param secondSongId
     * @param playlistId
     * @return
     * @throws BLLException
     */
    public boolean swapSongsInPlaylist(int firstSongId, int secondSongId, int playlistId) throws BLLException
    {
        try
        {
            return dalManager.swapSongsInPlaylist(firstSongId, secondSongId, playlistId);
        }
        catch (DALException ex)
        {
            throw new BLLException(ex.getMessage(), ex.getCause());
        }
    }
}
