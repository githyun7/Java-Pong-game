import java.awt.Graphics;
import java.util.Random;


public class Ball extends Instance 
{
	Random rand = new Random();
	// Set Radius of ball
	int radius = 16;
	
	// Set Variables
	public Ball() 
	{
		// Set X and Y
		super(250 - 8, 0);
		this.y = rand.nextInt(161) + 95;
	}
	
	// Graphics stuff to render ball
	@Override
	public void render(Graphics g) 
	{
		// Render it using this 
		g.fillOval(this.x, this.y, this.radius, this.radius);
	}
}
