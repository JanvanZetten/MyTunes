/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mytunes.bll;

import java.io.File;
import java.util.Random;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.media.Media;
import javafx.scene.media.MediaException;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaPlayer.Status;
import javafx.util.Duration;
import mytunes.be.Playlist;
import mytunes.be.Song;

/**
 *
 * @author Asbamz
 */
public class AudioPlayer implements Player
{
    private final ObservableList<Song> songs;
    private final SimpleStringProperty artist;
    private final SimpleStringProperty title;
    private final SimpleStringProperty album;
    private final SimpleStringProperty currentTime;
    private final SimpleStringProperty durationTime;
    private final SimpleDoubleProperty progress;
    private Media sound;
    private MediaPlayer mediaPlayer;
    private int currentIndex = -1;
    private Double currentVolume = 1.0;
    private boolean isLooping = false;
    private boolean isShuffling = false;

    public AudioPlayer()
    {
        this.songs = FXCollections.observableArrayList();
        artist = new SimpleStringProperty("");
        title = new SimpleStringProperty("");
        album = new SimpleStringProperty("");
        currentTime = new SimpleStringProperty("");
        durationTime = new SimpleStringProperty("");
        progress = new SimpleDoubleProperty(0.0);
    }

    /**
     * gets the observablelist with the songs
     *
     * @return a observablelist with Playlist objects
     */
    @Override
    public ObservableList<Song> getSongs()
    {
        return songs;
    }

    /**
     * Get observable String
     * @return
     */
    @Override
    public SimpleStringProperty getArtist()
    {
        return artist;
    }

    /**
     * Get observable String
     * @return
     */
    @Override
    public SimpleStringProperty getTitle()
    {
        return title;
    }

    /**
     * Get observable String
     * @return
     */
    @Override
    public SimpleStringProperty getAlbum()
    {
        return album;
    }

    /**
     * Get observable String
     * @return
     */
    @Override
    public SimpleStringProperty getCurrentTime()
    {
        return currentTime;
    }

    /**
     * Get observable String
     * @return
     */
    @Override
    public SimpleStringProperty getDurationTime()
    {
        return durationTime;
    }

    /**
     * Get observable Double
     * @return
     */
    @Override
    public SimpleDoubleProperty getProgress()
    {
        return progress;
    }

    /**
     * set the songs from the given playlist in the observablelist. remember to
     * add the observablelist to the view with getSong()
     *
     * @param selectedItem the playlist from which to take the song
     * @throws mytunes.bll.BLLException
     */
    @Override
    public void setSongs(Playlist selectedItem) throws BLLException
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
     * Play song.
     */
    @Override
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
    @Override
    public void pauseMedia()
    {
        if (currentIndex != -1)
        {
            mediaPlayer.pause();
        }
    }

    /**
     * Change to previous song in list.
     * @throws mytunes.bll.BLLException
     */
    @Override
    public void previousMedia() throws BLLException
    {
        if (currentIndex - 1 < 0 && isLooping)
        {
            currentIndex = songs.size() - 1;
            switchSong();
        }
        else if (isShuffling)
        {
            currentIndex = new Random().nextInt(songs.size());
            switchSong();
        }
        else if (currentIndex - 1 >= 0)
        {
            currentIndex--;
            switchSong();
        }
    }

    /**
     * Change to next song in list.
     * @throws mytunes.bll.BLLException
     */
    @Override
    public void nextMedia() throws BLLException
    {
        if (currentIndex + 1 >= songs.size() && isLooping)
        {
            currentIndex = 0;
            switchSong();
        }
        else if (isShuffling)
        {
            currentIndex = new Random().nextInt(songs.size());
            switchSong();
        }
        else if (currentIndex + 1 < songs.size())
        {
            currentIndex++;
            switchSong();
        }
    }

    /**
     * Get current volume of player.
     * @return current volume as double.
     */
    @Override
    public double getVolume()
    {
        return mediaPlayer.getVolume();
    }

    /**
     * Set volume of player.
     * @param value new volume as double.
     */
    @Override
    public void setVolume(double value)
    {
        mediaPlayer.setVolume(value);
        currentVolume = value;
    }

    /**
     * Switch song to current index.
     */
    private void switchSong() throws BLLException
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
            try
            {
                sound = new Media(new File(songs.get(currentIndex).getpath()).toURI().toString());
            }
            catch (MediaException ex)
            {
                throw new BLLException("Loading new media: " + songs.get(currentIndex).getpath() + ", " + ex.getMessage(), ex.getCause());
            }

            mediaPlayer = new MediaPlayer(sound);
            mediaPlayer.setVolume(currentVolume);

            SimpleBooleanProperty runException;
            runException = new SimpleBooleanProperty(false);

            // When the media ends it should execute nextMedia method.
            mediaPlayer.setOnEndOfMedia(new Runnable()
            {
                @Override
                public void run()
                {
                    try
                    {
                        nextMedia();
                    }
                    catch (BLLException ex)
                    {
                        throw new RuntimeException(ex.getMessage(), ex.getCause());
                    }
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
     * Switch looping playlist.
     */
    public void switchLooping()
    {
        if (isShuffling)
        {
            isShuffling = false;
        }

        isLooping = !isLooping;
    }

    /**
     * Switch shuffling playlist.
     */
    public void switchShuffling()
    {
        if (isLooping)
        {
            isLooping = false;
        }

        isShuffling = !isShuffling;
    }

    /**
     * Switch to wanted song on index.
     * @param index of song.
     * @throws BLLException if index does not run.
     */
    @Override
    public void switchSong(int index) throws BLLException
    {
        int oldIndex = currentIndex;
        currentIndex = index;
        try
        {
            switchSong();
        }
        catch (BLLException ex)
        {
            currentIndex = oldIndex;
            throw new BLLException("Could not switch to index: " + index + ", " + ex.getMessage(), ex.getCause());
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
}
