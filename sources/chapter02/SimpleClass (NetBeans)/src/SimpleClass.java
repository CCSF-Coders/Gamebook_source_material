import java.lang.*;
import javax.swing.*;
import java.awt.*;

public class SimpleClass extends JFrame
{
    vehicle car;
    truck lightning;

    public SimpleClass()
    {
        super("SimpleClass");
        setSize(600,400);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //initialize a vehicle object
        car = new vehicle();
        car.setMake("Ford");
        car.setNumWheels(4);
        //initialize a truck object using a constructor
        lightning = new truck("Ford SVT", "F-150 Lightning", "5.4L Triton V8", 7700);
        lightning.
    }

    public void paint(Graphics g)
    {
        //let's use a nice big font
        g.setFont(new Font("Verdana", Font.BOLD, 12));

        //display the car info
        g.drawString("Car make: " + car.getMake(), 20, 50);
        g.drawString("Number of wheels: " + car.getNumWheels(), 20, 70);

        //display the truck info
        g.drawString("Truck make: " + lightning.getMake(), 20, 90);
        g.drawString("Truck model: " + lightning.getModel(), 20, 110);
        g.drawString("Truck engine: " + lightning.getEngine(), 20, 130);
        g.drawString("Truck towing capacity: " +
                     lightning.getTowingCapacity(), 20, 150);
    }

    public static void main(String[] args)
    {
        new SimpleClass();
    }

}



