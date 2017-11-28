/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mytunes.dal;

import java.io.File;
import java.io.IOException;

import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.TagException;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.CannotWriteException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.FieldKey;

/**
 *
 * @author Asbamz
 */
public class AudioMedia
{

    File file;

    private String artist;
    private String title;
    private String album;
    private int year;
    private String genre;
    private double duration;

    /**
     * Receives an file assumed to be a Audio File.
     *
     * @param file
     * @throws IOException
     * @throws ReadOnlyFileException
     * @throws CannotReadException
     * @throws TagException
     * @throws ReadOnlyFileException
     * @throws InvalidAudioFrameException
     */
    public AudioMedia(File file) throws IOException, ReadOnlyFileException, CannotReadException, TagException, ReadOnlyFileException, InvalidAudioFrameException
    {
        this.file = file;
        loadMetaData();
    }

    /**
     * Gets the artist registered in Audio File.
     *
     * @return metadata.
     */
    public String getArtist()
    {
        return artist;
    }

    /**
     * Gets the title registered in Audio File.
     *
     * @return metadata.
     */
    public String getTitle()
    {
        return title;
    }

    /**
     * Gets the album registered in Audio File.
     *
     * @return metadata.
     */
    public String getAlbum()
    {
        return album;
    }

    /**
     * Gets the year registered in Audio File.
     *
     * @return metadata.
     */
    public int getYear()
    {
        return year;
    }

    /**
     * Gets the genre registered in Audio File.
     *
     * @return metadata.
     */
    public String getGenre()
    {
        return genre;
    }

    /**
     * Gets the duration in miliseconds registered in Audio File.
     *
     * @return metadata.
     */
    public double getDuration()
    {
        return duration;
    }

    /**
     * Sets the artist in the Audio File.
     *
     * @param artist new value.
     */
    public void setArtist(String artist)
    {
        this.artist = artist;
        updateMetaData();
    }

    /**
     * Sets the title in the Audio File.
     *
     * @param title new value.
     */
    public void setTitle(String title)
    {
        this.title = title;
        updateMetaData();
    }

    /**
     * Sets the album in the Audio File.
     *
     * @param album new value.
     */
    public void setAlbum(String album)
    {
        this.album = album;
        updateMetaData();
    }

    /**
     * Sets the year in the Audio File.
     *
     * @param year new value.
     */
    public void setYear(int year)
    {
        this.year = year;
        updateMetaData();
    }

    /**
     * Sets the genre in the Audio File.
     *
     * @param genre new value.
     */
    public void setGenre(String genre)
    {
        this.genre = genre;
        updateMetaData();
    }

    /**
     * Load in metadata from file. Using jaudiotagger-2.2.6-SNAPSHOT.jar.
     */
    private void loadMetaData()
    {
        try
        {
            AudioFile af = AudioFileIO.read(file);
            Tag tag = af.getTag();

            artist = tag.getFirst(FieldKey.ARTIST);
            title = tag.getFirst(FieldKey.TITLE);
            album = tag.getFirst(FieldKey.ALBUM);
            String str = tag.getFirst(FieldKey.YEAR);
            if (!str.isEmpty())
            {
                if (str.length() > 4)
                {
                    year = Integer.parseInt(str.substring(0, 3));
                }
                else
                {
                    year = Integer.parseInt(str.substring(0, str.length()));
                }
            }
            else
            {
                year = -1;
            }
            genre = tag.getFirst(FieldKey.GENRE);
            duration = af.getAudioHeader().getTrackLength();

        }
        catch (NumberFormatException ex)
        {
            throw new RuntimeException(ex.getMessage(), ex.getCause());
        }
        catch (CannotReadException ex)
        {
            throw new RuntimeException(ex.getMessage(), ex.getCause());
        }
        catch (IOException ex)
        {
            throw new RuntimeException(ex.getMessage(), ex.getCause());
        }
        catch (TagException ex)
        {
            throw new RuntimeException(ex.getMessage(), ex.getCause());
        }
        catch (ReadOnlyFileException ex)
        {
            throw new RuntimeException(ex.getMessage(), ex.getCause());
        }
        catch (InvalidAudioFrameException ex)
        {
            throw new RuntimeException(ex.getMessage(), ex.getCause());
        }
    }

    /**
     * Update in metadata from file. Using jaudiotagger-2.2.6-SNAPSHOT.jar.
     */
    private void updateMetaData()
    {
        try
        {
            AudioFile af = AudioFileIO.read(file);
            Tag tag = af.getTag();

            tag.setField(FieldKey.ARTIST, artist);
            tag.setField(FieldKey.TITLE, title);
            tag.setField(FieldKey.ALBUM, album);
            if (year != -1)
            {
                tag.setField(FieldKey.YEAR, String.valueOf(year));
            }
            tag.setField(FieldKey.GENRE, genre);

            AudioFileIO.write(af);
            loadMetaData();
        }
        catch (CannotReadException ex)
        {
            throw new RuntimeException(ex.getMessage(), ex.getCause());
        }
        catch (IOException ex)
        {
            throw new RuntimeException(ex.getMessage(), ex.getCause());
        }
        catch (TagException ex)
        {
            throw new RuntimeException(ex.getMessage(), ex.getCause());
        }
        catch (ReadOnlyFileException ex)
        {
            throw new RuntimeException(ex.getMessage(), ex.getCause());
        }
        catch (InvalidAudioFrameException ex)
        {
            throw new RuntimeException(ex.getMessage(), ex.getCause());
        }
        catch (CannotWriteException ex)
        {
            throw new RuntimeException(ex.getMessage(), ex.getCause());
        }
    }
}
