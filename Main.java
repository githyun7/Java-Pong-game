import java.awt.Component;
import javax.swing.*;
// Main function
public class Main
{
	 public static void main(String[] args)
	 {
		// Launches title screen
       Main main = new Main();
       Menu Choose = new Menu(main);
       // Declare Object for JFrame
       JFrame Selection = new JFrame("HOME");
       Selection.setSize(640, 400);
       Selection.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
       Selection.add(Choose);
       // Open Window
       Selection.setVisible(true);
   }
	// Launches Game function
   public void startGame(boolean is_ai)
   {
   	Draw draw = new Draw(); //Object for drawing
		//Frame for Pong
		JFrame frame = new JFrame("PONG");
		frame.setSize(640, 400); //Size of Pong
		//Add stuff
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(draw);
		frame.setVisible(true);
		
		draw.start(is_ai);
   }
}
