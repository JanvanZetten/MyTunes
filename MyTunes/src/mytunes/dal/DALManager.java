/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mytunes.dal;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import mytunes.be.Genre;
import mytunes.be.Playlist;
import mytunes.be.Song;

/**
 *
 * @author janvanzetten
 */
public class DALManager
{
    DatabaseConnector dc;
    DAO databaseDAO;
    DAO localDAO;
    boolean offlineMode;

    public DALManager() throws DALException
    {
        databaseDAO = new DatabaseDAO();
        localDAO = new LocalDAO();
        dc = new DatabaseConnector();
        offlineMode = false;
        syncAll();
    }

    /**
     * Get all Playlists in data.
     *
     * @return A list of Playlist objects.
     * @throws DALException
     */
    public List<Playlist> getAllPlaylists() throws DALException
    {
        if (!offlineMode)
        {
            try
            {
                try
                {
                    System.out.println("Try Connection");
                    if (dc.getConnection().isValid(1))
                    {
                        System.out.println("Try getting playlist from database");
                        List<Playlist> tmp = databaseDAO.getAllPlaylists();
                        syncLocal();
                        return tmp;
                    }
                    else
                    {
                        System.out.println("Try getting playlist from local");
                        try
                        {
                            return localDAO.getAllPlaylists();
                        }
                        catch (DALException ex)
                        {
                            System.out.println("No localDB.");
                            return new ArrayList<>();
                        }
                    }
                }
                catch (SQLException e)
                {
                    offlineMode = true;
                    System.out.println("Connection failed! Went offline mode!");
                    try
                    {
                        return localDAO.getAllPlaylists();
                    }
                    catch (DALException ex)
                    {
                        System.out.println("No localDB.");
                        return new ArrayList<>();
                    }
                }
            }
            catch (DALException ex)
            {
                throw new DALException(ex.getMessage(), ex.getCause());
            }
        }
        else
        {
            try
            {
                return localDAO.getAllPlaylists();
            }
            catch (DALException ex)
            {
                System.out.println("No localDB.");
                return new ArrayList<>();
            }
        }
    }

    /**
     * Get all Songs in data.
     *
     * @return A list of Song objects
     * @throws DALException
     */
    public List<Song> getAllSongs() throws DALException
    {
        if (!offlineMode)
        {
            try
            {
                try
                {
                    System.out.println("Try Connection");
                    if (dc.getConnection().isValid(1))
                    {
                        System.out.println("Try getting songs from database");
                        List<Song> tmp = databaseDAO.getAllSongs();
                        syncLocal();
                        return tmp;
                    }
                    else
                    {
                        System.out.println("Try getting songs from local");
                        try
                        {
                            return localDAO.getAllSongs();
                        }
                        catch (DALException ex)
                        {
                            System.out.println("No localDB.");
                            return new ArrayList<>();
                        }
                    }
                }
                catch (SQLException e)
                {
                    offlineMode = true;
                    System.out.println("Connection failed! Went offline mode!");
                    try
                    {
                        return localDAO.getAllSongs();
                    }
                    catch (DALException ex)
                    {
                        System.out.println("No localDB.");
                        return new ArrayList<>();
                    }
                }
            }
            catch (DALException ex)
            {
                throw new DALException(ex.getMessage(), ex.getCause());
            }
        }
        else
        {
            try
            {
                return localDAO.getAllSongs();
            }
            catch (DALException ex)
            {
                System.out.println("No localDB.");
                return new ArrayList<>();
            }
        }
    }

