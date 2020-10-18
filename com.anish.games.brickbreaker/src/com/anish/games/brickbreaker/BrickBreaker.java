package com.anish.games.brickbreaker;

import java.awt.*;
import javax.swing.*;

public class BrickBreaker {
	
	public static void main(String args[])
	{
		EventQueue.invokeLater(() ->
		{
			// Creating a frame
			JFrame myframe = new JFrame("Brick Breaker");
			
			// The program terminates upon closing the frame
			myframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			
			// Setting the background of the frame
			myframe.getContentPane().setBackground(Color.LIGHT_GRAY);
			
			// The frame cannot be resized
			myframe.setExtendedState(Frame.MAXIMIZED_BOTH);
			
			// The frame cannot be resized
			myframe.setResizable(false);
			
			myframe.add(new GameGraphics());
			
			// Displaying the frame to the user
			myframe.show();
		});
	}
}
