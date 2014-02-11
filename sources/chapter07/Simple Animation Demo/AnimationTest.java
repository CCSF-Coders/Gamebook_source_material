/*****************************************************
* Beginning Java Game Programming, 3rd Edition
* by Jonathan S. Harbour
* AnimationTest program
*****************************************************/
import java.awt.*;
import javax.swing.*;
import java.util.*;
import java.awt.image.*;
import java.net.*;

public class AnimationTest extends JFrame implements Runnable {
    static int ScreenWidth = 640;
    static int ScreenHeight = 480;
    Thread gameloop;
    Random rand = new Random();

    //double buffer objects
    BufferedImage backbuffer;
    Graphics2D g2d;

    //sprite variables
    Image image;
    Point pos = new Point(300,200);

    //animation variables
    int currentFrame = 0;
    int totalFrames = 30;
    int animationDirection = 1;
    int frameCount = 0;
    int frameDelay = 10;
    
    public static void main(String[] args) {
        new AnimationTest();
    }
    
    public AnimationTest() {
        super("Animation Test");
        setSize(ScreenWidth,ScreenHeight);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //create the back buffer for smooth graphics
        backbuffer = new BufferedImage(ScreenWidth, ScreenHeight,
            BufferedImage.TYPE_INT_RGB);
        g2d = backbuffer.createGraphics();

        //load the ball animation strip
        Toolkit tk = Toolkit.getDefaultToolkit();
        image = tk.getImage(getURL("explosion.png"));

        gameloop = new Thread(this);
        gameloop.start();
    }

    private URL getURL(String filename) {
        URL url = null;
        try {
            url = this.getClass().getResource(filename);
        }
        catch (Exception e) {}
        return url;
    }

    public void run() {
        Thread t = Thread.currentThread();
        while (t == gameloop) {
            try {
                Thread.sleep(5);
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
            gameUpdate();
        }
    }

    public void gameUpdate() {
        //clear the background
        g2d.setColor(Color.BLACK);
        g2d.fill( new Rectangle(0, 0, ScreenWidth-1, ScreenHeight-1) );

        //draw the current frame of animation
        drawFrame(image, g2d, pos.x, pos.y, 6, currentFrame, 128, 128);

        g2d.setColor(Color.WHITE);
        g2d.drawString("Position: " + pos.x + "," + pos.y, 10, 50);
        g2d.drawString("Animation: " + currentFrame, 10, 70);

        //see if it's time to animate
        frameCount++;
        if (frameCount > frameDelay) {
            frameCount=0;
            //update the animation frame
            currentFrame += animationDirection;
            if (currentFrame > totalFrames - 1) {
                currentFrame = 0;
                pos.x = rand.nextInt(ScreenWidth-128);
                pos.y = rand.nextInt(ScreenHeight-128);
            }
            else if (currentFrame < 0) {
                currentFrame = totalFrames - 1;
            }
        }

        repaint();
    }

    public void paint(Graphics g) {
        //draw the back buffer to the screen
        g.drawImage(backbuffer, 0, 0, this);
    }

    //draw a single frame of animation
    public void drawFrame(Image source, Graphics2D dest,
                          int x, int y, int cols, int frame,
                          int width, int height)
    {
        int fx = (frame % cols) * width;
        int fy = (frame / cols) * height;
        dest.drawImage(source, x, y, x+width, y+height,
                       fx, fy, fx+width, fy+height, this);
    }

}

