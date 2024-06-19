package org.tgaudioplayer.app.data.models;

import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import com.mpatric.mp3agic.ID3Wrapper;
import java.io.ByteArrayInputStream;
import java.io.IOException;

/**
 * Represents an album.
 */
public class Album {
    String title;
    BufferedImage artwork = null;

    public Album(String title) {
        this.title = title != null ? title : "Unknown album";
    }

    /**
     * Sets the artwork attribute
     * @param tags ID3 tags of the audio file
     */
    public void setArtwork(ID3Wrapper tags) {
        try {
            byte[] imageData = tags.getAlbumImage();
            ByteArrayInputStream stream = new ByteArrayInputStream(imageData);
            this.artwork = ImageIO.read(stream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public BufferedImage getArtwork() {
        return this.artwork;
    }

    public String getTitle() {
        return this.title;
    }
};
