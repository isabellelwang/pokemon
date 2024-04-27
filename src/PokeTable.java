import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Scanner;

public class PokeTable{
    private Hashtable<String, ArrayList<String>> table;

    /**
     * Constructor for table of pokemon and their types
     * Key = pokemon Value = Types
     */
    public PokeTable(){
        this.table = new Hashtable<>();

        File file = new File("gen01.csv");

          try{
            Scanner sc = new Scanner(file);

            while(sc.hasNext()){
                String line = sc.nextLine().replaceAll(String.valueOf('"'), "");
                for(int i = 1; i < line.length(); i++){
                    String[] tokens = line.split(",",13); // Categories
                    ArrayList<String> types = new ArrayList<>();
                    types.add(tokens[3]); // Type 1 
                    if (!tokens[4].equals(" ")){
                    types.add(tokens[4]); // Type 2
                }
                    table.put(tokens[1].toLowerCase(), types);
                }
            }
            sc.close();
        } catch (Exception e){
            System.out.println("File not found");
        }
    }

    /**
     * Finds types of given pokemon
     * @param pokemon
     * @return Array of pokemon types
     */
    public ArrayList<String> getTypes(String pokemon){
        try {
            return this.table.get(pokemon);
        } catch (Exception e) {
            throw new RuntimeException("Invalid Pokemon");
        }
    }

    /**
     * Find the eastiest opps to defeat
     * @param type
     * @return Array List of opp types
     */
    public  ArrayList<String> easyOpps(String pokemon){
        ArrayList<String> easyOpps = new ArrayList<>(); 
        ArrayList<String> types = getTypes(pokemon); // types of given pokemon

        for (String t : types){
            for (String opp : Main.strength.predecessors(t)){ // get nodes that point at this type 
                if (!easyOpps.contains(opp)){
                    easyOpps.add(opp);
                }
            }
        }
        return easyOpps;
    }

    /**
     * Find the hardest opps to defeat
     * @param type
     * @return Array List of opp types
     */
    public  ArrayList<String> hardOpps(String pokemon){
        ArrayList<String> hardOpps= new ArrayList<>();
        ArrayList<String> types = getTypes(pokemon); // types of given pokemon
        for (String t : types){
            for (String opp : Main.weakness.successors(t)){ // get nodes that this node points at 
                if (!hardOpps.contains(opp)){
                hardOpps.add(opp);
                }
            }
        }
        return hardOpps;
    }


    public static void main(String[] args){ 

        try {
            Main.strengthMultiplier();
            Main.weaknessMultiplier();
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        }

        PokeTable poketable = new PokeTable(); // Set up
        System.out.println(poketable.table.toString());
        Scanner in = new Scanner(System.in);

        System.out.println("What's ur fav pokemon");
        String pokemon = in.nextLine().toLowerCase(); // get input 

        System.out.println(pokemon + " is strongest against " + poketable.easyOpps(pokemon)); 
        System.out.println(pokemon + " is weakest against " + poketable.hardOpps(pokemon));

        in.close();
    }
}
