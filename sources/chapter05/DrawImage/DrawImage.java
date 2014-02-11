/*************************************************************
 * Beginning Java Game Programming, 3rd Edition
 * by Jonathan S. Harbour
 * DrawImage program
 *************************************************************/

import java.awt.*;
import java.util.*;
import javax.swing.*;
import java.net.*;

public class DrawImage extends JFrame {
    private Image image;
    
    public static void main(String[] args) {
        new DrawImage();    
    }
    
    public DrawImage() {
        super("DrawImage");
        setSize(600,600);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Toolkit tk = Toolkit.getDefaultToolkit();
        image = tk.getImage(getURL("castle.png"));
    }

    private URL getURL(String filename) {
        URL url = null;
        try {
            url = this.getClass().getResource(filename);
        }
        catch (Exception e) { }
        return url;
    }

    public void paint(Graphics g) {
        //create an instance of Graphics2D
        Graphics2D g2d = (Graphics2D) g;

        //fill the background with black
        g2d.setColor(Color.BLACK);
        g2d.fillRect(0, 0, getSize().width, getSize().height);

        //draw the image
        g2d.drawImage(image, 0, 40, this);
    }
}
