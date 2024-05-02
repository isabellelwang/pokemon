import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import java.io.FileNotFoundException;

import javax.swing.*;
import javax.swing.text.DefaultCaret;

public class pokeGUI extends JFrame {
    static Image img;
    static int top = 3, left = 3, bottom = 3, right = 3;
    static Insets i = new Insets(top, left, bottom, right);

    /*
     * Constructor for the GUI
     */
    public pokeGUI() {
        setTitle("CSC210 Final Project: PokeDex");
        img = Toolkit.getDefaultToolkit().getImage("src/pokeball.png");
        setSize(500, 600);

        // setExtendedState(JFrame.MAXIMIZED_BOTH);

        MediaTracker track = new MediaTracker(this);
        track.addImage(img, 0);
        try {
            track.waitForID(0);
        } catch (InterruptedException ae) {
            System.out.println(ae);
        }

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent windowEvent) {
                System.exit(0);
            }
        });
    }

    // public void paint(Graphics g){
    // g.drawImage(img, 0, 0, null);
    // }
    /**
     * Creates a new frame for the user to input a pokemon
     */
    public static void createFrame() {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                JFrame frame = new JFrame("Pokemon Analysis");
                frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                JPanel panel = new JPanel();
                panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
                panel.setOpaque(true);
                JTextArea textArea = new JTextArea(15, 50);
                textArea.setWrapStyleWord(true);
                textArea.setLineWrap(true);
                textArea.setEditable(false);
                textArea.setFont(new Font("Monospace 821 BT", Font.PLAIN, 18));
                JScrollPane scroller = new JScrollPane(textArea);
                scroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
                scroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
                JPanel inputPanel = new JPanel();
                inputPanel.setLayout(new FlowLayout());
                JTextField input = new JTextField(20);
                input.setFont(new Font("Monospace 821 BT", Font.PLAIN, 18));
                pokeButton button = new pokeButton("Enter");
                textArea.append("What's ur fav pokemon? (Gen 1 only)\n");
                button.addActionListener(e -> {
                    PokeTable table = new PokeTable();
                    String pokemon = input.getText();
                    textArea.append(pokemon + "\n");
                    input.setText("");
                    textArea.append(pokemon + " is strongest against " + table.easyOpps(pokemon.toLowerCase()));
                    textArea.append("\n");
                    textArea.append(pokemon + " is weakest against " + table.hardOpps(pokemon.toLowerCase()));
                });
                DefaultCaret caret = (DefaultCaret) textArea.getCaret();
                caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
                panel.add(scroller);
                inputPanel.add(input);
                inputPanel.add(button);
                panel.add(inputPanel);
                frame.getContentPane().add(BorderLayout.CENTER, panel);
                frame.pack();
                frame.setLocationByPlatform(true);
                frame.setIconImage(img);
                frame.setVisible(true);
                frame.setResizable(true);
                input.requestFocus();

            }
        });
    }

    public static void printStats() {
        JFrame statsFrame = new JFrame("Pokemon Stats");
        PokemonStats strengthStats = new PokemonStats(Main.getStrength());
        PokemonStats weakStats = new PokemonStats(Main.getWeakness());

        statsFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        statsFrame.setSize(500, 500);
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        JPanel header = new JPanel();
        JLabel title = new JLabel("Project Stats");
        title.setFont(new Font("Britannic Bold", Font.BOLD, 50));
        header.add(title);

        JPanel body = new JPanel();
        body.setLayout(new BoxLayout(body, BoxLayout.Y_AXIS));
        body.setOpaque(true);

        JLabel text = new JLabel("What would you like to do?");
        text.setFont(new Font("Monospace 821 BT", Font.PLAIN, 18));
        text.setAlignmentX(CENTER_ALIGNMENT);

        JLabel promptType = new JLabel("Select a type");
        promptType.setFont(new Font("Monospace 821 BT", Font.PLAIN, 18));
        promptType.setAlignmentX(CENTER_ALIGNMENT);

        Choice choice = new Choice(); // Action options
        choice.setFont(new Font("Monospace 821 BT", Font.PLAIN, 18));

        choice.add("print stats");
        choice.add("run breadth first traversal");
        choice.add("see in degrees");
        choice.add("see out degrees");
        choice.add("see number of degrees");

        Choice typeChoice = new Choice(); // Pokemon type options
        typeChoice.setFont(new Font("Monospace 821 BT", Font.PLAIN, 18));
        for (String t : PokemonStats.getPokemonTypes(Main.strength)) {
            typeChoice.add(t);
        }

        JTextArea textArea = new JTextArea();
        textArea.setLayout(new BorderLayout());
        textArea.setWrapStyleWord(true);
        textArea.setLineWrap(true);
        textArea.setEditable(false);
        choice.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent itemEvent) {
                String selection = (String) choice.getSelectedItem();
                switch (selection) {
                    case "print stats":
                        body.remove(promptType);
                        body.remove(typeChoice);
                        body.revalidate();
                        body.repaint();
                        textArea.setText("");
                        textArea.setText("The stats for the Strength Graph are: \n");
                        textArea.append(PokemonStats.printStats(Main.strength));
                        textArea.append("\n\nThe stats for the Weakness Graph are: \n");
                        textArea.append(PokemonStats.printStats(Main.weakness));
                        break;

                    case "run breadth first traversal":
                        body.add(promptType); // BUG : Doesn't show up in page
                        body.add(typeChoice);
                        body.revalidate();
                        body.repaint();
                        typeChoice.addItemListener(i -> {
                            textArea.setText("");
                            textArea.setText("Commencing Breadth First Traversal at type "
                                    + typeChoice.getSelectedItem() + " ...\n");
                            textArea.append("\nBreadth First Traversal Strength Graph:\n");
                            textArea.append(
                                    PokemonStats.BreadthFirstTraversal(Main.strength, typeChoice.getSelectedItem())
                                            + "\n");
                            textArea.append("\n Breadth First Traversal Weakness Graph:\n");
                            textArea.append(
                                    PokemonStats.BreadthFirstTraversal(Main.weakness, typeChoice.getSelectedItem())
                                            + "\n");
                        });
                        break;

                    case "see in degrees":
                        body.add(promptType); // BUG : Doesn't show up in page
                        body.add(typeChoice);
                        body.revalidate();
                        body.repaint();
                        typeChoice.addItemListener(i -> {
                            textArea.setText("");
                            textArea.setText("");
                            textArea.append(
                                    "In Degree of type " + typeChoice.getSelectedItem() + " in Strength Graph is ");
                            textArea.append(String
                                    .valueOf(PokemonStats.numInDegree(Main.strength, typeChoice.getSelectedItem()))
                                    + "\n");
                            textArea.append(
                                    "\nIn Degree of type " + typeChoice.getSelectedItem() + " in Weakness Graph is ");
                            textArea.append(String
                                    .valueOf(PokemonStats.numInDegree(Main.weakness, typeChoice.getSelectedItem()))
                                    + "\n");
                        });
                        break;

                    case "see out degrees":
                        body.add(promptType); // BUG : Doesn't show up in page
                        body.add(typeChoice);
                        body.revalidate();
                        body.repaint();
                        typeChoice.addItemListener(i -> {
                            textArea.setText("");
                            textArea.setText("");
                            textArea.append(
                                    "Out Degree of type " + typeChoice.getSelectedItem() + " in Strength Graph is ");
                            textArea.append(String
                                    .valueOf(PokemonStats.numOutDegree(Main.strength, typeChoice.getSelectedItem()))
                                    + "\n");
                            textArea.append(
                                    "\nOut Degree of type " + typeChoice.getSelectedItem() + " in Weakness Graph is ");
                            textArea.append(String
                                    .valueOf(PokemonStats.numOutDegree(Main.weakness, typeChoice.getSelectedItem()))
                                    + "\n");
                        });
                        break;

                    case "see number of degrees":
                        body.add(promptType); // BUG : Doesn't show up in page
                        body.add(typeChoice);
                        body.revalidate();
                        body.repaint();
                        typeChoice.addItemListener(i -> {
                            textArea.setText("");
                            textArea.append("The number of degrees of type " + typeChoice.getSelectedItem()
                                    + " for the Strength graph is ");
                            textArea.append(String
                                    .valueOf(PokemonStats.numDegrees(Main.strength, typeChoice.getSelectedItem())));
                            textArea.append("\n\nThe number of degrees of type " + typeChoice.getSelectedItem()
                                    + " for the Weakness Graph is ");
                            textArea.append(String
                                    .valueOf(PokemonStats.numDegrees(Main.weakness, typeChoice.getSelectedItem())));
                        });
                        break;
                }

            }

        });
        body.add(textArea);
        body.add(text);
        body.add(choice);

        statsFrame.add(header, BorderLayout.NORTH);
        statsFrame.add(body, BorderLayout.CENTER);

        statsFrame.setIconImage(img);
        statsFrame.setVisible(true);
        statsFrame.setResizable(true);
    }

    // public void itemStateChanged(ItemEvent i) {
    // JFrame statsFrame = new JFrame("Pokemon Stats");
    // statsFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    // statsFrame.setSize(500, 500);
    // try {
    // UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    // } catch (Exception e) {
    // e.printStackTrace();
    // }
    // JPanel header = new JPanel();
    // JLabel title = new JLabel("Project Stats");
    // title.setFont(new Font("Britannic Bold", Font.BOLD, 50));
    // header.add(title);

    // System.out.println('i');
    // if (choice.getSelectedIndex() == 0) {
    // PokemonStats.printStats(Main.strength);
    // PokemonStats.printStats(Main.weakness);
    // } else if (choice.getSelectedIndex() == 1) {
    // System.out.println("your mom");
    // }

    // }

    /**
     * Main method for the GUI
     * 
     * @param args command line arguments(ignored)
     * @throws FileNotFoundException if data files not found
     */
    public static void main(String[] args) throws FileNotFoundException {
        pokeGUI pokeDex = new pokeGUI();
        try {
            Main.strengthMultiplier();
            Main.weaknessMultiplier();
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
            System.exit(-1);
        }
        // -----------------------------------------------------------------------------
        // HEADER
        Panel header = new Panel();
        // header components
        JLabel title = new JLabel("PokeDex");
        title.setFont(new Font("Britannic Bold", Font.BOLD, 100));
        // add components to header
        header.add(title);
        // -----------------------------------------------------------------------------
        // BODY
        Panel body = new Panel();
        body.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = i;
        // c.weightx=0.5; //c.weightx=0.0;
        c.weighty = 0.25; // c.weighty=0.5;

        // body components
        pokeButton strength = new pokeButton("Click to see type strengths graph");
        strength.addActionListener(i -> {
            Main.showGraph(Main.getStrength(), "Strength Graph");
        });
        pokeButton weakness = new pokeButton("Click to see type weaknesses graph");
        weakness.addActionListener(i -> {
            Main.showGraph(Main.getWeakness(), "Weakness Graph");
        });
        pokeButton pokemon = new pokeButton("Click to analyze a specific Pokemon");
        pokemon.addActionListener(i -> createFrame());
        pokeButton stats = new pokeButton("stats for nerds");
        stats.addActionListener(i -> printStats());
        // add components to body
        body.add(strength, c);
        body.add(weakness, c);
        body.add(pokemon, c);
        body.add(stats, c);
        // -----------------------------------------------------------------------------
        // FOOTER
        Panel footer = new Panel();
        // footer components
        JLabel footerText = new JLabel("Created by: Buddy, Isabelle, and Dani");
        footer.add(footerText);
        // -----------------------------------------------------------------------------
        // ADDING PANELS TO FRAME
        pokeDex.add(header, BorderLayout.NORTH);
        pokeDex.add(body, BorderLayout.CENTER);
        pokeDex.add(footer, BorderLayout.SOUTH);

        pokeDex.setIconImage(img);
        pokeDex.setResizable(false);

        pokeDex.setVisible(true);
    }
}