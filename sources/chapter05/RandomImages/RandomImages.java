/*************************************************************
 * Beginning Java Game Programming, 3rd Edition
 * by Jonathan S. Harbour
 * RandomImages program
 *************************************************************/
import java.awt.*;
import javax.swing.*;
import java.util.*;
import java.awt.geom.*;
import java.net.*;

public class RandomImages extends JFrame {
    private Image image;
    
    public static void main(String[] args) {
        new RandomImages();
    }
    
    //applet init event
    public RandomImages() {
        super("RandomImages");
        setSize(600,500);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Toolkit tk = Toolkit.getDefaultToolkit();
        image = tk.getImage(getURL("spaceship.png"));
    }


    //identity transformation
    AffineTransform identity = new AffineTransform();

    private URL getURL(String filename) {
        URL url = null;
        try {
            url = this.getClass().getResource(filename);
        }
        catch (Exception e) { }

        return url;
    }

    //applet paint event
    public void paint(Graphics g) {
        //create an instance of Graphics2D
        Graphics2D g2d = (Graphics2D) g;

        //working transform object
        AffineTransform trans = new AffineTransform();

        //random number generator
        Random rand = new Random();

        //applet window width/height
        int width = getSize().width;
        int height = getSize().height;

        //fill the background with black
        g2d.setColor(Color.BLACK);
        g2d.fillRect(0, 0, getSize().width, getSize().height);

        //draw the image multiple times
        for (int n = 0; n < 50; n++) {
            trans.setTransform(identity);
            //move, rotate, scale the image randomly
            trans.translate(rand.nextInt()%width, rand.nextInt()%height);
            trans.rotate(Math.toRadians(360 * rand.nextDouble()));
            double scale = rand.nextDouble()+1;
            trans.scale(scale, scale);

            //draw the image
            g2d.drawImage(image, trans, this);
        }
    }
}
