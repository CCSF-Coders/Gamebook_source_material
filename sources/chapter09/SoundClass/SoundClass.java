/******************************************************************
 * Beginning Java Game Programming, 3rd Edition
 * by Jonathan S. Harbour
 * SoundClass test program
 ******************************************************************/

import java.awt.*;
import javax.swing.*;

public class SoundClass extends JFrame {

    SoundClip clip;
    
    public static void main(String[] args) {
        new SoundClass();
    }    

    public SoundClass() {
        super("SoundClass Demo");
        setSize(500,400);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        clip = new SoundClip();
        clip.load("screamandrun.aiff");
        if (clip.isLoaded()) clip.play();
        
        repaint();
    }

    //redraw the screen
    public void paint(Graphics g) {
        if (clip.isLoaded()) {
            g.drawString("Sample file: " + clip.getFilename(), 10, 40);
            g.drawString(clip.getClip().getFormat().toString(), 10, 55);
        } else {
            g.drawString("Error loading sound file " + clip.getFilename(), 10, 70);
        }
    }
}
