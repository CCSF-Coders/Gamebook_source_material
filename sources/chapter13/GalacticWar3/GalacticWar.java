/*****************************************************
* Beginning Java Game Programming, 3rd Edition
* by Jonathan S. Harbour
* GALACTIC WAR, Chapter 13
*****************************************************/
import java.applet.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.util.*;
import java.lang.System;

/*****************************************************
* Primary class for the game
*****************************************************/
public class GalacticWar extends Applet implements Runnable, KeyListener {
    //global constants
    static int SCREENWIDTH = 800;
    static int SCREENHEIGHT = 600;
    static int CENTERX = SCREENWIDTH / 2;
    static int CENTERY = SCREENHEIGHT / 2;
    static int ASTEROIDS = 10;
    static int BULLETS = 10;
    static int BULLET_SPEED = 4;
    static double ACCELERATION = 0.05;

    //sprite state values
    static int STATE_NORMAL = 0;
    static int STATE_COLLIDED = 1;
    static int STATE_EXPLODING = 2;

    //the main thread becomes the game loop
    Thread gameloop;

    //double buffer objects
    BufferedImage backbuffer;
    Graphics2D g2d;

    //various toggles
    boolean showBounds = true;
    boolean collisionTesting = true;
    long collisionTimer = 0;

    //define the game objects
    ImageEntity background;
    Sprite ship;
    Sprite[] ast = new Sprite[ASTEROIDS];
    Sprite[] bullet = new Sprite[BULLETS];
    int currentBullet = 0;
    AnimatedSprite explosion;

    //create a random number generator
    Random rand = new Random();

    //simple way to handle multiple keypresses
    boolean keyDown, keyUp, keyLeft, keyRight, keyFire;

    //frame rate counters and other timing variables
    int frameCount = 0, frameRate = 0;
    long startTime = System.currentTimeMillis();


    /*****************************************************
     * applet init event
     *****************************************************/
    public void init() {
        //create the back buffer for smooth graphics
        backbuffer = new BufferedImage(SCREENWIDTH, SCREENHEIGHT, BufferedImage.TYPE_INT_RGB);
        g2d = backbuffer.createGraphics();

        //load the background image
        background = new ImageEntity(this);
        background.load("bluespace.png");

        //set up the ship
        ship = new Sprite(this, g2d);
        ship.load("spaceship.png");
        ship.setPosition(new Point2D(CENTERX, CENTERY));
        ship.setAlive(true);

        //set up the bullets
        for (int n = 0; n<BULLETS; n++) {
            bullet[n] = new Sprite(this, g2d);
            bullet[n].load("plasmashot.png");
        }

        //set up the asteroids
        for (int n = 0; n<ASTEROIDS; n++) {
            ast[n] = new Sprite(this, g2d);
            ast[n].setAlive(true);
            //load the asteroid image
            int i = rand.nextInt(5)+1;
            ast[n].load("asteroid" + i + ".png");
            //set to a random position on the screen
            int x = rand.nextInt(SCREENWIDTH);
            int y = rand.nextInt(SCREENHEIGHT);
            ast[n].setPosition(new Point2D(x, y));
            //set rotation angles to a random value
            ast[n].setFaceAngle(rand.nextInt(360));
            ast[n].setMoveAngle(rand.nextInt(360));
            ast[n].setRotationRate(rand.nextDouble());
            //set velocity based on movement direction
            double ang = ast[n].moveAngle() - 90;
            double velx = calcAngleMoveX(ang);
            double vely =  calcAngleMoveY(ang);
            ast[n].setVelocity(new Point2D(velx, vely));
        }

        //load the explosion
        explosion = new AnimatedSprite(this, g2d);
        explosion.load("explosion96x96x16.png", 4, 4, 96, 96);
        explosion.setFrameDelay(2);
        explosion.setAlive(false);

        //start the user input listener
        addKeyListener(this);
    }

