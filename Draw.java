import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.*;
import java.util.Random;
public class Draw extends JPanel implements Runnable, KeyListener
{
	// Declare Bitfield to record multiple user inputs cleanly
	private Thread t;
	private int bitfield;
	
	// Declare Variables for Objects in Pong
	Paddle right;
	Paddle left;
	Ball ball;
  
	// Set Variables for Game
	int velocity = 0;
	int counter = 0;
	int difficulty = 3; // 1 == Pro; 2 == Hard; 3 == Medium; 4 == Easy; 5 == Baby Mode
	int speed_x = 3; // Default/Start speed
	int speed_y = 2;
	
	// Declare Score Variables
	int score1;
	int score2;
	boolean is_ai;
	Random rand = new Random(); // Create object for AI randomizer
	
	// Declare Timeout time (in ms)
	private int timeout_ctr = 0;
	private int timeout_for = 0;
	int scored = 0; // Store if someone scored, to change appropriate text
	// Draw Class - Set up game
	public Draw()
	{
		int tableTop = 50;
		int paddleY = 125 + tableTop; // still centered inside rectangle
		
		this.right = new Paddle("right", 10, paddleY);  	// Left paddle (10 px from left)
		this.left = new Paddle("left", 485, paddleY); 	// Right paddle (15 px from right, since width is 5)
		this.ball = new Ball();
		this.score1 = 0;
		this.score2 = 0;
		
		super.setFocusable(true);
	    super.addKeyListener(this);
	}
	// Paint Class - Draw as game updates
	public void paint(Graphics g)
	{
		super.paint(g); // Clears the panel
		// Draw scoreboard text above the table
		g.setFont(new Font("Arial", Font.BOLD, 20));
		if (scored == 1)
		{
			g.setColor(Color.MAGENTA);
			g.drawString("" + score1, 125, 30);
			g.drawString("Left Scored", 197, 30);
			g.setColor(Color.BLACK);
			g.drawString("" + score2, 375, 30);
		}
		else if (scored == 2)
		{
			g.drawString("" + score1, 125, 30);
			g.setColor(Color.MAGENTA);
			g.drawString("Right Scored", 190, 30);
			g.drawString("" + score2, 375, 30);
			g.setColor(Color.BLACK);
		}
		else
		{
			g.drawString("" + score1, 125, 30);
			g.drawString("" + score2, 375, 30);
		}
		// Lowered rectangle and center line (shifted 50px down)
		g.drawRect(0, 50, 500, 300);           	// y = 50 //Width 295
		g.drawLine(250, 50, 250, 350);         	// from y=50 to y=350
		// Draw game elements
		this.right.render(g);
		this.left.render(g);
		this.ball.render(g);
	}
	
	 @Override
	 public void keyPressed(KeyEvent e)
	 {
		 // Uses bitfield  instead of many booleans bc itz cool
		 // If key pressed set appropriate bit every frame
		 if (e.getKeyCode() == KeyEvent.VK_W)
			 this.set_bit(1 << 1);
		 else if (e.getKeyCode() == KeyEvent.VK_S)
			 this.set_bit(1 << 2);
		 else if (e.getKeyCode() == KeyEvent.VK_UP && !this.is_ai)
			 this.set_bit(1 << 3);
		 else if (e.getKeyCode() == KeyEvent.VK_DOWN  && !this.is_ai)
			 this.set_bit(1 << 4);
		
		// Paint again to update paddles
		repaint();
	 }
	
	// Bitfield for recording user inputs
	@Override
	public void keyReleased(KeyEvent e)
	{
		// If key not pressed remove appropriate bit every frame
		if(e.getKeyCode()== KeyEvent.VK_W)
			this.remove_bit(1 << 1);
		 else if(e.getKeyCode()== KeyEvent.VK_S)
			 this.remove_bit(1 << 2);
		 else if(e.getKeyCode()== KeyEvent.VK_UP)
			 this.remove_bit(1 << 3);
		 else if(e.getKeyCode()== KeyEvent.VK_DOWN)
			 this.remove_bit(1 << 4);
	}
	
