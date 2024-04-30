import com.google.common.graph.GraphBuilder;
import com.google.common.graph.MutableGraph;
import com.google.common.graph.*;
import com.google.common.graph.EndpointPair;

import java.io.FileNotFoundException;
import java.io.File;
import java.util.*;
import java.util.Scanner;

public class PokemonStats {
    private static ArrayList<String> typesList; // set of types
    private static Set<EndpointPair<String>> nEdge; // sets of edges
    private static Hashtable<String, Integer> numPerNode; // <type, number of edges connected to it>

    // running
    public static void main(String[] args) {
        stateract();
    }

    /**
     * Constructor for graph statistics
     * 
     * @param graph mutable graph to analyze
     */
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

    /**
     * prints all nodes
     * 
     * @param graph graph to print all nodes
     * @return arraylist nodes
     */
    public static ArrayList<String> getPokemonTypes(MutableGraph<String> g) {
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

    /**
     * Returns the edge pairs of graph g
     * 
     * @param graph graph g to show edge pairs
     * @return Edge pairs of the type it matches to
     */
    public static Set<EndpointPair<String>> getEdges(MutableGraph<String> graph) {
        return graph.edges();
    }

    /**
     * node with the maximum degree
     * 
     * @param graph Mutable graph
     * @return maximum degree
     */
    public static int maxFoes(MutableGraph<String> graph) {
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
     * @param graph MutableGraph
     * @return int minimum degree of the graph
     */
    public static int minFoes(MutableGraph<String> graph) {
        int minIndex = 0;
        for (int i = 1; i < typesList.size(); i++) {
            if (numPerNode.get(typesList.get(i)) < numPerNode.get(typesList.get(minIndex))) {
                minIndex = i;
            }
        }
        return numPerNode.get(typesList.get(minIndex));
    }

    /**
     * Average number of enemies Pokemons have.
     * 
     * @param graph graph of enemy
     * @return int average number of enemies
     */
    public static int avgFoes(MutableGraph<String> graph) {
        int degreeSum = 0;
        for (int i = 0; i < typesList.size(); i++) {
            degreeSum += numPerNode.get(typesList.get(i));
        }
        return degreeSum / numNodes(graph);
    }

    /**
     * return degree of the node
     * 
     * @param graph Mutable graph g to be analyzed
     * @param n     Type to see degree of nodes
     * @return int degree
     */
    public static int numDegrees(MutableGraph<String> graph, String n) {
        return graph.degree(n);
    }

    /**
     * Number of outdegrees for Node n
     * 
     * @param strGraph graph to see outdegrees
     * @param n        String n type
     * @return int outdegree of node n
     */
    public static int numOutDegree(MutableGraph<String> strGraph, String n) {
        return strGraph.outDegree(n);
    }

    /**
     * Number of in degrees for Node n
     * 
     * @param wkGraph graph to see in degrees
     * @param n       String Node n
     * @return int number of in degrees
     */
    public static int numInDegree(MutableGraph<String> wkGraph, String n) {
        return wkGraph.inDegree(n);
    }

    /**
     * Prints the statistics of the graph
     * 
     * @param g Mutable graph
     */
    public static void printStats(MutableGraph<String> g) {
        System.out.println("The nodes in the graph are: " + getPokemonTypes(g));
        System.out.println("The number of nodes in the chart is: " + numNodes(g));
        System.out.println("The number of edges in total in graph is " + totalEdges(g));
        System.out.println("Max node degree is " + maxFoes(g));
        System.out.println("Min node degree is " + minFoes(g));
    }

    /**
     * runs breadth first traversal at a starting node
     * 
     * @param graph Mutablegraph g
     * @param start String node to start
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

    /**
     * different user optoins
     * 
     * @param l Arraylist to add the options
     */
    private static void userOptions(ArrayList<String> l) {
        l.add("print stats");
        l.add("run breadth first traversal");
        l.add("see in degree");
        l.add("see out degree");
        l.add("see number of degrees");
        l.add("exit");
    }

    /**
     * Interact w/ user to print statistics.
     */
    public static void stateract() {
        try {
            Main.strengthMultiplier();
            Main.weaknessMultiplier();
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        }

        PokemonStats strengthStats = new PokemonStats(Main.strength);
        PokemonStats weaknessStats = new PokemonStats(Main.weakness);
        ArrayList<String> options = new ArrayList<String>();
        userOptions(options);

        Scanner input = new Scanner(System.in);
        System.out.println("Welcome to PokeStats.");

        System.out.println("What would you like to do?");
        System.out.println(
                "print stats \nrun breadth first traversal \nsee in degree \nsee out degree \nsee number of degrees \nexit");
        String response = input.nextLine();

        while (options.contains(response) && !response.equals("exit")) {
            switch (response) {
                case "print stats":
                    printStats(Main.strength);
                    printStats(Main.weakness);
                    break;

                case "run breadth first traversal":
                    System.out.println(getPokemonTypes(Main.strength));
                    System.out.println("Which type would you like to start at?");
                    response = input.nextLine();

                    while (!typesList.contains(response)) {
                        System.out.println("Which type would you like to start at?");
                        response = input.nextLine();
                    }

                    System.out.println("Breadth First Traversal Strength Graph: ");
                    BreadthFirstTraversal(Main.strength, response);
                    System.out.println("Breadth First Traversal Weakness Graph: ");
                    BreadthFirstTraversal(Main.weakness, response);
                    break;

                case "see in degree":
                    System.out.println(getPokemonTypes(Main.strength));
                    System.out.println("Which type would you like to see?");
                    response = input.nextLine();

                    while (!typesList.contains(response)) {
                        System.out.println("Which type would you like to see?");
                        response = input.nextLine();
                    }

                    System.out.println("In Degree of type" + response + " in Strength Graph");
                    System.out.println(numInDegree(Main.strength, response));
                    System.out.println("In Degree of type" + response + " in Weakness Graph");
                    System.out.println(numInDegree(Main.weakness, response));
                    break;

                case "see out degree":
                    System.out.println(getPokemonTypes(Main.strength));
                    System.out.println("Which type would you like to see?");
                    response = input.nextLine();

                    while (!typesList.contains(response)) {
                        System.out.println("Which type would you like to see?");
                        response = input.nextLine();
                    }

                    System.out.println("Out Degree of type" + response + " in Strength Graph");
                    System.out.println(numOutDegree(Main.strength, response));
                    System.out.println("Out Degree of type" + response + " in Weakness Graph");
                    System.out.println(numOutDegree(Main.weakness, response));
                    break;

                case "see number of degrees":
                    System.out.println(getPokemonTypes(Main.strength));
                    System.out.println("Which type would you like to see?");
                    response = input.nextLine();

                    while (!typesList.contains(response)) {
                        System.out.println("Which type would you like to see?");
                        response = input.nextLine();
                    }

                    System.out.println("Number of degrees for type" + response + " in Strength Graph");
                    System.out.println(numDegrees(Main.strength, response));
                    System.out.println("Number of degrees for type" + response + " in Weakness Graph");
                    System.out.println(numDegrees(Main.weakness, response));
                    break;

                case "exit":
                    break;
            }

            System.out.print("Press enter");
            input.nextLine();
            System.out.println("What would you like to do?");
            System.out.println(
                    "print stats \nrun breadth first traversal \nsee in degree \nsee out degree\nsee number of degrees \nexit");
            response = input.nextLine();
        }
    }

}