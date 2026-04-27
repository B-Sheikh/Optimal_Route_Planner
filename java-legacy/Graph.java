import java.util.*;

public class Graph {

    private Map<String, Map<String, Integer>> graph;

    public Graph() {
        graph = new HashMap<>();
    }

    public void addNode(String node) {

        if(!graph.containsKey(node)) {
            graph.put(node,new HashMap<>());
        }
    }

    public void addEdge(String node1,String node2,int weight) {

        addNode(node1);
        addNode(node2);

        graph.get(node1).put(node2,weight);
        graph.get(node2).put(node1,weight);
    }

    public void displayGraph() {

        System.out.println("\nGraph Structure:\n");

        for(String node : graph.keySet()) {
            System.out.println(node + " -> " + graph.get(node));
        }
    }

    public Map<String, Map<String, Integer>> getGraph(){
        return graph;
    }
}