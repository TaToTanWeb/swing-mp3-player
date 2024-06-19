package org.tgaudioplayer.app;
import javax.swing.*;

import org.tgaudioplayer.app.pages.TabsPanel;
import org.tgaudioplayer.app.pages.library.Library;
import org.tgaudioplayer.app.pages.player.PlayerController;
import org.tgaudioplayer.app.pages.sources.Sources;
import org.tgaudioplayer.app.utils.Constants;
import org.tgaudioplayer.app.utils.Images;

import java.awt.*;
import java.util.List;

/**
 * App starting point.
 * Holds its only instance and cannot be instantiated outside.
 */
public class App extends TabsPanel {
    static App app;

    private App() {
        super();
        this.setLayout(new GridLayout(1, 1));

        // tabbedPane.setMnemonicAt(0, KeyEvent.VK_1);
        // Add the tabbed pane to this panel.
        PlayerController player = new PlayerController();
        Library library = new Library(player);
        Sources sources = new Sources();
        this.addTab(library);
        this.addTab(sources);
        this.addTab(player.getView());
    }
    
    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from
     * the event dispatch thread.
     */
    private static void createAndShowGUI() {
        // Create and set up the window.
        JFrame frame = new JFrame("Music Player");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // Add content to the window.
        app = new App();
        frame.add(app, BorderLayout.CENTER);
        
        // Display the window.
        frame.setSize(Constants.width, Constants.height);
        frame.setResizable(false);
        frame.setIconImages(List.of(
            Images.getBufferedImage("window-icon.png"),
            Images.getBufferedImage("rest.png")
        ));
        // frame.setUndecorated(true);
        frame.setVisible(true);
        // frame.pack();
    }
    
    public static void main(String[] args) {
        System.setProperty("sun.java2d.uiScale", "1.0");
        // Schedule a job for the event dispatch thread:
        // creating and showing this application's GUI.
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                // Turn off metal's use of bold fonts
                UIManager.put("swing.boldMetal", Boolean.FALSE);
                createAndShowGUI();
            }
        });
    }
}
