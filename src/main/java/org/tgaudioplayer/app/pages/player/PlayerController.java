package org.tgaudioplayer.app.pages.player;

import java.util.List;

import javax.swing.JOptionPane;

import org.tgaudioplayer.app.data.models.Track;
import org.tgaudioplayer.app.pages.player.model.PlayerModelInactive;
import org.tgaudioplayer.app.pages.player.model.PlayerModelState;

/**
 * Interface for the player tab, to be used from outside.
 * Depends heavily on PlayerModel
 */
public class PlayerController {
    PlayerView view;
    PlayerModelState model;

    public PlayerController() {
        this.view = new PlayerView(this);
        this.model = new PlayerModelInactive(this.view);
    }

    public PlayerView getView() {
        return this.view;
    }

    void playpause() {
        this.model = this.model.playpause();
    }

    void previous() {
        this.model = this.model.previous();
    }

    void next() {
        this.model = this.model.next(true);
    }

    public boolean isActive() {
        return this.model.getClass() != PlayerModelInactive.class;
    }

    /**
     * Start playing a list of tracks,
     * starting from the one at the given index
     * @param tracks the list of tracks to play
     * @param index the index of the first track to be played
     */
    public void start(List<Track> tracks, int index) {
        this.model = this.model.start(tracks, index);
        if (!this.isActive()) {
            //  Player is still inactive after start().
            JOptionPane.showMessageDialog(
                this.view,
                "The requested files have been moved or deleted.",
                "Files not found.",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }
}
