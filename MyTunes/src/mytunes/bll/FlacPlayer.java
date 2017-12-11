/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mytunes.bll;

import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleDoubleProperty;
import mytunes.be.Song;
import mytunes.dal.DALException;
import mytunes.dal.flac.FlacDecoder;

/**
 *
 * @author Alex, Asbjørn og Jan
 */
public class FlacPlayer implements Player
{
    FlacDecoder fd;

    public FlacPlayer(SimpleDoubleProperty currentTime, SimpleDoubleProperty durationTime)
    {
        fd = new FlacDecoder(currentTime, durationTime);
    }

    /**
     * Play loaded media.
     * @throws BLLException
     */
    @Override
    public void playMedia() throws BLLException
    {
        try
        {
            fd.playMedia();
        }
        catch (DALException ex)
        {
            throw new BLLException(ex.getMessage(), ex.getCause());
        }
    }

    /**
     * Pause decoding.
     */
    @Override
    public void pauseMedia()
    {
        fd.pauseMedia();
    }

    /**
     * Stop decoding.
     */
    @Override
    public void stopMedia()
    {
        fd.stopMedia();
    }

    /**
     * Seek media.
     * @param duration
     * @throws BLLException
     */
    @Override
    public void seekMedia(double duration) throws BLLException
    {
        try
        {
            fd.seekMedia(duration);
        }
        catch (DALException ex)
        {
            throw new BLLException(ex.getMessage(), ex.getCause());
        }
    }

    /**
     * Add song to FileInputStream.
     * @param song.
     * @throws BLLException
     */
    @Override
    public void setSong(Song song) throws BLLException
    {
        try
        {
            fd.setSong(song);
        }
        catch (DALException ex)
        {
            throw new BLLException(ex.getMessage(), ex.getCause());
        }
    }

    /**
     * Sets volume of SourceDataLine.
     * @param value between 0 and 100.
     */
    @Override
    public void setVolume(double value)
    {
        fd.setVolume(value);
    }
}
