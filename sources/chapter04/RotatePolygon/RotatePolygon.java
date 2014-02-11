/**********************************************************
 * Beginning Java Game Programming, 3rd Edition
 * by Jonathan S. Harbour
 * RotatePolygon program
 **********************************************************/

import java.awt.*;
import java.awt.event.*;
import java.applet.*;
import java.util.*;
import java.awt.geom.*;

public class RotatePolygon extends Applet implements KeyListener, MouseListener {
    private int[] xpoints = {  0,-10, -7,  7, 10 };
    private int[] ypoints = {-10, -2, 10, 10, -2 };

    //here's the shape used for drawing
    private Polygon poly;

    //polygon rotation variable
    int rotation = 0;

    //applet init event
    public void init() {
        //create the polygon
        poly = new Polygon(xpoints, ypoints, xpoints.length);

        //initialize the listeners
        addKeyListener(this);
        addMouseListener(this);
    }

    //applet paint event
    public void paint(Graphics g) {
        //create an instance of Graphics2D
        Graphics2D g2d = (Graphics2D) g;

        //create a random number generator
        Random rand = new Random();

        //save the window width/height
        int width = getSize().width;
        int height = getSize().height;

        //fill the background with black
        g2d.setColor(Color.BLACK);
        g2d.fillRect(0, 0, width, height);

        //move, rotate, and scale the shape randomly
        g2d.translate(width / 2, height / 2);
        g2d.scale(20, 20);
        g2d.rotate(Math.toRadians(rotation));

        //draw the shape with a random color
        g2d.setColor(Color.RED);
        g2d.fill(poly);
        g2d.setColor(Color.BLUE);
        g2d.draw(poly);
    }

    //handle keyboard events
    public void keyReleased(KeyEvent k) { }
    public void keyTyped(KeyEvent k) { }
    public void keyPressed(KeyEvent k) {
        switch (k.getKeyCode()) {
        case KeyEvent.VK_LEFT:
            rotation--;
            if (rotation < 0) rotation = 359;
            repaint();
            break;
        case KeyEvent.VK_RIGHT:
            rotation++;
            if (rotation > 360) rotation = 0;
            repaint();
            break;
        }
    }

    //handle mouse events
    public void mouseEntered(MouseEvent m) { }
    public void mouseExited(MouseEvent m) { }
    public void mouseReleased(MouseEvent m) { }
    public void mouseClicked(MouseEvent m) { }
    public void mousePressed(MouseEvent m) {
        switch(m.getButton()) {
        case MouseEvent.BUTTON1:
            rotation--;
            if (rotation < 0) rotation = 359;
            repaint();
            break;
        case MouseEvent.BUTTON3:
            rotation++;
            if (rotation > 360) rotation = 0;
            repaint();
            break;
        }
    }
}
