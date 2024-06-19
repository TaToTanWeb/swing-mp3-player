package org.tgaudioplayer.app.pages.library;

import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import org.tgaudioplayer.app.App;
import org.tgaudioplayer.app.pages.Tab;
import org.tgaudioplayer.app.pages.TabsPanel;
import org.tgaudioplayer.app.pages.library.tracks.TracklistController;
import org.tgaudioplayer.app.pages.player.PlayerController;
import org.tgaudioplayer.app.utils.Images;

/**
 * Displays the user library, including
 * tracks, artists, albums and playlists.
 */
public class Library extends TabsPanel implements Tab {
    TracklistController controller;

    public Library(PlayerController player) {
        super();
        this.controller = new TracklistController(player);

        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        // this.addTab(tracklist.getView(), "Tracks");
        // this.setup(Albums.getInstance(), "Album");
        this.addTab(controller.getView());
    }

    @Override
    public String getTitle() {
        return "Library";
    }

    @Override
    public Icon getIcon() {
        return Images.note18;
    }

    @Override
    public String getTip() {
        return "View your music library.";
    }

    @Override
    public JPanel getPanel() {
        return this;
    }

    public TracklistController getController() {
        return this.controller;
    }
}