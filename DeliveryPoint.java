// Represents a delivery location or landmark in the city
public class DeliveryPoint {

    private String name;
    private int x;   // GUI x-coordinate
    private int y;   // GUI y-coordinate
    private boolean isWarehouse;
    private boolean isDelivered;

    public DeliveryPoint(String name, int x, int y, boolean isWarehouse) {
        this.name = name;
        this.x = x;
        this.y = y;
        this.isWarehouse = isWarehouse;
        this.isDelivered = false;
    }

    public String getName()        { return name; }
    public int getX()              { return x; }
    public int getY()              { return y; }
    public boolean isWarehouse()   { return isWarehouse; }
    public boolean isDelivered()   { return isDelivered; }
    public void setDelivered(boolean d) { this.isDelivered = d; }

    @Override
    public String toString() {
        return name;
    }
}