    /*****************************************************
     * applet update event to redraw the screen
     *****************************************************/
    public void update(Graphics g) {
        //calculate frame rate
        frameCount++;
        if (System.currentTimeMillis() > startTime + 1000) {
            startTime = System.currentTimeMillis();
            frameRate = frameCount;
            frameCount = 0;
        }

        //draw the background
        g2d.drawImage(background.getImage(),0,0,SCREENWIDTH-1,SCREENHEIGHT-1,this);

        //draw the game graphics
        drawAsteroids();
        drawShip();
        drawBullets();
        drawExplosions();

        //print status information on the screen
        g2d.setColor(Color.WHITE);
        g2d.drawString("FPS: " + frameRate, 5, 10);
        long x = Math.round(ship.position().X());
        long y = Math.round(ship.position().Y());
        g2d.drawString("Ship: " + x + "," + y , 5, 25);
        g2d.drawString("Move angle: " + Math.round(ship.moveAngle())+90, 5, 40);
        g2d.drawString("Face angle: " +  Math.round(ship.faceAngle()), 5, 55);
        if (ship.state()==STATE_NORMAL)
            g2d.drawString("State: NORMAL", 5, 70);
        else if (ship.state()==STATE_COLLIDED)
            g2d.drawString("State: COLLIDED", 5, 70);
        else if (ship.state()==STATE_EXPLODING)
            g2d.drawString("State: EXPLODING", 5, 70);

        if (showBounds) {
            g2d.setColor(Color.GREEN);
            g2d.drawString("BOUNDING BOXES", SCREENWIDTH-150, 10);
        }
        if (collisionTesting) {
            g2d.setColor(Color.GREEN);
            g2d.drawString("COLLISION TESTING", SCREENWIDTH-150, 25);
        }

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
       if (showBounds) {
           if (ship.state() == STATE_COLLIDED)
               ship.drawBounds(Color.RED);
           else
               ship.drawBounds(Color.BLUE);
       }
    }

    /*****************************************************
     * drawBullets called by applet update event
     *****************************************************/
    public void drawBullets() {
        for (int n = 0; n < BULLETS; n++) {
            if (bullet[n].alive()) {
                //draw the bullet
                bullet[n].transform();
                bullet[n].draw();
                if (showBounds) {
                    if (bullet[n].state() == STATE_COLLIDED)
                        bullet[n].drawBounds(Color.RED);
                    else
                        bullet[n].drawBounds(Color.BLUE);
                }
            }
        }
    }

    /*****************************************************
     * drawAsteroids called by applet update event
     *****************************************************/
    public void drawAsteroids() {
        for (int n = 0; n < ASTEROIDS; n++) {
            if (ast[n].alive()) {
                //draw the asteroid
                ast[n].transform();
                ast[n].draw();
                if (showBounds) {
                    if (ast[n].state() == STATE_COLLIDED)
                        ast[n].drawBounds(Color.RED);
                    else
                        ast[n].drawBounds(Color.BLUE);
                }
            }
        }
    }

