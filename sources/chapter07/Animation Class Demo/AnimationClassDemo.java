/*****************************************************
* Beginning Java Game Programming, 3rd Edition
* by Jonathan S. Harbour
* AnimationClass program
*****************************************************/
import java.awt.*;
import javax.swing.*;
import java.util.*;
import java.awt.image.*;
import java.net.*;

public class AnimationClassDemo extends JFrame implements Runnable {
    static int ScreenWidth = 640;
    static int ScreenHeight = 480;
    Thread gameloop;
    Random rand = new Random();

    //double buffer objects
    BufferedImage backbuffer;
    Graphics2D g2d;

    //sprite variables
    AnimatedSprite sprite;

    public static void main(String[] args) {
        new AnimationClassDemo();
    }
    
    public AnimationClassDemo() {
        super("Animation Class Demo");
        setSize(ScreenWidth,ScreenHeight);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        //create the back buffer for smooth graphics
        backbuffer = new BufferedImage(ScreenWidth, ScreenHeight,
            BufferedImage.TYPE_INT_RGB);
        g2d = backbuffer.createGraphics();

        //load the explosion animation 
        sprite = new AnimatedSprite(this, g2d);
        sprite.load("explosion.png", 6, 5, 128, 128);
        sprite.position = new Point(300,200);
        sprite.frameDelay = 10;
        sprite.totalFrames = 30;
        sprite.velocity = new Point(1,1);
        sprite.rotationRate = 1.0;

        gameloop = new Thread(this);
        gameloop.start();
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
        //draw the background
        g2d.setColor(Color.BLACK);
        g2d.fill( new Rectangle(0, 0, ScreenWidth-1, ScreenHeight-1) );

        //draw the sprite
        sprite.draw();

        //keep the sprite in the screen boundary
        if (sprite.position.x < 0 || sprite.position.x > ScreenWidth - 128) 
            sprite.velocity.x *= -1;
        if (sprite.position.y < 0 || sprite.position.y > ScreenHeight - 128) 
            sprite.velocity.y *= -1;


        g2d.setColor(Color.WHITE);
        g2d.drawString("Position: " + sprite.position.x + "," +
                       sprite.position.y, 10, 40);
        g2d.drawString("Velocity: " + sprite.velocity.x + "," +
                       sprite.velocity.y, 10, 60);
        g2d.drawString("Animation: " + sprite.currentFrame, 10, 80);

        repaint();
    }


    public void paint(Graphics g) {
        //draw the back buffer to the screen
        g.drawImage(backbuffer, 0, 0, this);
    }


}

