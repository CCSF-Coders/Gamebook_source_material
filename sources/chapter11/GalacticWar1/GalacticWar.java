/*****************************************************
 * Beginning Java Game Programming, 3rd Edition
 * by Jonathan S. Harbour
 * GALACTIC WAR, Chapter 11
 *****************************************************/
import java.applet.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.awt.image.*;
import java.util.*;

/*****************************************************
 * Primary class for the game
 *****************************************************/
public class GalacticWar extends Applet implements Runnable, KeyListener  {
        //the main thread becomes the game loop
        Thread gameloop;
        //use this as a double buffer
        BufferedImage backbuffer;
        //the main drawing object for the back buffer
        Graphics2D g2d;
        //toggle for drawing bounding boxes
        boolean showBounds = false;

        //create the asteroid array
        int ASTEROIDS = 20;
        Asteroid[] ast = new Asteroid[ASTEROIDS];

        //create the bullet array
        int BULLETS = 10;
        Bullet[] bullet = new Bullet[BULLETS];
        int currentBullet = 0;

        //the player's ship
        ImageEntity ship = new ImageEntity(this);

        //create the identity transform
        AffineTransform identity = new AffineTransform();

        //create a random number generator
        Random rand = new Random();

        /*****************************************************
         * applet init event
         *****************************************************/
        public void init() {
            //create the back buffer for smooth graphics
            backbuffer = new BufferedImage(640, 480, BufferedImage.TYPE_INT_RGB);
            g2d = backbuffer.createGraphics();

            //set up the ship
            ship.setX(320);
            ship.setY(240);
            ship.load("spaceship1.png");
            ship.setGraphics(g2d);

            //set up the bullets
            for (int n = 0; n<BULLETS; n++) {
                bullet[n] = new Bullet();
            }

            //set up the asteroids
            for (int n = 0; n<ASTEROIDS; n++) {
                ast[n] = new Asteroid();
                ast[n].setRotationVelocity(rand.nextInt(3)+1);
                ast[n].setX((double)rand.nextInt(600)+20);
                ast[n].setY((double)rand.nextInt(440)+20);
                ast[n].setMoveAngle(rand.nextInt(360));
                double ang = ast[n].getMoveAngle() - 90;
                ast[n].setVelX(calcAngleMoveX(ang));
                ast[n].setVelY(calcAngleMoveY(ang));
            }

            //start the user input listener
            addKeyListener(this);
        }

        /*****************************************************
         * applet update event to redraw the screen
         *****************************************************/
        public void update(Graphics g) {
            //start off transforms at identity
            g2d.setTransform(identity);

            //erase the background
            g2d.setPaint(Color.BLACK);
            g2d.fillRect(0, 0, getSize().width, getSize().height);

            //print some status information
            g2d.setColor(Color.WHITE);
            g2d.drawString("Ship: " + Math.round(ship.getX()) + "," +
                Math.round(ship.getY()) , 5, 10);
            g2d.drawString("Move angle: " + Math.round(
                ship.getMoveAngle())+90, 5, 25);
            g2d.drawString("Face angle: " +  Math.round(
                ship.getFaceAngle()), 5, 40);

            //draw the game graphics
            drawShip();
            drawBullets();
            drawAsteroids();

            //repaint the applet window
            paint(g);
        }

        /*****************************************************
         * drawShip called by applet update event
         *****************************************************/
        public void drawShip() {
           // set the transform for the image
           ship.transform();
           ship.draw();

            //draw bounding rectangle around ship
            if (showBounds) {
                g2d.setTransform(identity);
                g2d.setColor(Color.BLUE);
                g2d.draw(ship.getBounds());
            }
        }

        /*****************************************************
         * drawBullets called by applet update event
         *****************************************************/
        public void drawBullets() {
            for (int n = 0; n < BULLETS; n++) {
                if (bullet[n].isAlive()) {
                    //draw the bullet
                    g2d.setTransform(identity);
                    g2d.translate(bullet[n].getX(), bullet[n].getY());
                    g2d.setColor(Color.MAGENTA);
                    g2d.draw(bullet[n].getShape());
                }
            }
        }

