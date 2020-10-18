package com.anish.games.snakegame;

import java.awt.*;
import javax.swing.*;

public class SnakeBlock {
	
	// The coordinates of the snake block
	private int snakeBlockX;
	private int snakeBlockY;
	
	// The velocities of the snake block
	private int snakeBlockVelX;
	private int snakeBlockVelY;
	
	// Constructor
	public SnakeBlock(int x, int y, int velX, int velY) {
		
		// Assigning the attributes of the snake block
		snakeBlockX = x;
		snakeBlockY = y;
		
		snakeBlockVelX = velX;
		snakeBlockVelY = velY;
	}
	
	// Adding getters to obtain the attributes of this block
	public int getSnakeBlockX() {
		return snakeBlockX;
	}
	
	public int getSnakeBlockY() {
		return snakeBlockY;
	}
	
	public int getSnakeBlockVelX() {
		return snakeBlockVelX;
	}
	
	public int getSnakeBlockVelY() {
		return snakeBlockVelY;
	}
	
	// Adding setters to change the attributes of this block
	public void setSnakeBlockX(int x) {
		snakeBlockX = x;
	}
	
	public void setSnakeBlockY(int y) {
		snakeBlockY = y;
	}
	
	public void setSnakeBlockVelX(int velX) {
		snakeBlockVelX = velX;
	}
	
	public void setSnakeBlockVelY(int velY) {
		snakeBlockVelY = velY;
	}
}

