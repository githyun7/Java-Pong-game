import java.awt.Graphics;
// Declare Instance class
public abstract class Instance
{
	// Set x and y
	int x;
	int y;
	// Give object x and y
	public Instance(int x, int y)
	{
   		this.x = x;
   		this.y = y;
	}
	// Abstract method to give object to render
	public abstract void render(Graphics g);
}
