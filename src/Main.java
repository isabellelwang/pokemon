import com.google.common.graph.GraphBuilder;
import com.google.common.graph.MutableGraph;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

class Main {
    public static MutableGraph<String> strength;
    public static MutableGraph<String> weakness;
    // public static void main(String[] args) {
    //
    // //group members: Dani and Isabelle
    //
    // // Mutable graphs can be changed after we build them
    // MutableGraph<String> graph =
    // GraphBuilder.directed().build();
    //
    // //System.out.println(graph instanceof MutableGraph);
    // //System.out.println(graph instanceof Graph);
    // //System.out.println(graph instanceof ValueGraph);
    // //System.out.println(graph instanceof Network);
    //
    //
    // // Here we make changes:
    // graph.addNode("Joe");
    // graph.putEdge("Jordan", "Nick"); // if edge references nodes that don't
    // exist, they get added
    //
    // new GraphDisplay(graph);
    // //System.out.println(graph);
    //
    // // Can add/remove anytime
    // graph.putEdge("Jordan", "Joe");
    //
    // //System.out.println(graph);
    //
    // // Immutable ones can't: you have to add everything before you build
    // ImmutableValueGraph<String,Integer> graph2 =
    // ValueGraphBuilder.undirected()
    // .<String,Integer>immutable()
    // .addNode("Ford")
    // .putEdgeValue("Bass", "McConnell", 1)
    // .putEdgeValue("Sabin-Reed", "McConnell", 0)
    // .putEdgeValue("Sabin-Reed", "Burton", 0) // edge values not necessarily
    // unique
    // .build();
    //
    // //System.out.println(graph2);
    // new GraphDisplay(graph2);
    //
    // // Throws error
    // // graph2.removeEdge("Bass", "McConnell");
    //
    // // Network allows objects on the edges
    // MutableNetwork<String,String> network = NetworkBuilder.undirected().build();
    // network.addEdge("Northampton","Boston","I-90");
    // network.addEdge("Northampton","New York","I-91");
    // network.addEdge("New York","Boston","I-95");
    //
    // System.out.println(network);
    // //System.out.println(network.incidentEdges("Northampton"));
    // //System.out.println(network.successors("Northampton"));
    // //System.out.println(network.incidentNodes("I-95"));
    // GraphDisplay d3 = new GraphDisplay(network);
    // d3.setNodeColors(Color.GREEN);
    // d3.setEdgeColors(Color.BLUE);
    // d3.setColor("Northampton",Color.RED);
    // for (Object n : d3.getNodeSet()) {
    // System.out.println(n+" : "+d3.getLabel(n));
    // }
    // for (Object e : d3.getEdgeSet()) {
    // System.out.println(e+" : "+d3.getLabel(e));
    // }
    // //System.out.println(network instanceof MutableGraph);
    // //System.out.println(network instanceof Graph);
    // //System.out.println(network instanceof ValueGraph);
    // //System.out.println(network instanceof Network);
    // }

    public static void main(String[] args) throws FileNotFoundException {
        File pokemonData = new File((args.length > 0) ? args[0] : "data/PokeTypeMatchupData.csv");
        String pokemon = pokemonData.getAbsolutePath();
        weaknessMultiplier();
        strengthMultiplier();
    }

    public static ArrayList<String> arrayify(String line) {
        ArrayList<String> array = new ArrayList<>();
        String data = "";
        while (line.contains(",")) {
            data = line.substring(0, line.indexOf(","));
            array.add(data);
            line = line.substring(line.indexOf(",") + 1);
        }
        array.add(line);
        return array;
    }

    public static HashMap<String, Integer> hashify(String line) {
        HashMap<String, Integer> hashmap = new HashMap<>();
        String data = "";
        int index = 0;
        while (line.contains(",")) {
            data = line.substring(0, line.indexOf(","));
            hashmap.put(data, index);
            line = line.substring(line.indexOf(",") + 1);
            index++;
        }
        hashmap.put(line, index);
        return hashmap;
    }
    // public static Object[] pokemonData(String filename) throws
    // FileNotFoundException{
    // int rows = 0;
    // int cols = 0;
    // try{
    // HashMap<Integer, String> pokemonData = new HashMap<>();
    // Scanner scan = new Scanner(new File(filename));
    // String line = scan.nextLine();
    // String data = "";
    // int index = 0;
    // while(line.contains(",")){
    // data = line.substring(0, line.indexOf(","));
    // pokemonData.put(index, data);
    // line = line.substring(line.indexOf(",")+1);
    // index++;
    // }
    // pokemonData.put(index, line);
    // cols = pokemonData.size();
    // while(scan.hasNext()){
    // line = scan.nextLine();
    // }
    //
    // } catch (FileNotFoundException e){
    // System.out.println("File not found");
    // }
    // return null;
    // }

    public static void weaknessMultiplier() throws FileNotFoundException {
        File strengthData = new File("chart.csv");
        // HashMap<String,Integer> types = new HashMap<>();
        ArrayList<String> types = new ArrayList<>();
        weakness = GraphBuilder.directed().allowsSelfLoops(true).build();
        try {
            Scanner scan = new Scanner(strengthData);
            String line = scan.nextLine();
            types = arrayify(line);

            for (String type : types) {
                weakness.addNode(type);
            }

            while (scan.hasNext()) {
                line = scan.nextLine();
                ArrayList<String> data = arrayify(line);
                for (int i = 1; i < data.size(); i++) {
                    if (data.get(i).equals("0.5")) {
                        weakness.putEdge(data.get(0), types.get(i));
                    }
                }

            }

            // for (String type : types) {
            // graph.addNode(type);
            // }
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        }
        new GraphDisplay(weakness);
    }

    public static void strengthMultiplier() throws FileNotFoundException {
        File strengthData = new File("chart.csv");
        // HashMap<String,Integer> types = new HashMap<>();
        ArrayList<String> types = new ArrayList<>();
        strength = GraphBuilder.directed().allowsSelfLoops(true).build();
        try {
            Scanner scan = new Scanner(strengthData);
            String line = scan.nextLine();
            types = arrayify(line);

            for (String type : types) {
                strength.addNode(type);
            }
            while (scan.hasNext()) {
                line = scan.nextLine();
                ArrayList<String> data = arrayify(line);
                for (int i = 1; i < data.size(); i++) {
                    if (data.get(i).equals("2")) {
                        strength.putEdge(types.get(i), data.get(0));
                    }
                }

            }

            // for (String type : types) {
            // graph.addNode(type);
            // }
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        }
        new GraphDisplay(strength);
    }
}