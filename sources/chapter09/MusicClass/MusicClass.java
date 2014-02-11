/******************************************************************
 * Beginning Java Game Programming, 3rd Edition
 * by Jonathan S. Harbour
 * MusicClass program
 ******************************************************************/

import java.awt.*;
import javax.swing.*;

public class MusicClass extends JFrame {
    MidiSequence midi;

    public static void main(String[] args) {
        new MusicClass();
    }
    
    public MusicClass() {
        super("MusicClass Demo");
        setSize(500,400);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        midi = new MidiSequence();
        midi.load("bsg.mid");
        if (midi.isLoaded()) midi.play();
        
        repaint();
    }

    //repaint the applet window
    public void paint(Graphics g) {
        int x=10, y = 1;
        if (midi.isLoaded()) {
            g.drawString("Midi File: " + midi.getFilename(), x, 15 * y++);
            g.drawString("Resolution: " + midi.getSong().getResolution(), x, 15 * y++);
            g.drawString("Tick length: " + midi.getSong().getTickLength(), x, 15 * y++);
            g.drawString("Tracks: " + midi.getSong().getTracks().length, x, 15 * y++);
        } else {
            g.drawString("Error loading sequence file " + midi.getFilename(), 10, 15);
        }
    }

}
