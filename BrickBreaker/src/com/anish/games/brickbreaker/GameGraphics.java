package com.anish.games.brickbreaker;

import java.awt.*;
import java.awt.geom.*;
import java.awt.event.*;

import javax.swing.*;

public class GameGraphics extends JComponent implements KeyListener, ActionListener {
	
	// Obtaining the dimensions of the screen
	Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	
	// Attributes of the paddle
	private int paddleWidth = 140;
	private int paddleHeight = 15;
	
	private int maxPaddleX = screenSize.width - paddleWidth;
	private int minPaddleX = 0;
	
	private int maxPaddleY = screenSize.height - 60;
	private int minPaddleY = maxPaddleY - 40;
	
	// These are the initial coordinates of the paddle
	private int initPaddleX = (screenSize.width - paddleWidth) / 2;
	private int initPaddleY = maxPaddleY;
	
	// Initializing the coordinates of the paddle
	private int paddleX = initPaddleX;
	private int paddleY = initPaddleY;
	
	private int shiftDistance = 20;
	
	// Attributes of the ball
	
	// These are the initial coordinates of the ball
	private int initBallX = screenSize.width / 2 - 200;
	private int initBallY = screenSize.height / 2 + 100;
	
	// Initializing the coordinates of the paddle
	private int ballX = initBallX;
	private int ballY = initBallY;
	
	private int ballWidth = 20;
	private int ballHeight = 20;
	
	// These are the initial velocities of the ball
	private int initBallVelX = 2;
	private int initBallVelY = 2;
	
	// Initializing the velocities of the ball in 'X' and 'Y' directions
	private int ballVelX = initBallVelX;
	private int ballVelY = initBallVelY;
	
	// The time delay of the moving ball 
	private int delayTime = 5;
	
	// Attributes of the bricks
	// The number of rows of bricks
	private int rowsOfBricks = 3;
	
	// The number of columns of bricks
	private int columnsOfBricks = 10;
	
	// This is the space between the screen and the edge bricks towards the side
	private int leftSpace = 124;
	
	// This is the blank space between the bricks
	// If the bricks do not fit with your screen dimensions, please change this value
	private int blankSpace = 2;
	
	// This huge expression is to align the bricks symmetrically
	private int brickWidth = (screenSize.width - 2 * leftSpace - (columnsOfBricks - 1) * blankSpace) / columnsOfBricks;
	private int brickHeight = 50;
	
	private int brickX = leftSpace;
	private int brickY = 120;
	
	// A timer to set the ball moving
	Timer ballTimer = new Timer(delayTime, this);
	
	// A brick map to identify which bricks were hit
	private boolean brickMap[][] = new boolean [rowsOfBricks][columnsOfBricks];
	
	// This is the score of the player
	private int score = 0;
	private int scoreOfBrick = 10;
	private int maxScore = rowsOfBricks * columnsOfBricks * scoreOfBrick;
	private int sizeOfText = 30;
	private int scoreX = (screenSize.width - sizeOfText - 100) / 2;
	private int scoreY = 40;
	
	// This variable is to determine whether the play is in progress or not
	private boolean play = true;
	
	// This variable is to determine whether the game is to be restarted or not
	private boolean restart = false;
	
	public GameGraphics() {
		
		// Allowing to respond to key commands
		setFocusable(true);
		
		addKeyListener(this);
		
		// Initially all the bricks are present
		for(int i = 0; i < rowsOfBricks; ++i)
		{
			for(int j = 0; j < columnsOfBricks; ++j)
				brickMap[i][j] = true;
		}
	}
	
