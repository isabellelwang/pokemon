import com.google.common.graph.GraphBuilder;
import com.google.common.graph.MutableGraph;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.Color;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.*;
import javax.swing.text.DefaultCaret;

public class pokeGUI extends JFrame{
    static Image img;
    static int top = 3, left = 3, bottom = 3, right = 3;
    static Insets i = new Insets(top, left, bottom, right);
    public pokeGUI(){
        setTitle("CSC210 Final Project: PokeDex");
        img = Toolkit.getDefaultToolkit().getImage("src/pokeball.png");
        setSize(500, 500);

//        setExtendedState(JFrame.MAXIMIZED_BOTH);

        MediaTracker track = new MediaTracker(this);
        track.addImage(img,0);
        try {
            track.waitForID(0);
        } catch(InterruptedException ae){
            System.out.println(ae);
        }

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent windowEvent){
                System.exit(0);
            }
        });
    }
//    public void paint(Graphics g){
//        g.drawImage(img, 0, 0, null);
//    }
    public static void createFrame()
    {
        EventQueue.invokeLater(new Runnable()
        {
            @Override
            public void run()
            {
                JFrame frame = new JFrame("Test");
                frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                try
                {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                JPanel panel = new JPanel();
                panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
                panel.setOpaque(true);
                JTextArea textArea = new JTextArea(15, 50);
                textArea.setWrapStyleWord(true);
                textArea.setEditable(false);
                textArea.setFont(new Font("Monospace 821 BT", Font.PLAIN, 18));
                JScrollPane scroller = new JScrollPane(textArea);
                scroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
                scroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
                JPanel inputpanel = new JPanel();
                inputpanel.setLayout(new FlowLayout());
                JTextField input = new JTextField(20);
                input.setFont(new Font("Monospace 821 BT", Font.PLAIN, 18));
                pokeButton button = new pokeButton("Enter");
                button.addActionListener(e -> {
                    String text = input.getText();
                    textArea.append(text + "\n");
                    input.setText("");
                });
                DefaultCaret caret = (DefaultCaret) textArea.getCaret();
                caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
                panel.add(scroller);
                inputpanel.add(input);
                inputpanel.add(button);
                panel.add(inputpanel);
                frame.getContentPane().add(BorderLayout.CENTER, panel);
                frame.pack();
                frame.setLocationByPlatform(true);
                frame.setIconImage(img);
                frame.setVisible(true);
                frame.setResizable(false);
                input.requestFocus();

            }
        });
    }

    public static void main(String[] args) throws FileNotFoundException{
        pokeGUI pokeDex = new pokeGUI();
//-----------------------------------------------------------------------------
    // HEADER
        Panel header = new Panel();
        //header components
        JLabel title = new JLabel("PokeDex");
        title.setFont(new Font("Britannic Bold", Font.BOLD, 100));
        // add components to header
        header.add(title);
//-----------------------------------------------------------------------------
    // BODY
        Panel body = new Panel();
        body.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = i;
//        c.weightx=0.5; //c.weightx=0.0;
        c.weighty=0.25; //c.weighty=0.5;

        // body components
        pokeButton strength = new pokeButton("Click to see type strengths graph");
        strength.addActionListener(e -> {
            try {
                Main.strengthMultiplier();
            } catch (FileNotFoundException ex) {
                throw new RuntimeException(ex);
            }
        });
        pokeButton weakness = new pokeButton("Click to see type weaknesses graph");
        weakness.addActionListener(e -> {
            try {
                Main.weaknessMultiplier();
            } catch (FileNotFoundException ex) {
                throw new RuntimeException(ex);
            }
        });
        pokeButton pokemon = new pokeButton("Click to analyze a specific Pokemon");
        pokemon.addActionListener(e -> createFrame());
        // add components to body
        body.add(strength, c);
        body.add(weakness, c);
        body.add(pokemon, c);
//-----------------------------------------------------------------------------
    // FOOTER
        Panel footer = new Panel();
        // footer components
        JLabel footerText = new JLabel("Created by: Buddy, Isabelle, and Dani");
        footer.add(footerText);
//-----------------------------------------------------------------------------
        // ADDING PANELS TO FRAME
        pokeDex.add(header, BorderLayout.NORTH);
        pokeDex.add(body, BorderLayout.CENTER);
        pokeDex.add(footer, BorderLayout.SOUTH);

        pokeDex.setIconImage(img);

        pokeDex.setVisible(true);
    }
}
