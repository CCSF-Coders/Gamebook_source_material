/*****************************************************
 * Beginning Java Game Programming, 3rd Edition
 * by Jonathan S. Harbour
 * ThreadedLoop program
 *****************************************************/
import java.awt.*;
import java.lang.*;
import javax.swing.*;
import java.util.*;

public class ThreadedLoop extends JFrame implements Runnable {
    Random rand = new Random();
    
    //the main thread
    Thread thread;

    //count the number of rectangles drawn
    long count = 0, total = 0;

    public static void main(String[] args) {
        new ThreadedLoop();
    }    

    public ThreadedLoop() {
        super("ThreadedLoop");
        setSize(500,400);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        thread = new Thread(this);
        thread.start();
    }

    //thread run event
    public void run() {
        long start = System.currentTimeMillis();

        //acquire the current thread
        Thread current = Thread.currentThread();

        //here's the new game loop
        while (current == thread) {
            try { Thread.sleep(0); }
            catch(InterruptedException e) { e.printStackTrace(); }

            //draw something
            repaint();

            //figure out how fast it's running
            if (start + 1000 < System.currentTimeMillis()) {
                start = System.currentTimeMillis();
                total = count;
                count = 0;
            }
        }
    }
    
    //JFrame paint event
    public void paint(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;

        //create a random rectangle
        int w = rand.nextInt(100);
        int h = rand.nextInt(100);
        int x = rand.nextInt(getSize().width - w);
        int y = rand.nextInt(getSize().height - h);
        Rectangle rect = new Rectangle(x,y,w,h);

        //generate a random color
        int red = rand.nextInt(256);
        int green = rand.nextInt(256);
        int blue = rand.nextInt(256);
        g2d.setColor(new Color(red,green,blue));

        //draw the rectangle
        g2d.fill(rect);

        //add another to the counter
        count++;
        
        g2d.setColor(Color.WHITE);
        g2d.fill(new Rectangle(0,360,500,400));
        g2d.setColor(Color.BLACK);
        g2d.drawString("Rectangles per second: " + total, 10, 380);
        
    }
}
