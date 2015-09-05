package com.obugames.gameworld;

import java.util.List;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenEquations;
import aurelienribon.tweenengine.TweenManager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.obugames.TweenAccessors.Value;
import com.obugames.TweenAccessors.ValueAccessor;
import com.obugames.bshelpers.AssetLoader;
import com.obugames.bshelpers.InputHandler;
import com.obugames.gameobjects.Plastic;
import com.obugames.gameobjects.ScrollHandler;
import com.obugames.gameobjects.SeaFloor;
import com.obugames.gameobjects.SeaSurface;
import com.obugames.gameobjects.Shark;
import com.obugames.ui.SimpleButton;

public class GameRenderer {

	private GameWorld myWorld;
	private OrthographicCamera cam;
	private ShapeRenderer shapeRenderer;

	private SpriteBatch batcher;

	private int midPointY;
	private int gameHeight;

	// Game Objects
	private Shark shark;
	private ScrollHandler scroller;
	private SeaFloor frontSeaFloor, backSeaFloor;
	private SeaSurface frontSeaSurface, backSeaSurface;
	private Plastic plastic1, plastic2, plastic3, plastic4, plastic5;

	// Game Assets
	private TextureRegion bg, seaFloor, seaSurface;
	private Animation sharkAnimation;
	private TextureRegion sharkMid, sharkDown, sharkUp;
	private TextureRegion skullUp, skullDown, plastic;

	// Tween stuff
	private TweenManager manager;
	private Value alpha = new Value();

	// Buttons
	private List<SimpleButton> menuButtons;

	public GameRenderer(GameWorld world, int gameHeight, int midPointY) {
		myWorld = world;

		// The word "this" refers to this instance.
		// We are setting the instance variables' values to be that of the
		// parameters passed in from GameScreen.
		this.gameHeight = gameHeight;
		this.midPointY = midPointY;
		this.menuButtons = ((InputHandler) Gdx.input.getInputProcessor())
				.getMenuButtons();

		cam = new OrthographicCamera();
		cam.setToOrtho(true, 136, gameHeight);

		batcher = new SpriteBatch();
		// Attach batcher to camera
		batcher.setProjectionMatrix(cam.combined);

		shapeRenderer = new ShapeRenderer();
		shapeRenderer.setProjectionMatrix(cam.combined);

		// Call helper methods to initialize instance variables
		initGameObjects();
		initAssets();
		setupTweens();
	}

	private void setupTweens() {
		Tween.registerAccessor(Value.class, new ValueAccessor());
		manager = new TweenManager();
		Tween.to(alpha, -1, .5f).target(0).ease(TweenEquations.easeOutQuad)
				.start(manager);
	}

	public void render(float delta, float runTime) {

		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		// Begin ShapeRenderer
		shapeRenderer.begin(ShapeType.Filled);

		// Draw Background color
		shapeRenderer.setColor(113 / 255.0f, 241 / 255.0f, 242 / 255.0f, 1);
		shapeRenderer.rect(0, 0, 136, midPointY + 66);

		// Draw Grass
		shapeRenderer.setColor(111 / 255.0f, 186 / 255.0f, 45 / 255.0f, 1);
		shapeRenderer.rect(0, midPointY + 66, 136, 11);

		// Draw Dirt
		shapeRenderer.setColor(147 / 255.0f, 80 / 255.0f, 27 / 255.0f, 1);
		shapeRenderer.rect(0, midPointY + 77, 136, 52);

		// End ShapeRenderer
		shapeRenderer.end();

		// Begin SpriteBatch
		batcher.begin();
		// Disable transparency
		// This is good for performance when drawing images that do not require
		// transparency.
		batcher.disableBlending();
		batcher.draw(bg, 0, midPointY + 23, 136, 43);

		// Draw Grass
		drawSeaFloor();

		drawSeaSurface();

		// The bird needs transparency, so we enable that again.
		batcher.enableBlending();

		// Draw Plastics
		drawPlastics();

		if (myWorld.isRunning()) {
			drawShark(runTime);
			drawScore();
		} else if (myWorld.isReady()) {
			drawShark(runTime);
			drawScore();
		} else if (myWorld.isMenu()) {
			drawSharkCentered(runTime);
			drawMenuUI();
		} else if (myWorld.isGameOver()) {
			drawShark(runTime);
			drawScore();
		} else if (myWorld.isHighScore()) {
			drawShark(runTime);
			drawScore();
		}

		batcher.end();
		drawTransition(delta);
	}

	private void drawSharkCentered(float runTime) {
		batcher.draw(sharkAnimation.getKeyFrame(runTime), 59,
				shark.getY() - 15, shark.getWidth() / 2.0f,
				shark.getHeight() / 2.0f, shark.getWidth(), shark.getHeight(),
				1, 1, shark.getRotation());
	}

