/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mytunes.be;

import java.io.Serializable;

/**
 *
 * @author Alex, Asbjørn og Jan
 */
public class Song implements Serializable
{

    private final int songId;
    private String title;
    private String album;
    private String artist;
    private int year;
    private Genre genre;
    private String path;
    private String duration;
    private String createdBy;

    public Song(int songId, String title, String artist, String path)
    {
        this.songId = songId;
        this.title = title;
        this.artist = artist;
        this.path = path;
    }

    /**
     * edit the title of the song
     *
     * @param title as string
     */
    public void setTitle(String title)
    {
        this.title = title;
    }

    /**
     * set the album of the song
     *
     * @param album as string
     */
    public void setAlbum(String album)
    {
        this.album = album;
    }

    /**
     * set the artist of the song
     *
     * @param artist as string
     */
    public void setArtist(String artist)
    {
        this.artist = artist;
    }

    /**
     * set the year of the song
     *
     * @param year as int
     */
    public void setYear(int year)
    {
        this.year = year;
    }

    /**
     * set th genre of the song
     *
     * @param genre as string
     */
    public void setGenre(Genre genre)
    {
        this.genre = genre;
    }

    /**
     * set the duration of the song.
     *
     * @param duration as double seconds.
     */
    public void setDuration(double duration)
    {
        int min;
        int sec;
        min = (int) duration / 60;
        sec = (int) duration % 60;

        if (sec > 9)
        {
            this.duration = min + ":" + sec;
        }
        else
        {
            this.duration = min + ":0" + sec;
        }
    }

    /**
     * Set the value of createdBy
     *
     * @param createdBy new value of createdBy
     */
    public void setCreatedBy(String createdBy)
    {
        this.createdBy = createdBy;
    }

    /**
     * get the songs songId
     *
     * @return a int with the songId
     */
    public int getSongId()
    {
        return songId;
    }

    /**
     * get the songs title
     *
     * @return a String with the title
     */
    public String getTitle()
    {
        return title;
    }

    /**
     * get the songs album
     *
     * @return a String with the album
     */
    public String getAlbum()
    {
        return album;
    }

    /**
     * get the songs artist
     *
     * @return a String with the artist
     */
    public String getArtist()
    {
        return artist;
    }

    /**
     * get the songs year
     *
     * @return a int with the year
     */
    public int getYear()
    {
        return year;
    }

    /**
     * get the songs genre
     *
     * @return a Genre with the genre
     */
    public Genre getGenre()
    {
        return genre;
    }

    /**
     * returns the path
     *
     * @return string path
     */
    public String getPath()
    {
        return path;
    }

    public void setPath(String path)
    {
        this.path = path;
    }

    /**
     * returns the duration
     *
     * @return string duration
     */
    public String getDuration()
    {
        return duration;
    }

    /**
     * Get the value of createdBy
     *
     * @return the value of createdBy
     */
    public String getCreatedBy()
    {
        return createdBy;
    }

    @Override
    public String toString()
    {
        return "Song{" + "songId=" + songId + ", title=" + title + ", album=" + album + ", artist=" + artist + ", year=" + year + ", genre=" + genre + ", path=" + path + ", duration=" + duration + '}';
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj == null)
        {
            return false;
        }
        if (obj instanceof Song)
        {
            return this.songId == ((Song) obj).getSongId();
        }
        return super.equals(obj); //To change body of generated methods, choose Tools | Templates.
    }
}
