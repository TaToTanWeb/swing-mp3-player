package org.tgaudioplayer.app.pages.library.tracks;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import org.tgaudioplayer.app.data.models.Track;
import org.tgaudioplayer.app.utils.Constants;
import org.tgaudioplayer.app.utils.Images;


public class TrackView extends JPanel implements ActionListener {
    TracklistModel parent;
    Track data;

    public TrackView(TracklistModel parent, Track track) {
        super(false);
        this.data = track;
        this.parent = parent;
        this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        this.setBorder(new EmptyBorder(0, 20, 0, 0));
        this.setMaximumSize(new Dimension(Constants.width, 50));
        this.setAlignmentX(Component.LEFT_ALIGNMENT);
        this.setBackground(Constants.backgroundColor);
        
        this.createPlayButton();
        JPanel info = this.createInfoPanel();
        Dimension textSize = new Dimension(Constants.width / 8 * 5, Constants.height);
        this.createTitleLabel(
            info, track.title.length() > 30 ?
                track.title.substring(0, 30) + ".." :
                track.title, textSize
        );

        this.createArtistLabel(
            info, track.artist.length() > 32 ?
                track.artist.substring(0, 32) + ".." :
                track.artist, textSize
        );
        this.add(info);
    }

    private void createPlayButton() {
        JButton playButton = new JButton(Images.play18);
        playButton.addActionListener(this);
        playButton.setContentAreaFilled(false);
        playButton.setBorder(null);
        playButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        this.add(playButton);
    }

    private JPanel createInfoPanel() {
        JPanel info = new JPanel();
        info.setLayout(new BoxLayout(info, BoxLayout.Y_AXIS));
        // info.setAlignmentX(Component.LEFT_ALIGNMENT);
        info.setBorder(new EmptyBorder(5, 20, 5, 40));
        info.setBackground(Constants.backgroundColor);
        return info;
    }

    private void createTitleLabel(JPanel parent, String name, Dimension size) {
        JLabel title = new JLabel(name);
        title.setMaximumSize(size);
        title.setForeground(Color.WHITE);
        parent.add(title);
    }

    private void createArtistLabel(JPanel parent, String name, Dimension size) {
        JLabel artist = new JLabel(name);
        artist.setForeground(Color.GRAY);
        artist.setMaximumSize(size);
        parent.add(artist);
    }

    public Track getData() {
        return data;
    }

    /**
     * Fired when the "play" button is clicked.
     * Starts playing the song.
     */
    public void actionPerformed(ActionEvent e) {
        this.parent.startPlayback(this.data);
    }
}
