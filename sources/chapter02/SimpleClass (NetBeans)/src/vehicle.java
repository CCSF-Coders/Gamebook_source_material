public class vehicle {
    private String make;
    private int numwheels;

    public String getMake() {
        return make;
    }

    public boolean setMake(String newmake) {
        if (newmake.length() > 0) {
            make = newmake;
            return true;
        } else {
            return false;
        }
    }

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
