package org.tgaudioplayer.app.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Flow.Subscriber;
import java.util.concurrent.SubmissionPublisher;

import org.tgaudioplayer.app.data.models.Track;
import org.tgaudioplayer.app.pages.library.tracks.TracklistUpdate;



/**
 * Main class for interacting
 * with the SQLite local database.
 */
public class Data {
    // The relative path to the database
    final String dbpath = "jdbc:sqlite:sample.db";
    // The unique singleton instance
    static Data data = new Data();
    // In-memory cache of the database content
    List<Track> localTracksCache = null;
    // Instance of SubmissionPublisher
    private SubmissionPublisher<TracklistUpdate> publisher = new SubmissionPublisher<>();


    protected Data() {
        try (
            // Instantiating objects that need to be closed after use
            Connection connection = DriverManager.getConnection(this.dbpath);
            Statement statement = connection.createStatement();
        ) {
            statement.setQueryTimeout(30);
            statement.executeUpdate(Statements.INIT);
        } catch(SQLException e) {
            e.printStackTrace(System.err);
        }
    }

    /** Returns the only database instance. */
    public static Data getInstance() {
        return data;
    }

    /** Start to receive updates. */
    public void attach(Subscriber<TracklistUpdate> t) {
        this.publisher.subscribe(t);
    }

    private void execute(String command, List<String> params) {
        try (
            Connection connection = DriverManager.getConnection(this.dbpath);
            // A PreparedStatement will help to prevent SQL injections
            PreparedStatement statement = connection.prepareStatement(command);
        ) {
            statement.setQueryTimeout(30);
            statement.clearParameters();
            statement.clearBatch();
            for (int i=0; i<params.size(); i++) {
                statement.setString(i+1, params.get(i));
            }
            
            statement.executeUpdate();
            connection.close();
            statement.close();
        } catch(SQLException e) {
            e.printStackTrace(System.err);
        }
    }

    public void insertTrack(Track track) {
        /**
         * Stores data about an audio file.
         */
        List<String> params = Arrays.asList(track.path, track.title, track.artist, track.album.getTitle());
        this.execute(Statements.INSERT_TRACK, params);
        // Updating the in-memory cache
        this.localTracksCache.add(track);
        // Updating subscribers about the new track
        this.publisher.submit(new TracklistUpdate(TracklistUpdate.ADD_TRACK, track));
    }

    public void removeTrack(String path) {
        /**
         * Removes stored data about an audio file.
         */
        List<String> params = Arrays.asList(path);
        this.execute(Statements.REMOVE_TRACK, params);
        // Updating the local cache
        Iterator<Track> cacheIterator = this.localTracksCache.iterator();
        while (cacheIterator.hasNext()) {
            if (cacheIterator.next().path == path)
                cacheIterator.remove();
        }
        // Updating subscribers about the change
        this.publisher.submit(new TracklistUpdate(TracklistUpdate.REMOVE_TRACK, path));
    }

    public List<Track> getLocalTracks() {
        /**
         * Returns the list of audio files stored.
         * To avoid useless calls to the database,
         * the result is cached in a class variable
         */
        if (this.localTracksCache == null) {
            try (
                Connection connection = DriverManager.getConnection(this.dbpath);
                Statement statement = connection.createStatement();
            ) {
                statement.setQueryTimeout(30);
                ResultSet rs = statement.executeQuery("select * from localtrack");
                List<Track> results = new ArrayList<Track>();
                while (rs.next()) {
                    results.add(new Track(
                        rs.getString("path"),
                        rs.getString("title"),
                        rs.getString("artist"),
                        rs.getString("album")
                    ));
                }
                connection.close();
                statement.close();
                this.localTracksCache = results;
            } catch(SQLException e) {
                e.printStackTrace(System.err);
            }
        }

        return this.localTracksCache;
    }

    /** Returns true if a track exists */
    public boolean contains(Track data) {
        return this.localTracksCache.stream().filter(t -> t.path.equals(data.path)).count() > 0;
    }
}
