/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mytunes.dal;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.parser.mp3.Mp3Parser;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 *
 * @author Asbamz
 */
public class MP3 implements AudioMedia
{

    private String mp3File;

    private String artist;
    private String title;
    private String album;
    private int year;
    private String genre;
    private double duration;

    public MP3(String mp3File)
    {
        this.mp3File = mp3File;
        loadMetaData();
    }

    @Override
    public String getArtist()
    {
        return artist;
    }

    @Override
    public String getTitle()
    {
        return title;
    }

    @Override
    public String getAlbum()
    {
        return album;
    }

    @Override
    public int getYear()
    {
        return year;
    }

    @Override
    public String getGenre()
    {
        return genre;
    }

    @Override
    public double getDuration()
    {
        return duration;
    }

    /**
     * Load in metadata from file. Using tika-app-1.16.jar.
     */
    private void loadMetaData()
    {
        try
        {
            InputStream input = new FileInputStream(new File(mp3File));
            ContentHandler handler = new DefaultHandler();
            Metadata metadata = new Metadata();
            Parser parser = new Mp3Parser();
            ParseContext parseCtx = new ParseContext();
            parser.parse(input, handler, metadata, parseCtx);
            input.close();

            // List all metadata codes.
            //String[] metadataNames = metadata.names();
            //for (String name : metadataNames)
            //{
            //    System.out.println(name + ": " + metadata.get(name));
            //}
            artist = metadata.get("xmpDM:artist");
            title = metadata.get("title");
            album = metadata.get("xmpDM:album");
            year = Integer.parseInt(metadata.get("xmpDM:releaseDate"));
            genre = metadata.get("xmpDM:genre");
            duration = Double.parseDouble(metadata.get("xmpDM:duration"));
        }
        catch (FileNotFoundException e)
        {
            throw new RuntimeException(e.getMessage(), e.getCause());
        }
        catch (IOException e)
        {
            throw new RuntimeException(e.getMessage(), e.getCause());
        }
        catch (SAXException e)
        {
            throw new RuntimeException(e.getMessage(), e.getCause());
        }
        catch (TikaException e)
        {
            throw new RuntimeException(e.getMessage(), e.getCause());
        }
        catch (NumberFormatException e)
        {
            throw new RuntimeException(e.getMessage(), e.getCause());
        }
    }
}
