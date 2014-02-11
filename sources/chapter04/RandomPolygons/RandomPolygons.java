/**********************************************************
 * Beginning Java Game Programming, 3rd Edition
 * by Jonathan S. Harbour
 * RandomPolygons program
 **********************************************************/

import java.awt.*;
import java.applet.*;
import java.util.*;
import java.awt.geom.*;

public class RandomPolygons extends Applet {
    private int[] xpoints = {  0,-10, -7,  7, 10 };
    private int[] ypoints = {-10, -2, 10, 10, -2 };

    //here's the shape used for drawing
    private Polygon poly;

    //applet init event
    public void init() {
        poly = new Polygon(xpoints, ypoints, xpoints.length);
    }

    //applet paint event
    public void paint(Graphics g) {
        //create an instance of Graphics2D
        Graphics2D g2d = (Graphics2D) g;

        //save the identity transform
        AffineTransform identity = new AffineTransform();

        //create a random number generator
        Random rand = new Random();

        //save the window width/height
        int width = getSize().width;
        int height = getSize().height;

        //fill the background with black
        g2d.setColor(Color.BLACK);
        g2d.fillRect(0, 0, width, height);

        for (int n = 0; n < 300; n++) {
            //reset Graphics2D to the identity transform
            g2d.setTransform(identity);

            //move, rotate, and scale the shape randomly
            g2d.translate(rand.nextInt() % width, rand.nextInt() % height);
            g2d.rotate(Math.toRadians(360 * rand.nextDouble()));
            g2d.scale(5 * rand.nextDouble(), 5 * rand.nextDouble());

            //draw the shape with a random color
            g2d.setColor(new Color(rand.nextInt()));
            g2d.fill(poly);
            g2d.draw(poly);
        }
    }

}
