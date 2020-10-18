package com.anish.games.snakegame;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.awt.geom.Rectangle2D.Double;

import javax.swing.*;
import javax.swing.Timer;
import java.util.*;

public class GameGraphics extends JComponent implements KeyListener, ActionListener{
	
	// Obtaining the dimensions of the screen
	Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	
	// This is the size (of the square) of each part of the snake
	private int snakeBlockSize = 18;
	
	// The initial coordinates of the head of the snake
	private int initSnakeHeadX = screenSize.width / 2;
	private int initSnakeHeadY = screenSize.height / 2;
	
	// The coordinates of the head of the snake
	private int snakeHeadX = initSnakeHeadX;
	private int snakeHeadY = initSnakeHeadY;
	
	// These are the initial velocities of the snake in X and Y direction
	private int initSnakeVelX = 2;
	private int initSnakeVelY = 2;
	
	// This is the information of the text displayed on the screen
	private int sizeOfText = 30;
	private int scoreX = (screenSize.width - sizeOfText - 100) / 2;
	private int scoreY = 40;
	private int scoreIncrease = 1;
	
	// These are the coordinates of the food particle to be generated
	private int foodX;
	private int foodY;
	
	// This is the diameter of the food particle
	private int foodSize = 15;
	
	// This is the number of blocks by which the size of the snake is increased once it eats its food
	int increaseSize = 5;
	
	// Upper bounds for the coordinates of the food
	private int upperBoundYRandom = screenSize.height - 40;
	private int upperBoundXRandom = screenSize.width - snakeBlockSize;
	
	// Determines whether the snake ate the food or not
	private boolean ateFood = true;
	
	// This stores whether the game is in progress or not
	private boolean play = true;
	
	// The boolean which stores whether the game has to be restarted or not
	private boolean restart = false;
	
	private int delayTime = 5;
	
	// This is the score of the game
	private int score = 0;
	
	// A timer to keep the snake moving
	Timer snakeTimer = new Timer(delayTime, this);
	
	// This is the ArrayList which stores the blocks of the snake
	ArrayList<SnakeBlock> snakeList = new ArrayList<SnakeBlock>();
	
	// Constructor
	public GameGraphics() {
		
		// Enabling the keys to give commands
		setFocusable(true);
		
		addKeyListener(this);
		
		// Adding the head of the snake into the list
		snakeList.add(new SnakeBlock(snakeHeadX, snakeHeadY, initSnakeVelX, 0));
	}
	
