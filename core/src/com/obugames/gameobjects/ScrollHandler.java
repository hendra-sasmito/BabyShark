package com.obugames.gameobjects;

import com.obugames.bshelpers.AssetLoader;
import com.obugames.gameworld.GameWorld;

public class ScrollHandler {

	// ScrollHandler will create all five objects that we need.
	private SeaFloor frontSeaFloor, backSeaFloor;
	private Plastic plastic1, plastic2, plastic3, plastic4, plastic5;
	private Bomb bomb1;

	// ScrollHandler will use the constants below to determine
	// how fast we need to scroll and also determine
	// the size of the gap between each pair of pipes.

	// Capital letters are used by convention when naming constants.
	public static final int SCROLL_SPEED = -59;
	public static final int PIPE_GAP = 49;

	private GameWorld gameWorld;

	// Constructor receives a float that tells us where we need to create our
	// Grass and Pipe objects.
	public ScrollHandler(GameWorld gameWorld, float yPos) {
		this.gameWorld = gameWorld;
		frontSeaFloor = new SeaFloor(0, yPos, 143, 11, SCROLL_SPEED);
		backSeaFloor = new SeaFloor(frontSeaFloor.getTailX(), yPos, 143, 11,
				SCROLL_SPEED);

		plastic1 = new Plastic(210, 0, 24, 19, SCROLL_SPEED, yPos);
		plastic2 = new Plastic(plastic1.getTailX() + PIPE_GAP, 0, 24, 19,
				SCROLL_SPEED, yPos);
		plastic3 = new Plastic(plastic2.getTailX() + PIPE_GAP, 0, 24, 19,
				SCROLL_SPEED, yPos);
		plastic4 = new Plastic(plastic1.getTailX() + PIPE_GAP/2, 0, 24, 19,
				SCROLL_SPEED, yPos);
		plastic5 = new Plastic(plastic2.getTailX() + PIPE_GAP/2, 0, 24, 19,
				SCROLL_SPEED, yPos);

		bomb1 = new Bomb(240, 0, 9, 11, SCROLL_SPEED);
	}
	
	public void updateReady(float delta) {

        frontSeaFloor.update(delta);
        backSeaFloor.update(delta);

        // Same with grass
        if (frontSeaFloor.isScrolledLeft()) {
            frontSeaFloor.reset(backSeaFloor.getTailX());

        } else if (backSeaFloor.isScrolledLeft()) {
            backSeaFloor.reset(frontSeaFloor.getTailX());

        }

    }

	public void update(float delta) {
		// Update our objects
		frontSeaFloor.update(delta);
		backSeaFloor.update(delta);
		plastic1.update(delta);
		plastic2.update(delta);
		plastic3.update(delta);
		plastic4.update(delta);
		plastic5.update(delta);
		bomb1.update(delta);

		// Check if any of the pipes are scrolled left,
		// and reset accordingly
		if (plastic1.isScrolledLeft()) {
			plastic1.reset(plastic3.getTailX() + PIPE_GAP);
		} else if (plastic2.isScrolledLeft()) {
			plastic2.reset(plastic1.getTailX() + PIPE_GAP);

		} else if (plastic3.isScrolledLeft()) {
			plastic3.reset(plastic2.getTailX() + PIPE_GAP);
		} else if (plastic4.isScrolledLeft()) {
			plastic4.reset(plastic1.getTailX() + PIPE_GAP/2);
		} else if (plastic5.isScrolledLeft()) {
			plastic5.reset(plastic2.getTailX() + PIPE_GAP/2);
		}

		if (bomb1.isScrolledLeft()) {
			bomb1.reset(plastic3.getTailX() + PIPE_GAP);
		}

		// Same with grass
		if (frontSeaFloor.isScrolledLeft()) {
			frontSeaFloor.reset(backSeaFloor.getTailX());

		} else if (backSeaFloor.isScrolledLeft()) {
			backSeaFloor.reset(frontSeaFloor.getTailX());

		}
	}

	public void stop() {
		frontSeaFloor.stop();
		backSeaFloor.stop();
		plastic1.stop();
		plastic2.stop();
		plastic3.stop();
		plastic4.stop();
		plastic5.stop();
	}

	public boolean collides(Shark shark) {

		if (!plastic1.isScored()
				&& plastic1.getX() + (plastic1.getWidth() / 2) < shark.getX()
						+ shark.getWidth()) {
			addScore(1);
			plastic1.setScored(true);
			AssetLoader.coin.play();
		} else if (!plastic2.isScored()
				&& plastic2.getX() + (plastic2.getWidth() / 2) < shark.getX()
						+ shark.getWidth()) {
			addScore(1);
			plastic2.setScored(true);
			AssetLoader.coin.play();

		} else if (!plastic3.isScored()
				&& plastic3.getX() + (plastic3.getWidth() / 2) < shark.getX()
						+ shark.getWidth()) {
			addScore(1);
			plastic3.setScored(true);
			AssetLoader.coin.play();

		} else if (!plastic4.isScored()
				&& plastic4.getX() + (plastic4.getWidth() / 2) < shark.getX()
						+ shark.getWidth()) {
			addScore(1);
			plastic4.setScored(true);
			AssetLoader.coin.play();

		} else if (!plastic5.isScored()
				&& plastic5.getX() + (plastic5.getWidth() / 2) < shark.getX()
						+ shark.getWidth()) {
			addScore(1);
			plastic5.setScored(true);
			AssetLoader.coin.play();

		}  

		return (plastic1.collides(shark) || plastic2.collides(shark) || plastic3
				.collides(shark) || plastic4.collides(shark) || plastic5.collides(shark));
	}

	// The getters for our five instance variables
	public SeaFloor getFrontSeaFloor() {
		return frontSeaFloor;
	}

	public SeaFloor getBackSeaFloor() {
		return backSeaFloor;
	}

	public Plastic getPlastic1() {
		return plastic1;
	}

	public Plastic getPlastic2() {
		return plastic2;
	}

	public Plastic getPlastic3() {
		return plastic3;
	}
	
	public Plastic getPlastic4() {
		return plastic4;
	}
	
	public Plastic getPlastic5() {
		return plastic5;
	}

	public Bomb getBomb1() {
		return bomb1;
	}

	private void addScore(int increment) {
		gameWorld.addScore(increment);
	}

	public void onRestart() {
		frontSeaFloor.onRestart(0, SCROLL_SPEED);
		backSeaFloor.onRestart(frontSeaFloor.getTailX(), SCROLL_SPEED);
		plastic1.onRestart(210, SCROLL_SPEED);
		plastic2.onRestart(plastic1.getTailX() + PIPE_GAP, SCROLL_SPEED);
		plastic3.onRestart(plastic2.getTailX() + PIPE_GAP, SCROLL_SPEED);
		plastic4.onRestart(plastic1.getTailX() + PIPE_GAP/2, SCROLL_SPEED);
		plastic5.onRestart(plastic2.getTailX() + PIPE_GAP/2, SCROLL_SPEED);
	}
}