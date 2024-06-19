package org.tgaudioplayer.app.pages.player;

import java.util.ArrayList;
import java.util.List;

import org.tgaudioplayer.app.data.Data;
import org.tgaudioplayer.app.data.models.Track;

import java.io.File;

/** 
 * Manages the queue of tracks
 * to play after the current one.
 */
public class QueueManager {
    AudioPlayer audioPlayer;
    // Keeping track of the list of songs
    // and the index pointing to the currently
    // playing song on it.
    List<Track> tracks = new ArrayList<>();
    Data database = Data.getInstance();
    boolean repeat = false; // repeat the queue on end
    int index = -1;

    public QueueManager(AudioPlayer audioPlayer) {
        this.audioPlayer = audioPlayer;
    }

    private boolean addTrack(Track track, boolean enqueue) {
        File file = new File(track.path);
        if (file.exists() && !file.isDirectory()) {
            this.tracks.add(track);
            if (enqueue)
                this.audioPlayer.addToPlayList(file);
            return true;
        } 

        // Invalid file! Removing from database
        this.database.removeTrack(track.path);
        return false;
    }

    /**
     * Clears the queue and replaces it with the given one
     * @tracks list of tracks to store
     * @index where to start playing from
     */
    public List<Track> setQueue(List<Track> tracks, int index) {
        this.index = index;
        this.tracks.clear();
        this.audioPlayer.getPlayList().clear();
        for (int i = 0; i < tracks.size(); i++) {
            boolean result = this.addTrack(tracks.get(i), i >= index);
            // Adjusting index in case a track before
            // it has been removed from the queue
            if (!result && i < this.index) {
                this.index--;
            }
        }
        return tracks;
    }

    /**
     * Increments the counter to the next song.
     */
    private boolean increment(boolean force) {
        if (this.tracks != null) {
            if (this.index + 1 < this.tracks.size()) {
                this.index += 1;
            } else {
                if (this.repeat || force) {
                    // Repeat mode is active or user pressed "next"
                    this.index = 0;
                } else {
                    // Queue has ended and repeat mode isn't active
                    return false;
                }
            }
            return true;
        } else {
            System.out.println("Warning: tried to increment QueueManager counter with no active queue.");
            return false;
        }
    }

    /**
     * Decrements the counter to the previous song.
     */
    private void decrement() {
        if (this.index > 0) {
            this.index -= 1;
        } else {
            this.index = 0;
        }
    }

    /**
     * Clears the player's internal queue
     * and replaces it with the one stored here
     * starting from the currently playing track
     */
    private void playCurrent() {
        if (this.audioPlayer.isPaused()) {
            this.audioPlayer.play();
        }
        
        this.audioPlayer.getPlayList().clear();
        this.audioPlayer.addToPlayList(new File(tracks.get(this.index).path));
        // starting playback again
        this.audioPlayer.play();
    }

    /**
     * Plays the previous song in queue.
     */
    public void skipBackward() {
        // Preventing the AudioPlayer's thread
        // to detect the end of the song
        this.audioPlayer.lock();
        this.decrement();
        this.playCurrent();
        this.audioPlayer.unlock();
    }

    /**
     * Plays the next song in queue.
     */
    public boolean skipForward(boolean force) {
        this.audioPlayer.lock();
        boolean success = this.increment(force);
        if (success) {
            this.playCurrent();
        }
        this.audioPlayer.unlock();
        return success;
    }

    /**
     * Returns the currently playing track.
     */
    public Track getCurrent() {
        if (this.tracks != null) {
            return this.tracks.get(this.index);
        } else {
            System.out.println("Warning: tried to retrieve QueueManager's current track with no active queue.");
            return null;
        }
    }
}
