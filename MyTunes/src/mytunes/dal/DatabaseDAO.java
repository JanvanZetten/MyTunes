/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mytunes.dal;

import java.io.File;
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
 * @author Alex, Asbjørn og Jan
 */
public class DatabaseDAO implements DAO
{

    private final DatabaseConnector dbc;

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
    @Override
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
                playlist.setCreatedBy(this.getClass().getName());
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

                try
                {
                    AudioMedia am = new AudioMedia(new File(rs.getString("directory")));
                    song.setDuration(am.getDuration());
                }
                catch (DALException ex)
                {
                    song.setDuration(0.0);
                }
                song.setCreatedBy(this.getClass().getName());

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
    @Override
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

                try
                {
                    AudioMedia am = new AudioMedia(new File(rs.getString("directory")));
                    song.setDuration(am.getDuration());
                }
                catch (DALException ex)
                {
                    song.setDuration(0.0);
                }
                song.setCreatedBy(this.getClass().getName());

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
    @Override
    public List<Genre> getAllGenres() throws DALException
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
                g.setCreatedBy(this.getClass().getName());
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
    @Override
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
                Genre newGenre = new Genre(rs.getInt(1), genre);
                newGenre.setCreatedBy(this.getClass().getName());
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
    @Override
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
                int id = rs.getInt(1);
                Song newSong = new Song(id, title, artist, directory);
                newSong.setAlbum(album);
                newSong.setYear(year);
                newSong.setGenre(genre);
                addSongToPlaylist(new Playlist(1, "My Library"), newSong);

                try
                {
                    AudioMedia am = new AudioMedia(new File(directory));
                    newSong.setDuration(am.getDuration());
                }
                catch (DALException ex)
                {
                    newSong.setDuration(0.0);
                }
                newSong.setCreatedBy(this.getClass().getName());

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
    @Override
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
                Playlist newPlaylist = new Playlist(rs.getInt(1), name);
                newPlaylist.setCreatedBy(this.getClass().getName());
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
    @Override
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
    @Override
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
                newGenre.setCreatedBy(this.getClass().getName());
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
    @Override
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

                try
                {
                    AudioMedia am = new AudioMedia(new File(directory));
                    newSong.setDuration(am.getDuration());
                }
                catch (DALException ex)
                {
                    newSong.setDuration(0.0);
                }

                newSong.setCreatedBy(this.getClass().getName());
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
    @Override
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
                newPlaylist.setCreatedBy(this.getClass().getName());
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
    @Override
    public boolean deleteGenre(int genreId) throws DALException
    {
        try (Connection con = dbc.getConnection())
        {
            String sql = "SELECT COUNT(*) as count FROM Song WHERE genreId = ?;";

            PreparedStatement statement = con.prepareStatement(sql);
            statement.setInt(1, genreId);

            ResultSet rs = statement.executeQuery(sql);
            rs.next();
            int count = rs.getInt("count");

            if (count > 0)
            {
                try
                {
                    sql = "DELETE Genre WHERE genreId=?;";

                    statement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

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
                    throw new DALException("Deleting genre: " + ex.getMessage(), ex.getCause());
                }
            }
            else
            {
                throw new DALException("Could not delete genre: " + genreId + ", genre is used by one or many songs!");
            }
        }
        catch (SQLException ex)
        {
            throw new DALException("Getting genre used count: " + ex.getMessage(), ex.getCause());
        }
    }

