package org.tgaudioplayer.app.data.models;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.tgaudioplayer.app.data.Data;

import com.mpatric.mp3agic.ID3Wrapper;
import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.Mp3File;
import com.mpatric.mp3agic.UnsupportedTagException;


/**
 * Represents a song in the local database.
*/
public class Track {
    public String path;
    public String title;
    public String artist;
    public Album album;

    public Track(String path, String title, String artist, String album) {
        this.path = path;
        this.title = title != null ? title : new File(path).getName();
        this.artist = artist != null ? artist : "Unknown artist";
        this.album = new Album(album);
    }

    /**
     * Reads ID3 tags written in an audio file.
     * @param file the file to read
     * @return an object containing metadata or null
     */
    private static ID3Wrapper readMetadata(File file) {
        String path = file.getAbsolutePath();
        try {
            Mp3File tags = new Mp3File(path);
            boolean hasTags = tags.hasId3v1Tag() || tags.hasId3v2Tag();
            return hasTags ? new ID3Wrapper(tags.getId3v1Tag(), tags.getId3v2Tag()) : null;
        } catch (FileNotFoundException e) {
            // File could not be found. Removing occurrence from the database
            Data.getInstance().removeTrack(path);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (UnsupportedTagException e) {
            e.printStackTrace();
        } catch (InvalidDataException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Creates a track object from a file by
     * reading ID3 tags (may return null).
     */
    public static Track readFromFile(File file) {
        ID3Wrapper metadata = Track.readMetadata(file);
        if (metadata != null) {
            // Retrieve the necessary info from metadata
            // Names - title, xmpDM:artist etc. - mentioned below may differ
            return new Track(
                file.getAbsolutePath(),
                metadata.getTitle(),
                metadata.getArtist(),
                metadata.getAlbum()
            );
        }

        return null;
    }

    /**
     * Reads metadata again, needed because
     * some informations such as album cover
     * are not stored in the database
     */
    public ID3Wrapper reread() {
        return Track.readMetadata(new File(this.path));
    }

    /**
     * Check whether two instances refer to the same song.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Track t = (Track) o;
        return this.path.equals(t.path);
    }
}