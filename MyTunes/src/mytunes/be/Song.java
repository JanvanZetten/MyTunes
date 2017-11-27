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
    
    String title;
    String album;
    String artist;
    int year;
    String genre;

    
    
    
    
    
    /**
     * get the songs title
     * @return a String with the title
     */
    public String getTitle() {
        return title;
    }

    /**
     *get the songs album
     * @return a String with the album
     */
    public String getAlbum() {
        return album;
    }

    /**
     * get the songs artist
     * @return a String with the artist
     */
    public String getArtist() {
        return artist;
    }

    /**
     * get the songs year
     * @return a int with the year
     */
    public int getYear() {
        return year;
    }

    /**
     * get the songs genre
     * @return a String with the genre
     */
    public String getGenre() {
        return genre;
    }
    
    
    
    
}
