/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mytunes.dal;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import mytunes.be.Genre;
import mytunes.be.Playlist;
import mytunes.be.Song;

/**
 *
 * @author Alex, Asbjørn og Jan
 */
public class LocalDAO implements DAO
{

    File playlistFile;
    File songFile;
    File genreFile;
    File songsInPlaylistFile;

    public LocalDAO()
    {
        this.playlistFile = new File("localDB/playlist.dao");
        this.songFile = new File("localDB/song.dao");
        this.genreFile = new File("localDB/genre.dao");
        this.songsInPlaylistFile = new File("localDB/songsInPlaylist.dao");
        File dir = new File("localDB/");
        dir.mkdir();
    }

    @Override
    public List<Playlist> getAllPlaylists() throws DALException
    {
        try
        {
            FileInputStream fis = new FileInputStream(playlistFile);
            try (ObjectInputStream ois = new ObjectInputStream(fis))
            {
                List<Playlist> playlists = (List<Playlist>) ois.readObject();
                return playlists;
            }
        }
        catch (Exception ex)
        {
            List<Playlist> tmp = new ArrayList();
            Playlist p = new Playlist(1, "My Library");
            p.setCreatedBy(this.getClass().getName());
            tmp.add(p);
            saveAllPlaylists(tmp);
            return tmp;
        }
    }

    @Override
    public List<Song> getAllSongs() throws DALException
    {
        try
        {
            FileInputStream fis = new FileInputStream(songFile);
            try (ObjectInputStream ois = new ObjectInputStream(fis))
            {
                List<Song> songs = (List<Song>) ois.readObject();
                return songs;
            }
        }
        catch (Exception ex)
        {
            return new ArrayList<>();
        }
    }

    @Override
    public List<Genre> getAllGenres() throws DALException
    {
        try
        {
            FileInputStream fis = new FileInputStream(genreFile);
            try (ObjectInputStream ois = new ObjectInputStream(fis))
            {
                List<Genre> genres = (List<Genre>) ois.readObject();
                return genres;
            }
        }
        catch (Exception ex)
        {
            return new ArrayList<>();
        }
    }

    public void saveAllPlaylists(List<Playlist> playlists) throws DALException
    {
        try
        {
            FileOutputStream fos = new FileOutputStream(playlistFile);
            try (ObjectOutputStream oos = new ObjectOutputStream(fos);)
            {
                oos.writeObject(playlists);
            }
        }
        catch (Exception ex)
        {
            throw new DALException("Saving local playlists: " + ex.getMessage(), ex.getCause());
        }
    }

    public void saveAllSongs(List<Song> songs) throws DALException
    {
        try
        {
            FileOutputStream fos = new FileOutputStream(songFile);
            try (ObjectOutputStream oos = new ObjectOutputStream(fos);)
            {
                oos.writeObject(songs);
            }
        }
        catch (Exception ex)
        {
            throw new DALException("Saving local songs: " + ex.getMessage(), ex.getCause());
        }
    }

    public void saveAllGenres(List<Genre> genres) throws DALException
    {
        try
        {
            FileOutputStream fos = new FileOutputStream(genreFile);
            try (ObjectOutputStream oos = new ObjectOutputStream(fos);)
            {
                oos.writeObject(genres);
            }
        }
        catch (Exception ex)
        {
            throw new DALException("Saving local genres: " + ex.getMessage(), ex.getCause());
        }
    }

    @Override
    public Genre addGenre(String genre) throws DALException
    {
        List<Genre> genres = new ArrayList<>();
        int id;
        try
        {
            genres = getAllGenres();
            id = genres.get(genres.size() - 1).getGenreId() + 1;
        }
        catch (DALException ex)
        {
            id = 1;
        }
        Genre newGenre = new Genre(id, genre);
        newGenre.setCreatedBy(this.getClass().getName());
        genres.add(newGenre);
        saveAllGenres(genres);
        return newGenre;
    }

