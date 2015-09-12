package com.obugames.gameobjects;

import java.util.Random;

import com.obugames.bshelpers.AssetLoader;
import com.obugames.gameworld.GameWorld;

public class ScrollHandler {

	// ScrollHandler will create all five objects that we need.
	private SeaFloor frontSeaFloor, backSeaFloor;
	private SeaSurface frontSeaSurface, backSeaSurface;
	private Background frontBg, backBg;
	private Plastic plastic1, plastic2, plastic3, plastic4, plastic5, plastic6;
	private Oil oil1;
	private Random r;

	// ScrollHandler will use the constants below to determine
	// how fast we need to scroll and also determine
	// the size of the gap between each pair of pipes.

	// Capital letters are used by convention when naming constants.
	public static final int SCROLL_SPEED = -59;
	public static final int PLASTIC_GAP = 48;
	
	public static final int DIVIDER = 3;

	private GameWorld gameWorld;

	// Constructor receives a float that tells us where we need to create our
	// Grass and Pipe objects.
	public ScrollHandler(GameWorld gameWorld, float yPos) {
		this.gameWorld = gameWorld;
		r = new Random();
		
		frontBg = new Background(0, yPos, 134, 43, SCROLL_SPEED);
		backBg = new Background(frontBg.getTailX(), yPos, 134, 43,
				SCROLL_SPEED);
		
		frontSeaFloor = new SeaFloor(0, yPos, 143, 11, SCROLL_SPEED);
		backSeaFloor = new SeaFloor(frontSeaFloor.getTailX(), yPos, 143, 11,
				SCROLL_SPEED);

		frontSeaSurface = new SeaSurface(0, 0, 143, 12, SCROLL_SPEED);
		backSeaSurface = new SeaSurface(frontSeaSurface.getTailX(), 0, 143, 12,
				SCROLL_SPEED);

		plastic1 = new Plastic(210, 10, 24, 19, SCROLL_SPEED, yPos);
		plastic2 = new Plastic(plastic1.getTailX() + PLASTIC_GAP / DIVIDER, r.nextInt(100), 24, 19,
				SCROLL_SPEED, yPos);
		plastic3 = new Plastic(plastic2.getTailX() + PLASTIC_GAP / DIVIDER, r.nextInt(100), 24, 19,
				SCROLL_SPEED, yPos);
		plastic4 = new Plastic(plastic3.getTailX() + PLASTIC_GAP / DIVIDER, r.nextInt(100), 24, 19,
				SCROLL_SPEED, yPos);
		plastic5 = new Plastic(plastic4.getTailX() + PLASTIC_GAP / DIVIDER, r.nextInt(100), 24, 19,
				SCROLL_SPEED, yPos);
		plastic6 = new Plastic(plastic5.getTailX() + PLASTIC_GAP / DIVIDER, r.nextInt(100), 24, 19,
				SCROLL_SPEED, yPos);
		
		oil1 = new Oil(210, 0, 69, 12, SCROLL_SPEED);

	}

	public void updateReady(float delta) {

		frontBg.update(delta);
		backBg.update(delta);
		
		frontSeaFloor.update(delta);
		backSeaFloor.update(delta);

		frontSeaSurface.update(delta);
		backSeaSurface.update(delta);

		// Same with grass
		if (frontBg.isScrolledLeft()) {
			frontBg.reset(backBg.getTailX());

		} else if (backBg.isScrolledLeft()) {
			backBg.reset(frontBg.getTailX());

		}

		
		if (frontSeaFloor.isScrolledLeft()) {
			frontSeaFloor.reset(backSeaFloor.getTailX());

		} else if (backSeaFloor.isScrolledLeft()) {
			backSeaFloor.reset(frontSeaFloor.getTailX());

		}
		if (frontSeaSurface.isScrolledLeft()) {
			frontSeaSurface.reset(backSeaSurface.getTailX());

		} else if (backSeaSurface.isScrolledLeft()) {
			backSeaSurface.reset(frontSeaSurface.getTailX());

		}

	}

