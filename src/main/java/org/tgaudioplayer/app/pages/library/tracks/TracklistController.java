package org.tgaudioplayer.app.pages.library.tracks;

import org.tgaudioplayer.app.data.models.Track;
import org.tgaudioplayer.app.pages.player.PlayerController;

public class TracklistController {
    TracklistView view;
    TracklistModel model;

    public TracklistController(PlayerController player) {
        this.model = new TracklistModel(player);
        this.view = new TracklistView(model);
    }

    public boolean contains(Track t) {
        return this.model.contains(t);
    }

    public TracklistView getView() {
        return this.view;
    }
}
