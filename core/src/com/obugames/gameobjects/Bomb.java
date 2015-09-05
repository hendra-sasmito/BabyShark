package com.obugames.gameobjects;

import java.util.Random;

public class Bomb extends Scrollable {

	private Random r;

	// When Bomb constructor is invoked, invoke the super (Scrollable)
	// constructor
	public Bomb(float x, float y, int width, int height, float scrollSpeed) {
		super(x, y, width, height, scrollSpeed);
		// Initialize a Random object for Random number generation
		r = new Random();
	}

	@Override
	public void reset(float newX) {
		// Call the reset method in the superclass (Scrollable)
		super.reset(newX);
		position.y = r.nextInt(90) + 15;
	}

}