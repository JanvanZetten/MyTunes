/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mytunes.dal;

/**
 *
 * @author Asbamz
 */
public interface AudioMedia
{

    /**
     * Gets the artist registered in Audio File.
     *
     * @return metadata
     */
    public String getArtist();

    /**
     * Gets the title registered in Audio File.
     *
     * @return metadata
     */
    public String getTitle();

    /**
     * Gets the album registered in Audio File.
     *
     * @return metadata
     */
    public String getAlbum();

    /**
     * Gets the year registered in Audio File.
     *
     * @return metadata
     */
    public int getYear();

    /**
     * Gets the genre registered in Audio File.
     *
     * @return metadata
     */
    public String getGenre();

    /**
     * Gets the duration in miliseconds registered in Audio File.
     *
     * @return metadata
     */
    public double getDuration();
}
