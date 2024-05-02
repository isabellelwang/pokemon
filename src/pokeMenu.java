import javax.swing.*;
import java.awt.*;

public class pokeMenu {

    public pokeMenu(){
        Choice menu = new Choice(); // Pokemon type options
        for (String t : PokemonStats.getPokemonTypes(Main.strength)){
            typeChoice.add(t);
        }
    }
}