    /**
     * Get all genres in data.
     *
     * @return A list of Genre.
     * @throws DALException
     */
    public List<Genre> getAllGenres() throws DALException
    {
        if (!offlineMode)
        {
            try
            {
                try
                {
                    System.out.println("Try Connection");
                    if (dc.getConnection().isValid(1))
                    {
                        System.out.println("Try getting genres from database");
                        List<Genre> tmp = databaseDAO.getAllGenres();
                        syncLocal();
                        return tmp;
                    }
                    else
                    {
                        System.out.println("Try getting genres from local");
                        try
                        {
                            return localDAO.getAllGenres();
                        }
                        catch (DALException ex)
                        {
                            System.out.println("No localDB.");
                            return new ArrayList<>();
                        }
                    }
                }
                catch (SQLException e)
                {
                    offlineMode = true;
                    System.out.println("Connection failed! Went offline mode!");
                    try
                    {
                        return localDAO.getAllGenres();
                    }
                    catch (DALException ex)
                    {
                        System.out.println("No localDB.");
                        return new ArrayList<>();
                    }
                }
            }
            catch (DALException ex)
            {
                throw new DALException(ex.getMessage(), ex.getCause());
            }
        }
        else
        {
            try
            {
                return localDAO.getAllGenres();
            }
            catch (DALException ex)
            {
                System.out.println("No localDB.");
                return new ArrayList<>();
            }
        }
    }

