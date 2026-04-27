public class MainModule1 {

    public static void main(String[] args) {

        Graph graph = new Graph();

        graph.addEdge("A","B",5);
        graph.addEdge("A","C",3);
        graph.addEdge("B","D",2);
        graph.addEdge("C","D",4);

        graph.displayGraph();
    }
}
