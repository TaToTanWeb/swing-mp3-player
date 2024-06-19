package org.tgaudioplayer.app.pages.library.tracks;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.util.concurrent.Flow.Subscriber;
import java.util.concurrent.Flow.Subscription;

import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

import org.tgaudioplayer.app.data.models.Track;
import org.tgaudioplayer.app.pages.Tab;
import org.tgaudioplayer.app.utils.Constants;
import org.tgaudioplayer.app.utils.Images;


/**
 * UI component that displays a list of tracks.
 */
public class TracklistView extends JPanel implements Subscriber<TracklistUpdate>, Tab {
    private Subscription subscription;
    TracklistModel model;
    JTextArea placeholder;

    public TracklistView(TracklistModel model) {
        super(false);
        this.model = model;
        this.model.attach(this);
        
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setBorder(new EmptyBorder(10, 0, 10, 0));
        this.setSize(Constants.width, Constants.height);
        this.setBackground(Constants.backgroundColor);

        this.createLabel();
        this.model.init();
    }

    @Override
    public String getTitle() {
        return "Tracks";
    }

    @Override
    public Icon getIcon() {
        return Images.note18;
    }

    @Override
    public String getTip() {
        return "List your tracks.";
    }

    @Override
    public JPanel getPanel() {
        return this;
    }

    private void createLabel() {
        this.placeholder = new JTextArea("You haven't added any track yet. Head over to the \"Sources\" tab to get started.");
        this.placeholder.setForeground(Color.WHITE);
        this.placeholder.setLineWrap(true);
        this.placeholder.setWrapStyleWord(false);
        this.placeholder.setEditable(false);
        this.placeholder.setBackground(Constants.backgroundColor);
        this.placeholder.setFocusable(false);
        this.placeholder.setBorder(new EmptyBorder(20, 20, 20, 20));
        this.placeholder.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 14));
        this.add(this.placeholder);
    }

    /** Adds a track to the UI */
    private void addTrack(Track t) {
        // Hiding placeholder if it is still showing up
        if (this.placeholder.isVisible())
            this.placeholder.setVisible(false);
        // Adding track to the UI
        this.add(new TrackView(this.model, t));
        // Requesting another update
        subscription.request(1);
    }

    /** Removes a track from the UI */
    private void removeTrack(String path) {
        // isEmpty will be used to know whether to display
        // the placeholder text after removing the requested track.
        boolean isEmpty = true;
        // Iterating over child components
        Component[] children = this.getComponents();
        for (int i=0; i < children.length; i++) {
            if (children[i].getClass() == TrackView.class) {
                // Safely cast to TrackView
                TrackView view = (TrackView) children[i];
                if (view.getData().path == path) {
                    // The path matches, remove it.
                    this.remove(view);
                } else {
                    // There is a track we don't have to remove.
                    isEmpty = false;
                }
            }
        };

        // Hiding placeholder if it is still showing up
        if (isEmpty)
            this.placeholder.setVisible(true);
        // Requesting another update
        subscription.request(1);
    }

    @Override
    public void onSubscribe(Subscription s) {
        subscription = s;
        s.request(1);
    }

    @Override
    public void onNext(TracklistUpdate tu) {
        System.out.println("[TracklistView] Received item from tracklistmodel.");
        if (tu.update_type == TracklistUpdate.ADD_TRACK) {
            this.addTrack(tu.track);
        } else {
            this.removeTrack(tu.path);
        }
    }

    @Override
    public void onError(Throwable throwable) {
        throwable.printStackTrace();
    }

    @Override
    public void onComplete() {}
}
