package com.anish.games.snakegame;

import java.awt.*;
import javax.swing.*;

public class SnakeGame {
	
	public static void main(String args[]) {
		
		EventQueue.invokeLater(() ->
		{
			// Creating a new frame
			JFrame myframe = new JFrame();
			
			// The program terminates on closing the window
			myframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			
			// Maximizing the size of the window
			myframe.setExtendedState(Frame.MAXIMIZED_BOTH);
			
			// The window cannot be resized
			myframe.setResizable(false);
			
			// Setting the background of the frame as light gray
			myframe.getContentPane().setBackground(Color.LIGHT_GRAY);
			
			// Adding components to the frame
			myframe.add(new GameGraphics());
			
			// Display the frame to the user
			myframe.show();
		});
	}
}

