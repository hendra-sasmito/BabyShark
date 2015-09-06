package com.obugames.gameobjects;

import java.util.Random;

import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

public class Oil extends Scrollable {

	private Random r;
	private Rectangle boundingRect;
	private boolean isScored = false;

	// When Plastic constructor is invoked, invoke the super (Scrollable)
	// constructor
	public Oil(float x, float y, int width, int height, float scrollSpeed) {
		super(x, y, width, height, scrollSpeed);
		// Initialize a Random object for Random number generation
		r = new Random();
		boundingRect = new Rectangle();
	}

	@Override
	public void update(float delta) {
		// Call the update method in the superclass (Scrollable)
		super.update(delta);

		boundingRect.set(position.x, position.y + 2, 69, 6);
	}

	@Override
	public void reset(float newX) {
		// Call the reset method in the superclass (Scrollable)
		super.reset(newX + r.nextInt(100) + 15);
		isScored = false;
	}

	public boolean collides(Shark shark) {
		if (position.x < shark.getX() + shark.getWidth()) {
			return (Intersector.overlaps(shark.getBoundingCircle(),
					boundingRect));
		}
		return false;
	}

	public Rectangle getBoundingRect() {
		return boundingRect;
	}

	public boolean isScored() {
		return isScored;
	}

	public void setScored(boolean b) {
		isScored = b;
	}

	public void onRestart(float x, float scrollSpeed) {
		velocity.x = scrollSpeed;
		reset(x);
	}
}