package org.tgaudioplayer.app.pages.sources.local;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JPanel;

import org.tgaudioplayer.app.data.Data;


/**
 * Entry point for the UI component that
 * takes track of the folders to scan for audio files
 */
public class LocalController extends JPanel implements ActionListener {
    LocalView localView;
    LocalModel localModel;

    public LocalController() {
        this.localView = new LocalView(this);
        this.localModel = new LocalModel(localView, Data.getInstance());
    }
    
    /**
     * Adds a directory to the list
     */
    void addDirectory(File directory) {
        this.localModel.addDirectory(directory);
    }

    /** Removes a directory */
    void removeDirectory(File directory) {
        this.localModel.removeDirectory(directory);
    }

    /**
     * @return a reference to the Swing component
     */
    public LocalView getView() {
        return this.localView;
    }
    
    /** Opens the File Selector UI to choose a folder */
    private File chooseFolder() {
        final JFileChooser fc = new JFileChooser();
        // Start off from the music folder
        fc.setCurrentDirectory(new File  
        (System.getProperty("user.home") + System.getProperty("file.separator") + "Music"));
        // Only choose between folders
        fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        fc.setAcceptAllFileFilterUsed(false);
        
        int returnVal = fc.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File folder = fc.getSelectedFile();
            return folder;
        } else {
            return null;
        }
    }

    /**
     * Event listener for the buttons in the panel.
     */
    public void actionPerformed(ActionEvent e) {
        // First we need to detect which button was pressed.
        String buttonText = ((JButton) e.getSource()).getText();
        if (buttonText == this.localView.ADD_BUTTON_TEXT) {
            // Let the user choose a folder to add
            File folder = this.chooseFolder();
            if (folder != null) {
                this.addDirectory(folder);
            }
        } else if (buttonText == this.localView.REMOVE_BUTTON_TEXT) {
            // Remove the currently selected folder.
            String path = this.localView.getSelectedPath();
            if (path != null) {
                this.removeDirectory(new File(path));
            }
        }
    }
};
