/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mytunes.dal.flac;

import javafx.application.Platform;
import javafx.beans.property.SimpleDoubleProperty;
import org.jflac.FLACDecoder;

/**
 * Made to listen and change currentTime. Used and tested with custom JFlac
 * codec 1.5.3.
 * @author Alex, Asbjørn og Jan
 */
public class FlacProgressListener implements Runnable
{
    private volatile Thread blinker;

    private final SimpleDoubleProperty currentTime;
    private FLACDecoder decoder;
    private int sampleRate;

    /**
     * Get needed resources.
     * @param currentTime
     * @param decoder
     * @param sampleRate
     */
    public FlacProgressListener(SimpleDoubleProperty currentTime, FLACDecoder decoder, int sampleRate)
    {
        this.currentTime = currentTime;
        this.decoder = decoder;
        this.sampleRate = sampleRate;
    }

    /**
     * Stop listening.
     */
    public void stop()
    {
        blinker = null;
    }

    /**
     * Changing currentTime from decoded samples.
     */
    @Override
    public void run()
    {
        blinker = Thread.currentThread();
        Thread thisThread = Thread.currentThread();
        while (blinker == thisThread)
        {
            Platform.runLater(new Runnable()
            {
                @Override
                public void run()
                {
                    currentTime.set(((decoder.getSamplesDecoded() * 1.0) / (sampleRate * 1.0)) * 1000);
                }
            });

            try
            {
                Thread.sleep(100);
            }
            catch (InterruptedException ex)
            {
            }
        }
    }

}