	public void paintComponent(Graphics g) {
		
		Graphics2D g2 = (Graphics2D) g;
		
		g2.setColor(Color.BLACK);
		g2.setFont(new Font("Serif", Font.BOLD, sizeOfText));
		g2.drawString("Score : " + score, scoreX, scoreY);
		
		// If the game is to be stopped
		if(play == false)
		{
			ballVelX = ballVelY = 0;
			g2.setFont(new Font("Serif", Font.BOLD, sizeOfText * 2));
			
			if(score == maxScore)
				g2.drawString("You Won!", (screenSize.width - sizeOfText - 280) / 2, (screenSize.height) / 2);
			else
				g2.drawString("Game Over!", (screenSize.width - sizeOfText - 280) / 2, (screenSize.height) / 2);
			
			g2.drawString("Press 'Enter' to restart", (screenSize.width - sizeOfText - 540) / 2, (screenSize.height) / 2 + sizeOfText + 20);
		}
		
		// If the game is to be restarted
		if(restart == true)
		{
			// All the bricks have to be repainted
			for(int i = 0; i < rowsOfBricks; ++i)
			{
				for(int j = 0; j < columnsOfBricks; ++j)
					brickMap[i][j] = true;
			}
			
			// Initializing the position of the ball
			ballX = initBallX;
			ballY = initBallY;
			
			// Initializing the position of the paddle
			paddleX = initPaddleX;
			paddleY = initPaddleY;
			
			// Initializing the velocities of the ball
			ballVelX = initBallVelX;
			ballVelY = initBallVelY;
			
			// Re-assigning the score
			score = 0;
			
			// Starting the play
			play = true;
			
			// Disabling restart so that the game is in progress
			restart = false;
		}
		
		// Drawing the paddle on the frame
		g2.setColor(Color.GREEN.darker());
		g2.fillRect(paddleX, paddleY, paddleWidth, paddleHeight);
		
		// Coloring the bricks red
		g2.setColor(Color.RED.darker());
		// Filling the bricks row-wise
		
		int y = brickY;
		
		for(int i = 0; i < rowsOfBricks; ++i)
		{
			int x = leftSpace;
			
			for(int j = 0; j < columnsOfBricks; ++j)
			{
				// If the brick was never hit, fill it with red
				if(brickMap[i][j] == true)
					g2.fillRect(x, y, brickWidth, brickHeight);
				else
				{
					g2.setColor(Color.LIGHT_GRAY);
					// If the brick was hit, changing the color as the background
					g2.fillRect(x, y, brickWidth, brickHeight);
					
					// Re-setting the color to red
					g2.setColor(Color.RED.darker());
				}
				
				x += brickWidth + blankSpace;
			}
			
			// Leaving a space of some pixels between the bricks
			y += blankSpace + brickHeight;
		}
		
		// Drawing the ball on the screen
		g2.setColor(Color.BLUE);
		g2.fillOval(ballX, ballY, ballWidth, ballHeight);
		
		// Starting the timer of the ball to get it moving
		ballTimer.start();
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
		
		// Obtaining the key which was pressed
		int pressedKey = e.getKeyCode();
		
		// If 'enter' is pressed after the game is over, restart the game
		if((pressedKey == KeyEvent.VK_ENTER) && (play == false))
			restart = true;
		
		// If the game is on hold, the paddle is not to be moved
		if(play == false)
			return;
		
		// If right key is pressed, move the paddle to the right
		if(pressedKey == KeyEvent.VK_RIGHT)	
			movePaddleRight();
		// If the left key is pressed, move the paddle to the left
		else if(pressedKey == KeyEvent.VK_LEFT)
			movePaddleLeft();		
		// If the up key is pressed, move the paddle up
		// Note that you can move the paddle upwards only to a certain level
		else if(pressedKey == KeyEvent.VK_UP)
			movePaddleUp();
		// If the down key is pressed, move the paddle down
		// Note that you can move the paddle downwards only to a certain level
		else if(pressedKey == KeyEvent.VK_DOWN)
			movePaddleDown();
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		
		// Updating the coordinates of the ball
		ballX += ballVelX;
		ballY += ballVelY;
		
		// If the ball goes above the screen, reflect it
		if(ballY < 0)
		{
			ballY = 0;
			ballVelY *= -1;
		}
		
		// If the ball goes beyond the left end of the screen, reflect it
		if(ballX < 0)
		{
			ballX = 0;
			ballVelX *= -1;
		}
		
		// If the ball goes beyond the right end of the screen, reflect it
		if(ballX > screenSize.width)
		{
			ballX = screenSize.width;
			ballVelX *= -1;
		}
		
		// If the ball goes under the screen or if all the bricks were hit, the game is over
		if((ballY > screenSize.height) || (score == maxScore))
			play = false;
		
		// If the ball touches the paddle, reflect the ball
		if(new Rectangle(paddleX, paddleY, paddleWidth, paddleHeight).intersects(ballX, ballY, ballWidth, ballHeight) == true)
		{
			// Only if the ball hits the upper surface of the paddle, it has to be reflected
			if(ballY + ballHeight <= paddleY + paddleHeight)
				ballVelY *= -1;
		}
		
		// Finding out whether the ball hit any brick
		for(int i = 0; i < rowsOfBricks; ++i)
		{
			for(int j = 0; j < columnsOfBricks; ++j)
			{
				// If the brick was already hit, continue searching for the other ones
				if(brickMap[i][j] == false)
					continue;
				
				// A reference rectangle to find out whether it intersected the ball
				Rectangle2D.Double refRectangle = new Rectangle2D.Double(brickX + j * (brickWidth + blankSpace), 
						brickY + i * (brickHeight + blankSpace), brickWidth, brickHeight);
				
				// Noting that the brick has been hit
				if(refRectangle.intersects(ballX, ballY, ballWidth, ballHeight) == true)
				{
					score += scoreOfBrick;
					
					// Noting that the brick has been hit
					brickMap[i][j] = false;
					
					// Determining the fate of the ball
					
					// Storing the X and Y coordinates of the brick which is hit
					int refBrickX = brickX + j * (brickWidth + blankSpace);
					int refBrickY = brickY + i * (brickHeight + blankSpace);
					
					// If the ball hits the side of the brick, motion in 'X' direction needs to change
					
					// The ball hits the left side of the brick
					if(ballX + ballWidth / 2 <= refBrickX)
					{
						// This condition deals with multiple collisions
						if(ballVelX > 0)
							ballVelX *= -1;
					}

					// The ball hits the right side of the brick
					if(refBrickX + brickWidth <= ballX + ballWidth / 2)
					{
						// This condition deals with multiple collisions
						if(ballVelX < 0)
							ballVelX *= -1;
					}
					
					// If the ball hits the top or bottom of the brick, motion in the 'Y' direction needs to change
					
					// The ball hits the top of the brick
					if(ballY + ballHeight / 2 <= refBrickY)
					{
						// This condition deals with multiple collisions
						if(ballVelY > 0)
							ballVelY *= -1;
					}
					
					// The ball hits the bottom of the brick
					if(refBrickY + brickHeight <= ballY + ballHeight / 2)
					{
						// This condition deals with multiple collisions
						if(ballVelY < 0)
							ballVelY *= -1;
					}
				}
			}
		}
		
		repaint();
	}
	
