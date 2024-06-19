package org.tgaudioplayer.app.pages.player.model;

import java.util.List;

import org.tgaudioplayer.app.data.models.Track;
import org.tgaudioplayer.app.pages.player.AudioPlayer;
import org.tgaudioplayer.app.pages.player.PlayerView;
import org.tgaudioplayer.app.pages.player.QueueManager;


/**
 * Represents the PlayerModel when
 * the queue is not empty.
 */
public class PlayerModelActive implements PlayerModelState {
    PlayerView view;
    QueueManager queueManager;
    AudioPlayer audio;

    PlayerModelActive(PlayerView view) {
        this.audio = new AudioPlayer(this);
        this.queueManager = new QueueManager(this.audio);
        this.view = view;
    }

    /**
     * Play or resume the current song.
    */
    public PlayerModelState playpause() {
        if (this.audio.isPaused()) {
            this.audio.play();
        } else {
            this.audio.pause();
        }
        return this;
    }

    /**
     * Skip to the next track.
     */
    public PlayerModelState next(boolean onUserClick) {
        boolean isPlaying = this.queueManager.skipForward(onUserClick);
        if (isPlaying) {
            this.display();
            return this;
        }

        // the queue has ended.
        if (onUserClick)
            this.audio.stop();
        return new PlayerModelInactive(this.view);
    }

    /**
     * Go to the previous track.
     */
    public PlayerModelState previous() {
        this.queueManager.skipBackward();
        this.display();
        return this;
    }

    /**
     * Called when the player stops.
     */
    public PlayerModelState onStop() {
        // It probably means the song has ended
        // (or at least we hope so)
        return this.next(false);
    }

    /**
     * Called when the player is paused.
     */
    public PlayerModelState onPause() {
        this.view.onPause();
        return this;
    }

    /**
     * Called when the player is resumed.
     */
    public PlayerModelState onResume() {
        this.view.onResume();
        return this;
    }

    /**
     * Makes info on the currently playing
     * song appear in the player tab.
     */
    private void display() {
        Track current = this.queueManager.getCurrent();
        if (current != null) {
            this.view.display(current);
        }
    }

    /** Stops the player. */
    private void stop() {
        if (!this.audio.isStopped()) {
            this.audio.lock();
            this.audio.stop();
            this.audio.unlock();
        }
    }

    /**
     * Start playing a list of tracks,
     * starting from the one at the given index
     * @param tracks the list of tracks to play
     * @param index the index of the first track to be played
     * @return a boolean saying whether files could be found
     */
    public PlayerModelState start(List<Track> tracks, int index) {
        this.stop(); // needs to be done for some reason
        tracks = this.queueManager.setQueue(tracks, index);
        // In case files are missing, the queue manager
        // updates the list of tracks, so checking it again
        if (tracks.size() > index) {
            // Starting the player
            this.audio.play();
            // Displaying basic info on the first track
            this.view.display(tracks.get(index));
            return this;
        }

        return new PlayerModelInactive(this.view);
    }
}
