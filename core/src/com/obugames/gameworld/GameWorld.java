package com.obugames.gameworld;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.obugames.bshelpers.AssetLoader;
import com.obugames.gameobjects.ScrollHandler;
import com.obugames.gameobjects.Shark;

public class GameWorld {
	public enum GameState {
		MENU, READY, RUNNING, GAMEOVER, HIGHSCORE, EXIT
	}

	private Shark shark;
	private ScrollHandler scroller;
	private boolean isAlive = true;
	private Rectangle ground;
	private int score = 0;
	private float runTime = 0;
	private GameState currentState;
	private int midPointY;
	private GameRenderer renderer;

	public GameWorld(int midPointY) {
		currentState = GameState.MENU;
		this.midPointY = midPointY;
		shark = new Shark(33, midPointY - 5, 20, 15);
		// The grass should start 66 pixels below the midPointY
		scroller = new ScrollHandler(this, midPointY + 66);
		ground = new Rectangle(0, midPointY + 66, 136, 11);
	}

	public void update(float delta) {
		runTime += delta;
		
        switch (currentState) {
        case READY:
        case MENU:
            updateReady(delta);
            break;

        case RUNNING:
            updateRunning(delta);
            break;
        default:
            break;
        }

    }
	
	private void updateReady(float delta) {
		shark.updateReady(runTime);
        scroller.updateReady(delta);
    }
	
	private void updateRunning(float delta) {
		// Add a delta cap so that if our game takes too long
		// to update, we will not break our collision detection.

		if (delta > .15f) {
			delta = .15f;
		}

		shark.update(delta);
		scroller.update(delta);

		if (scroller.collides(shark) && shark.isAlive()) {
			scroller.stop();
			shark.die();
			AssetLoader.dead.play();
			renderer.prepareTransition(255, 255, 255, .3f);

			AssetLoader.fall.play();
		}

		if (Intersector.overlaps(shark.getBoundingCircle(), ground)) {
			if (shark.isAlive()) {
				AssetLoader.dead.play();
				renderer.prepareTransition(255, 255, 255, .3f);

				shark.die();
			}
            scroller.stop();
            shark.decelerate();
            currentState = GameState.GAMEOVER;
            
            if (score > AssetLoader.getHighScore()) {
                AssetLoader.setHighScore(score);
                currentState = GameState.HIGHSCORE;
            }
        }
	}
	
	public boolean isReady() {
        return currentState == GameState.READY;
    }
	
	public void start() {
        currentState = GameState.RUNNING;
    }

    public void exit() {
        currentState = GameState.EXIT;
    }

    public void restart() {
        score = 0;
        shark.onRestart(midPointY - 5);
        scroller.onRestart();
        ready();
    }

    public boolean isGameOver() {
        return currentState == GameState.GAMEOVER;
    }

    public boolean isGameExit() {
        return currentState == GameState.EXIT;
    }

	public void setRenderer(GameRenderer renderer) {
		this.renderer = renderer;
	}
	
	public Shark getShark() {
		return shark;

	}

	public ScrollHandler getScroller() {
		return scroller;
	}

	public int getScore() {
		return score;
	}

	public void addScore(int increment) {
		score += increment;
	}
	
	public boolean isRunning() {
        return currentState == GameState.RUNNING;
    }
	
	public boolean isHighScore() {
	    return currentState == GameState.HIGHSCORE;
	}
	
	public boolean isMenu() {
        return currentState == GameState.MENU;
    }
	
	public void ready() {
        currentState = GameState.READY;
        renderer.prepareTransition(0, 0, 0, 1f);
    }
	
	public int getMidPointY() {
        return midPointY;
    }

}