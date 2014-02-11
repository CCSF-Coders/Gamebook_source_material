import java.applet.*;
import java.awt.*;

public class SimpleApplet extends Applet {

    public void paint(Graphics g) {
        g.drawString("This simple applet runs in a web browser.", 20, 30);
    }
}


class vehicle {
    private int numwheels;

    public int getNumWheels() {
        return numwheels;
    }

    public boolean setNumWheels(int count) {
        if (count > 0 && count < 20) {
            numwheels = count;
            return true;
        } else {
            return false;
        }
    }

}
