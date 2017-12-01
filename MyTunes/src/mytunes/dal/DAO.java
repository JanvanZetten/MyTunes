/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mytunes.dal;

import java.util.List;
import mytunes.be.Genre;
import mytunes.be.Playlist;
import mytunes.be.Song;

/**
 *
 * @author Asbamz
 */
public interface DAO
{
    /**
     * Get all Playlists in data.
     *
     * @return A list of Playlist objects.
     * @throws DALException
     */
    public List<Playlist> getAllPlaylists() throws DALException;

    /**
     * Get all Songs in data.
     *
     * @return A list of Song objects
     * @throws DALException
     */
    public List<Song> getAllSongs() throws DALException;

    /**
     * Get all genres in data.
     *
     * @return A list of Genre.
     * @throws DALException
     */
    public List<Genre> getAllGenres() throws DALException;

    /**
     * Add new Genre to data and return the new genre.
     *
     * @param genre new genre to add.
     * @return Genre object with new information.
     * @throws DALException
     */
    public Genre addGenre(String genre) throws DALException;

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
     * @throws DALException
     */
    public Song addSong(String artist, String title, String album, int year, Genre genre, String directory) throws DALException;

    /**
     * Add new Playlist to data and return the new playlist.
     *
     * @param name new name of playlist.
     * @return Playlist object with new information.
     * @throws DALException
     */
    public Playlist addPlaylist(String name) throws DALException;

    /**
     * Add a Song to a Playlist in data.
     *
     * @param playlist wanted to add to.
     * @param song wanted added.
     * @return true if song was added to playlist.
     * @throws DALException
     */
    public boolean addSongToPlaylist(Playlist playlist, Song song) throws DALException;

    /**
     * Update Genre in data and return the updated genre.
     *
     * @param genreId genre wanted renamed.
     * @param genre new genre.
     * @return updated Genre object.
     * @throws DALException
     */
    public Genre updateGenre(int genreId, String genre) throws DALException;

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
     * @throws DALException
     */
    public Song updateSong(int songId, String artist, String title, String album, int year, Genre genre, String directory) throws DALException;

    /**
     * Update Playlist in data and return the updated playlist.
     *
     * @param playlistId Id of playlist wanted updated.
     * @param name new name of playlist.
     * @return Playlist object with new information.
     * @throws DALException
     */
    public Playlist updatePlaylist(int playlistId, String name) throws DALException;

    /**
     * Delete Genre from data and return true if operation succeeded.
     *
     * @param genreId genre wanted removed.
     * @return succession boolean.
     * @throws DALException
     */
    public boolean deleteGenre(int genreId) throws DALException;

    /**
     * Delete Song from data and return true if operation succeeded.
     *
     * @param songId song wanted removed.
     * @return succession boolean.
     * @throws DALException
     */
    public boolean deleteSong(int songId) throws DALException;

    /**
     * Delete Playlist from data and return true if operation succeeded.
     *
     * @param playlistId playlist wanted removed.
     * @return succession boolean.
     * @throws DALException
     */
    public boolean deletePlaylist(int playlistId) throws DALException;

    /**
     * Delete Song from Playlist in data and return true if operation succeeded.
     *
     * @param songId song wanted removed from playlist.
     * @param playlistId playlist wanted song removed from.
     * @return succession boolean.
     * @throws DALException
     */
    public boolean deleteSongInPlaylist(int songId, int playlistId) throws DALException;

    /**
     * Swap songs in playlist to get wanted order.
     * @param firstSongId song wanted swapped.
     * @param secondSongId song wanted swapped.
     * @param playlistId playlist wanted affected.
     * @return succession boolean.
     * @throws DALException
     */
    public boolean swapSongsInPlaylist(int firstSongId, int secondSongId, int playlistId) throws DALException;
}
