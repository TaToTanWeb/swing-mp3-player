package org.tgaudioplayer.app.pages.player;
import org.tgaudioplayer.app.pages.player.model.PlayerModelActive;
import org.tgaudioplayer.app.utils.Constants;

import jaco.mp3.player.MP3Player;
import jaco.mp3.player.N;

/**
 * Wrapper for the jaco.mp3.player.MP3Player class.
 * Will listen for events coming from it and redirect
 * them to PlayerModel.
 */
public class AudioPlayer extends MP3Player implements N {
    PlayerModelActive playerModel;
    boolean _lock = false;

    public AudioPlayer(PlayerModelActive playerModel) {
        this.playerModel = playerModel;
        this.addMP3PlayerListener(this);
        // The thread will be used to detect
        // the end of the current playing track
        Thread thread = new Thread(this::run);
        thread.start();
    }

    /**
     * Fired when the music is paused.
     */
    public void a() {
        this.playerModel.onPause();
    }

    /**
     * Fired when the music starts playing or is resumed.
     */
    public void b() {
        this.playerModel.onResume();
    }

    /**
     * Prevents from detecting false onStop events.
     */
    public void lock() {
        this._lock = true;
    }

    /**
     * Re-enables detection of onStop events.
     */
    public void unlock() {
        this._lock = false;
    }

    /**
     * Thread to check when the music has ended.
     */
    public void run() {
        boolean previous = true;
        while (true) {
            if (this.isStopped()) {
                if (!previous && !this._lock) {
                    this.playerModel.onStop();
                }
                previous = true;
            } else {
                previous = false;
            }

            try {
                Thread.sleep(Constants.playerStatusInterval);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
