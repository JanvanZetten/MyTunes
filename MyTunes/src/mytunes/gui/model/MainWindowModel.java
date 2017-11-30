/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mytunes.gui.model;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Slider;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaPlayer.Status;
import javafx.util.Duration;
import mytunes.be.Playlist;
import mytunes.be.Song;
import mytunes.bll.BLLException;
import mytunes.bll.BLLManager;

/**
 *
 * @author Alex
 */
public class MainWindowModel
{

    private static MainWindowModel instance;
    private BLLManager bllManager;
    private ObservableList<Playlist> playlists;
    private ObservableList<Song> songs;
    private SimpleStringProperty artist;
    private SimpleStringProperty title;
    private SimpleStringProperty album;
    private SimpleStringProperty currentTime;
    private SimpleStringProperty durationTime;
    private SimpleDoubleProperty progress;
    Media sound;
    MediaPlayer mediaPlayer;
    int currentIndex = -1;
    private Double currentVolume = 1.0;
    private String selectedElement;

    /**
     * Singleton method which makes sure that two MainWindowModels cannot be
     * created by two different classes that make use of the class.
     * @return MainWindowModel
     */
    public static MainWindowModel getInstance()
    {
        if (instance == null)
        {
            instance = new MainWindowModel();
        }
        return instance;
    }

    public MainWindowModel()
    {
        try
        {
            bllManager = new BLLManager();
            playlists = FXCollections.observableArrayList();
            songs = FXCollections.observableArrayList();
            playlists.addAll(bllManager.getAllPlaylists());
            songs.addAll(bllManager.getAllSongs());
            artist = new SimpleStringProperty("");
            title = new SimpleStringProperty("");
            album = new SimpleStringProperty("");
            currentTime = new SimpleStringProperty("");
            durationTime = new SimpleStringProperty("");
            progress = new SimpleDoubleProperty(0.0);
        }
        catch (BLLException ex)
        {
            throw new RuntimeException(ex.getMessage(), ex.getCause());
        }
    }

    /**
     * Updates the list for the gui element which shows the playlists remember
     * to call the getPlaylists() and assign the observable list to the element
     * in which it has to be shown
     */
    public void addAllPlaylistsToGUI()
    {
        playlists.clear();
        try
        {
            playlists.add(getAllSongsPlaylist());
            playlists.addAll(bllManager.getAllPlaylists());
        }
        catch (BLLException ex)
        {
            throw new RuntimeException(ex.getMessage(), ex.getCause());
        }
    }

    /**
     * gets the observablelist with the playlists
     *
     * @return a observablelist with Playlist objects
     */
    public ObservableList<Playlist> getPlaylists()
    {
        return playlists;
    }

    /**
     * gets the observablelist with the songs
     *
     * @return a observablelist with Playlist objects
     */
    public ObservableList<Song> getSongs()
    {
        return songs;
    }

    /**
     * Get observable String
     * @return
     */
    public SimpleStringProperty getArtist()
    {
        return artist;
    }

    /**
     * Get observable String
     * @return
     */
    public SimpleStringProperty getTitle()
    {
        return title;
    }

    /**
     * Get observable String
     * @return
     */
    public SimpleStringProperty getAlbum()
    {
        return album;
    }

    /**
     * Get observable String
     * @return
     */
    public SimpleStringProperty getCurrentTime()
    {
        return currentTime;
    }

    /**
     * Get observable String
     * @return
     */
    public SimpleStringProperty getDurationTime()
    {
        return durationTime;
    }

    /**
     * Get observable Double
     * @return
     */
    public SimpleDoubleProperty getProgress()
    {
        return progress;
    }

    /**
     * set the songs from the given playlist in the observablelist. remember to
     * add the observablelist to the view with getSong()
     *
     * @param selectedItem the playlist from which to take the song
     */
    public void setSongs(Playlist selectedItem)
    {
        songs.clear();
        songs.addAll(selectedItem.getSongs());

        if (songs.size() > 0)
        {
            currentIndex = 0;
        }
        else
        {
            currentIndex = -1;
        }
        switchSong();
    }

    /**
     * gets the playlist with all the songs
     *
     * @return playlist with all the known songs
     */
    public Playlist getAllSongsPlaylist()
    {
        Playlist playlist = new Playlist(-1, "My Library");
        try
        {
            playlist.addAllSongToPlaylist(bllManager.getAllSongs());
            return playlist;
        }
        catch (BLLException ex)
        {
            throw new RuntimeException("Could not read all songs.");
        }
    }

