/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mytunes.dal.flac;

import java.io.FileInputStream;
import java.io.IOException;
import javafx.beans.property.SimpleDoubleProperty;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.BooleanControl;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

import mytunes.be.Song;
import mytunes.bll.BLLException;
import mytunes.bll.Player;

import org.jflac.FrameListener;
import org.jflac.PCMProcessor;
import org.jflac.FLACDecoder;
import org.jflac.frame.Frame;
import org.jflac.io.RandomFileInputStream;
import org.jflac.metadata.Metadata;
import org.jflac.metadata.SeekPoint;
import org.jflac.metadata.StreamInfo;
import org.jflac.util.ByteData;

/**
 * JFlac implemented in MyTunes project. Used and tested with custom JFlac codec
 * 1.5.3.
 * @author Asbamz
 */
public class FlacPlayer implements PCMProcessor, FrameListener, Player
{
    private final SimpleDoubleProperty currentTime;
    private final SimpleDoubleProperty durationTime;

    private int sampleRate;
    private long totalSamples;
    private int bitPerSamples;
    private double currentVolume = 1.0;

    private AudioFormat fmt;
    private DataLine.Info info;
    private SourceDataLine line;
    private String audioPath;
    private FLACDecoder decoder;
    private FileInputStream is;
    private FlacRunnable fr;
    private FlacProgressListener fpl;

    private volatile Thread t;
    private volatile Thread t2;

    /**
     * Setup flac player.
     * @param currentTime
     * @param durationTime
     * @throws BLLException
     */
    public FlacPlayer(SimpleDoubleProperty currentTime, SimpleDoubleProperty durationTime) throws BLLException
    {
        this.currentTime = currentTime;
        this.durationTime = durationTime;
    }

    /**
     * Process the StreamInfo block.
     * @param streamInfo the StreamInfo block
     * @throws javax.sound.sampled.LineUnavailableException
     */
    @Override
    public void processStreamInfo(StreamInfo streamInfo)
    {
        try
        {
            fmt = streamInfo.getAudioFormat();
            info = new DataLine.Info(SourceDataLine.class, fmt, AudioSystem.NOT_SPECIFIED);
            line = (SourceDataLine) AudioSystem.getLine(info);

            line.open(fmt, AudioSystem.NOT_SPECIFIED);
            line.start();
        }
        catch (LineUnavailableException e)
        {
            System.out.println("Flac Player -> processStreamInfor: Could not get line.");
        }
    }

    /**
     * Process the decoded PCM bytes.
     * @param pcm The decoded PCM data
     */
    @Override
    public void processPCM(ByteData pcm)
    {
        setVolume(currentVolume);
        line.write(pcm.getData(), 0, pcm.getLen());
    }

    /**
     * Called for each Metadata frame read.
     * @param metadata The metadata frame read
     */
    @Override
    public void processMetadata(Metadata metadata)
    {
    }

    /**
     * Called for each data frame read.
     * @param frame The data frame read
     */
    @Override
    public void processFrame(Frame frame)
    {
    }

    /**
     * Process a decoder error.
     * @param msg The error message
     */
    @Override
    public void processError(String msg)
    {
        System.out.println("FLAC Error: " + msg);
    }

    /**
     * Decode and play an input FLAC file.
     * @param fromSeekPoint The starting Seek Point in percent.
     * @param toSeekPoint The ending Seek Point in percent.
     * @throws IOException Thrown if error reading file.
     * @throws LineUnavailableException Thrown if error playing file.
     */
    public void playFlac(double fromSeekPoint, double toSeekPoint) throws IOException, LineUnavailableException
    {
        // Get stream.
        RandomFileInputStream is = new RandomFileInputStream(audioPath);

        // Setup decoder.
        FLACDecoder decoder = new FLACDecoder(is);
        decoder.addPCMProcessor(this);
        decoder.addFrameListener(this);
        decoder.readMetadata();

        // Get length of file and reset.
        long lengthOfFile = is.getLength();
        is.reset();

        // Calculate start seek point.
        SeekPoint from = null;
        if (fromSeekPoint >= 0 && fromSeekPoint <= 100)
        {
            from = new SeekPoint((long) (totalSamples / 100.0 * fromSeekPoint), (long) (lengthOfFile * (fromSeekPoint / (totalSamples / sampleRate * 1.0))), bitPerSamples);
        }
        else
        {
            from = new SeekPoint(0, 0, bitPerSamples);
        }

        // Calculate end seek point.
        SeekPoint to = null;
        if (toSeekPoint >= 0 && toSeekPoint <= 100)
        {
            to = new SeekPoint((long) (totalSamples / 100.0 * toSeekPoint), (long) (lengthOfFile * (toSeekPoint / (totalSamples / sampleRate * 1.0))), bitPerSamples);
        }
        else
        {
            to = new SeekPoint(totalSamples, lengthOfFile, bitPerSamples);
        }

        // Run thread handling decoding.
        fr = new FlacRunnable(decoder, from, to);
        t = new Thread(fr, "Flac Decoding Thread");
        t.setDaemon(true);
        t.start();

        // Run thread handling currentTime change.
        fpl = new FlacProgressListener(currentTime, decoder, sampleRate);
        t2 = new Thread(fpl, "Flac Time Listener");
        t2.setDaemon(true);
        t2.start();

    }

