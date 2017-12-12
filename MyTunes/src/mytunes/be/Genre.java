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
public class Genre implements Serializable {

    private final int genreId;
    private String genre;

    public Genre(int genreId, String genre) {
        this.genreId = genreId;
        this.genre = genre;
    }

    /**
     * Gets genre id
     *
     * @return genre id
     */
    public int getGenreId() {
        return genreId;
    }

    /**
     * Gets genre
     *
     * @return genre
     */
    public String getGenre() {
        return genre;
    }

    /**
     * Set genre
     *
     * @param genre
     */
    public void setGenre(String genre) {
        this.genre = genre;
    }

    @Override
    public String toString() {
        return genre;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj instanceof Genre) {
            return this.genreId == ((Genre) obj).getGenreId();
        }
        return super.equals(obj); //To change body of generated methods, choose Tools | Templates.
    }
}
