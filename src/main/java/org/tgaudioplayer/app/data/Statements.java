package org.tgaudioplayer.app.data;

/**
 * Stores the most common operations
 * in the SQLite database.
 */
public class Statements {
    public static final String INIT = "create table if not exists localtrack (path string, title string, artist string, album string);";
    public static final String INSERT_TRACK = "insert into localtrack values (?, ?, ?, ?)";
    public static final String REMOVE_TRACK = "delete from localtrack where path = ?";
};