	private void drawShark(float runTime) {
		if (shark.shouldntFlap()) {
			batcher.draw(sharkMid, shark.getX(), shark.getY(),
					shark.getWidth() / 2.0f, shark.getHeight() / 2.0f,
					shark.getWidth(), shark.getHeight(), 1, 1,
					shark.getRotation());

		} else {
			batcher.draw(sharkAnimation.getKeyFrame(runTime), shark.getX(),
					shark.getY(), shark.getWidth() / 2.0f,
					shark.getHeight() / 2.0f, shark.getWidth(),
					shark.getHeight(), 1, 1, shark.getRotation());
		}
	}

	private void initGameObjects() {
		shark = myWorld.getShark();
		scroller = myWorld.getScroller();
		frontSeaFloor = scroller.getFrontSeaFloor();
		backSeaFloor = scroller.getBackSeaFloor();
		frontSeaSurface = scroller.getFrontSeaSurface();
		backSeaSurface = scroller.getBackSeaSurface();
		plastic1 = scroller.getPlastic1();
		plastic2 = scroller.getPlastic2();
		plastic3 = scroller.getPlastic3();
		plastic4 = scroller.getPlastic4();
		plastic5 = scroller.getPlastic5();
	}

	private void initAssets() {
		bg = AssetLoader.bg;
		seaFloor = AssetLoader.seaFloor;
		seaSurface = AssetLoader.seaSurface;
		sharkAnimation = AssetLoader.sharkAnimation;
		sharkMid = AssetLoader.shark;
		sharkDown = AssetLoader.sharkDown;
		sharkUp = AssetLoader.sharkUp;
		plastic = AssetLoader.plastic;
	}

	private void drawSeaFloor() {
		batcher.draw(seaFloor, frontSeaFloor.getX(), frontSeaFloor.getY(),
				frontSeaFloor.getWidth(), frontSeaFloor.getHeight());
		batcher.draw(seaFloor, backSeaFloor.getX(), backSeaFloor.getY(),
				backSeaFloor.getWidth(), backSeaFloor.getHeight());
	}

	private void drawSeaSurface() {
		batcher.draw(seaSurface, frontSeaSurface.getX(),
				frontSeaSurface.getY(), frontSeaSurface.getWidth(),
				frontSeaSurface.getHeight());
		batcher.draw(seaSurface, backSeaSurface.getX(), backSeaSurface.getY(),
				backSeaSurface.getWidth(), backSeaSurface.getHeight());
	}

	private void drawPlastics() {
		// Temporary code! Sorry about the mess :)
		// We will fix this when we finish the Pipe class.
		batcher.draw(plastic, plastic1.getX(), plastic1.getY(),
				plastic1.getWidth(), plastic1.getHeight());

		// batcher.draw(plastic, plastic1.getX(),
		// plastic1.getY() + plastic1.getHeight() + 45,
		// plastic1.getWidth(), plastic1.getHeight());

		batcher.draw(plastic, plastic2.getX(), plastic2.getY(),
				plastic2.getWidth(), plastic2.getHeight());

		// batcher.draw(plastic, plastic2.getX(),
		// plastic2.getY() + plastic2.getHeight() + 45,
		// plastic2.getWidth(), plastic2.getHeight());

		batcher.draw(plastic, plastic3.getX(), plastic3.getY(),
				plastic3.getWidth(), plastic3.getHeight());

		// batcher.draw(plastic, plastic3.getX(),
		// plastic3.getY() + plastic3.getHeight() + 45,
		// plastic3.getWidth(), plastic3.getHeight());

		batcher.draw(plastic, plastic4.getX(), plastic4.getY(),
				plastic4.getWidth(), plastic4.getHeight());

		batcher.draw(plastic, plastic5.getX(), plastic5.getY(),
				plastic5.getWidth(), plastic5.getHeight());

	}

	private void drawMenuUI() {
		batcher.draw(AssetLoader.bsLogo, 136 / 2 - 56, midPointY - 50,
				AssetLoader.bsLogo.getRegionWidth() / 1.2f,
				AssetLoader.bsLogo.getRegionHeight() / 1.2f);

		for (SimpleButton button : menuButtons) {
			button.draw(batcher);
		}

	}

	private void drawScore() {
		int length = ("" + myWorld.getScore()).length();
		AssetLoader.shadow.draw(batcher, "" + myWorld.getScore(),
				68 - (3 * length), midPointY - 82);
		AssetLoader.font.draw(batcher, "" + myWorld.getScore(),
				68 - (3 * length), midPointY - 83);
	}

	private void drawTransition(float delta) {
		if (alpha.getValue() > 0) {
			manager.update(delta);
			Gdx.gl.glEnable(GL20.GL_BLEND);
			Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
			shapeRenderer.begin(ShapeType.Filled);
			shapeRenderer.setColor(1, 1, 1, alpha.getValue());
			shapeRenderer.rect(0, 0, 136, 300);
			shapeRenderer.end();
			Gdx.gl.glDisable(GL20.GL_BLEND);

		}
	}

}