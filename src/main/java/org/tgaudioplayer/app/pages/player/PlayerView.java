package org.tgaudioplayer.app.pages.player;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import org.tgaudioplayer.app.data.models.Track;
import org.tgaudioplayer.app.utils.Constants;
import org.tgaudioplayer.app.utils.Images;
import org.tgaudioplayer.app.lib.swingx.DropShadowBorder;
import org.tgaudioplayer.app.pages.Tab;

import com.mpatric.mp3agic.ID3Wrapper;


/**
 * UI Component for viewing informations
 * on the currently playing song and for
 * controlling playback.
 */
public class PlayerView extends JPanel implements ActionListener, Tab {
    public static String tip = "Control music playback.";
    PlayerController controller;
    BlurredBackground background;
    // when set to true, the view will
    // display a simple color as background
    boolean backgroundLock;
    JLabel artwork;
    JLabel title;
    JLabel artist;
    JPanel buttons;
    JButton previous;
    JButton play;
    JButton next;

    public PlayerView(PlayerController controller) {
        super(false);
        this.controller = controller;

        // Defining layout
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setBorder(new EmptyBorder(10, 10, 10, 10));
        this.background = new BlurredBackground(this);
        this.setBackground(Constants.backgroundColor);
        // Creating components
        this.createArtworkLabel();
        this.createTitleLabel();
        this.createArtistLabel();
        this.buttons = this.createButtonsPanel();
        this.createPreviousButton(this.buttons);
        this.createPlayButton(this.buttons);
        this.createNextButton(this.buttons);
        this.add(this.buttons);
    }

    @Override
    public String getTitle() {
        return "Player";
    }

    @Override
    public Icon getIcon() {
        return Images.playBlack18;
    }

    @Override
    public String getTip() {
        return "Control the audio playback.";
    }

    @Override
    public JPanel getPanel() {
        return this;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Draw the previously loaded image to Component.
        if (this.background.image != null) { //  && !this.backgroundLock
            g.drawImage(this.background.image, -Constants.height/2 + Constants.width/2, -10, null);
            int brightness = (int)(256 - 256 * 0.25f);
            g.setColor(new Color(0,0,0, brightness));
            g.fillRect(0, 0, Constants.width, Constants.height);
        } else {
            // Could not display a default background here.
        }
    }

    /** Displays the album artwork. */
    private void createArtworkLabel() {
        this.artwork = new JLabel(Images.placeholder290);
        this.artwork.setAlignmentX(Component.CENTER_ALIGNMENT);
        DropShadowBorder shadow = new DropShadowBorder();
        shadow.setShadowColor(Color.BLACK);
        shadow.setShowLeftShadow(true);
        shadow.setShowRightShadow(true);
        shadow.setShowBottomShadow(true);
        shadow.setShowTopShadow(false);
        shadow.setShadowOpacity(0.5f);
        shadow.setShadowSize(10);
        shadow.setShadowColor(Color.GRAY);
        this.artwork.setBorder(shadow);
        this.add(this.artwork);
    }

    /** Displays the song title. */
    private void createTitleLabel() {
        this.title = new JLabel("Track title");
        this.title.setBorder(new EmptyBorder(10, 20, 0, 20));
        this.title.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 22));
        this.title.setAlignmentX(Component.CENTER_ALIGNMENT);
        this.title.setForeground(Color.WHITE);
        this.add(this.title);
    }

    /** Displays the artist name. */
    private void createArtistLabel() {
        this.artist = new JLabel("Unknown artist");
        this.artist.setForeground(Color.GRAY);
        this.artist.setAlignmentX(Component.CENTER_ALIGNMENT);
        this.add(this.artist);
    }

    /** Will include buttons for controlling playback. */
    private JPanel createButtonsPanel() {
        JPanel buttons = new JPanel();
        buttons.setAlignmentX(Component.CENTER_ALIGNMENT);
        buttons.setLayout(new BoxLayout(buttons, BoxLayout.X_AXIS));
        buttons.setOpaque(false);
        buttons.setVisible(false);
        return buttons;
    }

    /** Generic button creator */
    private JButton createButton(String path, JPanel parent) {
        JButton button = new JButton(Images.create(path + ".gif", 75, 75));
        button.setContentAreaFilled(false);
        button.setBorder(null);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.addActionListener(this);
        parent.add(button);
        return button;
    }

    /** Button to skip to the previous song. */
    private void createPreviousButton(JPanel parent) {
        this.previous = this.createButton("previous", parent);
    }

    /** Button to pause or resume music. */
    private void createPlayButton(JPanel parent) {
        this.play = this.createButton("play", parent);
    }

    /** Button to skip to the next song. */
    private void createNextButton(JPanel parent) {
        this.next = this.createButton("next", parent);
    }

    /** Called when any button is clicked. */
    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();
        if (src == this.play) {
            this.controller.playpause();
        } else if (src == this.next) {
            this.controller.next();
        } else if (src == this.previous) {
            this.controller.previous();
        }
    }

    /**
     * Called when the song is paused.
     * Changes the icon from "play" to "pause"
     */
    public void onPause() {
        this.play.setIcon(Images.pause75);
    }

    /**
     * Called when the song is started or resumed.
     * Changes the icon from "pause" to "play"
     */
    public void onResume() {
        this.play.setIcon(Images.play75);
    }

    /**
     * Sets the album artwork and updates the background.
     * @param imageData byte array with image data
     */
    private void setArtwork(byte[] imageData) {
        try {
            this.backgroundLock = true;
            if (imageData != null) {
                BufferedImage artworkImage = ImageIO.read(new ByteArrayInputStream(imageData));
                this.background.send(artworkImage);
                this.artwork.setIcon(new ImageIcon(artworkImage.getScaledInstance(
                    290, 290,
                    Image.SCALE_SMOOTH
                )));
            } else {
                this.artwork.setIcon(Images.placeholder290);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Called when the background image is ready to display.
     * Removes the lock which prevents it from displaying.
     */
    public void removeBackgroundLock() {
        this.backgroundLock = false;
    }

    /**
     * Displays info on a song in the player tab.
     * @param track the track to show
     */
    public void display(Track track) {
        this.buttons.setVisible(true);
        this.title.setText(track.title.length() > 20 ? track.title.substring(0, 22) + ".." : track.title);
        this.artist.setText(track.artist.length() > 20 ? track.artist.substring(0, 22) + ".." : track.artist);
        ID3Wrapper tags = track.reread();
        byte[] imageData = tags.getAlbumImage();
        this.setArtwork(imageData);
    }

    /**
     * Reverts to the initial state of the view.
     */
    public void display() {
        this.buttons.setVisible(false);
        this.title.setText("Not playing");
        this.artist.setText("Choose a song from the library to start.");
        this.artwork.setIcon(Images.placeholder290);
        this.backgroundLock = true;
    }
}