    /**
     * Add new Genre to data and return the new genre.
     *
     * @param genre new genre to add.
     * @return Genre object with new information.
     * @throws DALException
     */
    public Genre addGenre(String genre) throws DALException
    {
        if (!offlineMode)
        {
            try
            {
                try
                {
                    System.out.println("Try Connection");
                    if (dc.getConnection().isValid(1))
                    {
                        System.out.println("Try adding genre to database");
                        Genre tmp = databaseDAO.addGenre(genre);
                        syncLocal();
                        return tmp;
                    }
                    else
                    {
                        System.out.println("Try adding genre to local");
                        return localDAO.addGenre(genre);
                    }
                }
                catch (SQLException e)
                {
                    offlineMode = true;
                    System.out.println("Connection failed! Went offline mode!");
                    return localDAO.addGenre(genre);
                }
            }
            catch (DALException ex)
            {
                throw new DALException(ex.getMessage(), ex.getCause());
            }
        }
        else
        {
            return localDAO.addGenre(genre);
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
     * @throws DALException
     */
    public Song addSong(String artist, String title, String album, int year, Genre genre, String directory) throws DALException
    {
        if (!offlineMode)
        {
            try
            {
                try
                {
                    System.out.println("Try Connection");
                    if (dc.getConnection().isValid(1))
                    {
                        System.out.println("Try adding song to database");
                        Song tmp = databaseDAO.addSong(artist, title, album, year, genre, directory);
                        syncLocal();
                        return tmp;
                    }
                    else
                    {
                        System.out.println("Try adding song to local");
                        return localDAO.addSong(artist, title, album, year, genre, directory);
                    }
                }
                catch (SQLException e)
                {
                    offlineMode = true;
                    System.out.println("Connection failed! Went offline mode!");
                    return localDAO.addSong(artist, title, album, year, genre, directory);
                }
            }
            catch (DALException ex)
            {
                throw new DALException(ex.getMessage(), ex.getCause());
            }
        }
        else
        {
            return localDAO.addSong(artist, title, album, year, genre, directory);
        }
    }

    /**
     * Add new Playlist to data and return the new playlist.
     *
     * @param name new name of playlist.
     * @return Playlist object with new information.
     * @throws DALException
     */
    public Playlist addPlaylist(String name) throws DALException
    {
        if (!offlineMode)
        {
            try
            {
                try
                {
                    System.out.println("Try Connection");
                    if (dc.getConnection().isValid(1))
                    {
                        System.out.println("Try adding playlist to database");
                        Playlist tmp = databaseDAO.addPlaylist(name);
                        syncLocal();
                        return tmp;
                    }
                    else
                    {
                        System.out.println("Try adding playlist to local");
                        return localDAO.addPlaylist(name);
                    }
                }
                catch (SQLException e)
                {
                    offlineMode = true;
                    System.out.println("Connection failed! Went offline mode!");
                    return localDAO.addPlaylist(name);
                }
            }
            catch (DALException ex)
            {
                throw new DALException(ex.getMessage(), ex.getCause());
            }
        }
        else
        {
            return localDAO.addPlaylist(name);
        }
    }

    /**
     * Add a Song to a Playlist in data.
     *
     * @param playlist wanted to add to.
     * @param song wanted added.
     * @return true if song was added to playlist.
     * @throws DALException
     */
    public boolean addSongToPlaylist(Playlist playlist, Song song) throws DALException
    {
        if (!offlineMode)
        {
            try
            {
                try
                {
                    System.out.println("Try Connection");
                    if (dc.getConnection().isValid(1))
                    {
                        System.out.println("Try adding song playlist relation to database");
                        boolean tmp = databaseDAO.addSongToPlaylist(playlist, song);
                        syncLocal();
                        return tmp;
                    }
                    else
                    {
                        System.out.println("Try adding song playlist relation to local");
                        return localDAO.addSongToPlaylist(playlist, song);
                    }
                }
                catch (SQLException e)
                {
                    offlineMode = true;
                    System.out.println("Connection failed! Went offline mode!");
                    return localDAO.addSongToPlaylist(playlist, song);
                }
            }
            catch (DALException ex)
            {
                throw new DALException(ex.getMessage(), ex.getCause());
            }
        }
        else
        {
            return localDAO.addSongToPlaylist(playlist, song);
        }
    }

    /**
     * Update Genre in data and return the updated genre.
     *
     * @param genreId genre wanted renamed.
     * @param genre new genre.
     * @return updated Genre object.
     * @throws DALException
     */
    public Genre updateGenre(int genreId, String genre) throws DALException
    {
        if (!offlineMode)
        {
            try
            {
                try
                {
                    System.out.println("Try Connection");
                    if (dc.getConnection().isValid(1))
                    {
                        System.out.println("Try updating genre in database");
                        Genre tmp = databaseDAO.updateGenre(genreId, genre);
                        syncLocal();
                        return tmp;
                    }
                    else
                    {
                        System.out.println("Try updating genre in local");
                        return localDAO.updateGenre(genreId, genre);
                    }
                }
                catch (SQLException e)
                {
                    offlineMode = true;
                    System.out.println("Connection failed! Went offline mode!");
                    return localDAO.updateGenre(genreId, genre);
                }
            }
            catch (DALException ex)
            {
                throw new DALException(ex.getMessage(), ex.getCause());
            }
        }
        else
        {
            return localDAO.updateGenre(genreId, genre);
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
     * @throws DALException
     */
    public Song updateSong(int songId, String artist, String title, String album, int year, Genre genre, String directory) throws DALException
    {
        if (!offlineMode)
        {
            try
            {
                try
                {
                    System.out.println("Try Connection");
                    if (dc.getConnection().isValid(1))
                    {
                        System.out.println("Try updating song in database");
                        Song tmp = databaseDAO.updateSong(songId, artist, title, album, year, genre, directory);
                        syncLocal();
                        return tmp;
                    }
                    else
                    {
                        System.out.println("Try updating song in local");
                        return localDAO.updateSong(songId, artist, title, album, year, genre, directory);
                    }
                }
                catch (SQLException e)
                {
                    offlineMode = true;
                    System.out.println("Connection failed! Went offline mode!");
                    return localDAO.updateSong(songId, artist, title, album, year, genre, directory);
                }
            }
            catch (DALException ex)
            {
                throw new DALException(ex.getMessage(), ex.getCause());
            }
        }
        else
        {
            return localDAO.updateSong(songId, artist, title, album, year, genre, directory);
        }
    }

    /**
     * Update Playlist in data and return the updated playlist.
     *
     * @param playlistId Id of playlist wanted updated.
     * @param name new name of playlist.
     * @return Playlist object with new information.
     * @throws DALException
     */
    public Playlist updatePlaylist(int playlistId, String name) throws DALException
    {
        if (!offlineMode)
        {
            try
            {
                try
                {
                    System.out.println("Try Connection");
                    if (dc.getConnection().isValid(1))
                    {
                        System.out.println("Try updating playlist in database");
                        Playlist tmp = databaseDAO.updatePlaylist(playlistId, name);
                        syncLocal();
                        return tmp;
                    }
                    else
                    {
                        System.out.println("Try updating playlist in local");
                        return localDAO.updatePlaylist(playlistId, name);
                    }
                }
                catch (SQLException e)
                {
                    offlineMode = true;
                    System.out.println("Connection failed! Went offline mode!");
                    return localDAO.updatePlaylist(playlistId, name);
                }
            }
            catch (DALException ex)
            {
                throw new DALException(ex.getMessage(), ex.getCause());
            }
        }
        else
        {
            return localDAO.updatePlaylist(playlistId, name);
        }
    }

    /**
     * Delete Genre from data and return true if operation succeeded.
     *
     * @param genreId genre wanted removed.
     * @return succession boolean.
     * @throws DALException
     */
    public boolean deleteGenre(int genreId) throws DALException
    {
        if (!offlineMode)
        {
            try
            {
                try
                {
                    System.out.println("Try Connection");
                    if (dc.getConnection().isValid(1))
                    {
                        System.out.println("Try deleting genre from database");
                        boolean tmp = databaseDAO.deleteGenre(genreId);
                        syncLocal();
                        return tmp;
                    }
                    else
                    {
                        System.out.println("Try deleting genre from local");
                        return localDAO.deleteGenre(genreId);
                    }
                }
                catch (SQLException e)
                {
                    offlineMode = true;
                    System.out.println("Connection failed! Went offline mode!");
                    return localDAO.deleteGenre(genreId);
                }
            }
            catch (DALException ex)
            {
                throw new DALException(ex.getMessage(), ex.getCause());
            }
        }
        else
        {
            return localDAO.deleteGenre(genreId);
        }
    }

    /**
     * Delete Song from data and return true if operation succeeded.
     *
     * @param songId song wanted removed.
     * @return succession boolean.
     * @throws DALException
     */
    public boolean deleteSong(int songId) throws DALException
    {
        if (!offlineMode)
        {
            try
            {
                try
                {
                    System.out.println("Try Connection");
                    if (dc.getConnection().isValid(1))
                    {
                        System.out.println("Try deleting song from database");
                        boolean tmp = databaseDAO.deleteSong(songId);
                        syncLocal();
                        return tmp;
                    }
                    else
                    {
                        System.out.println("Try deleting song from local");
                        return localDAO.deleteSong(songId);
                    }
                }
                catch (SQLException e)
                {
                    offlineMode = true;
                    System.out.println("Connection failed! Went offline mode!");
                    return localDAO.deleteSong(songId);
                }
            }
            catch (DALException ex)
            {
                throw new DALException(ex.getMessage(), ex.getCause());
            }
        }
        else
        {
            return localDAO.deleteSong(songId);
        }
    }

    /**
     * Delete Playlist from data and return true if operation succeeded.
     *
     * @param playlistId playlist wanted removed.
     * @return succession boolean.
     * @throws DALException
     */
    public boolean deletePlaylist(int playlistId) throws DALException
    {
        if (!offlineMode)
        {
            try
            {
                try
                {
                    System.out.println("Try Connection");
                    if (dc.getConnection().isValid(1))
                    {
                        System.out.println("Try deleting playlist from database");
                        boolean tmp = databaseDAO.deletePlaylist(playlistId);
                        syncLocal();
                        return tmp;
                    }
                    else
                    {
                        System.out.println("Try deleting playlist from local");
                        return localDAO.deletePlaylist(playlistId);
                    }
                }
                catch (SQLException e)
                {
                    offlineMode = true;
                    System.out.println("Connection failed! Went offline mode!");
                    return localDAO.deletePlaylist(playlistId);
                }
            }
            catch (DALException ex)
            {
                throw new DALException(ex.getMessage(), ex.getCause());
            }
        }
        else
        {
            return localDAO.deletePlaylist(playlistId);
        }
    }

    /**
     * Delete Song from Playlist in data and return true if operation succeeded.
     *
     * @param songId song wanted removed from playlist.
     * @param playlistId playlist wanted song removed from.
     * @return succession boolean.
     * @throws DALException
     */
    public boolean deleteSongInPlaylist(int songId, int playlistId) throws DALException
    {
        if (!offlineMode)
        {
            try
            {
                try
                {
                    System.out.println("Try Connection");
                    if (dc.getConnection().isValid(1))
                    {
                        System.out.println("Try deleting song playlist relation in database");
                        boolean tmp = databaseDAO.deleteSongInPlaylist(songId, playlistId);
                        syncLocal();
                        return tmp;
                    }
                    else
                    {
                        System.out.println("Try deleting song playlist relation in local");
                        return localDAO.deleteSongInPlaylist(songId, playlistId);
                    }
                }
                catch (SQLException e)
                {
                    offlineMode = true;
                    System.out.println("Connection failed! Went offline mode!");
                    return localDAO.deleteSongInPlaylist(songId, playlistId);
                }
            }
            catch (DALException ex)
            {
                throw new DALException(ex.getMessage(), ex.getCause());
            }
        }
        else
        {
            return localDAO.deleteSongInPlaylist(songId, playlistId);
        }
    }

    /**
     * swab the placement of two songs in a playlist in the database
     * @param firstSongId
     * @param secondSongId
     * @param playlistId
     * @return
     * @throws DALException
     */
    public boolean swapSongsInPlaylist(int firstSongId, int secondSongId, int playlistId) throws DALException
    {
        if (!offlineMode)
        {
            try
            {
                try
                {
                    System.out.println("Try Connection");
                    if (dc.getConnection().isValid(1))
                    {
                        System.out.println("Try swapping song in databse");
                        boolean tmp = databaseDAO.swapSongsInPlaylist(firstSongId, secondSongId, playlistId);
                        syncLocal();
                        return tmp;
                    }
                    else
                    {
                        System.out.println("Try swapping song in local");
                        return localDAO.swapSongsInPlaylist(firstSongId, secondSongId, playlistId);
                    }
                }
                catch (SQLException e)
                {
                    offlineMode = true;
                    System.out.println("Connection failed! Went offline mode!");
                    return localDAO.swapSongsInPlaylist(firstSongId, secondSongId, playlistId);
                }
            }
            catch (DALException ex)
            {
                throw new DALException(ex.getMessage(), ex.getCause());
            }
        }
        else
        {
            return localDAO.swapSongsInPlaylist(firstSongId, secondSongId, playlistId);
        }
    }

    private void syncAll() throws DALException
    {
        try
        {
            System.out.println("Try Connection");
            if (dc.getConnection().isValid(1))
            {
                System.out.println("Try Syncing Database");
                try
                {
                    databaseDAO.sync(localDAO);
                }
                catch (DALException ex)
                {
                    System.out.println("Sync failed");
                }
                System.out.println("Try Syncing Local");
                try
                {
                    localDAO.sync(databaseDAO);
                }
                catch (DALException ex)
                {
                    System.out.println("Sync failed");
                }
            }
        }
        catch (SQLException e)
        {
            offlineMode = true;
            System.out.println("Connection or sync failed! Went offline mode!");
        }
    }

    private void syncLocal() throws DALException
    {
        try
        {
            System.out.println("Try Connection");
            if (dc.getConnection().isValid(1))
            {
                System.out.println("Try Syncing");
                localDAO.sync(databaseDAO);
            }
        }
        catch (SQLException e)
        {
            System.out.println("Connection or sync failed!");
        }
    }
}
