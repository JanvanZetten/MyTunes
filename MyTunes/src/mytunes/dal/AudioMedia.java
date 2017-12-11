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
 * @author Alex, Asbjørn og Jan
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
    private String extension;

    /**
     * Receives an file assumed to be a Audio File.
     *
     * @param file
     * @throws mytunes.dal.DALException
     */
    public AudioMedia(File file) throws DALException
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

    public String getExtension()
    {
        return extension;
    }

    /**
     * Sets the artist in the Audio File.
     *
     * @param artist new value.
     * @throws mytunes.dal.DALException
     */
    public void setArtist(String artist) throws DALException
    {
        this.artist = artist;
        try
        {
            updateMetaData();
        }
        catch (DALException ex)
        {
            throw new DALException("Could not set artist: " + artist + ", " + ex.getMessage(), ex.getCause());
        }
    }

    /**
     * Sets the title in the Audio File.
     *
     * @param title new value.
     * @throws mytunes.dal.DALException
     */
    public void setTitle(String title) throws DALException
    {
        this.title = title;
        try
        {
            updateMetaData();
        }
        catch (DALException ex)
        {
            throw new DALException("Could not set title: " + title + ", " + ex.getMessage(), ex.getCause());
        }
    }

    /**
     * Sets the album in the Audio File.
     *
     * @param album new value.
     * @throws mytunes.dal.DALException
     */
    public void setAlbum(String album) throws DALException
    {
        this.album = album;
        try
        {
            updateMetaData();
        }
        catch (DALException ex)
        {
            throw new DALException("Could not set album: " + album + ", " + ex.getMessage(), ex.getCause());
        }
    }

    /**
     * Sets the year in the Audio File.
     *
     * @param year new value.
     * @throws mytunes.dal.DALException
     */
    public void setYear(int year) throws DALException
    {
        this.year = year;
        try
        {
            updateMetaData();
        }
        catch (DALException ex)
        {
            throw new DALException("Could not set year: " + year + ", " + ex.getMessage(), ex.getCause());
        }
    }

    /**
     * Sets the genre in the Audio File.
     *
     * @param genre new value.
     * @throws mytunes.dal.DALException
     */
    public void setGenre(String genre) throws DALException
    {
        this.genre = genre;
        try
        {
            updateMetaData();
        }
        catch (DALException ex)
        {
            throw new DALException("Could not set genre: " + genre + ", " + ex.getMessage(), ex.getCause());
        }
    }

    /**
     * Load in metadata from file. Using jaudiotagger-2.2.6-SNAPSHOT.jar.
     */
    private void loadMetaData() throws DALException
    {
        try
        {
            AudioFile af = AudioFileIO.read(file);
            Tag tag = af.getTag();

            try
            {
                artist = tag.getFirst(FieldKey.ARTIST);
            }
            catch (NullPointerException ex)
            {
                artist = "";
            }

            try
            {
                title = tag.getFirst(FieldKey.TITLE);
            }
            catch (NullPointerException ex)
            {
                title = "";
            }

            try
            {
                album = tag.getFirst(FieldKey.ALBUM);
            }
            catch (NullPointerException ex)
            {
                album = "";
            }

            try
            {
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
            }
            catch (NullPointerException ex)
            {
                year = -1;
            }

            try
            {
                genre = tag.getFirst(FieldKey.GENRE);
            }
            catch (NullPointerException ex)
            {
                genre = "";
            }
            duration = af.getAudioHeader().getTrackLength();

            try
            {
                duration = af.getAudioHeader().getTrackLength();
            }
            catch (NullPointerException ex)
            {
                duration = 0.0;
            }

            try
            {
                extension = af.getExt();
            }
            catch (NullPointerException ex)
            {
                extension = "";
            }
        }
        catch (Exception ex)
        {
            throw new DALException(ex.getMessage(), ex.getCause());
        }
    }

    /**
     * Update in metadata from file. Using jaudiotagger-2.2.6-SNAPSHOT.jar.
     */
    private void updateMetaData() throws DALException
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
        catch (CannotReadException | IOException | TagException | ReadOnlyFileException | InvalidAudioFrameException | CannotWriteException ex)
        {
            throw new DALException(ex.getMessage(), ex.getCause());
        }
    }
}
