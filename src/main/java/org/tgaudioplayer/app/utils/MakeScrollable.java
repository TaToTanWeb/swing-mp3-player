package org.tgaudioplayer.app.utils;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
// import java.awt.BorderLayout;

/**
 * Simple class for making a JPanel scollable vertically.
 */
public class MakeScrollable extends JScrollPane {
    public MakeScrollable(JPanel panel) {
        super(panel);
        this.getVerticalScrollBar().setUnitIncrement(16);
        this.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        // this.add(listScroller, BorderLayout.LINE_START);
    }
}
