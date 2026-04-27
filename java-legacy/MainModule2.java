import java.util.*;

public class MainModule2 {

    public static void main(String[] args) {

        Graph graph = new Graph();

        graph.addEdge("A","B",5);
        graph.addEdge("A","C",3);
        graph.addEdge("B","D",2);
        graph.addEdge("C","D",4);

        graph.displayGraph();

        Shortestpath sp = new Shortestpath();

        List<String> path =
        sp.dijkstra(graph.getGraph(),"A","D");

        System.out.println("\nShortest Path from A to D:");
        System.out.println(path);
    }
}