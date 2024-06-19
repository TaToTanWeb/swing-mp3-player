package org.tgaudioplayer.app.utils;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import org.tgaudioplayer.app.App;


/**
 * Set of utilities to work with images in Swing.
 */
public class Images {
    /**
     * Keeping references to some of the most used images
     * and icons to avoid loading them multiple times
     */
    public static ImageIcon play75 = Images.create("play.gif", 75, 75);
    public static ImageIcon pause75 = Images.create("pause.gif", 75, 75);
    public static ImageIcon placeholder100 = Images.create("placeholder.gif", 100, 100);
    public static ImageIcon placeholder290 = Images.create("placeholder.gif", 290, 290);
    public static ImageIcon note18 = Images.create("note.gif");
    public static BufferedImage trayIcon = Images.getBufferedImage("tray.svg");
    public static ImageIcon play18 = Images.create("play.gif");
    public static ImageIcon playBlack18 = Images.create("play-black.gif");
    public static ImageIcon folder18 = Images.create("folder.gif");
    public static Image defaultBackground = Images.getBufferedImage("background.gif").getScaledInstance(Constants.height, Constants.height, Image.SCALE_FAST);

    /** Returns an ImageIcon, or null if the path was invalid. */
    public static ImageIcon create(String path, int width, int height) {
        BufferedImage image = Images.getBufferedImage(path);
        if (image != null) {
            return new ImageIcon(image.getScaledInstance(width, height, Image.SCALE_SMOOTH));
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }

    /**
     * Shorthand for creating an ImageIcon of 18x18
     * @param path
     */
    public static ImageIcon create(String path) {
        return Images.create(path, 18, 18);
    }

    /**
     * Reads an image from a given file path
     */
    public static BufferedImage getBufferedImage(String path) {
        URL url = App.class.getResource("assets/images/" + path);
        try {
            return ImageIO.read(url);
        } catch(IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
