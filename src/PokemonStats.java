import com.google.common.graph.GraphBuilder;
import com.google.common.graph.MutableGraph;
import com.google.common.graph.*;
import com.google.common.graph.EndpointPair;

import java.io.FileNotFoundException;
import java.io.File;
import java.util.*;

public class PokemonStats {
    private static ArrayList<String> typesList; // set of types
    private static Set<EndpointPair<String>> nEdge; // sets of edges
    private static Hashtable<String, Integer> numPerNode; // <type, number of edges connected to it>

    public PokemonStats(MutableGraph<String> graph) {
        Set<String> typesSet = graph.nodes();
        typesList = new ArrayList<>();
        for (String t : typesSet) { // convert from set to list
            typesList.add(t);
        }

        nEdge = graph.edges(); // initialize edges
        numPerNode = new Hashtable<String, Integer>(); // initialize hashtable
        // match degree to the type/node
        for (String type : typesList) {
            numPerNode.put(type, graph.degree(type));
        }
    }

    // print all nodes
    public static ArrayList<String> getNodes(MutableGraph<String> graph) {
        return typesList;
    }

    /**
     * Number of nodes in the graph
     * 
     * @return int number of nodes
     */
    public static int numNodes(MutableGraph<String> graph) {
        return graph.nodes().size();
    }

    /**
     * Total number of edges in graph
     * 
     * @param graph graph to be examined
     * @return int number of edges in the graph
     */
    public static int totalEdges(MutableGraph<String> graph) {
        return graph.edges().size();
    }

    public static Set<EndpointPair<String>> getEdges(MutableGraph<String> graph) {
        return graph.edges();
    }

    /**
     * node with the maximum degree
     * 
     * @param graph
     * @return
     */
    public static int maxNodeDegree(MutableGraph<String> graph) {
        int maxIndex = 0;
        for (int i = 1; i < typesList.size(); i++) {
            if (numPerNode.get(typesList.get(i)) > numPerNode.get(typesList.get(maxIndex))) {
                maxIndex = i;
            }
        }
        return numPerNode.get(typesList.get(maxIndex));
    }

    /**
     * return node with minimum degree
     * 
     * @param graph
     * @return
     */
    public static int minNodeDegree(MutableGraph<String> graph) {
        int minIndex = 0;
        for (int i = 1; i < typesList.size(); i++) {
            if (numPerNode.get(typesList.get(i)) < numPerNode.get(typesList.get(minIndex))) {
                minIndex = i;
            }
        }
        return numPerNode.get(typesList.get(minIndex));
    }

    public static int avgNodeDegree(MutableGraph<String> graph) {
        int degreeSum = 0;
        for (int i = 0; i < typesList.size(); i++) {
            degreeSum += numPerNode.get(typesList.get(i));
        }
        return degreeSum / numNodes(graph);
    }

    /**
     * return degree of the node
     * 
     * @param graph
     * @param n
     * @return
     */
    public static int numNodeDegree(MutableGraph<String> graph, String n) {
        return graph.degree(n);
    }

    public static int numOutDegree(MutableGraph<String> strGraph, String n) {
        return strGraph.outDegree(n);
    }

    public static int numInDegree(MutableGraph<String> wkGraph, String n) {
        return wkGraph.inDegree(n);
    }

    // public static ArrayList<String> pokemonInTypes(PokeTable_Test table, String
    // type) {
    // ArrayList<String> pkm = new ArrayList<String>();

    // }

    /**
     * returns the node that n is stronger than
     */
    public static String strongerThan(MutableGraph<String> graph, String n) {
        return "";
    }

    public static String weakerThan(MutableGraph<String> graph, String n) {
        return "";
    }

    public static String printStats(MutableGraph<String> graph, String n) {
        return "";
    }

    /**
     * Breadth first traversal starting from node start
     * 
     * @param graph
     * @param start
     * @return
     */
    public static void BreadthFirstTraversal(MutableGraph<String> graph, String start) {
        ArrayDeque<String> queue = new ArrayDeque<String>();
        ArrayDeque<String> visited = new ArrayDeque<String>();

        visited.add(start);
        queue.add(start);

        System.out.println("Commencing Breadth First Traversal at type " + start);

        while (!queue.isEmpty()) {
            String visitingNode = queue.pollFirst();
            Set<String> adjNodes = graph.adjacentNodes(visitingNode);

            for (String n : adjNodes) {
                if (!visited.contains(n)) {
                    visited.addLast(n);
                    queue.addLast(n);
                }
            }
        }
        System.out.println(visited.toString());

    }

    public static void main(String[] args) {
        try {
            Main.strengthMultiplier();
            // Main.weaknessMultiplier();
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        }
        PokemonStats strengthStats = new PokemonStats(Main.strength);

        System.out.println("The nodes in the Strength graph are: " + getNodes(Main.strength));
        System.out.println("The number of nodes in the strength chart is: " + numNodes(Main.strength));
        System.out.println("The number of edges in total in Strength graph is " + totalEdges(Main.strength));
        System.out.println("Max node degree is " + maxNodeDegree(Main.strength));
        System.out.println("Min node degree is " + minNodeDegree(Main.strength));

        // PokemonStats weakStats = new PokemonStats(Main.weakness);
        // System.out.println("The nodes in the weak graph are: " +
        // getNodes(Main.weakness));
        // System.out.println("The number of nodes in the weak chart is: " +
        // numNodes(Main.weakness));
        // System.out.println("The number of edges in total in weakness graph is " +
        // totalEdges(Main.weakness));
        // System.out.println("Max node degree is " + maxNodeDegree(Main.weakness));
        // System.out.println("Min node degree is " + minNodeDegree(Main.strength));

        BreadthFirstTraversal(Main.strength, "Ice");

        // ystem.out.println(getEdges(Main.strength));
    }

}