    @Override
    public Song addSong(String artist, String title, String album, int year, Genre genre, String directory) throws DALException
    {
        List<Song> songs = new ArrayList<>();
        int id;
        try
        {
            songs = getAllSongs();
            id = songs.get(songs.size() - 1).getSongId() + 1;
        }
        catch (DALException ex)
        {
            id = 1;
        }
        Song newSong = new Song(id, title, artist, directory);
        newSong.setAlbum(album);
        newSong.setYear(year);
        newSong.setGenre(genre);
        newSong.setCreatedBy(this.getClass().getName());

        try
        {
            AudioMedia am = new AudioMedia(new File(directory));
            newSong.setDuration(am.getDuration());
        }
        catch (DALException ex)
        {
            newSong.setDuration(0.0);
        }

        addSongToPlaylist(new Playlist(1, "My Library"), newSong);

        songs.add(newSong);
        saveAllSongs(songs);
        return newSong;
    }

    @Override
    public Playlist addPlaylist(String name) throws DALException
    {
        List<Playlist> playlists = new ArrayList<>();
        int id;
        try
        {
            playlists = getAllPlaylists();
            id = playlists.get(playlists.size() - 1).getPlaylistId() + 1;
        }
        catch (DALException ex)
        {
            id = 1;
        }
        Playlist newPlaylist = new Playlist(id, name);
        newPlaylist.setCreatedBy(this.getClass().getName());
        playlists.add(newPlaylist);
        saveAllPlaylists(playlists);
        return newPlaylist;
    }

    @Override
    public boolean addSongToPlaylist(Playlist playlist, Song song) throws DALException
    {
        try
        {
            List<Playlist> playlists = getAllPlaylists();
            for (Playlist playlist1 : playlists)
            {
                if (playlist1.getPlaylistId() == playlist.getPlaylistId())
                {
                    playlist1.addSongToPlaylist(song);
                    saveAllPlaylists(playlists);
                    return true;
                }
            }
            return false;
        }
        catch (DALException ex)
        {
            return false;
        }
    }

    @Override
    public Genre updateGenre(int genreId, String genre) throws DALException
    {
        List<Genre> genres = getAllGenres();
        for (int i = 0; i < genres.size(); i++)
        {
            if (genres.get(i).getGenreId() == genreId)
            {
                genres.get(i).setGenre(genre);
                genres.get(i).setCreatedBy(this.getClass().getName());
                saveAllGenres(genres);
                return genres.get(i);
            }
        }
        throw new DALException("Could not update genre on local: " + genreId);
    }