	// Just need it or Java has an aneurysm
	@Override
	public void keyTyped(KeyEvent e)
	{
	}
	// Run method
	@Override
	public void run()
	{
		// Always run = 100fps
		while (true)
		{
			final int TIMEOUT = 10;
			
			try
			{
				Thread.sleep(TIMEOUT);
			}
			catch (InterruptedException e)
  			{   
				e.printStackTrace();
			}
			
			// Timeout stuff
			if (this.timeout_for > 0)
			{
				if (this.timeout_ctr >= this.timeout_for) // End timeout
				{
					this.timeout_ctr = 0;
					this.timeout_for = 0;
					this.scored = 0;
				}
				else
				{
					this.timeout_ctr += TIMEOUT;
					continue;
				}
			}
			
			// Move according to user-input per update
			if (this.has_bit(1 << 1) && right.y > 55)
				right.y -= 5;
			if (this.has_bit(1 << 2) && right.y < 295)
				right.y += 5;
			if (this.has_bit(1 << 3) && left.y > 55)
				left.y -= 5;
			if (this.has_bit(1 << 4) && left.y < 295)
				left.y += 5;
			
			// Check if ai is available, do_ai
			if (this.is_ai)
				this.do_ai();
			
			// Move ball in x direction
			this.ball.x += speed_x;
			// Move ball left or right based on sing of x
			if (speed_x < 0)
				this.ball.x -= velocity;
			else
				this.ball.x += velocity;
			
			// Move ball in y-direction
			this.ball.y += speed_y;
			
			// Check for if ball hits the right paddle
			if (this.ball.x >= 475)
				this.calc_hit(this.left, score1);
			else if (this.ball.x <= 15)
				this.calc_hit(this.right, score2);		
			
			// Check if ball hit the ceiling or floor, if it did change y velocity
			if (this.ball.y >= 335)
				speed_y *= -1;
			else if (this.ball.y <= 50)
				speed_y *= -1;
			// Paint everything
			repaint();
			// Check if someone won
			if (score1 == 10)
			{
				// Display screen and exit
			    JOptionPane.showMessageDialog(null, "Player 1 wins!");
			    System.exit(0);
			}
			else if (score2 == 10)
			{
				// Display screen and exit
			    JOptionPane.showMessageDialog(null, "Player 2 wins!");
			    System.exit(0);
			}
		}	
	}
	
	// Start Method
	public void start(boolean is_ai)
	{
		// Check if AI is on or not
		if (this.t == null)
		{
			this.is_ai = is_ai;
			this.t = new Thread(this, "Renderer");
			this.t.start();
		}
	}
	
	// Reset_ball Method
	private void reset_ball()
	{
		// Set Paddle positions to middle
		this.right.y = 175;
		this.left.y = 175;
		
		// Set ball to middle
		this.ball.x = 250 - 8;
		this.ball.y = 200;
		this.velocity = 0;
		this.set_timeout(1);
	}
	
	// Calculate hit method
	private void calc_hit(Paddle p, int score)
	{
		// Make ball move opposite y velocity
		speed_x *= -1;
		// Find relative ball and paddle positions
		int relative_ball = this.ball.y - 55 + 16;	
		int relative_paddle = p.y - 55;
		
		// Check for collision
		if (!((relative_ball >= relative_paddle) && (relative_ball <= relative_paddle + 50 + 16)))
		{	
			if (p.name == "right")
			{
				scored = 2;
				score2++;
			}
			else
			{
				scored = 1;
				score1++;
			}
			System.out.println("Right Scored: ");
			this.reset_ball();
		}
		else // hit paddle
		{
			// Increase a counter
			counter++;
			if (counter == difficulty) // If it equals difficulty increase velocity and reset counter
			{
				velocity++;
				counter = 0;
			}
			// Set ball to position outside paddle so it doesn't bug out
			if (p.name == "right")
				this.ball.x = 15 + 2;
			else
				this.ball.x = 475 - 2;
		}
	}
	
	// AI here
	public void do_ai()
	{		
		int ai_speed = speed_y; // Set AI
		if (rand.nextInt(100) <= 22) // 0-99 < 22
			ai_speed = 0; // Stop ball
		
		int currentball = this.ball.y; // See current ball position
		int futureball = this.ball.y + ai_speed; // Sees future ball position
		
		if (currentball > futureball) // If ball going down, move paddle down
			left.y -= 2;
		if (currentball < futureball) // If ball going up, move paddle up
			left.y += 2;
	}
	
	// Method for setting duration
	void set_timeout(int seconds) // How many seconds you want inputted
	{
		timeout_for = seconds * 1000; // Multiply ms by 1000 to get seconds
	}
	
	// Check if a bit is set
	public boolean has_bit(int bit)
	{
		return (this.bitfield & bit) == bit;
	}
	// Set a bit
	public void set_bit(int bit)
	{
		this.bitfield |= bit;
	}
	
	// Remove a bit
	public void remove_bit(int bit)
	{
		this.bitfield &= ~bit;
	}
}
