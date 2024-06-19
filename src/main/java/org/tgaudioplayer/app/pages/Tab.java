package org.tgaudioplayer.app.pages;

import javax.swing.Icon;
import javax.swing.JPanel;


/** Interface for all children of JTabbedPane. */
public interface Tab {
    public String getTitle();
    public Icon getIcon();
    public String getTip();
    public JPanel getPanel();
};
