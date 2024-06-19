package org.tgaudioplayer.app.pages.library.tracks;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Flow.Subscriber;
import java.util.concurrent.Flow.Subscription;
import java.util.concurrent.SubmissionPublisher;

import org.tgaudioplayer.app.data.Data;
import org.tgaudioplayer.app.data.models.Track;
import org.tgaudioplayer.app.pages.player.PlayerController;

/** Implements the main functionalities of the Tracklist component. */
public class TracklistModel implements Subscriber<TracklistUpdate> {
    List<Track> tracklist = new ArrayList<Track>();
    Data database = Data.getInstance();
    // The player model needed to start music playback
    PlayerController player;
    // The subscription to the database
    private Subscription subscription;
    // Instance of SubmissionPublisher
    private SubmissionPublisher<TracklistUpdate> publisher = new SubmissionPublisher<>();

    TracklistModel(PlayerController player) {
        this.database.attach(this);
        this.player = player;
    }

    /** Sends updates to the view. */
    public void attach(Subscriber<TracklistUpdate> t) {
        this.publisher.subscribe(t);
    }

    /** Starts sending updates. */
    public void init() {
        // Taking the list of tracks on the database
        // and adding them to the view.
        List<Track> localTracks = this.database.getLocalTracks();
        if (localTracks.size() > 0) {
            for(Track track : localTracks) {
                this.addTrack(track);
            }
        }
    }

    /**
     * Returns true if the track is in this list.
     */
    public boolean contains(Track t) {
        for(Track track : this.tracklist) {
            if (track.equals(t))
                return true;
        }
        return false;
    }

    /**
     * Adds a track to this list
     * @param t the track to add
     */
    private void addTrack(Track t) {
        System.out.println("[TracklistModel] Submitting " + t.path);
        this.tracklist.add(t);
        this.publisher.submit(new TracklistUpdate(TracklistUpdate.ADD_TRACK, t));
    }

    /**
     * Removes a track from the list
     * @param t the track to remove
     */
    private void removeTrack(TracklistUpdate t) {
        System.out.println("[TracklistModel] Removing " + t.path);
        Iterator<Track> ti = tracklist.iterator();
        while (ti.hasNext()) {
            if (ti.next().path == t.path) {
                ti.remove();
            }
        }

        this.publisher.submit(t);
    }

    /**
     * Starts playing a song from this list
     * @param t the track to start with
     */
    public void startPlayback(Track t) {
        List<Track> tracklist = this.tracklist;
        this.player.start(tracklist, tracklist.indexOf(t));
    }


    @Override
    public void onSubscribe(Subscription s) {
        subscription = s;
        s.request(1);
    }

    @Override
    public void onNext(TracklistUpdate t) {
        System.out.println("[TracklistModel] Received item from the database.");
        if (t.update_type == TracklistUpdate.ADD_TRACK) {
            this.addTrack(t.track);
        } else if (t.update_type == TracklistUpdate.REMOVE_TRACK) {
            this.removeTrack(t);
        }
        subscription.request(1);
    }

    @Override
    public void onError(Throwable throwable) {
        throwable.printStackTrace();
    }

    @Override
    public void onComplete() {}
}
