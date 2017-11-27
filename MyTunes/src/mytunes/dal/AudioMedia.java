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

    /**
     * Sets the artist in the Audio File.
     *
     * @param artist new value.
     */
    public void setArtist(String artist);

    /**
     * Sets the title in the Audio File.
     *
     * @param title new value.
     */
    public void setTitle(String title);

    /**
     * Sets the album in the Audio File.
     *
     * @param album new value.
     */
    public void setAlbum(String album);

    /**
     * Sets the year in the Audio File.
     *
     * @param year new value.
     */
    public void setYear(int year);

    /**
     * Sets the genre in the Audio File.
     *
     * @param genre new value.
     */
    public void setGenre(String genre);
}
