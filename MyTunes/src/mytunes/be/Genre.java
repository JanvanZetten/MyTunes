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
public class Genre implements Serializable
{

    private final int genreId;
    private String genre;
    private String createdBy;

    public Genre(int genreId, String genre)
    {
        this.genreId = genreId;
        this.genre = genre;
    }

    /**
     * Gets genre id
     *
     * @return genre id
     */
    public int getGenreId()
    {
        return genreId;
    }

    /**
     * Gets genre
     *
     * @return genre
     */
    public String getGenre()
    {
        return genre;
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

    /**
     * Set genre
     *
     * @param genre
     */
    public void setGenre(String genre)
    {
        this.genre = genre;
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

    @Override
    public String toString()
    {
        return genre;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj == null)
        {
            return false;
        }
        if (obj instanceof Genre)
        {
            return this.genreId == ((Genre) obj).getGenreId();
        }
        return super.equals(obj); //To change body of generated methods, choose Tools | Templates.
    }
}
