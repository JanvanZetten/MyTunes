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
public interface audioMedia
{

    public String getArtist();

    public String getTitle();

    public String getAlbum();

    public int getYear();

    public String getGenre();

    public int getLength();

    public void setArtist();

    public void setTitle();

    public void setAlbum();

    public void setYear();

    public void setGenre();
}
