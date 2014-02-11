import java.lang.*;
import java.applet.*;
import java.awt.*;

public class SimpleClass extends Applet {
    vehicle car;
    truck lightning;

    public void init() {
        //initialize a vehicle object
        car = new vehicle();
        car.setMake("Ford");
        car.setNumWheels(4);
        //initialize a truck object using a constructor
        lightning = new truck("Ford SVT", "F-150 Lightning", "5.4L Triton V8", 7700);
    }

    public void paint(Graphics g) {
        //let's use a nice big font
        g.setFont(new Font("Verdana", Font.BOLD, 12));

        //display the car info
        g.drawString("Car make: " + car.getMake(), 20, 20);
        g.drawString("Number of wheels: " + car.getNumWheels(), 20, 40);

        //display the truck info
        g.drawString("Truck make: " + lightning.getMake(), 20, 70);
        g.drawString("Truck model: " + lightning.getModel(), 20, 90);
        g.drawString("Truck engine: " + lightning.getEngine(), 20, 110);
        g.drawString("Truck towing capacity: " +
                     lightning.getTowingCapacity(), 20, 130);
    }
}



