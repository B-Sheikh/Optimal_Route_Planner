// Represents a road (edge) connecting two locations with a distance in km
public class Road {

    private String from;
    private String to;
    private double distanceKm;

    public Road(String from, String to, double distanceKm) {
        this.from = from;
        this.to = to;
        this.distanceKm = distanceKm;
    }

    public String getFrom()          { return from; }
    public String getTo()            { return to; }
    public double getDistanceKm()    { return distanceKm; }

    @Override
    public String toString() {
        return from + " <-> " + to + " (" + distanceKm + " km)";
    }
}
