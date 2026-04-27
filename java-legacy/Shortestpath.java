import java.util.*;

public class Shortestpath {

    public List<String> dijkstra(
        Map<String, Map<String, Integer>> graph,
        String start,
        String end) {

        Map<String,Integer> distances = new HashMap<>();
        Map<String,String> previous = new HashMap<>();

        PriorityQueue<Node> queue = new PriorityQueue<>();

        for(String vertex : graph.keySet()) {
            distances.put(vertex,Integer.MAX_VALUE);
            previous.put(vertex,null);
        }

        distances.put(start,0);
        queue.add(new Node(start,0));

        while(!queue.isEmpty()) {

            Node current = queue.poll();
            String currentVertex = current.vertex;

            if(currentVertex.equals(end)) break;

            for(Map.Entry<String,Integer> neighbor :
                graph.get(currentVertex).entrySet()) {

                String next = neighbor.getKey();
                int weight = neighbor.getValue();

                int newDist = distances.get(currentVertex)+weight;

                if(newDist < distances.get(next)) {

                    distances.put(next,newDist);
                    previous.put(next,currentVertex);

                    queue.add(new Node(next,newDist));
                }
            }
        }

        List<String> path = new ArrayList<>();

        for(String at=end; at!=null; at=previous.get(at)) {
            path.add(0,at);
        }

        return path;
    }
}

class Node implements Comparable<Node>{

    String vertex;
    int distance;

    public Node(String vertex,int distance){
        this.vertex = vertex;
        this.distance = distance;
    }

    public int compareTo(Node other){
        return this.distance - other.distance;
    }
}