	public void update(float delta) {
		// Update our objects
		frontBg.update(delta);
		backBg.update(delta);
		frontSeaFloor.update(delta);
		backSeaFloor.update(delta);
		frontSeaSurface.update(delta);
		backSeaSurface.update(delta);
		plastic1.update(delta);
		plastic2.update(delta);
		plastic3.update(delta);
		plastic4.update(delta);
		plastic5.update(delta);
		plastic6.update(delta);
		oil1.update(delta);

		if (oil1.isScrolledLeft()) {
			oil1.reset(210);
		}
		// Check if any of the pipes are scrolled left,
		// and reset accordingly
		if (plastic1.isScrolledLeft()) {
			plastic1.reset(plastic6.getTailX() + PLASTIC_GAP / DIVIDER);
		} else if (plastic2.isScrolledLeft()) {
			plastic2.reset(plastic1.getTailX() + PLASTIC_GAP / DIVIDER);

		} else if (plastic3.isScrolledLeft()) {
			plastic3.reset(plastic2.getTailX() + PLASTIC_GAP / DIVIDER);
		} else if (plastic4.isScrolledLeft()) {
			plastic4.reset(plastic3.getTailX() + PLASTIC_GAP / DIVIDER);
		} else if (plastic5.isScrolledLeft()) {
			plastic5.reset(plastic4.getTailX() + PLASTIC_GAP / DIVIDER);
		} else if (plastic6.isScrolledLeft()) {
			plastic6.reset(plastic5.getTailX() + PLASTIC_GAP / DIVIDER);
		}

		if (frontBg.isScrolledLeft()) {
			frontBg.reset(backBg.getTailX());

		} else if (backBg.isScrolledLeft()) {
			backBg.reset(frontBg.getTailX());

		}
		
		if (frontSeaFloor.isScrolledLeft()) {
			frontSeaFloor.reset(backSeaFloor.getTailX());

		} else if (backSeaFloor.isScrolledLeft()) {
			backSeaFloor.reset(frontSeaFloor.getTailX());

		}
		if (frontSeaSurface.isScrolledLeft()) {
			frontSeaSurface.reset(backSeaSurface.getTailX());

		} else if (backSeaSurface.isScrolledLeft()) {
			backSeaSurface.reset(frontSeaSurface.getTailX());

		}

	}

	public void stop() {
		frontBg.stop();
		backBg.stop();
		frontSeaFloor.stop();
		backSeaFloor.stop();
		frontSeaSurface.stop();
		backSeaSurface.stop();
		plastic1.stop();
		plastic2.stop();
		plastic3.stop();
		plastic4.stop();
		plastic5.stop();
		plastic6.stop();
		oil1.stop();
	}

	public boolean collides(Shark shark) {

		/*if (!oil1.isScored()
				&& oil1.getX() + (oil1.getWidth() / 2) < shark.getX()
						+ shark.getWidth()) {
			addScore(1);
			oil1.setScored(true);
			AssetLoader.coin.play();
		}*/
		
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

		} else if (!plastic6.isScored()
				&& plastic6.getX() + (plastic6.getWidth() / 2) < shark.getX()
						+ shark.getWidth()) {
			addScore(1);
			plastic6.setScored(true);
			AssetLoader.coin.play();

		}

		return (plastic1.collides(shark) || plastic2.collides(shark)
				|| plastic3.collides(shark) || plastic4.collides(shark) || plastic5
					.collides(shark) || plastic6.collides(shark) || oil1.collides(shark));
	}

	public Background getFrontBackground() {
		return frontBg;
	}
	
	public Background getBackBackground() {
		return backBg;
	}
	
	public SeaFloor getFrontSeaFloor() {
		return frontSeaFloor;
	}

	public SeaFloor getBackSeaFloor() {
		return backSeaFloor;
	}

	public SeaSurface getFrontSeaSurface() {
		return frontSeaSurface;
	}

	public SeaSurface getBackSeaSurface() {
		return backSeaSurface;
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
	
	public Plastic getPlastic6() {
		return plastic6;
	}
	
	public Oil getOil1() {
		return oil1;
	}

	private void addScore(int increment) {
		gameWorld.addScore(increment);
	}

	public void onRestart() {
		frontBg.onRestart(0, SCROLL_SPEED);
		backBg.onRestart(frontBg.getTailX(), SCROLL_SPEED);
		frontSeaFloor.onRestart(0, SCROLL_SPEED);
		backSeaFloor.onRestart(frontSeaFloor.getTailX(), SCROLL_SPEED);
		frontSeaSurface.onRestart(0, SCROLL_SPEED);
		backSeaSurface.onRestart(frontSeaSurface.getTailX(), SCROLL_SPEED);
		plastic1.onRestart(210, SCROLL_SPEED);
		plastic2.onRestart(plastic1.getTailX() + PLASTIC_GAP / DIVIDER, SCROLL_SPEED);
		plastic3.onRestart(plastic2.getTailX() + PLASTIC_GAP / DIVIDER, SCROLL_SPEED);
		plastic4.onRestart(plastic3.getTailX() + PLASTIC_GAP / DIVIDER, SCROLL_SPEED);
		plastic5.onRestart(plastic4.getTailX() + PLASTIC_GAP / DIVIDER, SCROLL_SPEED);
		plastic6.onRestart(plastic5.getTailX() + PLASTIC_GAP / DIVIDER, SCROLL_SPEED);
		oil1.onRestart(210, SCROLL_SPEED);
	}
}