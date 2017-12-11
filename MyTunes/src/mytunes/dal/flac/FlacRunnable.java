/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mytunes.dal.flac;

import java.io.IOException;

import org.jflac.FLACDecoder;
import org.jflac.metadata.SeekPoint;

/**
 * Made to handle decoding. Used and tested with custom JFlac codec 1.5.3.
 * @author Alex, Asbjørn og Jan
 */
public class FlacRunnable implements Runnable
{
    private FLACDecoder decoder;
    private SeekPoint from;
    private SeekPoint to;

    /**
     * Get needed resources.
     * @param decoder
     * @param from
     * @param to
     */
    public FlacRunnable(FLACDecoder decoder, SeekPoint from, SeekPoint to)
    {
        this.decoder = decoder;
        this.from = from;
        this.to = to;
    }

    /**
     * Start decoding.
     */
    @Override
    public void run()
    {
        try
        {
            decoder.decode(from, to);
        }
        catch (IOException ex)
        {
            throw new RuntimeException("Play Flac: " + ex.getMessage(), ex.getCause());
        }
    }

    /**
     * Stop decoding.
     */
    public void stop()
    {
        decoder.stop();
    }

    /**
     * Pause decoding.
     */
    public void pause()
    {
        decoder.pause();
    }

    /**
     * Resume decoding.
     */
    public void resume()
    {
        decoder.resume();
    }
}
