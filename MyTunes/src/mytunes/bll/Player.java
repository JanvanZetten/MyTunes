/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mytunes.bll;

import mytunes.be.Song;

/**
 *
 * @author Asbamz
 */
public interface Player
{
    /**
     * Play song.
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
     * @param duration
     */
    public void seekMedia(double duration);

    /**
     * Set song to play.
     * @param song
     * @throws BLLException
     */
    public void setSong(Song song) throws BLLException;

    /**
     * Set volume of player.
     * @param value new volume as double.
     */
    public void setVolume(double value);
}
