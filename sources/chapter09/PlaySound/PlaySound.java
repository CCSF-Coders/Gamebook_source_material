/******************************************************************
 * Beginning Java Game Programming, 3rd Edition
 * by Jonathan S. Harbour
 * PlaySound program
 ******************************************************************/

import java.awt.*;
import javax.swing.*;
import java.io.*;
import java.net.*;
import javax.sound.sampled.*;

public class PlaySound extends JFrame {

    String filename = "gong.wav";
    AudioInputStream sample;

    public static void main(String[] args) {
        new PlaySound();
    }
    
    public PlaySound() {
        super("PlaySound");
        setSize(500,400);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        try {
            sample = AudioSystem.getAudioInputStream(getURL(filename));
            
            //create a sound buffer
            Clip clip = AudioSystem.getClip();
            
            //load the audio file
            clip.open(sample);
            
            //play sample
            clip.start();
            
        } catch (MalformedURLException e) {
        } catch (IOException e) {
        } catch (LineUnavailableException e) {
        } catch (UnsupportedAudioFileException e) {
        } catch (Exception e) { }

        repaint();
    }

    public void paint(Graphics g) {
        g.drawString("Sample file: " + filename, 10, 40);
        g.drawString(sample.getFormat().toString(), 10, 55);
        g.drawString("Sampling rate: " + (int)sample.getFormat().
            getSampleRate(), 10, 70);
        g.drawString("Sample channels: " + sample.getFormat().getChannels() , 10, 85);
        g.drawString("Encoded format: " + sample.getFormat().getEncoding().toString(), 10, 100);
        g.drawString("Sample size: " + sample.getFormat().getSampleSizeInBits() + "-bit", 10, 115);
        g.drawString("Frame size: " + sample.getFormat().getFrameSize(), 10, 130);
    }

    private URL getURL(String filename) {
        URL url = null;
        try {
            url = this.getClass().getResource(filename);
        }
        catch (Exception e) { e.printStackTrace(); }
        return url;
    }

}
