package org.tgaudioplayer.app.pages.library.tracks;

import org.tgaudioplayer.app.data.models.Track;

/** Represents an update sent to the TracklistView. */
public class TracklistUpdate {
    public final static int REMOVE_TRACK = 0;
    public final static int ADD_TRACK = 1;
    int update_type;
    Track track = null;
    String path = null;

    public TracklistUpdate(int update_type, Track track) {
        if (update_type != TracklistUpdate.ADD_TRACK) {
            System.err.println("[TracklistUpdate] unknown or invalid update type.");
            update_type = TracklistUpdate.ADD_TRACK;
        }

        this.update_type = update_type;
        this.track = track;
    }

    public TracklistUpdate(int update_type, String path) {
        if (update_type != TracklistUpdate.REMOVE_TRACK) {
            System.err.println("[TracklistUpdate] unknown or invalid update type.");
            update_type = TracklistUpdate.REMOVE_TRACK;
        }

        this.update_type = update_type;
        this.path = path;
    }
};