	// Moves the paddle to the right
	public void movePaddleRight() {
		
		// Shifting the paddle to the right and repainting it
		paddleX += shiftDistance;
		
		// The paddle cannot move beyond the screen towards right
		if(paddleX > maxPaddleX)
			paddleX = maxPaddleX;
		
		repaint();
	}
		
	// Moves the paddle to the left
	public void movePaddleLeft() {
		
		// Shifting the paddle to the left and repainting it
		paddleX-= shiftDistance;
		
		// The paddle cannot move beyond the screen towards left
		if(paddleX < minPaddleX)
			paddleX = minPaddleX;
		
		repaint();
	}
	
	// Move the paddle upwards
	public void movePaddleUp() {
		
		// Shifting the paddle upwards and repainting it
		paddleY -= shiftDistance;
		
		// The paddle cannot move up a certain level
		if(paddleY < minPaddleY)
			paddleY = minPaddleY;
		
		repaint();
	}
	
	// Move the paddle upwards
	public void movePaddleDown() {
		
		// Shifting the paddle downwards and repainting it
		paddleY += shiftDistance;
		
		// The paddle cannot move down a certain level
		if(paddleY > maxPaddleY)
			paddleY = maxPaddleY;
		
		repaint();
	}
	
	// We aren't bothered about these methods
	// They have to be overridden as the KeyListener interface is implemented
	@Override
	public void keyReleased(KeyEvent e) {}

	@Override
	public void keyTyped(KeyEvent e) {}
}
