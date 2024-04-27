import java.io.File;
import java.util.Hashtable;
import java.util.Scanner;

public class PokeTable_Test{

    private Hashtable<String, String> table;

    public PokeTable_Test(){
        this.table = new Hashtable<>();

        File file = new File("gen01.csv");

          try{
            Scanner sc = new Scanner(file);

            while(sc.hasNext()){
                String line = sc.nextLine().replaceAll(String.valueOf('"'), "");
                for(int i = 1; i < line.length(); i++){
                    String[] tokens = line.split(",",13);
                    table.put(tokens[1].toLowerCase(), tokens[3] + " " + tokens[4]);
                }
            }
            sc.close();
        } catch (Exception e){
            System.out.println("File not found");
        }
    }

    public static void main(String[] args){
        PokeTable_Test poketable = new PokeTable_Test();
        System.out.println(poketable.table.toString());

        Scanner in = new Scanner(System.in);
        String pokemon = in.nextLine().toLowerCase();

        if (poketable.table.containsKey(pokemon)){ 
            // Find Pokemon Return types
        } else {
            System.out.println("I can't find it!");
        }
        in.close();
    }
}
