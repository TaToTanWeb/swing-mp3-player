package org.tgaudioplayer.app.pages.player.model;

import java.util.List;

import org.tgaudioplayer.app.data.models.Track;
import org.tgaudioplayer.app.pages.player.PlayerView;

/**
 * Represents the Player Model
 * when no song is being played.
 */
public class PlayerModelInactive implements PlayerModelState {
    PlayerView view;

    public PlayerModelInactive(PlayerView view) {
        this.view = view;
        this.view.display();
    }

    public PlayerModelState playpause() {
        return this;
    }
    
    public PlayerModelState next(boolean onUserClick) {
        return this;
    }
    
    public PlayerModelState previous() {
        return this;
    }
    
    public PlayerModelState onStop() {
        return this;
    }
    
    public PlayerModelState onPause() {
        return this;
    }
    
    public PlayerModelState onResume() {
        return this;
    }

    /**
     * Start playing a list of tracks,
     * starting from the one at the given index
     * @param tracks the list of tracks to play
     * @param index the index of the first track to be played
     * @return a boolean saying whether files could be found
     */
    public PlayerModelState start(List<Track> tracks, int index) {
        return new PlayerModelActive(this.view).start(tracks, index);
    }
}
