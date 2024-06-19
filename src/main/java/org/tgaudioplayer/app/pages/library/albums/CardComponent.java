package org.tgaudioplayer.app.pages.library.albums;

import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.SwingConstants;

import org.tgaudioplayer.app.data.models.Track;
import org.tgaudioplayer.app.utils.Images;


public class CardComponent extends JButton implements ActionListener {
    Track data;

    public CardComponent(Track track) {
        super(track.album.getTitle(), Images.placeholder100);

        this.data = track;
        // this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        // this.setBorder(new EmptyBorder(0, 10, 0, 0));
        this.setMargin(new Insets(0,0,0,0));
        this.addActionListener(this);
        // this.setAlignmentX(Component.LEFT_ALIGNMENT);
        this.setVerticalTextPosition(SwingConstants.BOTTOM);
        this.setHorizontalTextPosition(SwingConstants.CENTER);
        this.setPreferredSize(new Dimension(125, 125));
    }

    public void actionPerformed(ActionEvent e) {
        // List<Track> tracklist = Albums.getInstance().tracklist;
        // PlayerView.getInstance().start(tracklist, tracklist.indexOf(this.data));
    }
}