        /*****************************************************
         * drawAsteroids called by applet update event
         *****************************************************/
        public void drawAsteroids() {
            for (int n = 0; n < ASTEROIDS; n++) {
                if (ast[n].isAlive()) {
                    //draw the asteroid
                    g2d.setTransform(identity);
                    g2d.translate(ast[n].getX(), ast[n].getY());
                    g2d.rotate(Math.toRadians(ast[n].getMoveAngle()));
                    g2d.setColor(Color.DARK_GRAY);
                    g2d.fill(ast[n].getShape());

                    //draw bounding rectangle
                    if (showBounds) {
                        g2d.setTransform(identity);
                        g2d.setColor(Color.BLUE);
                        g2d.draw(ast[n].getBounds());
                    }
                }
            }
        }

        /*****************************************************
         * applet window repaint event--draw the back buffer
         *****************************************************/
        public void paint(Graphics g) {
            g.drawImage(backbuffer, 0, 0, this);
        }

        /*****************************************************
         * thread start event - start the game loop running
         *****************************************************/
        public void start() {
            gameloop = new Thread(this);
            gameloop.start();
        }
        /*****************************************************
         * thread run event (game loop)
         *****************************************************/
        public void run() {
            //acquire the current thread
            Thread t = Thread.currentThread();
            //keep going as long as the thread is alive
            while (t == gameloop) {
                try {
                    Thread.sleep(20);
                }
                catch(InterruptedException e) {
                    e.printStackTrace();
                }
                //update the game loop
                gameUpdate();
                repaint();
            }
        }
        /*****************************************************
         * thread stop event
         *****************************************************/
        public void stop() {
            gameloop = null;
        }

        /*****************************************************
         * move and animate the objects in the game
         *****************************************************/
        private void gameUpdate() {
            updateShip();
            updateBullets();
            updateAsteroids();
            checkCollisions();
        }

        /*****************************************************
         * Update the ship position based on velocity
         *****************************************************/
        public void updateShip() {
            //update ship's X position, wrap around left/right
            ship.incX(ship.getVelX());
            if (ship.getX() < -10)
                ship.setX(getSize().width + 10);
            else if (ship.getX() > getSize().width + 10)
                ship.setX(-10);
            //update ship's Y position, wrap around top/bottom
            ship.incY(ship.getVelY());
            if (ship.getY() < -10)
                ship.setY(getSize().height + 10);
            else if (ship.getY() > getSize().height + 10)
                ship.setY(-10);
        }

        /*****************************************************
         * Update the bullets based on velocity
         *****************************************************/
        public void updateBullets() {
            //move the bullets
            for (int n = 0; n < BULLETS; n++) {
                if (bullet[n].isAlive()) {
                    //update bullet's x position
                    bullet[n].incX(bullet[n].getVelX());
                    //bullet disappears at left/right edge
                    if (bullet[n].getX() < 0 ||
                        bullet[n].getX() > getSize().width)
                    {
                        bullet[n].setAlive(false);
                    }
                    //update bullet's y position
                    bullet[n].incY(bullet[n].getVelY());
                    //bullet disappears at top/bottom edge
                    if (bullet[n].getY() < 0 ||
                        bullet[n].getY() > getSize().height)
                    {
                        bullet[n].setAlive(false);
                    }
                }
            }
        }

        /*****************************************************
         * Update the asteroids based on velocity
         *****************************************************/
        public void updateAsteroids() {
            //move and rotate the asteroids
            for (int n = 0; n < ASTEROIDS; n++) {
                if (ast[n].isAlive()) {
                    //update the asteroid's X value
                    ast[n].incX(ast[n].getVelX());
                    if (ast[n].getX() < -20)
                        ast[n].setX(getSize().width + 20);
                    else if (ast[n].getX() > getSize().width + 20)
                        ast[n].setX(-20);

                    //update the asteroid's Y value
                    ast[n].incY(ast[n].getVelY());
                    if (ast[n].getY() < -20)
                        ast[n].setY(getSize().height + 20);
                    else if (ast[n].getY() > getSize().height + 20)
                        ast[n].setY(-20);

                    //update the asteroid's rotation
                    ast[n].incMoveAngle(ast[n].getRotationVelocity());
                    if (ast[n].getMoveAngle() < 0)
                        ast[n].setMoveAngle(360 - ast[n].getRotationVelocity());
                    else if (ast[n].getMoveAngle() > 360)
                        ast[n].setMoveAngle(ast[n].getRotationVelocity());
                }
            }
        }

