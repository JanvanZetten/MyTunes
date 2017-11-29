/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mytunes.dal;

import com.microsoft.sqlserver.jdbc.SQLServerException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import mytunes.be.Genre;
import mytunes.be.Playlist;
import mytunes.be.Song;

/**
 *
 * @author Asbamz
 */
public class DatabaseDAO implements DAO
{

    private DatabaseConnector dbc;

    public DatabaseDAO() throws DALException
    {
        dbc = new DatabaseConnector();
    }

    /**
     * Get all Playlists in database.
     *
     * @return A list of Playlist objects.
     * @throws DALException
     */
    public List<Playlist> getAllPlaylists() throws DALException
    {
        try (Connection con = dbc.getConnection())
        {
            String sql = "SELECT * FROM Playlist;";

            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);

            List<Playlist> playlists = new ArrayList<>();

            while (rs.next())
            {
                int id = rs.getInt("playlistId");
                Playlist playlist = new Playlist(
                        id,
                        rs.getString("name")
                );
                playlist.addAllSongToPlaylist(getAllSongsInPlaylist(id));
                playlists.add(playlist);
            }

            return playlists;
        }
        
        catch (SQLException ex)
        {
            throw new DALException("SQLException: " + ex.getMessage(), ex.getCause());
        }
    }

    /**
     * Get all Songs in database playlist with given playlistId.
     *
     * @param playlistId wished Playlist.
     * @return All Song objects in Playlist.
     * @throws DALException
     */
    private List<Song> getAllSongsInPlaylist(int playlistId) throws DALException
    {
        try (Connection con = dbc.getConnection())
        {
            String sql = "SELECT * FROM Song "
                    + "INNER JOIN Genre ON Song.genreId = Genre.genreId "
                    + "INNER JOIN SongsInPlaylist ON SongsInPlaylist.songId = Song.songId "
                    + "INNER JOIN Playlist ON Playlist.playlistId = SongsInPlaylist.playlistId "
                    + "WHERE Playlist.playlistId = " + playlistId + ";";

            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);

            List<Song> songs = new ArrayList<>();

            while (rs.next())
            {
                Song song = new Song(
                        rs.getInt("songId"),
                        rs.getString("title"),
                        rs.getString("artist"),
                        rs.getString("directory")
                );
                song.setAlbum(rs.getString("album"));
                song.setYear(rs.getInt("year"));
                song.setGenre(new Genre(rs.getInt("genreId"), rs.getString("genre")));
                songs.add(song);
            }

            return songs;
        }
        
        catch (SQLException ex)
        {
            throw new DALException("SQLException: " + ex.getMessage(), ex.getCause());
        }
    }

    /**
     * Get all Songs in database.
     *
     * @return A list of Song objects
     * @throws DALException
     */
    public List<Song> getAllSongs() throws DALException
    {
        try (Connection con = dbc.getConnection())
        {
            String sql = "SELECT * FROM Song INNER JOIN Genre ON Song.genreId = Genre.genreId;";

            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);

            List<Song> songs = new ArrayList<>();

            while (rs.next())
            {
                Song song = new Song(
                        rs.getInt("songId"),
                        rs.getString("title"),
                        rs.getString("artist"),
                        rs.getString("directory")
                );
                song.setAlbum(rs.getString("album"));
                song.setYear(rs.getInt("year"));
                song.setGenre(new Genre(rs.getInt("genreId"), rs.getString("genre")));
                songs.add(song);
            }

            return songs;
        }
        
        catch (SQLException ex)
        {
            throw new DALException(ex.getMessage(), ex.getCause());
        }
    }

    /**
     * Get all genres in database.
     *
     * @return A list of Genre.
     * @throws DALException
     */
    public List<Genre> GetGenres() throws DALException
    {
        try (Connection con = dbc.getConnection())
        {
            String sql = "SELECT * FROM Genre;";

            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);

            List<Genre> genres = new ArrayList<>();

            while (rs.next())
            {
                Genre g = new Genre(rs.getInt("genreId"), rs.getString("genre"));
                genres.add(g);
            }

            return genres;
        }
        
        catch (SQLException ex)
        {
            throw new DALException(ex.getMessage(), ex.getCause());
        }
    }

    /**
     * Add new Genre to database and return the new genre.
     *
     * @param genre new genre to add.
     * @return Genre object with new information.
     * @throws DALException
     */
    public Genre addGenre(String genre) throws DALException
    {
        try (Connection con = dbc.getConnection())
        {
            String sql = "INSERT INTO Genre VALUES (?);";

            PreparedStatement statement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            statement.setString(1, genre);

            if (statement.executeUpdate() == 1)
            {
                ResultSet rs = statement.getGeneratedKeys();
                rs.next();
                Genre newGenre = new Genre(rs.getInt("genreId"), genre);
                return newGenre;
            }
            else
            {
                throw new DALException("Could not add genre: " + genre);
            }
        }
        
        catch (SQLException ex)
        {
            throw new DALException(ex.getMessage(), ex.getCause());
        }
    }

    /**
     * Add new Song to database and return the new song.
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
        try (Connection con = dbc.getConnection())
        {
            String sql = "INSERT INTO Song VALUES (?, ?, ?, ?, ?, ?);";

            PreparedStatement statement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            statement.setString(1, artist);
            statement.setString(2, title);
            statement.setString(3, album);
            statement.setInt(4, year);
            statement.setInt(5, genre.getGenreId());
            statement.setString(6, directory);

            if (statement.executeUpdate() == 1)
            {
                ResultSet rs = statement.getGeneratedKeys();
                rs.next();
                Song newSong = new Song(rs.getInt("songId"), title, artist, directory);
                newSong.setAlbum(album);
                newSong.setYear(year);
                newSong.setGenre(genre);
                return newSong;
            }
            else
            {
                throw new DALException("Could not add song: " + artist + " - " + title + ", DIR: " + directory);
            }
        }
        
        catch (SQLException ex)
        {
            throw new DALException(ex.getMessage(), ex.getCause());
        }
    }

    /**
     * Add new Playlist to database and return the new playlist.
     *
     * @param name new name of playlist.
     * @return Playlist object with new information.
     * @throws DALException
     */
    public Playlist addPlaylist(String name) throws DALException
    {
        try (Connection con = dbc.getConnection())
        {
            String sql = "INSERT INTO Playlist VALUES (?);";

            PreparedStatement statement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            statement.setString(1, name);

            if (statement.executeUpdate() == 1)
            {
                ResultSet rs = statement.getGeneratedKeys();
                rs.next();
                Playlist newPlaylist = new Playlist(rs.getInt("playlistId"), name);
                return newPlaylist;
            }
            else
            {
                throw new DALException("Could not add playlist: " + name);
            }
        }
        
        catch (SQLException ex)
        {
            throw new DALException(ex.getMessage(), ex.getCause());
        }
    }

    /**
     * Add a Song to a Playlist in both database.
     *
     * @param playlist wanted to add to.
     * @param song wanted added.
     * @return true if song was added to playlist.
     * @throws DALException
     */
    public boolean addSongToPlaylist(Playlist playlist, Song song) throws DALException
    {
        try (Connection con = dbc.getConnection())
        {
            String sql = "INSERT INTO SongsInPlaylist VALUES (?, ?);";

            PreparedStatement statement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            statement.setInt(1, song.getSongId());
            statement.setInt(2, playlist.getPlaylistId());

            if (statement.executeUpdate() == 1)
            {
                return true;
            }
            else
            {
                throw new DALException("Could not add song to playlist: " + playlist.getName() + " to " + song.getArtist() + " - " + song.getTitle());
            }
        }
        
        catch (SQLException ex)
        {
            throw new DALException(ex.getMessage(), ex.getCause());
        }
    }

    /**
     * Update Genre in database and return the updated genre.
     *
     * @param genreId genre wanted renamed.
     * @param genre new genre.
     * @return updated Genre object.
     * @throws DALException
     */
    public Genre updateGenre(int genreId, String genre) throws DALException
    {
        try (Connection con = dbc.getConnection())
        {
            String sql = "UPDATE Genre SET genre=? WHERE genreId=?;";

            PreparedStatement statement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            statement.setString(1, genre);
            statement.setInt(2, genreId);

            if (statement.executeUpdate() == 1)
            {
                Genre newGenre = new Genre(genreId, genre);
                return newGenre;
            }
            else
            {
                throw new DALException("Could not update genre: " + genreId);
            }
        }
        
        catch (SQLException ex)
        {
            throw new DALException(ex.getMessage(), ex.getCause());
        }
    }

    /**
     * Update Song in database and return the updated song.
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
        try (Connection con = dbc.getConnection())
        {
            String sql = "UPDATE Song SET artist=?, title=?, album=?, year=?, genreId=?, directory=? WHERE songId=?;";

            PreparedStatement statement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            statement.setString(1, artist);
            statement.setString(2, title);
            statement.setString(3, album);
            statement.setInt(4, year);
            statement.setInt(5, genre.getGenreId());
            statement.setString(6, directory);
            statement.setInt(7, songId);

            if (statement.executeUpdate() == 1)
            {
                Song newSong = new Song(songId, title, artist, directory);
                newSong.setAlbum(album);
                newSong.setYear(year);
                newSong.setGenre(genre);
                return newSong;
            }
            else
            {
                throw new DALException("Could not update song: " + songId + " - " + artist + " - " + title + ", DIR: " + directory);
            }
        }
        
        catch (SQLException ex)
        {
            throw new DALException(ex.getMessage(), ex.getCause());
        }
    }

    /**
     * Update Playlist in database and return the updated playlist.
     *
     * @param playlistId Id of playlist wanted updated.
     * @param name new name of playlist.
     * @return Playlist object with new information.
     * @throws DALException
     */
    public Playlist updatePlaylist(int playlistId, String name) throws DALException
    {
        try (Connection con = dbc.getConnection())
        {
            String sql = "UPDATE Playlist SET name=? WHERE playlistId=?;";

            PreparedStatement statement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            statement.setString(1, name);
            statement.setInt(2, playlistId);

            if (statement.executeUpdate() == 1)
            {
                Playlist newPlaylist = new Playlist(playlistId, name);
                return newPlaylist;
            }
            else
            {
                throw new DALException("Could not add playlist: " + playlistId + " - " + name);
            }
        }
        
        catch (SQLException ex)
        {
            throw new DALException(ex.getMessage(), ex.getCause());
        }
    }

    /**
     * Delete Genre from database and return true if operation succeeded.
     *
     * @param genreId genre wanted removed.
     * @return succession boolean.
     * @throws DALException
     */
    public boolean deleteGenre(int genreId) throws DALException
    {
        try (Connection con = dbc.getConnection())
        {
            String sql = "DELETE Genre WHERE genreId=?;";

            PreparedStatement statement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            statement.setInt(1, genreId);

            if (statement.executeUpdate() == 1)
            {
                return true;
            }
            else
            {
                throw new DALException("Could not delete genre: " + genreId);
            }
        }
        
        catch (SQLException ex)
        {
            throw new DALException(ex.getMessage(), ex.getCause());
        }
    }

    /**
     * Delete Song from database and return true if operation succeeded.
     *
     * @param songId song wanted removed.
     * @return succession boolean.
     * @throws DALException
     */
    public boolean deleteSong(int songId) throws DALException
    {
        try (Connection con = dbc.getConnection())
        {
            String sql = "DELETE SongsInPlaylist WHERE songId=?; DELETE Song WHERE songId=?;";

            PreparedStatement statement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            statement.setInt(1, songId);
            statement.setInt(2, songId);

            if (statement.executeUpdate() == 1)
            {
                return true;
            }
            else
            {
                throw new DALException("Could not delete song: " + songId);
            }
        }
        
        catch (SQLException ex)
        {
            throw new DALException(ex.getMessage(), ex.getCause());
        }
    }

    /**
     * Delete Playlist from database and return true if operation succeeded.
     *
     * @param playlistId playlist wanted removed.
     * @return succession boolean.
     * @throws DALException
     */
    public boolean deletePlaylist(int playlistId) throws DALException
    {
        try (Connection con = dbc.getConnection())
        {
            String sql = "DELETE SongsInPlaylist WHERE playlistId=?; DELETE Playlist WHERE playlistId=?;";

            PreparedStatement statement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            statement.setInt(1, playlistId);
            statement.setInt(2, playlistId);

            if (statement.executeUpdate() == 1)
            {
                return true;
            }
            else
            {
                throw new DALException("Could not delete playlist: " + playlistId);
            }
        }
        
        catch (SQLException ex)
        {
            throw new DALException(ex.getMessage(), ex.getCause());
        }
    }

    /**
     * Delete Song from Playlist in database and return true if operation
     * succeeded.
     *
     * @param songId song wanted removed from playlist.
     * @param playlistId playlist wanted song removed from.
     * @return succession boolean.
     * @throws DALException
     */
    public boolean deleteSongInPlaylist(int songId, int playlistId) throws DALException
    {
        try (Connection con = dbc.getConnection())
        {
            String sql = "DELETE SongsInPlaylist WHERE songId=? AND playlistId=?;";

            PreparedStatement statement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            statement.setInt(1, songId);
            statement.setInt(2, playlistId);

            if (statement.executeUpdate() == 1)
            {
                return true;
            }
            else
            {
                throw new DALException("Could not delete song from playlist: " + songId + " from " + playlistId);
            }
        }
        
        catch (SQLException ex)
        {
            throw new DALException(ex.getMessage(), ex.getCause());
        }
    }
}
