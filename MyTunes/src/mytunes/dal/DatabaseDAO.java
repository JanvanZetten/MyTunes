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
public class DatabaseDAO
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
            String sql = "SELECT * FROM Playlists;";

            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);

            List<Playlist> playlists = new ArrayList<>();

            while (rs.next()) //While there are companies (rows) in the result set:
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
        catch (SQLServerException ex)
        {
            throw new DALException(ex.getMessage(), ex.getCause());
        }
        catch (SQLException ex)
        {
            throw new DALException(ex.getMessage(), ex.getCause());
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
                    + "INNER JOIN Playlist ON Playlist.playlistId = SongsInPlaylist.playlistId"
                    + "WHERE Playlist.playlistId = " + playlistId + ";";

            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);

            List<Song> songs = new ArrayList<>();

            while (rs.next()) //While there are companies (rows) in the result set:
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
        catch (SQLServerException ex)
        {
            throw new DALException(ex.getMessage(), ex.getCause());
        }
        catch (SQLException ex)
        {
            throw new DALException(ex.getMessage(), ex.getCause());
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

            while (rs.next()) //While there are companies (rows) in the result set:
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
        catch (SQLServerException ex)
        {
            throw new DALException(ex.getMessage(), ex.getCause());
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

            while (rs.next()) //While there are companies (rows) in the result set:
            {
                Genre g = new Genre(rs.getInt("genreId"), rs.getString("genre"));
                genres.add(g);
            }

            return genres;
        }
        catch (SQLServerException ex)
        {
            throw new DALException(ex.getMessage(), ex.getCause());
        }
        catch (SQLException ex)
        {
            throw new DALException(ex.getMessage(), ex.getCause());
        }
    }

    /**
     * Add new Genre to database and return the new genre.
     *
     * @param genre new genre to add
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
                //Good
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
        catch (SQLServerException ex)
        {
            throw new DALException(ex.getMessage(), ex.getCause());
        }
        catch (SQLException ex)
        {
            throw new DALException(ex.getMessage(), ex.getCause());
        }
    }

}
