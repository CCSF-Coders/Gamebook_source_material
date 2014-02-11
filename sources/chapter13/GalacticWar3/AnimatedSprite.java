/*****************************************************
* Beginning Java Game Programming, 3rd Edition
* by Jonathan S. Harbour
* AnimatedSprite class (Chapter 13 variation)
*****************************************************/
import java.awt.*;
import java.applet.*;
import java.awt.image.*;
import java.net.*;

public class AnimatedSprite extends Sprite {
    //this image holds the large tiled bitmap
    private Image animimage;
    //temp image passed to parent draw method
    BufferedImage tempImage;
    Graphics2D tempSurface;
    //custom properties
    private int currFrame, totFrames;
    private int animDir;
    private int frCount, frDelay;
    private int frWidth, frHeight;
    private int cols;

    public AnimatedSprite(Applet applet, Graphics2D g2d) {
        super(applet, g2d);
        currFrame = 0;
        totFrames = 0;
        animDir = 1;
        frCount = 0;
        frDelay = 0;
        frWidth = 0;
        frHeight = 0;
        cols = 0;
    }

    private URL getURL(String filename) {
        URL url = null;
        try {
            url = this.getClass().getResource(filename);
        } catch (Exception e) {}
        return url;
    }

    public void load(String filename, int columns, int rows,
        int width, int height)
    {
        //load the tiled animation bitmap
        Toolkit tk = Toolkit.getDefaultToolkit();
        animimage = tk.getImage(getURL(filename));
        setColumns(columns);
        setTotalFrames(columns * rows);
        setFrameWidth(width);
        setFrameHeight(height);

        //frame image is passed to parent class for drawing
        tempImage = new BufferedImage(width, height,
            BufferedImage.TYPE_INT_ARGB);
        tempSurface = tempImage.createGraphics();
        super.setImage(tempImage);
    }

    public int currentFrame() { return currFrame; }
    public void setCurrentFrame(int frame) { currFrame = frame; }

    public int frameWidth() { return frWidth; }
    public void setFrameWidth(int width) { frWidth = width; }

    public int frameHeight() { return frHeight; }
    public void setFrameHeight(int height) { frHeight = height; }

    public int totalFrames() { return totFrames; }
    public void setTotalFrames(int total) { totFrames = total; }

    public int animationDirection() { return animDir; }
    public void setAnimationDirection(int dir) { animDir = dir; }

    public int frameCount() { return frCount; }
    public void setFrameCount(int count) { frCount = count; }

    public int frameDelay() { return frDelay; }
    public void setFrameDelay(int delay) { frDelay = delay; }

    public int columns() { return cols; }
    public void setColumns(int num) { cols = num; }

    public void updateAnimation() {
        frCount++;
        if (frameCount() > frameDelay()) {
            setFrameCount(0);
            //update the animation frame
            setCurrentFrame(currentFrame() + animationDirection());
            if (currentFrame() > totalFrames() - 1) {
                setCurrentFrame(0);
            }
            else if (currentFrame() < 0) {
                setCurrentFrame(totalFrames() - 1);
            }
        }
    }

    public void draw() {
        //calculate the current frame's X and Y position
        int frameX = (currentFrame() % columns()) * frameWidth();
        int frameY = (currentFrame() / columns()) * frameHeight();

        //copy the frame onto the temp image
        tempSurface.drawImage(animimage, 0, 0, frameWidth()-1,
            frameHeight()-1, frameX, frameY, frameX+frameWidth(),
            frameY+frameHeight(), applet());

        //pass the temp image on to the parent class and draw it
        super.setImage(tempImage);
        super.transform();
        super.draw();
    }

}

