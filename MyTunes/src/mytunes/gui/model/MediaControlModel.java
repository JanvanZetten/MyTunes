/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mytunes.gui.model;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;
import mytunes.be.Song;
import mytunes.bll.BLLException;
import mytunes.bll.MediaHandler;

/**
 *
 * @author Alex, Asbjørn og Jan
 */
public class MediaControlModel {

    private MediaHandler mediaHandler;
    private boolean muted = false;

    public MediaControlModel() {
        mediaHandler = new MediaHandler();
    }

    /**
     * Play song.
     */
    public void playMedia() {
        try {
            mediaHandler.playMedia();
        } catch (BLLException ex) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Could not play Media: " + ex.getMessage() + ".", ButtonType.OK);
            alert.showAndWait();
        }
    }

    /**
     * Pause song.
     */
    public void pauseMedia() {
        mediaHandler.pauseMedia();
    }

    /**
     * Change to previous song in list.
     */
    public void previousMedia() {
        try {
            mediaHandler.previousMedia();
        } catch (BLLException ex) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Could not load Media: " + ex.getMessage() + ".", ButtonType.OK);
            alert.showAndWait();
        }
    }

    /**
     * Change to next song in list.
     */
    public void nextMedia() {
        try {
            mediaHandler.nextMedia();
        } catch (BLLException ex) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Could not load Media: " + ex.getMessage() + ".", ButtonType.OK);
            alert.showAndWait();
        }
    }

    /**
     * Switch song to index.
     */
    public void switchSong(int index) {
        try {
            mediaHandler.switchSong(index);
        } catch (BLLException ex) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Could not load Media: " + ex.getMessage() + ".", ButtonType.OK);
            alert.showAndWait();
        }
    }

    /**
     * Switch looping playlist.
     *
     * @return
     */
    public boolean switchLooping() {
        return mediaHandler.switchLooping();
    }

    /**
     * Switch shuffling playlist.
     *
     * @return
     */
    public boolean switchShuffling() {
        return mediaHandler.switchShuffling();
    }

    /**
     * Get observable String
     *
     * @return
     */
    public SimpleStringProperty getArtist() {
        return mediaHandler.getArtist();
    }

    /**
     * Get observable String
     *
     * @return
     */
    public SimpleStringProperty getTitle() {
        return mediaHandler.getTitle();
    }

    /**
     * Get observable String
     *
     * @return
     */
    public SimpleStringProperty getAlbum() {
        return mediaHandler.getAlbum();
    }

    /**
     * Get observable String
     *
     * @return
     */
    public SimpleStringProperty getCurrentTime() {
        return mediaHandler.getCurrentTime();
    }

    /**
     * Get observable String
     *
     * @return
     */
    public SimpleStringProperty getDurationTime() {
        return mediaHandler.getDurationTime();
    }

    /**
     * Get observable Double
     *
     * @return
     */
    public SimpleDoubleProperty getProgress() {
        return mediaHandler.getProgress();
    }

    /**
     * links the volumeslider to the mediaplayers volume
     *
     * @param volumeSlider the Slider who have to adjust the volume
     */
    public void volumeSliderSetup(Slider volumeSlider) {
        volumeSlider.setValue(mediaHandler.getVolume() * volumeSlider.getMax());
        volumeSlider.valueProperty().addListener(new InvalidationListener() {
            @Override
            public void invalidated(Observable observable) {
                mediaHandler.setVolume(volumeSlider.getValue() / volumeSlider.getMax());
                if (volumeSlider.getValue() == 0) {
                }
            }
        });

    }

    /**
     * links the musicSlider to the mediahandlers progress
     *
     * @param musicSlider
     */
    public void musicSliderSetup(Slider musicSlider) {
        musicSlider.valueProperty().bindBidirectional(mediaHandler.getProgress());

        musicSlider.valueProperty().addListener(new InvalidationListener() {
            @Override
            public void invalidated(Observable observable) {
                if (musicSlider.pressedProperty().get()) {
                    mediaHandler.setProgressing(false);
                }
            }
        });

        musicSlider.setOnMouseReleased(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                try {
                    mediaHandler.seek(musicSlider.getValue());
                } catch (BLLException ex) {
                    mediaHandler.stopMedia();
                }
                mediaHandler.setProgressing(true);
            }
        });
    }

    /**
     * a boolean for if the song is playing
     *
     * @return true if playing else false
     */
    public boolean isPlaying() {
        return mediaHandler.isPlaying();
    }

    /**
     * Sets the given observablelist for the songs in the que
     *
     * @param songs a obervable list
     */
    public void setListOfSongsForPlaying(ObservableList<Song> songs) {
        try {
            mediaHandler.setSongs(songs);
        } catch (BLLException ex) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Could not set Songs: " + ex.getMessage() + ".", ButtonType.OK);
            alert.showAndWait();
        }
    }

    /**
     * change the muted varibel
     *
     * @param mutedSetting true when muted false when unmuted
     */
    public void setMuted(boolean mutedSetting) {
        muted = mutedSetting;
    }

    /**
     * returns the muted varibel
     *
     * @return
     */
    public boolean isMuted() {
        return muted;
    }

}
