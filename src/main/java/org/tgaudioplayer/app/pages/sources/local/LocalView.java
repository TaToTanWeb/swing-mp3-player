package org.tgaudioplayer.app.pages.sources.local;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.io.File;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import org.tgaudioplayer.app.utils.Constants;
import org.tgaudioplayer.app.utils.Utils;


/** 
 * UI Component to let the user define
 * a list of directories to scan.
 * @param parent – the Sources JPanel tab
 */
public class LocalView extends JPanel {
    final String REMOVE_BUTTON_TEXT = "Remove folder";
    final String ADD_BUTTON_TEXT = "Add folder";
    LocalController controller;
    DefaultListModel<String> dirsList = new DefaultListModel<>();
    JList<String> dirs = new JList<String>(dirsList);
    JPanel dirsPanel = Utils.frame(dirs, Utils.pad(5, 5, 5, 5));
    JLabel placeholder = null;

    public LocalView(LocalController localController) {
        super(false);
        this.controller = localController;

        // Configuring the layout
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setAlignmentX(Component.LEFT_ALIGNMENT);
        this.setBackground(Constants.backgroundColor);
        // Creating components
        this.displayPlaceholder();
        this.createTextLabel();
        this.createDirsPanel();
        this.createButtonsPanel();
    }

    /** Introductory text label */
    private void createTextLabel() {
        JLabel text = new JLabel("Local folders:", SwingConstants.LEFT);
        text.setAlignmentX(Component.LEFT_ALIGNMENT);
        text.setBorder(new EmptyBorder(0, 0, 10, 0));
        text.setForeground(Color.WHITE);
        this.add(text);
    }

    /** List of currently selected directories */
    private void createDirsList() {
        this.dirs.setFixedCellHeight(20);
        int defaultChars = Constants.width / 4;
        this.dirs.setPrototypeCellValue(new String(new char[defaultChars]).replace('\0', ' '));
        this.dirs.setMinimumSize(new Dimension(Constants.width - 10, 0));
        this.dirs.setAlignmentX(Component.LEFT_ALIGNMENT);
    }

    /** White JPanel behind to add some space */
    private void createDirsPanel() {
        this.createDirsList();
        this.dirsPanel.setBackground(Color.WHITE);
        this.dirsPanel.setLayout(new BoxLayout(this.dirsPanel, BoxLayout.Y_AXIS));
        this.dirsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        this.dirsPanel.add(this.dirs);
        this.add(this.dirsPanel);
    }

    /** Button to add folders */
    private JButton createAddButton() {
        JButton button = new JButton(this.ADD_BUTTON_TEXT);
        button.addActionListener(this.controller);
        button.setAlignmentX(Component.LEFT_ALIGNMENT);
        return button;
    }

    /** Button to remove folders */
    private JButton createRemoveButton() {
        JButton button = new JButton(this.REMOVE_BUTTON_TEXT);
        button.addActionListener(this.controller);
        button.setAlignmentX(Component.LEFT_ALIGNMENT);
        return button;
    }

    /** Button panel */
    private void createButtonsPanel() {
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBorder(Utils.pad(10, 0, 0, 0));
        buttonPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        buttonPanel.setBackground(Constants.backgroundColor);
        buttonPanel.add(this.createAddButton());
        buttonPanel.add(Box.createRigidArea(new Dimension(5,0)));
        buttonPanel.add(this.createRemoveButton());
        this.add(buttonPanel);
    }

    /** Show a predefined text if no folders have been added yet. */
    public void displayPlaceholder() {
        if (this.placeholder != null) {
            this.placeholder.setVisible(true);
        } else {
            this.placeholder = new JLabel("No folders added yet.");
            this.dirsPanel.add(this.placeholder);
        }
    }

    /** Returns the currently selected path in the JList. */
    public String getSelectedPath() {
        String selectedText = this.dirs.getSelectedValue();
        if (selectedText != null) {
           return selectedText.split("\\(")[1].split("\\)")[0];
        } else {
            return null;
        }
    }

    private String formatDir(File dir) {
        // Formats dir path in a more human-readable format
        return dir.getName() + " (" + dir.getAbsolutePath() + ")";
    }

    /**
     * Updates the UI with
     * @param dirName the name of the new directory
     */
    public void addDirectory(File dir) {
        String dirName = this.formatDir(dir);
        if (!this.dirsList.contains(dirName))
            dirsList.addElement(dirName);
        // Removing the placeholder text
        if (this.placeholder != null)
            this.placeholder.setVisible(false);
    }

    /**
     * Updates the UI with
     * @param dirName the name of the new directory
     */
    public void removeDirectory(File dir) {
        String dirName = this.formatDir(dir);
        dirsList.removeElement(dirName);
        // Eventually show the placeholder text
        if (dirsList.isEmpty())
            this.placeholder.setVisible(true);
    }
};
