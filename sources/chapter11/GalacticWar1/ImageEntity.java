/*********************************************************
 * Base game image class for bitmapped game entities
 **********************************************************/
import java.awt.*;
import java.awt.Graphics2D;
import java.awt.geom.*;
import java.applet.*;
import java.net.*;

public class ImageEntity extends BaseGameEntity {
    //variables
    private Image image;
    private Applet applet;
    private AffineTransform at;
    private Graphics2D g2d;

    //default constructor
    ImageEntity(Applet a) {
        applet = a;
        setImage(null);
        setAlive(true);
    }

    //sets and returns the entity's image object
    public Image getImage() { return image; }
    public void setImage(Image image) { this.image = image; }

    //returns the width and height of the entity
    public int width() {
        return getImage().getWidth(applet);
    }
    public int height() {
        return getImage().getHeight(applet);
    }

    //returns the center of the entity in pixels
    public double getCenterX() {
        return getX() + width() / 2;
    }
    public double getCenterY() {
        return getY() + height() / 2;
    }

    //set reference to the drawing object
    public void setGraphics(Graphics2D g) {
        g2d = g;
    }

     private URL getURL(String filename) {
         URL url = null;
         try {
             url = this.getClass().getResource(filename);
         }
         catch (Exception e) { }
         return url;
     }

    //load an image file
    public void load(String filename) {
        setImage(applet.getImage(getURL(filename)));
        while(getImage().getWidth(applet) <= 0);
        double x = applet.getSize().width/2  - width()/2;
        double y = applet.getSize().height/2 - height()/2;
        at = AffineTransform.getTranslateInstance(x, y);
    }

    //move and rotate the entity
    public void transform() {
        at.setToIdentity();
        at.translate((int)getX() + width()/2, (int)getY() + height()/2);
        at.rotate(Math.toRadians(getFaceAngle()));
        at.translate(-width()/2, -height()/2);
    }

    //draw the entity
    public void draw() {
        g2d.drawImage(getImage(), at, applet);
    }

    //bounding rectangle
    public Rectangle getBounds() {
        return new Rectangle((int)getX(),(int)getY(),width(),height());
    }

}