    /**
     * Delete Song from database and return true if operation succeeded.
     *
     * @param songId song wanted removed.
     * @return succession boolean.
     * @throws DALException
     */
    @Override
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
    @Override
    public boolean deletePlaylist(int playlistId) throws DALException
    {
        if (playlistId != 1)
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
        else
        {
            throw new DALException("Cannot delete Main Playlist!");
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
    @Override
    public boolean deleteSongInPlaylist(int songId, int playlistId) throws DALException
    {
        if (playlistId != 1)
        {
            try (Connection con = dbc.getConnection())
            {
                int sipId = getSipId(songId, playlistId);
                String sql = "DELETE SongsInPlaylist WHERE sipId=?;";

                PreparedStatement statement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

                statement.setInt(1, sipId);

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
        else
        {
            throw new DALException("Cannot delete song from Main Playlist!");
        }
    }

    /**
     * Swap songs in playlist to get wanted order.
     *
     * @param firstSongId song wanted swapped.
     * @param secondSongId song wanted swapped.
     * @param playlistId playlist wanted affected.
     * @return succession boolean.
     * @throws DALException
     */
    @Override
    public boolean swapSongsInPlaylist(int firstSongId, int secondSongId, int playlistId) throws DALException
    {
        try (Connection con = dbc.getConnection())
        {
            int firstSipId = getSipId(firstSongId, playlistId);
            int secondSipId = getSipId(secondSongId, playlistId);

            String sql = "UPDATE SongsInPlaylist SET songId=? WHERE sipId=?;"
                    + "UPDATE SongsInPlaylist SET songId=? WHERE sipId=?;";

            PreparedStatement statement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            statement.setInt(1, secondSongId);
            statement.setInt(2, firstSipId);
            statement.setInt(3, firstSongId);
            statement.setInt(4, secondSipId);

            if (statement.executeUpdate() == 1)
            {
                return true;
            }
            else
            {
                throw new DALException("Could not swap songs: " + firstSongId + " and " + secondSongId + " in " + playlistId);
            }
        }
        catch (SQLException ex)
        {
            throw new DALException(ex.getMessage(), ex.getCause());
        }
    }

    /**
     * Gets SongInPlaylist Id. Sync changes as new entry. Does not sync removed
     * entries.
     *
     * @param songId
     * @param playlistId
     * @return
     * @throws DALException
     */
    private int getSipId(int songId, int playlistId) throws DALException
    {
        try (Connection con = dbc.getConnection())
        {
            String sql = "SELECT sipId FROM SongsInPlaylist WHERE songId=? AND playlistId=?;";

            PreparedStatement statement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            statement.setInt(1, songId);
            statement.setInt(2, playlistId);

            ResultSet rs = statement.executeQuery();
            rs.next();
            int id = rs.getInt("sipID");
            return id;

        }
        catch (SQLException ex)
        {
            throw new DALException(ex.getMessage(), ex.getCause());
        }
    }

    /**
     * Sync itself with the given DAO
     *
     * @param syncDAO
     * @throws DALException
     */
    @Override
    public void sync(DAO syncDAO) throws DALException
    {
        //System.out.println("Syncing DatabaseDAO with " + syncDAO.getClass().getName() + ":");
        // SYNC GENRE
        List<Genre> newGenres = new ArrayList<>();
        List<Integer> newGenresId = new ArrayList<>();
        // Try loading database data
        try
        {
            List<Genre> thisList = getAllGenres();
            List<Genre> syncList = syncDAO.getAllGenres();
            int index = 0;

            for (Genre genre : syncList)
            {
                if (!genre.getCreatedBy().equalsIgnoreCase(this.getClass().getName()))
                {
                    // Current list item is less than sync item.
                    while (index < thisList.size() - 1)
                    {
                        if (thisList.get(index).getGenreId() < genre.getGenreId())
                        {
                            index++;
                        }
                        else
                        {
                            break;
                        }
                    }

                    // Check if same id and add to database if different content.
                    if (thisList.get(index).getGenreId() == genre.getGenreId())
                    {
                        if (!thisList.get(index).getGenre().equalsIgnoreCase(genre.getGenre()))
                        {
                            newGenres.add(addGenre(genre.getGenre()));
                            newGenresId.add(genre.getGenreId());
                        }
                    } // If it still runs when it is at the end of thisList it should add all the rest from syncList.
                    else if (index == thisList.size() - 1)
                    {
                        newGenres.add(addGenre(genre.getGenre()));
                        newGenresId.add(genre.getGenreId());
                    }
                }
            }

            //System.out.println("Genres synced.");
        }
        catch (DALException e)
        {
            // Try loading data from sync
            try
            {
                List<Genre> syncList = syncDAO.getAllGenres();

                for (Genre genre : syncList)
                {
                    if (!genre.getCreatedBy().equalsIgnoreCase(this.getClass().getName()))
                    {
                        newGenres.add(addGenre(genre.getGenre()));
                        newGenresId.add(genre.getGenreId());
                    }
                }

                //System.out.println("Genres synced.");
            }
            catch (DALException ex)
            {
                //System.out.println("Genres sync failed.");
                throw new DALException("Syncing database genres with other DAO: " + syncDAO.getClass().getName() + " " + ex.getMessage(), ex.getCause());
            }
        }

        // SYNC SONG
        List<Song> newSongs = new ArrayList<>();
        List<Integer> newSongsId = new ArrayList<>();
        try
        {
            List<Song> thisList = getAllSongs();
            List<Song> syncList = syncDAO.getAllSongs();
            int index = 0;

            for (Song song : syncList)
            {
                if (!song.getCreatedBy().equalsIgnoreCase(this.getClass().getName()))
                {
                    // Current list item is less than sync item.
                    while (index < thisList.size() - 1)
                    {
                        if (thisList.get(index).getSongId() < song.getSongId())
                        {
                            index++;
                        }
                        else
                        {
                            break;
                        }
                    }

                    // Check if same id and add to database if different content.
                    if (thisList.get(index).getSongId() == song.getSongId())
                    {
                        if (!(thisList.get(index).getTitle().equalsIgnoreCase(song.getTitle())
                                && thisList.get(index).getArtist().equalsIgnoreCase(song.getArtist())
                                && thisList.get(index).getAlbum().equalsIgnoreCase(song.getAlbum())
                                && thisList.get(index).getYear() == song.getYear()
                                && thisList.get(index).getGenre().getGenreId() == song.getGenre().getGenreId()
                                && thisList.get(index).getPath().equalsIgnoreCase(song.getPath())))
                        {
                            Genre tmpGenre;
                            if (newGenresId.contains(song.getGenre().getGenreId()))
                            {
                                tmpGenre = newGenres.get(newGenresId.indexOf(song.getGenre().getGenreId()));
                            }
                            else
                            {
                                tmpGenre = song.getGenre();
                            }
                            newSongs.add(addSong(song.getArtist(), song.getTitle(), song.getAlbum(), song.getYear(), tmpGenre, song.getPath()));
                            newSongsId.add(song.getSongId());
                        }
                    } // If it still runs when it is at the end of thisList it should add all the rest from syncList.
                    else if (index == thisList.size() - 1)
                    {
                        Genre tmpGenre;
                        if (newGenresId.contains(song.getGenre().getGenreId()))
                        {
                            tmpGenre = newGenres.get(newGenresId.indexOf(song.getGenre().getGenreId()));
                        }
                        else
                        {
                            tmpGenre = song.getGenre();
                        }
                        newSongs.add(addSong(song.getArtist(), song.getTitle(), song.getAlbum(), song.getYear(), tmpGenre, song.getPath()));
                        newSongsId.add(song.getSongId());
                    }
                }
            }
            //System.out.println("Songs synced.");
        }
        catch (DALException e)
        {
            // Try loading data from sync
            try
            {
                List<Song> syncList = syncDAO.getAllSongs();

                for (Song song : syncList)
                {
                    if (!song.getCreatedBy().equalsIgnoreCase(this.getClass().getName()))
                    {
                        Genre tmpGenre;
                        if (newGenresId.contains(song.getGenre().getGenreId()))
                        {
                            tmpGenre = newGenres.get(newGenresId.indexOf(song.getGenre().getGenreId()));
                        }
                        else
                        {
                            tmpGenre = song.getGenre();
                        }
                        newSongs.add(addSong(song.getArtist(), song.getTitle(), song.getAlbum(), song.getYear(), tmpGenre, song.getPath()));
                        newSongsId.add(song.getSongId());
                    }
                }
                //System.out.println("Songs synced.");
            }
            catch (DALException ex)
            {
                //System.out.println("Songs sync failed.");
                throw new DALException("Syncing database genres with other DAO: " + syncDAO.getClass().getName() + " " + ex.getMessage(), ex.getCause());
            }
        }

        // SYNC PLAYLIST
        try
        {
            List<Playlist> thisList = getAllPlaylists();
            List<Playlist> syncList = syncDAO.getAllPlaylists();
            int index = 0;

            for (Playlist playlist : syncList)
            {
                if (playlist.getPlaylistId() == 1)
                {
                    continue;
                }

                // Current list item is less than sync item.
                while (index < thisList.size() - 1)
                {
                    if (thisList.get(index).getPlaylistId() < playlist.getPlaylistId())
                    {
                        index++;
                    }
                    else
                    {
                        break;
                    }
                }

                // Check if same id and add to database if different content.
                if (thisList.get(index).getPlaylistId() == playlist.getPlaylistId())
                {
                    if (!playlist.getCreatedBy().equalsIgnoreCase(this.getClass().getName()))
                    {
                        Playlist tmpPlaylist = addPlaylist(playlist.getName());
                        for (Song song : playlist.getSongs())
                        {
                            if (newSongsId.contains(song.getSongId()))
                            {
                                addSongToPlaylist(tmpPlaylist, newSongs.get(newSongsId.indexOf(song.getSongId())));
                            }
                            else
                            {
                                addSongToPlaylist(tmpPlaylist, song);
                            }
                        }
                    }
                    else
                    {
                        Playlist tmpPlaylist = new Playlist(playlist.getPlaylistId(), "tmp");
                        for (Song song : playlist.getSongs())
                        {
                            try
                            {
                                getSipId(song.getSongId(), tmpPlaylist.getPlaylistId());
                            }
                            catch (DALException ex)
                            {
                                if (newSongsId.contains(song.getSongId()))
                                {
                                    addSongToPlaylist(tmpPlaylist, newSongs.get(newSongsId.indexOf(song.getSongId())));
                                }
                                else
                                {
                                    addSongToPlaylist(tmpPlaylist, song);
                                }
                            }
                        }
                    }
                } // If it still runs when it is at the end of thisList it should add all the rest from syncList.
                else if (index == thisList.size() - 1)
                {
                    if (!playlist.getCreatedBy().equalsIgnoreCase(this.getClass().getName()))
                    {
                        Playlist tmpPlaylist = addPlaylist(playlist.getName());
                        for (Song song : playlist.getSongs())
                        {
                            if (newSongsId.contains(song.getSongId()))
                            {
                                addSongToPlaylist(tmpPlaylist, newSongs.get(newSongsId.indexOf(song.getSongId())));
                            }
                            else
                            {
                                addSongToPlaylist(tmpPlaylist, song);
                            }
                        }
                    }
                    else
                    {
                        Playlist tmpPlaylist = new Playlist(playlist.getPlaylistId(), "tmp");
                        for (Song song : playlist.getSongs())
                        {
                            try
                            {
                                getSipId(song.getSongId(), tmpPlaylist.getPlaylistId());
                            }
                            catch (DALException ex)
                            {
                                if (newSongsId.contains(song.getSongId()))
                                {
                                    addSongToPlaylist(tmpPlaylist, newSongs.get(newSongsId.indexOf(song.getSongId())));
                                }
                                else
                                {
                                    addSongToPlaylist(tmpPlaylist, song);
                                }
                            }
                        }
                    }
                }
            }

            //System.out.println("Playlists synced.");
        }
        catch (DALException e)
        {
            // Try loading data from sync
            try
            {
                List<Playlist> syncList = syncDAO.getAllPlaylists();

                for (Playlist playlist : syncList)
                {
                    if (playlist.getPlaylistId() == 1)
                    {
                        continue;
                    }
                    if (!playlist.getCreatedBy().equalsIgnoreCase(this.getClass().getName()))
                    {
                        Playlist tmpPlaylist = addPlaylist(playlist.getName());
                        for (Song song : playlist.getSongs())
                        {
                            if (newSongsId.contains(song.getSongId()))
                            {
                                addSongToPlaylist(tmpPlaylist, newSongs.get(newSongsId.indexOf(song.getSongId())));
                            }
                            else
                            {
                                addSongToPlaylist(tmpPlaylist, song);
                            }
                        }
                    }
                    else
                    {
                        Playlist tmpPlaylist = new Playlist(playlist.getPlaylistId(), "tmp");
                        for (Song song : playlist.getSongs())
                        {
                            try
                            {
                                getSipId(song.getSongId(), tmpPlaylist.getPlaylistId());
                            }
                            catch (DALException ex)
                            {
                                if (newSongsId.contains(song.getSongId()))
                                {
                                    addSongToPlaylist(tmpPlaylist, newSongs.get(newSongsId.indexOf(song.getSongId())));
                                }
                                else
                                {
                                    addSongToPlaylist(tmpPlaylist, song);
                                }
                            }
                        }
                    }
                }
                //System.out.println("Playlists synced.");
            }
            catch (DALException ex)
            {
                //System.out.println("Playlists sync failed.");
                throw new DALException("Syncing database genres with other DAO: " + syncDAO.getClass().getName() + " " + ex.getMessage(), ex.getCause());
            }
        }

        //System.out.println("Sync finished!");
    }
}
