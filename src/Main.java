import com.google.common.graph.GraphBuilder;
import com.google.common.graph.MutableGraph;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

class Main {
    public static MutableGraph<String> strength;
    public static MutableGraph<String> weakness;

    // running method.
    public static void main(String[] args) throws FileNotFoundException {
        File pokemonData = new File((args.length > 0) ? args[0] : "pokemon/PokeTypeMatchupData.csv");
        String pokemon = pokemonData.getAbsolutePath();
        weaknessMultiplier();
        strengthMultiplier();
    }

    /**
     * changes line in file to an arraylsit
     * 
     * @param line String line
     * @return Arraylist of lines as each component
     */
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

    /**
     * creates a hashmap for the index and the line in file
     * 
     * @param line String line in the file
     * @return Hashmap with line as key and index as value.
     */
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

    /**
     * Adds weak pokemons with multiplier of 0.5 in a graph
     * 
     * @throws FileNotFoundException file not found
     */
    public static void weaknessMultiplier() throws FileNotFoundException {
        File strengthData = new File("pokemon/chart.csv");

        ArrayList<String> types = new ArrayList<>();
        weakness = GraphBuilder.directed().allowsSelfLoops(true).build();
        try {
            Scanner scan = new Scanner(strengthData);
            String line = scan.nextLine();
            types = arrayify(line);

            for (String type : types) {
                if (!type.equals("Attacking")) {
                    weakness.addNode(type);

                }
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
            scan.close();

        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        }

    }

    /**
     * Adds strong pokemon with multiplier of 2 into a graph
     * 
     * @throws FileNotFoundException file not found
     */
    public static void strengthMultiplier() throws FileNotFoundException {
        File strengthData = new File("pokemon/chart.csv");

        ArrayList<String> types = new ArrayList<>();
        strength = GraphBuilder.directed().allowsSelfLoops(true).build();
        try {
            Scanner scan = new Scanner(strengthData);
            String line = scan.nextLine();
            types = arrayify(line);

            for (String type : types) {
                if (!type.equals("Attacking")) {
                    strength.addNode(type);

                }
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
            scan.close();

        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        }
    }

    /**
     * getter method to access Strength graph
     * 
     * @return MutableGraph of strength graph
     */
    public static MutableGraph<String> getStrength() {
        return strength;
    }

    /**
     * Getter method to access Weakness graph
     * 
     * @return MutableGraph of weakness graph
     */
    public static MutableGraph<String> getWeakness() {
        return weakness;
    }

    /**
     * Displays graph into a new window
     * 
     * @param graph     mutable graph to be shown
     * @param graphName label for the graph
     */
    public static void showGraph(MutableGraph<String> graph, String graphName) {
        new GraphDisplay(graph, graphName);
    }
}