	public void paintComponent(Graphics g) {
		
		Graphics2D g2 = (Graphics2D) g;
		
		// Coloring the text black in color
		g2.setColor(Color.BLACK);
		
		// Displaying the score on the screen
		g2.setFont(new Font("Serif", Font.BOLD, sizeOfText));
		g2.drawString("Score : " + score, scoreX, scoreY);
		
		// If the game stopped
		if(play == false)
		{
			// Holding all the blocks as they are
			for(int i = 0; i < snakeList.size(); ++i)
			{
				snakeList.get(i).setSnakeBlockVelX(0);
				snakeList.get(i).setSnakeBlockVelY(0);
			}
			
			// Displaying the message that the game is over
			g2.setFont(new Font("Serif", Font.BOLD, 2 * sizeOfText));
			g2.drawString("Game Over!", (screenSize.width - sizeOfText - 280) / 2, (screenSize.height) / 2);
			g2.drawString("Press 'Enter' to restart", (screenSize.width - sizeOfText - 540) / 2, (screenSize.height) / 2 + sizeOfText + 20);
		}
		
		// If the game has to be restarted
		if(restart == true)
		{
			// Removing all the snakeBlocks from the snakeList
			snakeList.clear();
			
			// Adding the head of the snake into the list
			snakeList.add(new SnakeBlock(snakeHeadX, snakeHeadY, initSnakeVelX, 0));
			
			// Starting the game again
			play = true;
			
			// Removing the 'restart' indication
			restart = false;
			
			// Re-initializing the score again
			score = 0;
			
			// Re-initializing this variable
			ateFood = true;
		}
		
		// Coloring the snake green
		g2.setColor(Color.GREEN.darker());
		
		// Stores the number of blocks in the snake
		int snakeSize = snakeList.size();
		
		// Filling all the blocks of the snake on the frame
		for(int i = snakeSize - 1; i >= 0; --i)
		{
			// Getting the snakeBlock from the list
			SnakeBlock refBlock = snakeList.get(i);
			
			// The coordinates of the refBlock
			int refBlockX = refBlock.getSnakeBlockX();
			int refBlockY = refBlock.getSnakeBlockY();
			
			// Filling the block on the frame
			g2.fillRect(refBlockX, refBlockY, snakeBlockSize, snakeBlockSize);
		}
		
		// If a new food particle has to be generated
		if(ateFood == true)
			generateFoodCoordinates();
		
		// Painting the snake red in color
		g2.setColor(Color.RED);
		
		// Painting the food particle
		g2.fillOval(foodX, foodY, foodSize, foodSize);
		
		// Starting the timer
		snakeTimer.start();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
		// Number of blocks in the snake
		int snakeSize = snakeList.size();
		
		// Updating the coordinates of each block of the snake
		for(int i = 0; i < snakeSize; ++i)
		{
			// Obtaining the snake block
			SnakeBlock refBlock = snakeList.get(i);
			
			// Obtaining the X and Y coordinates of the snake block
			int refBlockVelX = refBlock.getSnakeBlockVelX();
			int refBlockVelY = refBlock.getSnakeBlockVelY();
			
			// Updating the coordinates of the block
			refBlock.setSnakeBlockX(refBlock.getSnakeBlockX() + refBlockVelX);
			refBlock.setSnakeBlockY(refBlock.getSnakeBlockY() + refBlockVelY);
		}
		
		// If the snake is changing direction, the blocks following it must gradually change direction too
		for(int i = snakeSize - 1; i > 0; --i)
		{
			SnakeBlock refBlockPrev = snakeList.get(i);
			SnakeBlock refBlockNext = snakeList.get(i - 1);
			
			// The previous block is changing direction
			if(refBlockPrev.getSnakeBlockX() == refBlockNext.getSnakeBlockX() || refBlockPrev.getSnakeBlockY() == refBlockNext.getSnakeBlockY())
			{
				refBlockPrev.setSnakeBlockVelX(refBlockNext.getSnakeBlockVelX());
				refBlockPrev.setSnakeBlockVelY(refBlockNext.getSnakeBlockVelY());
			}
		}
		
		// This is the head of the snake
		SnakeBlock refBlock = snakeList.get(0);
		
		Rectangle2D.Double refHead = new Rectangle2D.Double(refBlock.getSnakeBlockX(), refBlock.getSnakeBlockY(),
				snakeBlockSize, snakeBlockSize);
		
		// If the snake's head touches the food, the snake needs to grow
		if(refHead.intersects(foodX, foodY, foodSize, foodSize) && (ateFood == false))
		{
			// Blocks are being added if the snake eats its food
			for(int i = 0; i < increaseSize; ++i)
				growSnake();
			
			// Marking that the food has been eaten by the snake
			ateFood = true;
			
			score += scoreIncrease;
		}
		
		// If the snake touches any of its own blocks, the game is over
		
		Rectangle2D.Double myrectangle = new Rectangle2D.Double(refBlock.getSnakeBlockX(), refBlock.getSnakeBlockY(), snakeBlockSize, snakeBlockSize);
		
		// Checking if the head touches any other block
		for(int i = snakeList.size() - 1; i > 1; --i)
		{
			int refX = snakeList.get(i).getSnakeBlockX();
			int refY = snakeList.get(i).getSnakeBlockY();
			
			// If the head intersects any other block, the game is over
			if(myrectangle.intersects(refX, refY, snakeBlockSize, snakeBlockSize))
				play = false;
		}
		
		// If the snake crossed the edges of the screen, the game is over
		if((refBlock.getSnakeBlockX() + snakeBlockSize > screenSize.width) || (refBlock.getSnakeBlockX() < 0) || (refBlock.getSnakeBlockY() < 0)
				|| (refBlock.getSnakeBlockY() + snakeBlockSize > screenSize.height))
			play = false;
		
		repaint();
	}

	@Override
	public void keyPressed(KeyEvent e) {
		
		// Obtaining the key which was pressed
		int pressedKey = e.getKeyCode();
		
		// If the game has to be restarted
		if((play == false) && (pressedKey == KeyEvent.VK_ENTER))
			restart = true;
		
		// If the game is not in progress, the display needs to be on hold
		if(play == false)
			return;
		
		// If the left key was pressed
		if(pressedKey == KeyEvent.VK_LEFT)
			moveSnakeHeadLeft();
		// If the right key was pressed
		else if(pressedKey == KeyEvent.VK_RIGHT)
			moveSnakeHeadRight();
		// If the up key was pressed
		else if(pressedKey == KeyEvent.VK_UP)
			moveSnakeHeadUp();
		// If the down key was pressed
		else if(pressedKey == KeyEvent.VK_DOWN)
			moveSnakeHeadDown();
	}
	
	// This function changes the X component of the velocity of the snakeHead to a negative value
	public void moveSnakeHeadLeft() {
		
		// This is the head of the snake
		SnakeBlock refBlock = snakeList.get(0);
		
		// Checking if the direction is asked to be changed while the snake is in a transition
		if(snakeList.size() > 1)
		{
			SnakeBlock checkBlock = snakeList.get(1);
			
			// The direction of the snake cannot be changed again, if it is in a transition of changing its direction
			if((refBlock.getSnakeBlockX() != checkBlock.getSnakeBlockX()) && (refBlock.getSnakeBlockY() != checkBlock.getSnakeBlockY()))
				return;
		}
		
		// If it is moving right, it can't move left
		if(refBlock.getSnakeBlockVelX() > 0)
			return;
		
		// Setting the Y component as '0'
		refBlock.setSnakeBlockVelY(0);
		
		// Making the snake move to the left
		refBlock.setSnakeBlockVelX(-initSnakeVelX);
	}
	
