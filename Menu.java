import javax.swing.*;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
public class Menu extends JPanel
{
	// Set Buttons to objects
   static JButton TwoPlayer;
   static JButton Computer;
  
   // Create an object of Main
   private Main mainInstance;
   // Menu function
   public Menu(Main main)
   {
   	// Set the object of main to main
       this.mainInstance = main;
       // Set Layout
       setLayout(new GridBagLayout());
       // Declare Label and buttons
       JLabel Prompt = new JLabel("PIBBLE PING PONG");
       TwoPlayer = new JButton("2 Player");
       Computer = new JButton("Computer");
       // Declare and set constraints
       GridBagConstraints constraints = new GridBagConstraints();
       constraints.insets = new Insets(10, 10, 10, 10);
       constraints.gridx = 0;
       constraints.gridy = 0;
       add(Prompt, constraints);
       constraints.gridy = 1;
       add(TwoPlayer, constraints);
       constraints.gridy = 2;
       add(Computer, constraints);
       // Add listeners
       setupListeners();
   }
   // Adding listeners function
   private void setupListeners()
   {
   	// Add actionListeners to buttons
       TwoPlayer.addActionListener(new ActionListener()
       {
           public void actionPerformed(ActionEvent e)
           {
               mainInstance.startGame(false);
           }
       });
       Computer.addActionListener(new ActionListener()
       {
           public void actionPerformed(ActionEvent e)
           {
               mainInstance.startGame(true);
           }
       });
   }
}
