/*********************************************************
 * Beginning Java Game Programming, 3rd Edition
 * by Jonathan S. Harbour
 * KeyboardTest Program
 **********************************************************/

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class KeyboardTest extends JFrame implements KeyListener {

    int keyCode;
    char keyChar;

    public static void main(String[] args) {
        new KeyboardTest();
    }
    
    public KeyboardTest() {
        super("Keyboard Test");
        setSize(500,400);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        addKeyListener(this);
    }

    public void paint(Graphics g) {
        Graphics2D g2d = (Graphics2D)g;
        
        g2d.setColor(Color.WHITE);
        g2d.fill(new Rectangle(0,0,500,400));
        
        g2d.setColor(Color.BLACK);
        g2d.drawString("Press a key...", 20, 40);
        g2d.drawString("Key code: " + keyCode, 20, 60);
        g2d.drawString("Key char: " + keyChar, 20, 80);
    }

    public void keyPressed(KeyEvent e) {
        keyCode = e.getKeyCode();
        keyChar = ' ';
        repaint();
    }

    public void keyReleased(KeyEvent e) {
    }

    public void keyTyped(KeyEvent e) {
        keyChar = e.getKeyChar();
        repaint();
    }

}
