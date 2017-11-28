/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mytunes.be;

/**
 *
 * @author janvanzetten
 */
public class Song {

    private String title;
    private String album;
    private String artist;
    private int year;
    private String genre;
    private final String path;

    public Song(String title, String artist, String path) {
        this.title = title;
        this.artist = artist;
        this.path = path;
    }

    /**
     * edit the title of the song
     *
     * @param title as string
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * set the album of the song
     *
     * @param album as string
     */
    public void setAlbum(String album) {
        this.album = album;
    }

    /**
     * set the artist of the song
     *
     * @param artist as string
     */
    public void setArtist(String artist) {
        this.artist = artist;
    }

    /**
     * set the year of the song
     *
     * @param year as int
     */
    public void setYear(int year) {
        this.year = year;
    }

    /**
     * set th genre of the song
     *
     * @param genre as string
     */
    public void setGenre(String genre) {
        this.genre = genre;
    }

    /**
     * get the songs title
     *
     * @return a String with the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * get the songs album
     *
     * @return a String with the album
     */
    public String getAlbum() {
        return album;
    }

    /**
     * get the songs artist
     *
     * @return a String with the artist
     */
    public String getArtist() {
        return artist;
    }

    /**
     * get the songs year
     *
     * @return a int with the year
     */
    public int getYear() {
        return year;
    }

    /**
     * get the songs genre
     *
     * @return a String with the genre
     */
    public String getGenre() {
        return genre;
    }

    /**
     * returns the path with a file:// prefix
     *
     * @return string path
     */
    public String getpath() {
        return ("file://" + path);
    }

    @Override
    public String toString() {
        return "Song{" + "title=" + title + ", album=" + album + ", artist=" + artist + ", year=" + year + ", genre=" + genre + '}';
    }
    
    
}
