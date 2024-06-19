package org.tgaudioplayer.app.pages;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import org.tgaudioplayer.app.utils.MakeScrollable;

public abstract class TabsPanel extends JPanel {
    JTabbedPane tabbedPane;

    public TabsPanel() {
        super(false);
        this.tabbedPane = new JTabbedPane();
        // Add the tabbed pane to this panel.
        this.add(tabbedPane);
        // The following line enables to use scrolling tabs.
        tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
    }

    public void addTab(Tab tab) {
        this.tabbedPane.addTab(
            tab.getTitle(),
            tab.getIcon(),
            new MakeScrollable(tab.getPanel()),
            tab.getTip()
        );
    }
}
