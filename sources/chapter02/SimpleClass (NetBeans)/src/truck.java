public class truck extends vehicle
{
    private String model;
    private String engine;
    private int towingcapacity;

    public truck()
    {
        setMake("make");
        setNumWheels(4);
        setModel("model");
        setEngine("engine");
        setTowingCapacity(0);
    }

    public truck(String make, String model, String engine, int towing) {
        setMake(make);
        setModel(model);
        setEngine(engine);
        setTowingCapacity(towing);
    }

    public String getModel() {
        return model;
    }
    public void setModel(String newmodel) {
        model = newmodel;
    }

    public String getEngine() {
        return engine;
    }
    public void setEngine(String newengine) {
        engine = newengine;
    }

    public int getTowingCapacity() {
        return towingcapacity;
    }
    public void setTowingCapacity(int value) {
        towingcapacity = value;
    }
}