    /*****************************************************
     * drawExplosions
     *****************************************************/
    public void drawExplosions() {
        //explosions don't need separate update method
        if (explosion.alive()) {
            explosion.updateAnimation();
            if (explosion.currentFrame() == explosion.totalFrames()-1) {
                explosion.setCurrentFrame(0);
                explosion.setAlive(false);
            }
            else {
                explosion.draw();
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
        checkInput();
        updateShip();
        updateBullets();
        updateAsteroids();
        if (collisionTesting) {
            checkCollisions();
            handleShipCollisions();
            handleBulletCollisions();
            handleAsteroidCollisions();
        }
    }

    /*****************************************************
     * Update the ship position based on velocity
     *****************************************************/
    public void updateShip() {
        ship.updatePosition();
        double newx = ship.position().X();
        double newy = ship.position().Y();

        //wrap around left/right
        int w = ship.imageWidth()-1;
        if (ship.position().X() < -w)
            newx = SCREENWIDTH + w;
        else if (ship.position().X() > SCREENWIDTH + w)
            newx = -w;

        //wrap around top/bottom
        int h = ship.imageHeight()-1;
        if (ship.position().Y() < -h)
            newy = SCREENHEIGHT + h;
        else if (ship.position().Y() > SCREENHEIGHT + h)
            newy = -h;

        ship.setPosition(new Point2D(newx, newy));
        //ship.setState(SPRITE_NORMAL);
    }

    /*****************************************************
     * Update the bullets based on velocity
     *****************************************************/
    public void updateBullets() {
        //move the bullets
        for (int n = 0; n < BULLETS; n++) {
            if (bullet[n].alive()) {

                //update bullet's x position
                bullet[n].updatePosition();

                //bullet disappears at left/right edge
                if (bullet[n].position().X() < 0 ||
                    bullet[n].position().X() > SCREENWIDTH)
                {
                    bullet[n].setAlive(false);
                }

                //update bullet's y position
                bullet[n].updatePosition();

                //bullet disappears at top/bottom edge
                if (bullet[n].position().Y() < 0 ||
                    bullet[n].position().Y() > SCREENHEIGHT)
                {
                    bullet[n].setAlive(false);
                }

                bullet[n].setState(STATE_NORMAL);

            }
        }
    }

    /*****************************************************
     * Update the asteroids based on velocity
     *****************************************************/
    public void updateAsteroids() {
        //move and rotate the asteroids
        for (int n = 0; n < ASTEROIDS; n++) {
            if (ast[n].alive()) {
                //update the asteroid's position and rotation
                ast[n].updatePosition();
                ast[n].updateRotation();

                int w = ast[n].imageWidth()-1;
                int h = ast[n].imageHeight()-1;
                double newx = ast[n].position().X();
                double newy = ast[n].position().Y();

                //wrap the asteroid around the screen edges
                if (ast[n].position().X() < -w)
                    newx = SCREENWIDTH + w;
                else if (ast[n].position().X() > SCREENWIDTH + w)
                    newx = -w;
                if (ast[n].position().Y() < -h)
                    newy = SCREENHEIGHT + h;
                else if (ast[n].position().Y() > SCREENHEIGHT + h)
                    newy = -h;
                ast[n].setPosition(new Point2D(newx,newy));
                ast[n].setState(STATE_NORMAL);

            }
        }
    }

    /*****************************************************
     * Test asteroids for collisions with ship and bullets
     *****************************************************/
    public void checkCollisions() {
        //check for collision between asteroids and bullets
        for (int m = 0; m<ASTEROIDS; m++) {
            if ((ast[m].alive()) && (ast[m].state()==STATE_NORMAL)) {
                //iterate through the bullets
                for (int n = 0; n < BULLETS; n++) {
                    if ((bullet[n].alive()) && (bullet[n].state()==STATE_NORMAL)) {
                        //collision?
                        if (ast[m].collidesWith(bullet[n])) {
                            bullet[n].setState(STATE_COLLIDED);
                            ast[m].setState(STATE_COLLIDED);
                        }
                    }
                }
            }
        }

        //check for collision asteroids and ship
        if (ship.state() == STATE_NORMAL) {
            for (int m = 0; m < ASTEROIDS; m++) {
                if (ast[m].alive()) {
                    if (ship.collidesWith(ast[m])) {
                        ast[m].setState(STATE_COLLIDED);
                        ship.setState(STATE_COLLIDED);
                    }
                }
            }
        }
    }

    public void handleShipCollisions() {
        if (ship.state() == STATE_COLLIDED) {
            collisionTimer = System.currentTimeMillis();
            ship.setVelocity(new Point2D(0,0));
            ship.setState(STATE_EXPLODING);
            startExplosion(ship);
        }
        else if (ship.state() == STATE_EXPLODING) {
            if (collisionTimer + 3000 < System.currentTimeMillis()) {
                ship.setState(STATE_NORMAL);
            }
        }
    }

    public void startExplosion(Sprite sprite) {
        if (!explosion.alive()) {
            double x = sprite.position().X() - sprite.getBounds().width / 2;
            double y = sprite.position().Y() - sprite.getBounds().height / 2;
            explosion.setPosition(new Point2D(x, y));
            explosion.setCurrentFrame(0);
            explosion.setAlive(true);
        }
    }

    public void handleBulletCollisions() {
        for (int n = 0; n<BULLETS; n++) {
            if (bullet[n].state() == STATE_COLLIDED) {
                //nothing to do yet
            }
        }
    }

    public void handleAsteroidCollisions() {
        for (int n = 0; n<ASTEROIDS; n++) {
            if (ast[n].state() == STATE_COLLIDED) {
                //nothing to do yet
            }
        }
    }

    /*****************************************************
     * process keys that have been pressed
     *****************************************************/
    public void checkInput() {
        if (keyLeft) {
            //left arrow rotates ship left 5 degrees
            ship.setFaceAngle(ship.faceAngle() - 5);
            if (ship.faceAngle() < 0) ship.setFaceAngle(360 - 5);
        }
        else if (keyRight) {
            //right arrow rotates ship right 5 degrees
            ship.setFaceAngle(ship.faceAngle() + 5);
            if (ship.faceAngle() > 360) ship.setFaceAngle(5);
        }
        if (keyUp) {
            //up arrow applies thrust to ship
            applyThrust();
        }
    }

    /*****************************************************
     * key listener events
     *****************************************************/
    public void keyTyped(KeyEvent k) { }
    public void keyPressed(KeyEvent k) {
        switch (k.getKeyCode()) {
        case KeyEvent.VK_LEFT:
            keyLeft = true;
            break;
        case KeyEvent.VK_RIGHT:
            keyRight = true;
            break;
        case KeyEvent.VK_UP:
            keyUp = true;
            break;
        case KeyEvent.VK_CONTROL:
            keyFire = true;
            break;
        case KeyEvent.VK_B:
            //toggle bounding rectangles
            showBounds = !showBounds;
            break;
        case KeyEvent.VK_C:
            //toggle collision testing
            collisionTesting = !collisionTesting;
            break;
        }
    }
    public void keyReleased(KeyEvent k) {
        switch (k.getKeyCode()) {
        case KeyEvent.VK_LEFT:
            keyLeft = false;
            break;
        case KeyEvent.VK_RIGHT:
            keyRight = false;
            break;
        case KeyEvent.VK_UP:
            keyUp = false;
            break;
        case KeyEvent.VK_CONTROL:
            keyFire = false;
            fireBullet();
            break;
        }
    }

    public void applyThrust() {
        //up arrow adds thrust to ship (1/10 normal speed)
        ship.setMoveAngle(ship.faceAngle() - 90);

        //calculate the X and Y velocity based on angle
        double velx = ship.velocity().X();
        velx += calcAngleMoveX(ship.moveAngle()) * ACCELERATION;
        double vely = ship.velocity().Y();
        vely += calcAngleMoveY(ship.moveAngle()) * ACCELERATION;
        ship.setVelocity(new Point2D(velx, vely));

        //thrust.play();

    }
    public void fireBullet() {
        //fire a bullet
        currentBullet++;
        if (currentBullet > BULLETS - 1) currentBullet = 0;
        bullet[currentBullet].setAlive(true);

        //set bullet's starting point
        int w = bullet[currentBullet].imageWidth();
        int h = bullet[currentBullet].imageHeight();
        double x = ship.center().X() - w/2;
        double y = ship.center().Y() - h/2;
        bullet[currentBullet].setPosition(new Point2D(x,y));

        //point bullet in same direction ship is facing
        bullet[currentBullet].setFaceAngle(ship.faceAngle());
        bullet[currentBullet].setMoveAngle(ship.faceAngle() - 90);

        //fire bullet at angle of the ship
        double angle = bullet[currentBullet].moveAngle();
        double svx = calcAngleMoveX(angle) * BULLET_SPEED;
        double svy = calcAngleMoveY(angle) * BULLET_SPEED;
        bullet[currentBullet].setVelocity(new Point2D(svx, svy));

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
