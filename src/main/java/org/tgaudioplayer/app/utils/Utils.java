package org.tgaudioplayer.app.utils;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

public class Utils {
    private Utils() {}

    /**
     * Encapsulates the given component into
     * a new JPanel and adds a border to it.
     */
    public static JPanel frame(JComponent component, Border border) {
        JPanel panel = new JPanel();
        panel.add(component);
        panel.setBorder(border);
        return panel;
    }

    /**
     * Shorthand for creating an EmptyBorder
     */
    public static EmptyBorder pad(int top, int left, int bottom, int right) {
        return new EmptyBorder(top, left, bottom, right);
    }
}