	// This function changes the X component of the velocity of the snakeHead to a positive value
	public void moveSnakeHeadRight() {
		
		// This is the head of the snake
		SnakeBlock refBlock = snakeList.get(0);
		
		// Checking if the direction is asked to be changed while the snake is in a transition
		if(snakeList.size() > 1)
		{
			SnakeBlock checkBlock = snakeList.get(1);
			
			// The direction of the snake cannot be changed again, if it is in a transition of changing its direction
			if((refBlock.getSnakeBlockX() != checkBlock.getSnakeBlockX()) && (refBlock.getSnakeBlockY() != checkBlock.getSnakeBlockY()))
				return;
		}
		
		// If it is moving left, it can't move right
		if(refBlock.getSnakeBlockVelX() < 0)
			return;
		
		// Setting the Y component as '0'
		refBlock.setSnakeBlockVelY(0);
		
		// Making the snake move to the right
		refBlock.setSnakeBlockVelX(initSnakeVelX);
	}
	
	// This function changes the Y component of the velocity of the snakeHead to a positive value
	public void moveSnakeHeadUp() {
		
		// This is the head of the snake
		SnakeBlock refBlock = snakeList.get(0);
		
		// Checking if the direction is asked to be changed while the snake is in a transition
		if(snakeList.size() > 1)
		{
			SnakeBlock checkBlock = snakeList.get(1);
			
			// The direction of the snake cannot be changed again, if it is in a transition of changing its direction
			if((refBlock.getSnakeBlockX() != checkBlock.getSnakeBlockX()) && (refBlock.getSnakeBlockY() != checkBlock.getSnakeBlockY()))
				return;
		}
		
		// If it is moving downwards, it can't move upwards
		if(refBlock.getSnakeBlockVelY() > 0)
			return;
		
		// Setting the X component as '0'
		refBlock.setSnakeBlockVelX(0);
		
		// Making the snake move upwards
		refBlock.setSnakeBlockVelY(-initSnakeVelY);
	}
	
	// This function changes the Y component of the velocity of the snakeHead to a negative value
	public void moveSnakeHeadDown() {
		
		// This is the head of the snake
		SnakeBlock refBlock = snakeList.get(0);
		
		// Checking if the direction is asked to be changed while the snake is in a transition
		if(snakeList.size() > 1)
		{
			SnakeBlock checkBlock = snakeList.get(1);
			
			// The direction of the snake cannot be changed again, if it is in a transition of changing its direction
			if((refBlock.getSnakeBlockX() != checkBlock.getSnakeBlockX()) && (refBlock.getSnakeBlockY() != checkBlock.getSnakeBlockY()))
				return;
		}
		
		// If it is moving upwards, it can't move down
		if(refBlock.getSnakeBlockVelY() < 0)
			return;
		
		// Setting the X component as '0'
		refBlock.setSnakeBlockVelX(0);
		
		// Making the snake move downwards
		refBlock.setSnakeBlockVelY(initSnakeVelY);
	}
	
	// Generates random coordinates for the food
	void generateFoodCoordinates() {
		
		Random rand = new Random();
		
		// Random X coordinate for the food
		foodX = rand.nextInt(upperBoundXRandom);
		
		// Random Y coordinate for the food
		foodY = rand.nextInt(upperBoundYRandom);
		
		// Marking that this new food particle hasn't been eaten yet
		ateFood = false;
	}
	
	// Increases the size of the snake
	public void growSnake() {
		
		// Number of blocks in the snake
		int snakeSize = snakeList.size();
		
		// The last block in the snake
		SnakeBlock lastBlock = snakeList.get(snakeSize - 1);
		
		// Getting the attributes of the last block
		int lastBlockX = lastBlock.getSnakeBlockX();
		int lastBlockY = lastBlock.getSnakeBlockY();
		
		int lastBlockVelX = lastBlock.getSnakeBlockVelX();
		int lastBlockVelY = lastBlock.getSnakeBlockVelY();
		
		// Store the coordinates of the block to be added
		int refBlockX;
		int refBlockY;
		
		// If the last block is moving right
		if(lastBlockVelX > 0)
		{
			refBlockX = lastBlockX - snakeBlockSize;
			refBlockY = lastBlockY;
		}
		// If the last block is moving left
		else if(lastBlockVelX < 0)
		{
			refBlockX = lastBlockX + snakeBlockSize;
			refBlockY = lastBlockY;
		}
		// If the last block is moving upwards
		else if(lastBlockVelY < 0)
		{
			refBlockX = lastBlockX;
			refBlockY = lastBlockY + snakeBlockSize;
		}
		// If the last block is moving downwards
		else
		{
			refBlockX = lastBlockX;
			refBlockY = lastBlockY - snakeBlockSize;
		}
		
		// This is the block to be inserted into the snakeList
		SnakeBlock refBlock = new SnakeBlock(refBlockX, refBlockY, lastBlockVelX, lastBlockVelY);
		
		// Adding the new block to the snakeList
		snakeList.add(refBlock);
	}
	
	// We aren't bothered about these methods
	// They have to be implemented as this class implements KeyListener
	@Override
	public void keyReleased(KeyEvent e) {}

	@Override
	public void keyTyped(KeyEvent e) {}
}
