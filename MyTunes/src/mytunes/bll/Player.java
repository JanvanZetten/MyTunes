/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mytunes.bll;

import mytunes.be.Song;

/**
 *
 * @author Alex, Asbjørn og Jan
 */
public interface Player {

    /**
     * Play song.
     * @throws mytunes.bll.BLLException
     */
    public void playMedia() throws BLLException;

    /**
     * Pause song.
     */
    public void pauseMedia();

    /**
     * Stop song.
     */
    public void stopMedia();

    /**
     * Seek song.
     *
     * @param duration
     */
    public void seekMedia(double duration) throws BLLException;

    /**
     * Set song to play.
     *
     * @param song
     * @throws BLLException
     */
    public void setSong(Song song) throws BLLException;

    /**
     * Set volume of player.
     *
     * @param value new volume as double.
     */
    public void setVolume(double value);
}
