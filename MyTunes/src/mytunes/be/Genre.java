/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mytunes.be;

/**
 *
 * @author Asbamz
 */
public class Genre
{

    private final int genreId;
    private String genre;

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
     * Set genre
     *
     * @param genre
     */
    public void setGenre(String genre)
    {
        this.genre = genre;
    }

    @Override
    public String toString()
    {
        return "Genre{" + "genreId=" + genreId + ", genre=" + genre + '}';
    }
}