        /*****************************************************
         * Test asteroids for collisions with ship or bullets
         *****************************************************/
        public void checkCollisions() {
            //check for ship and bullet collisions with asteroids
            for (int m = 0; m<ASTEROIDS; m++) {
                if (ast[m].isAlive()) {
                    //check for bullet collisions
                    for (int n = 0; n < BULLETS; n++) {
                        if (bullet[n].isAlive()) {
                            //perform the collision test
                            if (ast[m].getBounds().contains(
                                    bullet[n].getX(), bullet[n].getY()))
                            {
                                bullet[n].setAlive(false);
                                ast[m].setAlive(false);
                                continue;
                            }
                        }
                    }

                    //check for ship collision
                    if (ast[m].getBounds().intersects(ship.getBounds())) {
                        ast[m].setAlive(false);
                        ship.setX(320);
                        ship.setY(240);
                        ship.setFaceAngle(0);
                        ship.setVelX(0);
                        ship.setVelY(0);
                        continue;
                    }
                }
            }

        }

        /*****************************************************
         * key listener events
         *****************************************************/
        public void keyReleased(KeyEvent k) { }
        public void keyTyped(KeyEvent k) { }
        public void keyPressed(KeyEvent k) {
            int keyCode = k.getKeyCode();

            switch (keyCode) {

            case KeyEvent.VK_LEFT:
                //left arrow rotates ship left 5 degrees
                ship.incFaceAngle(-5);
                if (ship.getFaceAngle() < 0) ship.setFaceAngle(360-5);
                break;

            case KeyEvent.VK_RIGHT:
                //right arrow rotates ship right 5 degrees
                ship.incFaceAngle(5);
                if (ship.getFaceAngle() > 360) ship.setFaceAngle(5);
                break;

            case KeyEvent.VK_UP:
                //up arrow adds thrust to ship (1/10 normal speed)
                ship.setMoveAngle(ship.getFaceAngle() - 90);
                ship.incVelX(calcAngleMoveX(ship.getMoveAngle()) * 0.1);
                ship.incVelY(calcAngleMoveY(ship.getMoveAngle()) * 0.1);
//*********                if (!thrust.getClip().isActive())
//****                    thrust.play();
                break;

            //Ctrl, Enter, or Space can be used to fire weapon
            case KeyEvent.VK_CONTROL:
            case KeyEvent.VK_ENTER:
            case KeyEvent.VK_SPACE:
                //fire a bullet
                currentBullet++;
                if (currentBullet > BULLETS - 1) currentBullet = 0;
                bullet[currentBullet].setAlive(true);
                //point bullet in same direction ship is facing
                bullet[currentBullet].setX(ship.getCenterX());
                bullet[currentBullet].setY(ship.getCenterY());
                bullet[currentBullet].setMoveAngle(ship.getFaceAngle() - 90);
                //fire bullet at angle of the ship
                double angle = bullet[currentBullet].getMoveAngle();
                double svx = ship.getVelX();
                double svy = ship.getVelY();
                bullet[currentBullet].setVelX(svx + calcAngleMoveX(angle) * 2);
                bullet[currentBullet].setVelY(svy + calcAngleMoveY(angle) * 2);
                break;

            case KeyEvent.VK_B:
                //toggle bounding rectangles
                showBounds = !showBounds;
                break;
            }
        }

        /*****************************************************
         * calculate X movement value based on direction angle
         *****************************************************/
        public double calcAngleMoveX(double angle) {
            double movex = Math.cos(angle * Math.PI / 180);
            return movex;
        }

        /*****************************************************
         * calculate Y movement value based on direction angle
         *****************************************************/
        public double calcAngleMoveY(double angle) {
            double movey = Math.sin(angle * Math.PI / 180);
            return movey;
        }

}
