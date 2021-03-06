/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mytunes.bll;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import mytunes.be.Song;
import mytunes.dal.AudioMedia;
import mytunes.dal.DALException;

/**
 *
 * @author Alex, Asbjørn og Jan
 */
public class MediaHandler
{

    private final ObservableList<Song> songs;
    private final SimpleStringProperty artist;
    private final SimpleStringProperty title;
    private final SimpleStringProperty album;
    private final SimpleStringProperty currentTime;
    private final SimpleStringProperty durationTime;
    private final SimpleDoubleProperty currentTimeInDouble;
    private final SimpleDoubleProperty durationTimeInDouble;
    private final SimpleDoubleProperty progress;
    private Player player;
    private AudioMedia audioMedia;
    private int currentIndex;
    private Double currentVolume;
    private boolean isLooping;
    private boolean isShuffling;
    private boolean isPlaying;
    private boolean wasPlaying;
    private boolean isProgressing;
    private final List<Integer> shuffleList;

    public MediaHandler()
    {
        this.songs = FXCollections.observableArrayList();
        artist = new SimpleStringProperty("");
        title = new SimpleStringProperty("");
        album = new SimpleStringProperty("");
        currentTime = new SimpleStringProperty("");
        durationTime = new SimpleStringProperty("");
        currentTimeInDouble = new SimpleDoubleProperty(0.0);
        durationTimeInDouble = new SimpleDoubleProperty(0.0);
        progress = new SimpleDoubleProperty(0.0);
        player = null;
        currentIndex = -1;
        currentVolume = 1.0;
        isLooping = false;
        isShuffling = false;
        isPlaying = false;
        isProgressing = true;
        shuffleList = new ArrayList<>();

        // Set currentTime.
        currentTime.set(sec2minsec(currentTimeInDouble.doubleValue() / 1000));

        // A listener which checks if the value of currentTime changed. If so update it.
        currentTimeInDouble.addListener((observable, oldValue, newValue)
                ->
        {
            if (isProgressing)
            {
                if (durationTimeInDouble.get() - newValue.doubleValue() <= 100)
                {
                    try
                    {
                        nextMedia();
                    }
                    catch (BLLException ex)
                    {
                        stopMedia();
                        throw new RuntimeException(ex.getMessage(), ex.getCause());
                    }
                }
                progress.set(newValue.doubleValue() / durationTimeInDouble.get());
                currentTime.set(sec2minsec(newValue.doubleValue() / 1000));
            }
        });

        // A listener which checks if the value of durationTime changed. If so update it.
        durationTimeInDouble.addListener((observableValue, oldDuration, newDuration)
                ->
        {
            durationTime.set(sec2minsec(newDuration.doubleValue() / 1000));
        });
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
     *
     * @return
     */
    public SimpleStringProperty getArtist()
    {
        return artist;
    }

    /**
     * Get observable String
     *
     * @return
     */
    public SimpleStringProperty getTitle()
    {
        return title;
    }

    /**
     * Get observable String
     *
     * @return
     */
    public SimpleStringProperty getAlbum()
    {
        return album;
    }

    /**
     * Get observable String
     *
     * @return
     */
    public SimpleStringProperty getCurrentTime()
    {
        return currentTime;
    }

    /**
     * Get observable String
     *
     * @return
     */
    public SimpleStringProperty getDurationTime()
    {
        return durationTime;
    }

    /**
     * Get observable Double
     *
     * @return
     */
    public SimpleDoubleProperty getProgress()
    {
        return progress;
    }

    /**
     * set the songs from the given list in the observablelist.
     *
     * @param selectedItem the list of songs from which to take the song
     * @throws mytunes.bll.BLLException
     */
    public void setSongs(ObservableList<Song> selectedItem) throws BLLException
    {
        songs.clear();

        songs.addAll(selectedItem);

        if (songs.size() > 0)
        {
            currentIndex = 0;
        }
        else
        {
            currentIndex = -1;
        }

        if (player == null)
        {
            switchSong();
        }
    }

    /**
     * Play song.
     */
    public void playMedia() throws BLLException
    {
        if (player != null)
        {
            isPlaying = true;
            player.playMedia();
        }
        else
        {
            switchSong();
        }
    }

    /**
     * Pause song.
     */
    public void pauseMedia()
    {
        if (player != null)
        {
            isPlaying = false;
            player.pauseMedia();
        }
    }

    /**
     * Stops the song
     */
    public void stopMedia()
    {
        if (player != null)
        {
            isPlaying = false;
            player.stopMedia();
        }
    }

    /**
     * Change to previous song in list.
     *
     * @throws mytunes.bll.BLLException
     */
    public void previousMedia() throws BLLException
    {
        if (currentIndex - 1 < 0 && isLooping)
        {
            currentIndex = songs.size() - 1;
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
     *
     * @throws mytunes.bll.BLLException
     */
    public void nextMedia() throws BLLException
    {
        if (currentIndex + 1 >= songs.size() && isLooping)
        {
            currentIndex = 0;
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
     *
     * @return current volume as double.
     */
    public double getVolume()
    {
        return currentVolume;
    }

    /**
     * Set volume of player.
     *
     * @param value new volume as double.
     */
    public void setVolume(double value)
    {
        if (player != null)
        {
            player.setVolume(value);
        }
        currentVolume = value;
    }

    /**
     * Sets the songs player to the given value
     * @param value a value from 0 to 1 where 0 is the songs start and 1 is the
     * songs end
     * @throws BLLException
     */
    public void seek(double value) throws BLLException
    {
        try
        {
            if (player != null)
            {
                player.seekMedia(value);
            }
        }
        catch (BLLException ex)
        {
            stopMedia();
            throw new BLLException(ex.getMessage(), ex.getCause());
        }
    }

    /**
     * Switch song to current index.
     */
    private void switchSong() throws BLLException
    {
        if (currentIndex != -1)
        {
            wasPlaying = isPlaying;
            // Load new media.
            if (player != null)
            {
                stopMedia();
                player = null;
            }

            // Checks for shuffle.
            int index;
            if (isShuffling)
            {
                index = shuffleList.get(currentIndex);
            }
            else
            {
                index = currentIndex;
            }

            // Updates information labels for current media
            artist.set(songs.get(index).getArtist());
            title.set(songs.get(index).getTitle());
            album.set(songs.get(index).getAlbum());

            try
            {
                audioMedia = new AudioMedia(new File(songs.get(index).getPath()));
                if (audioMedia.getExtension().equalsIgnoreCase("mp3") || audioMedia.getExtension().equalsIgnoreCase("wav") || audioMedia.getExtension().equalsIgnoreCase("aiff"))
                {
                    player = new AudioPlayer(songs.get(index), currentTimeInDouble, durationTimeInDouble);
                }
                else if (audioMedia.getExtension().equalsIgnoreCase("flac"))
                {
                    player = new FlacPlayer(currentTimeInDouble, durationTimeInDouble);
                    player.setSong(songs.get(index));
                }
                else
                {
                    return;
                }
            }
            catch (DALException ex)
            {
                stopMedia();
                throw new BLLException("Loading new media: " + songs.get(index).getPath() + ", " + ex.getMessage(), ex.getCause());
            }

            setVolume(currentVolume);

            // Auto play if player was already playing.
            if (wasPlaying)
            {
                playMedia();
            }
        }
    }

    /**
     * Switch looping playlist.
     *
     * @return isLooping.
     */
    public boolean switchLooping()
    {
        isLooping = !isLooping;
        return isLooping;
    }

    /**
     * Switch shuffling playlist.
     *
     * @return isShuffling.
     */
    public boolean switchShuffling()
    {
        isShuffling = !isShuffling;
        if (isShuffling)
        {
            shuffleList.clear();
            shuffleList.add(currentIndex);
            currentIndex = 0;
            for (int i = 0; i < songs.size() - 1; i++)
            {
                while (true)
                {
                    int randomIndex = new Random().nextInt(songs.size());
                    if (!shuffleList.contains(randomIndex))
                    {
                        shuffleList.add(randomIndex);
                        break;
                    }
                }
            }
        }
        else
        {
            currentIndex = songs.indexOf(songs.get(shuffleList.get(currentIndex)));
        }
        return isShuffling;
    }

    /**
     * Switch to wanted song on index.
     *
     * @param index of song.
     * @throws BLLException if index does not run.
     */
    public void switchSong(int index) throws BLLException
    {
        currentIndex = index;
        try
        {
            switchSong();
        }
        catch (BLLException ex)
        {
            boolean tmp = wasPlaying;
            stopMedia();
            isPlaying = tmp;
            player = null;
            throw new BLLException("Could not switch to index: " + index + ", " + ex.getMessage(), ex.getCause());
        }
    }

    public void setProgressing(boolean bool)
    {
        isProgressing = bool;
    }

    /**
     * Turns double of seconds into a minutes second format 00:00
     *
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

    public boolean isPlaying()
    {
        return isPlaying;
    }
}