    /**
     * Play loaded media.
     * @throws BLLException
     */
    @Override
    public void playMedia() throws BLLException
    {
        // If the thread t is not instantiated.
        if (t == null)
        {
            try
            {
                // Play flac from beginning to end.
                playFlac(0, -1);
            }
            catch (IOException | LineUnavailableException ex)
            {
                throw new BLLException("Playing Flac: " + ex.getMessage(), ex.getCause());
            }
        }
        else
        {
            // Resume decoding.
            fr.resume();
        }
    }

    /**
     * Pause decoding.
     */
    @Override
    public void pauseMedia()
    {
        // If the thread t is instantiated.
        if (fr != null)
        {
            // Pause decoding.
            fr.pause();
        }
    }

    /**
     * Stop decoding.
     */
    @Override
    public void stopMedia()
    {
        // If the thread t is instantiated.
        if (fr != null)
        {
            // Stop decoding and listening.
            fr.stop();
            fpl.stop();

            t = null;
            t2 = null;

            //Drain and stop line.
            line.drain();
            line.close();
        }
    }

    /**
     * Seek media.
     * @param duration
     * @throws BLLException
     */
    @Override
    public void seekMedia(double duration) throws BLLException
    {
        // If the thread t is not instantiated.
        if (t == null)
        {
            try
            {
                if (duration >= 0.0 || duration <= 1.0)
                {
                    playFlac(duration * 100, -1);
                }
                else
                {
                    playFlac(0, -1);
                }
            }
            catch (IOException | LineUnavailableException ex)
            {
                throw new BLLException("Playing Flac: " + ex.getMessage(), ex.getCause());
            }
        }
        else
        {
            stopMedia();
            seekMedia(duration);
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
        // If the thread t is not instantiated.
        if (t == null)
        {
            try
            {
                audioPath = song.getpath();
                is = new FileInputStream(audioPath);
                decoder = new FLACDecoder(is);
                StreamInfo si = decoder.readStreamInfo();

                sampleRate = si.getSampleRate();
                totalSamples = si.getTotalSamples();
                bitPerSamples = si.getBitsPerSample();
                durationTime.set((totalSamples / sampleRate) * 1000);
            }
            catch (IOException ex)
            {
                throw new BLLException("Loading FLAC: " + ex.getMessage(), ex.getCause());
            }
        }
        else
        {
            stopMedia();
            setSong(song);
        }
    }

    /**
     * Sets volume of SourceDataLine.
     * @param value between 0 and 100.
     */
    @Override
    public void setVolume(double value)
    {
        // If line exists.
        if (line != null)
        {
            // Adjust the volume of the output line.
            if (line.isControlSupported(FloatControl.Type.MASTER_GAIN))
            {
                FloatControl volume = (FloatControl) line.getControl(FloatControl.Type.MASTER_GAIN);
                BooleanControl mute = (BooleanControl) line.getControl(BooleanControl.Type.MUTE);

                // calculate new volume from percent 0.0 - 1.0 to minimum - maximum.
                if (value == 0.0)
                {
                    mute.setValue(true);
                    currentVolume = value;
                }
                else if (value > 0.0 || value <= 1.0)
                {
                    mute.setValue(false);
                    float f = volume.getMaximum() - (volume.getMinimum() + 40);
                    volume.setValue((float) ((f / 100) * (value * 100)) + (volume.getMinimum() + 40));
                    currentVolume = value;
                }
                else
                {
                    mute.setValue(false);
                    volume.setValue(volume.getMaximum());
                    currentVolume = 1.0;
                }
            }
        }
        else
        {
            currentVolume = value;
        }
    }
}