    /**
     * Play song.
     */
    public void playMedia()
    {
        if (currentIndex != -1)
        {
            if (mediaPlayer.getStatus().equals(Status.PLAYING) || mediaPlayer.getStatus().equals(Status.STOPPED))
            {
                mediaPlayer.seek(Duration.ZERO);
            }
            else
            {
                mediaPlayer.play();
            }
        }
    }

    /**
     * Pause song.
     */
    public void pauseMedia()
    {
        if (currentIndex != -1)
        {
            mediaPlayer.pause();
        }
    }

    /**
     * Change to previous song in list.
     */
    public void previousMedia()
    {
        if (currentIndex - 1 < 0)
        {
            currentIndex = songs.size() - 1;
        }
        else
        {
            currentIndex--;
        }
        switchSong();
    }

    /**
     * Change to next song in list.
     */
    public void nextMedia()
    {
        if (currentIndex + 1 >= songs.size())
        {
            currentIndex = 0;
        }
        else
        {
            currentIndex++;
        }
        switchSong();
    }

    /**
     * Switch song to current index.
     */
    private void switchSong()
    {
        if (currentIndex != -1)
        {
            // Check if the mediaplayer was playing.
            boolean isPlaying = false;
            if (mediaPlayer != null)
            {
                isPlaying = mediaPlayer.getStatus().equals(Status.PLAYING);
                mediaPlayer.stop();
            }

            // Load new media.
            sound = new Media(new File(songs.get(currentIndex).getpath()).toURI().toString());

            mediaPlayer = new MediaPlayer(sound);
            mediaPlayer.setVolume(currentVolume);

            // When the media ends it should execute nextMedia method.
            mediaPlayer.setOnEndOfMedia(new Runnable()
            {
                @Override
                public void run()
                {
                    nextMedia();
                }
            });

            // When the media is loaded and ready it should update the duration and current time.
            mediaPlayer.setOnReady(new Runnable()
            {
                @Override
                public void run()
                {
                    durationTime.set(sec2minsec(mediaPlayer.getMedia().getDuration().toSeconds()));
                    currentTime.set(sec2minsec(mediaPlayer.getCurrentTime().toSeconds()));
                }
            });

            // A listener which checks if the value of currentTime changed. If so update it.
            mediaPlayer.currentTimeProperty().addListener((observableValue, oldDuration, newDuration) ->
            {
                currentTime.set(sec2minsec(newDuration.toSeconds()));
                progress.set(newDuration.toSeconds() / mediaPlayer.getMedia().getDuration().toSeconds());
            });

            // Updates information labels for current media
            artist.set(songs.get(currentIndex).getArtist());
            title.set(songs.get(currentIndex).getTitle());
            album.set(songs.get(currentIndex).getAlbum());

            // Auto play if player was already playing.
            if (isPlaying)
            {
                mediaPlayer.play();
            }
        }
    }

    /**
     * Turns double of seconds into a minutes second format 00:00
     * @param seconds
     * @return string formated 00:00.
     */
    private String sec2minsec(double seconds)
    {
        int min;
        int sec;
        min = (int) seconds / 60;
        sec = (int) seconds % 60;

        if (sec > 9)
        {
            return min + ":" + sec;
        }
        else
        {

            return min + ":0" + sec;
        }
    }

    /**
     * links the volumeslider to the mediaplayers volume
     * @param volumeSlider the Slider who have to adjust the volume
     */
    public void volumeSliderSetup(Slider volumeSlider)
    {
        volumeSlider.setValue(mediaPlayer.getVolume() * volumeSlider.getMax());
        volumeSlider.valueProperty().addListener(new InvalidationListener()
        {
            @Override
            public void invalidated(Observable observable)
            {
                mediaPlayer.setVolume(volumeSlider.getValue() / volumeSlider.getMax());
                currentVolume = (volumeSlider.getValue() / volumeSlider.getMax());
            }
        });

    }

    public String selectedDeletedElements(String SelectedElement)
    {
        selectedElement = SelectedElement;
        return selectedElement;
    }

    public String getSelectedElement()
    {
        return selectedElement;
    }

    /**
     * takes the current list of songs shown and filters it for songs who
     * contains the text in the title or in the artist
     * @param text the text which should be found in the songs title or artist
     * for it to be shown
     */
    public void filterSongList(String text)
    {
        List<Song> songsList = new ArrayList<>();
        songsList.addAll(songs);
        songs.clear();
        for (Song song : songsList)
        {
            if (song.getTitle().toLowerCase().contains(text.toLowerCase()) || song.getArtist().toLowerCase().contains(text.toLowerCase()))
            {
                songs.add(song);
            }
        }

    }

    public void createPlaylist(String text) throws BLLException
    {
        bllManager.addPlaylist(text);
    }

}