    @Override
    public Song updateSong(int songId, String artist, String title, String album, int year, Genre genre, String directory) throws DALException
    {
        List<Song> songs = getAllSongs();
        for (int i = 0; i < songs.size(); i++)
        {
            if (songs.get(i).getSongId() == songId)
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

                songs.set(i, newSong);
                saveAllSongs(songs);
                return songs.get(i);
            }
        }
        throw new DALException("Could not update song on local: " + songId);
    }

    @Override
    public Playlist updatePlaylist(int playlistId, String name) throws DALException
    {
        List<Playlist> playlists = getAllPlaylists();
        for (int i = 0; i < playlists.size(); i++)
        {
            if (playlists.get(i).getPlaylistId() == playlistId)
            {
                playlists.get(i).renamePlaylist(name);
                playlists.get(i).setCreatedBy(this.getClass().getName());
                saveAllPlaylists(playlists);
                return playlists.get(i);
            }
        }
        throw new DALException("Could not update playlist on local: " + playlistId);
    }

    @Override
    public boolean deleteGenre(int genreId) throws DALException
    {
        List<Song> songs = getAllSongs();
        for (Song song : songs)
        {
            if (song.getGenre().getGenreId() == genreId)
            {
                throw new DALException("Could not delete genre on local: " + genreId + " genre is in use");
            }
        }
        List<Genre> genres = getAllGenres();
        for (int i = 0; i < genres.size(); i++)
        {
            if (genres.get(i).getGenreId() == genreId)
            {
                genres.remove(i);
                saveAllGenres(genres);
                return true;
            }
        }
        throw new DALException("Could not delete genre on local: " + genreId);
    }

    @Override
    public boolean deleteSong(int songId) throws DALException
    {
        List<Playlist> playlists = getAllPlaylists();
        for (Playlist playlist : playlists)
        {
            for (int i = 0; i < playlist.getSongs().size(); i++)
            {
                if (playlist.getSongs().get(i).getSongId() == songId)
                {
                    playlist.getSongs().remove(i);
                }
            }
        }
        saveAllPlaylists(playlists);
        List<Song> songs = getAllSongs();
        for (int i = 0; i < songs.size(); i++)
        {
            if (songs.get(i).getSongId() == songId)
            {
                songs.remove(i);
                saveAllSongs(songs);
                return true;
            }
        }
        throw new DALException("Could not delete song on local: " + songId);
    }

    @Override
    public boolean deletePlaylist(int playlistId) throws DALException
    {
        List<Playlist> playlists = getAllPlaylists();
        for (int i = 0; i < playlists.size(); i++)
        {
            if (playlists.get(i).getPlaylistId() == playlistId)
            {
                playlists.remove(i);
                saveAllPlaylists(playlists);
                return true;
            }
        }
        throw new DALException("Could not delete playlist on local: " + playlistId);
    }

    @Override
    public boolean deleteSongInPlaylist(int songId, int playlistId) throws DALException
    {
        List<Playlist> playlists = getAllPlaylists();
        for (int i = 0; i < playlists.size(); i++)
        {
            if (playlists.get(i).getPlaylistId() == playlistId)
            {
                for (int j = 0; j < playlists.get(i).getSongs().size(); j++)
                {
                    if (playlists.get(i).getSongs().get(j).getSongId() == songId)
                    {
                        playlists.get(i).getSongs().remove(j);
                        saveAllPlaylists(playlists);
                        return true;
                    }
                }

            }
        }
        throw new DALException("Could not delete song from playlist on local: " + songId + " in " + playlistId);
    }

    @Override
    public boolean swapSongsInPlaylist(int firstSongId, int secondSongId, int playlistId) throws DALException
    {
        List<Playlist> playlists = getAllPlaylists();
        for (int i = 0; i < playlists.size(); i++)
        {
            if (playlists.get(i).getPlaylistId() == playlistId)
            {
                int firstIndex = -1;
                int secondIndex = -1;
                for (int j = 0; j < playlists.get(i).getSongs().size(); j++)
                {
                    if (playlists.get(i).getSongs().get(j).getSongId() == firstSongId)
                    {
                        firstIndex = j;
                    }
                    else if (playlists.get(i).getSongs().get(j).getSongId() == secondSongId)
                    {
                        secondIndex = j;
                    }
                }
                if (firstIndex != -1 && secondIndex != -1)
                {
                    Collections.swap(playlists.get(i).getSongs(), firstIndex, secondIndex);
                    playlists.get(i).setCreatedBy(this.getClass().getName());
                    saveAllPlaylists(playlists);
                    return true;
                }
            }
        }
        throw new DALException("Could not swap songs in playlist on local: " + firstSongId + " and " + secondSongId + " in " + playlistId);
    }

    @Override
    public void sync(DAO syncDAO) throws DALException
    {
        //System.out.println("Syncing LocalDAO with " + syncDAO.getClass().getName() + ":");
        // SYNC GENRE
        try
        {
            saveAllGenres(syncDAO.getAllGenres());
            //System.out.println("Genre synced.");
        }
        catch (DALException ex)
        {
            //System.out.println("Genre sync failed.");
        }

        // SYNC SONG
        try
        {
            saveAllSongs(syncDAO.getAllSongs());
            //System.out.println("Songs synced.");
        }
        catch (DALException ex)
        {
            //System.out.println("Songs sync failed.");
        }

        // SYNC PLAYLIST
        try
        {
            saveAllPlaylists(syncDAO.getAllPlaylists());
            //System.out.println("Playlists synced.");
        }
        catch (DALException ex)
        {
            //System.out.println("Playlists sync failed.");
        }
        //System.out.println("Sync finished!");
    }
}
