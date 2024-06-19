package org.tgaudioplayer.app.pages.player;
import java.awt.Image;
import java.awt.image.BufferedImage;

import org.tgaudioplayer.app.lib.GaussianBlur;
import org.tgaudioplayer.app.utils.Constants;

public class BlurredBackground implements Runnable {
    BufferedImage packet;
    PlayerView view;
    public Image image;
    
    // True if receiver should wait
    // False if sender should wait
    private boolean transfer = true;

    public BlurredBackground(PlayerView view) {
        this.view = view;
        Thread thread = new Thread(this);
        thread.start();
    }
 
    public synchronized BufferedImage receive() {
        while (transfer) {
            try {
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt(); 
                System.err.println("Thread Interrupted");
            }
        }
        transfer = true;
        
        BufferedImage returnPacket = packet;
        notifyAll();
        return returnPacket;
    }
 
    public synchronized void send(BufferedImage packet) {
        while (!transfer) {
            try { 
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt(); 
                System.err.println("Thread Interrupted");
            }
        }
        transfer = false;
        
        this.packet = packet;
        notifyAll();
    }

    public void run() {
        while (true) {
            BufferedImage receivedImage = this.receive();
            BufferedImage img = GaussianBlur.blur(receivedImage);
            this.image = img.getScaledInstance(Constants.height, Constants.height, Image.SCALE_FAST);
            this.view.repaint();
            this.view.removeBackgroundLock();
        }
    }
}
