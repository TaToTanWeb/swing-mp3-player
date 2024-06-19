package org.tgaudioplayer.app.pages.sources;

import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import org.tgaudioplayer.app.pages.Tab;
import org.tgaudioplayer.app.pages.sources.local.LocalController;
import org.tgaudioplayer.app.utils.Constants;
import org.tgaudioplayer.app.utils.Images;

/**
 * Sources tab – lets the user define
 * where to get the music from.
*/
public class Sources extends JPanel implements Tab {
    public Sources() {
        super(false);
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setBorder(new EmptyBorder(10, 10, 10, 10));
        this.setBackground(Constants.backgroundColor);

        LocalController localController = new LocalController();
        this.add(localController.getView());
    }

    @Override
    public String getTitle() {
        return "Sources";
    }

    @Override
    public Icon getIcon() {
        return Images.folder18;
    }

    @Override
    public String getTip() {
        return "Where to get audio files.";
    }

    @Override
    public JPanel getPanel() {
        return this;
    }
}
