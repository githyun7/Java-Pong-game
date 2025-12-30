import java.awt.Graphics;
public class Paddle extends Instance
{
	// Declare Necessary variables
	int width = 5;
	int height = 50;
	String name;
	// Set Variables to object
	public Paddle(String name, int x, int y)
	{
		super(x, y);
		this.name = name;
	}
	
	// Draw it
	@Override
	public void render(Graphics g)
	{
		g.fillRect(this.x, this.y, this.width, this.height);
	}
}