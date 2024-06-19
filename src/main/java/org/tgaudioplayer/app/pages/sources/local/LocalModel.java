package org.tgaudioplayer.app.pages.sources.local;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.tgaudioplayer.app.data.Data;
import org.tgaudioplayer.app.data.models.Track;

public class LocalModel {
    LocalView localView;
    Data database;
    List<File> directories = new ArrayList<File>();

    public LocalModel(LocalView localView, Data database) {
        this.localView = localView;
        this.database = database;
        this.init();
    }

    /**
     * Sets the list of active folders
     * according to tracks stored in the database
     */
    private void init() {
        List<Track> tracks = this.database.getLocalTracks();
        if (tracks != null && tracks.size() > 0) {
            tracks.forEach(t -> {
                File file = new File(t.path);
                this.addDirectory(file.getParentFile());
            });
        } else {
            this.localView.displayPlaceholder();
        }
    }

    /**
     * Read metadata of all audio files in a directory
     * and store them in the local database
     * @param directory the directory to scan
     */
    private void scanDirectory(File directory) {
        File[] files = directory.listFiles();
        if (files != null) {
            for (int i=0; i < files.length; i++) {
                if (files[i].getName().endsWith(".mp3")) {
                    // System.out.println("Reading " + files[i].getAbsolutePath());
                    Track data = Track.readFromFile(files[i]);
                    if (data != null && !this.database.contains(data)) {
                        this.database.insertTrack(data);
                    }
                }
            }
        }
    }

    /**
     * Adds a directory to the list of directories to scan
     */
    public void addDirectory(File directory) {
        if (!this.directories.contains(directory)) {
            // Add folder to the list
            // that will appear in the sources tab
            localView.addDirectory(directory);
            // Scan the newly added directory
            this.scanDirectory(directory);
            this.directories.add(directory);
        }
    }

    /** Removes a directory */
    public void removeDirectory(File directory) {
        // Creating a copy of the local cache because
        // it will later remove tracks from it,
        // causing java.util.ConcurrentModificationException
        List<Track> tracks = new ArrayList<>(this.database.getLocalTracks());
        tracks.stream().filter(t ->
            // Taking all the tracks belonging to the given dir
            t.path.contains(directory.getAbsolutePath())
        ).forEach(t -> this.database.removeTrack(t.path));
        
        this.directories.remove(directory);
        localView.removeDirectory(directory);
    }
};
