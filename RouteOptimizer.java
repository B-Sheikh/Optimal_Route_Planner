import java.util.*;

// Computes shortest path between two DeliveryPoints using Dijkstra's algorithm
public class RouteOptimizer {

    // Inner node class for the priority queue
    private static class NodeEntry implements Comparable<NodeEntry> {
        String name;
        double dist;

        NodeEntry(String name, double dist) {
            this.name = name;
            this.dist = dist;
        }

        @Override
        public int compareTo(NodeEntry other) {
            return Double.compare(this.dist, other.dist);
        }
    }

    // Returns the shortest path as a list of location names
    public List<String> findShortestPath(DeliveryGraph graph, String start, String end) {

        Map<String, Double>  distances = new HashMap<>();
        Map<String, String>  previous  = new HashMap<>();
        PriorityQueue<NodeEntry> pq    = new PriorityQueue<>();

        for (String node : graph.getAdjacency().keySet()) {
            distances.put(node, Double.MAX_VALUE);
            previous.put(node, null);
        }

        distances.put(start, 0.0);
        pq.add(new NodeEntry(start, 0.0));

        while (!pq.isEmpty()) {
            NodeEntry current = pq.poll();

            if (current.name.equals(end)) break;

            // Skip stale entries
            if (current.dist > distances.get(current.name)) continue;

            for (Map.Entry<String, Double> neighbour :
                    graph.getNeighbours(current.name).entrySet()) {

                double newDist = distances.get(current.name) + neighbour.getValue();

                if (newDist < distances.get(neighbour.getKey())) {
                    distances.put(neighbour.getKey(), newDist);
                    previous.put(neighbour.getKey(), current.name);
                    pq.add(new NodeEntry(neighbour.getKey(), newDist));
                }
            }
        }

        // Reconstruct path
        List<String> path = new ArrayList<>();
        for (String at = end; at != null; at = previous.get(at)) {
            path.add(0, at);
        }

        // If path doesn't start at 'start', no route exists
        if (path.isEmpty() || !path.get(0).equals(start)) {
            return new ArrayList<>();
        }

        return path;
    }

    // Returns total distance of a given path
    public double calculateTotalDistance(DeliveryGraph graph, List<String> path) {
        double total = 0.0;
        for (int i = 0; i < path.size() - 1; i++) {
            Map<String, Double> neighbours = graph.getNeighbours(path.get(i));
            if (neighbours.containsKey(path.get(i + 1))) {
                total += neighbours.get(path.get(i + 1));
            }
        }
        return total;
    }

    // Greedy multi-stop: nearest unvisited delivery next, using Dijkstra between each pair
    public List<String> planMultiStopRoute(DeliveryGraph graph,
                                           String warehouse,
                                           List<String> deliveries) {

        List<String> fullRoute   = new ArrayList<>();
        List<String> remaining   = new ArrayList<>(deliveries);
        String current           = warehouse;

        fullRoute.add(warehouse);

        while (!remaining.isEmpty()) {
            String nearest    = null;
            double minDist    = Double.MAX_VALUE;
            List<String> bestSegment = null;

            for (String target : remaining) {
                List<String> segment = findShortestPath(graph, current, target);
                double dist = calculateTotalDistance(graph, segment);
                if (dist < minDist) {
                    minDist    = dist;
                    nearest    = target;
                    bestSegment = segment;
                }
            }

            if (nearest == null) break;

            // Append segment (skip first node — already in route)
            for (int i = 1; i < bestSegment.size(); i++) {
                fullRoute.add(bestSegment.get(i));
            }

            remaining.remove(nearest);
            current = nearest;
        }

        // Return to warehouse
        List<String> returnPath = findShortestPath(graph, current, warehouse);
        for (int i = 1; i < returnPath.size(); i++) {
            fullRoute.add(returnPath.get(i));
        }

        return fullRoute;
    }
}
