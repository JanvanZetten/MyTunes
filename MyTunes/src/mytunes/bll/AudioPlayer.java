/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mytunes.bll;

import java.io.File;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.media.*;
import javafx.util.Duration;
import mytunes.be.Song;

/**
 *
 * @author Asbamz
 */
public class AudioPlayer implements Player {

    private final SimpleDoubleProperty currentTime;
    private final SimpleDoubleProperty durationTime;
    private final SimpleDoubleProperty progress;
    private Media sound;
    private MediaPlayer mediaPlayer;

    public AudioPlayer(Song song, SimpleDoubleProperty currentTime, SimpleDoubleProperty durationTime, SimpleDoubleProperty progress) throws BLLException {
        this.currentTime = currentTime;
        this.durationTime = durationTime;
        this.progress = progress;

        // Load new media.
        try {
            sound = new Media(new File(song.getpath()).toURI().toString());
        } catch (MediaException ex) {
            throw new BLLException("Loading new media: " + song.getpath() + ", " + ex.getMessage(), ex.getCause());
        }

        mediaPlayer = new MediaPlayer(sound);

        // When the media is loaded and ready it should update the duration and current time.
        mediaPlayer.setOnReady(new Runnable() {
            @Override
            public void run() {
                durationTime.set(mediaPlayer.getMedia().getDuration().toMillis());
                currentTime.set(mediaPlayer.getCurrentTime().toMillis());
            }
        });

        // A listener which checks if the value of currentTime changed. If so update it.
        mediaPlayer.currentTimeProperty().addListener((observableValue, oldDuration, newDuration)
                -> {
            currentTime.set(newDuration.toMillis());
            progress.set(newDuration.toMillis() / mediaPlayer.getMedia().getDuration().toMillis());
        });
    }

    /**
     * Play song.
     */
    @Override
    public void playMedia() {
        mediaPlayer.play();
    }

    /**
     * Pause song.
     */
    @Override
    public void pauseMedia() {
        mediaPlayer.pause();
    }

    /**
     * Stop song.
     */
    @Override
    public void stopMedia() {
        mediaPlayer.stop();
    }

    /**
     * Seek song.
     *
     * @param duration
     */
    @Override
    public void seekMedia(double duration) {
        mediaPlayer.seek(new Duration(duration));
    }

    /**
     * Set volume of player.
     *
     * @param value new volume as double.
     */
    @Override
    public void setVolume(double value) {
        mediaPlayer.setVolume(value);
    }

    /**
     * Set song to play.
     *
     * @param song
     * @throws BLLException if index does not run.
     */
    @Override
    public void setSong(Song song) throws BLLException {
        // Load new media.
        try {
            sound = new Media(new File(song.getpath()).toURI().toString());
        } catch (MediaException ex) {
            throw new BLLException("Loading new media: " + song.getpath() + ", " + ex.getMessage(), ex.getCause());
        }
    }
}
