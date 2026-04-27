import java.util.*;

// Encapsulates the city road network as a weighted undirected graph
public class DeliveryGraph {

    private Map<String, DeliveryPoint> points;
    private Map<String, Map<String, Double>> adjacency;

    public DeliveryGraph() {
        points     = new LinkedHashMap<>();
        adjacency  = new HashMap<>();
    }

    public void addPoint(DeliveryPoint dp) {
        points.put(dp.getName(), dp);
        adjacency.putIfAbsent(dp.getName(), new HashMap<>());
    }

    public void addRoad(Road road) {
        adjacency.get(road.getFrom()).put(road.getTo(),   road.getDistanceKm());
        adjacency.get(road.getTo()).put(road.getFrom(),   road.getDistanceKm());
    }

    public Map<String, DeliveryPoint> getPoints()              { return points; }
    public Map<String, Map<String, Double>> getAdjacency()     { return adjacency; }

    public DeliveryPoint getPoint(String name) {
        return points.get(name);
    }

    // Returns neighbour -> distance map for a given node
    public Map<String, Double> getNeighbours(String node) {
        return adjacency.getOrDefault(node, new HashMap<>());
    }
}
