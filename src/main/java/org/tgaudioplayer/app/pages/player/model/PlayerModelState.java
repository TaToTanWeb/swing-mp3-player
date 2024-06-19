package org.tgaudioplayer.app.pages.player.model;

import java.util.List;

import org.tgaudioplayer.app.data.models.Track;


/**
 * Implements the internal logic of the actual music player.
 * The possible states are
 *   • inactive: at startup or when the queue ends.
 *   • active: whenever music is played or is paused.
 */
public interface PlayerModelState {
    /**
     * Play or resume the current song.
    */
    public PlayerModelState playpause();

    /**
     * Skip to the next track.
     */
    public PlayerModelState next(boolean onUserClick);

    /**
     * Go to the previous track.
     */
    public PlayerModelState previous();

    /**
     * Called when the player stops.
     */
    public PlayerModelState onStop();

    /**
     * Called when the player is paused.
     */
    public PlayerModelState onPause();

    /**
     * Called when the player is resumed.
     */
    public PlayerModelState onResume();

    /**
     * Start playing a list of tracks,
     * starting from the one at the given index
     * @param tracks the list of tracks to play
     * @param index the index of the first track to be played
     * @return a boolean saying whether files could be found
     */
    public PlayerModelState start(List<Track> tracks, int index);
}
