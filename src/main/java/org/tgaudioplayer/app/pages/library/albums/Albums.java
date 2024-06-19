package org.tgaudioplayer.app.pages.library.albums;

import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import org.tgaudioplayer.app.data.Data;
import org.tgaudioplayer.app.data.models.Track;
import org.tgaudioplayer.app.lib.WrapLayout;
import org.tgaudioplayer.app.utils.Constants;
import org.tgaudioplayer.app.utils.Images;


public class Albums extends JPanel {
    static Albums albums = new Albums();
    List<Track> tracklist = new ArrayList<Track>();
    Data database = Data.getInstance();
    String title = "Albums";
    ImageIcon icon;
    String tip = "Albums added to the program.";

    private Albums() {
        super(false);
        this.icon = Images.create("note.gif");
        this.setLayout(new WrapLayout());
        this.setSize(Constants.width, Constants.height);
        this.setBorder(new EmptyBorder(10, 10, 10, 0));
        // Library.getInstance().setup(this);
        // will become tabbedPane.addTab(this.title, this.icon, this, this.tip)
        
        this.database.getLocalTracks().forEach(track -> {
            if (!this.exists(track)) {
                this.add(new CardComponent(track));
                this.tracklist.add(track);
            }
        });
    }

    static public Albums getInstance() {
        return albums;
    }

    boolean exists(Track track) {
        return this.tracklist.stream().filter(t ->
            t.artist.equals(track.artist)
            && t.album.equals(track.album)
        ).count() > 0;
    }
}
