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
import com.obugames.gameobjects.Background;
import com.obugames.gameobjects.Oil;
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
	private Background frontBg, backBg;
	private SeaSurface frontSeaSurface, backSeaSurface;
	private Plastic plastic1, plastic2, plastic3, plastic4, plastic5, plastic6;
	private Oil oil1;

	// Game Assets
	private TextureRegion seaFloor, seaSurface, background;
	private Animation sharkAnimation;
	private TextureRegion sharkMid, sharkDown, sharkUp;
	private TextureRegion plastic, oil, ready, bsLogo, gameOver, highScore,
			scoreboard, star, noStar, retry;;

	// Tween stuff
	private TweenManager manager;
	private Value alpha = new Value();

	// Buttons
	private List<SimpleButton> menuButtons;
	private Color transitionColor;

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
		
		transitionColor = new Color();
		prepareTransition(255, 255, 255, .5f);
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
		//batcher.draw(bg, 0, midPointY + 23, 136, 43);

		drawBackground(midPointY + 23);

		drawSeaSurface();

		// The bird needs transparency, so we enable that again.
		batcher.enableBlending();

		// Draw Plastics
		drawPlastics();

		drawOil();

		if (myWorld.isRunning()) {
			drawShark(runTime);
			drawScore();
		} else if (myWorld.isReady()) {
			drawShark(runTime);
			drawReady();
		} else if (myWorld.isMenu()) {
			drawSharkCentered(runTime);
			drawMenuUI();
		} else if (myWorld.isGameOver()) {
			drawScoreboard();
			drawShark(runTime);
			drawGameOver();
			drawRetry();
		} else if (myWorld.isHighScore()) {
			drawScoreboard();
			drawShark(runTime);
			drawHighScore();
			drawRetry();
		}

		drawSeaFloor();
		batcher.end();

		// drawDebugCollision();

		drawTransition(delta);

	}

	public void prepareTransition(int r, int g, int b, float duration) {
		transitionColor.set(r / 255.0f, g / 255.0f, b / 255.0f, 1);
		alpha.setValue(1);
		Tween.registerAccessor(Value.class, new ValueAccessor());
		manager = new TweenManager();
		Tween.to(alpha, -1, duration).target(0)
				.ease(TweenEquations.easeOutQuad).start(manager);
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
		frontBg = scroller.getFrontBackground();
		backBg = scroller.getBackBackground();
		frontSeaFloor = scroller.getFrontSeaFloor();
		backSeaFloor = scroller.getBackSeaFloor();
		frontSeaSurface = scroller.getFrontSeaSurface();
		backSeaSurface = scroller.getBackSeaSurface();
		plastic1 = scroller.getPlastic1();
		plastic2 = scroller.getPlastic2();
		plastic3 = scroller.getPlastic3();
		plastic4 = scroller.getPlastic4();
		plastic5 = scroller.getPlastic5();
		plastic6 = scroller.getPlastic6();
		oil1 = scroller.getOil1();
	}

	private void initAssets() {
		background = AssetLoader.bg;
		seaFloor = AssetLoader.seaFloor;
		seaSurface = AssetLoader.seaSurface;
		sharkAnimation = AssetLoader.sharkAnimation;
		sharkMid = AssetLoader.shark;
		sharkDown = AssetLoader.sharkDown;
		sharkUp = AssetLoader.sharkUp;
		plastic = AssetLoader.plastic;
		oil = AssetLoader.oil;
		ready = AssetLoader.ready;
		bsLogo = AssetLoader.bsLogo;
		gameOver = AssetLoader.gameOver;
		highScore = AssetLoader.highScore;
		scoreboard = AssetLoader.scoreboard;
		retry = AssetLoader.retry;
		star = AssetLoader.star;
		noStar = AssetLoader.noStar;
	}

	private void drawBackground(int ypos) {
		batcher.draw(background, frontBg.getX(),
				ypos, frontBg.getWidth(),
				frontBg.getHeight());
		batcher.draw(background, backBg.getX(), ypos,
				backBg.getWidth(), backBg.getHeight());
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

	private void drawOil() {
		batcher.draw(oil, oil1.getX(), oil1.getY(), oil1.getWidth(),
				oil1.getHeight());
	}

	private void drawPlastics() {
		// Temporary code! Sorry about the mess :)
		// We will fix this when we finish the Pipe class.
		batcher.draw(plastic, plastic1.getX(), plastic1.getY(),
				plastic1.getWidth() / 2.0f, plastic1.getHeight() / 2.0f,
				plastic1.getWidth(), plastic1.getHeight(), 1, 1,
				plastic1.getRotation());

		// batcher.draw(plastic, plastic1.getX(), plastic1.getY() + 65,
		// plastic1.getWidth() / 2.0f, plastic1.getHeight() / 2.0f,
		// plastic1.getWidth(), plastic1.getHeight(), 1, 1,
		// plastic1.getRotation());

		batcher.draw(plastic, plastic2.getX(), plastic2.getY(),
				plastic2.getWidth() / 2.0f, plastic2.getHeight() / 2.0f,
				plastic2.getWidth(), plastic2.getHeight(), 1, 1,
				plastic2.getRotation());

		// batcher.draw(plastic, plastic2.getX(), plastic2.getY() + 65,
		// plastic2.getWidth() / 2.0f, plastic2.getHeight() / 2.0f,
		// plastic2.getWidth(), plastic2.getHeight(), 1, 1,
		// plastic2.getRotation());

		batcher.draw(plastic, plastic3.getX(), plastic3.getY(),
				plastic3.getWidth() / 2.0f, plastic3.getHeight() / 2.0f,
				plastic3.getWidth(), plastic3.getHeight(), 1, 1,
				plastic3.getRotation());

		// batcher.draw(plastic, plastic3.getX(), plastic3.getY() + 65,
		// plastic3.getWidth() / 2.0f, plastic3.getHeight() / 2.0f,
		// plastic3.getWidth(), plastic3.getHeight(), 1, 1,
		// plastic3.getRotation());

		batcher.draw(plastic, plastic4.getX(), plastic4.getY(),
				plastic4.getWidth() / 2.0f, plastic4.getHeight() / 2.0f,
				plastic4.getWidth(), plastic4.getHeight(), 1, 1,
				plastic4.getRotation());

		// batcher.draw(plastic, plastic4.getX(), plastic4.getY() + 65,
		// plastic4.getWidth() / 2.0f, plastic4.getHeight() / 2.0f,
		// plastic4.getWidth(), plastic4.getHeight(), 1, 1,
		// plastic4.getRotation());

		batcher.draw(plastic, plastic5.getX(), plastic5.getY(),
				plastic5.getWidth() / 2.0f, plastic5.getHeight() / 2.0f,
				plastic5.getWidth(), plastic5.getHeight(), 1, 1,
				plastic5.getRotation());

		// batcher.draw(plastic, plastic5.getX(), plastic5.getY() + 65,
		// plastic5.getWidth() / 2.0f, plastic5.getHeight() / 2.0f,
		// plastic5.getWidth(), plastic5.getHeight(), 1, 1,
		// plastic5.getRotation());

		batcher.draw(plastic, plastic6.getX(), plastic6.getY(),
				plastic6.getWidth() / 2.0f, plastic6.getHeight() / 2.0f,
				plastic6.getWidth(), plastic6.getHeight(), 1, 1,
				plastic6.getRotation());

		// batcher.draw(plastic, plastic6.getX(), plastic6.getY() + 65,
		// plastic6.getWidth() / 2.0f, plastic6.getHeight() / 2.0f,
		// plastic6.getWidth(), plastic6.getHeight(), 1, 1,
		// plastic6.getRotation());

	}

	private void drawMenuUI() {
		batcher.draw(bsLogo, 136 / 2 - 56, midPointY - 50,
				bsLogo.getRegionWidth() / 1.2f, bsLogo.getRegionHeight() / 1.2f);

		for (SimpleButton button : menuButtons) {
			button.draw(batcher);
		}

	}

	private void drawScoreboard() {
		batcher.draw(scoreboard, 22, midPointY - 30, 97, 37);

		batcher.draw(noStar, 25, midPointY - 15, 10, 10);
		batcher.draw(noStar, 37, midPointY - 15, 10, 10);
		batcher.draw(noStar, 49, midPointY - 15, 10, 10);
		batcher.draw(noStar, 61, midPointY - 15, 10, 10);
		batcher.draw(noStar, 73, midPointY - 15, 10, 10);

		if (myWorld.getScore() > 2) {
			batcher.draw(star, 73, midPointY - 15, 10, 10);
		}

		if (myWorld.getScore() > 17) {
			batcher.draw(star, 61, midPointY - 15, 10, 10);
		}

		if (myWorld.getScore() > 50) {
			batcher.draw(star, 49, midPointY - 15, 10, 10);
		}

		if (myWorld.getScore() > 80) {
			batcher.draw(star, 37, midPointY - 15, 10, 10);
		}

		if (myWorld.getScore() > 120) {
			batcher.draw(star, 25, midPointY - 15, 10, 10);
		}

		int length = ("" + myWorld.getScore()).length();

		AssetLoader.whiteFont.draw(batcher, "" + myWorld.getScore(),
				104 - (2 * length), midPointY - 20);

		int length2 = ("" + AssetLoader.getHighScore()).length();
		AssetLoader.whiteFont.draw(batcher, "" + AssetLoader.getHighScore(),
				104 - (2.5f * length2), midPointY - 3);

	}

	private void drawRetry() {
		batcher.draw(retry, 36, midPointY + 10, 66, 14);
	}

	private void drawReady() {
		batcher.draw(ready, 36, midPointY - 50, 68, 14);
	}

	private void drawGameOver() {
		batcher.draw(gameOver, 24, midPointY - 50, 92, 14);
	}
	
	private void drawScore() {
		int length = ("" + myWorld.getScore()).length();
		AssetLoader.shadow.draw(batcher, "" + myWorld.getScore(),
				68 - (3 * length), midPointY - 82);
		AssetLoader.font.draw(batcher, "" + myWorld.getScore(),
				68 - (3 * length), midPointY - 83);
	}

	private void drawHighScore() {
		batcher.draw(highScore, 22, midPointY - 50, 96, 14);
	}
	
	private void drawTransition(float delta) {
		if (alpha.getValue() > 0) {
			manager.update(delta);
			Gdx.gl.glEnable(GL20.GL_BLEND);
			Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
			shapeRenderer.begin(ShapeType.Filled);
			shapeRenderer.setColor(transitionColor.r, transitionColor.g,
					transitionColor.b, alpha.getValue());
			shapeRenderer.rect(0, 0, 136, 300);
			shapeRenderer.end();
			Gdx.gl.glDisable(GL20.GL_BLEND);

		}
	}

	private void drawDebugCollision() {
		shapeRenderer.begin(ShapeType.Filled);
		shapeRenderer.setColor(Color.RED);
		shapeRenderer.circle(shark.getBoundingCircle().x,
				shark.getBoundingCircle().y, shark.getBoundingCircle().radius);
		shapeRenderer.end();

		shapeRenderer.begin(ShapeType.Filled);
		shapeRenderer.setColor(Color.CYAN);
		shapeRenderer.circle(plastic1.getBoundingCircle().x,
				plastic1.getBoundingCircle().y,
				plastic1.getBoundingCircle().radius);
		shapeRenderer.end();

		shapeRenderer.begin(ShapeType.Filled);
		shapeRenderer.setColor(Color.LIGHT_GRAY);
		shapeRenderer.circle(plastic2.getBoundingCircle().x,
				plastic2.getBoundingCircle().y,
				plastic2.getBoundingCircle().radius);
		shapeRenderer.end();

		shapeRenderer.begin(ShapeType.Filled);
		shapeRenderer.setColor(Color.MAGENTA);
		shapeRenderer.circle(plastic3.getBoundingCircle().x,
				plastic3.getBoundingCircle().y,
				plastic3.getBoundingCircle().radius);
		shapeRenderer.end();

		shapeRenderer.begin(ShapeType.Filled);
		shapeRenderer.setColor(Color.MAROON);
		shapeRenderer.circle(plastic4.getBoundingCircle().x,
				plastic4.getBoundingCircle().y,
				plastic4.getBoundingCircle().radius);
		shapeRenderer.end();

		shapeRenderer.begin(ShapeType.Filled);
		shapeRenderer.setColor(Color.OLIVE);
		shapeRenderer.circle(plastic5.getBoundingCircle().x,
				plastic5.getBoundingCircle().y,
				plastic5.getBoundingCircle().radius);
		shapeRenderer.end();

		shapeRenderer.begin(ShapeType.Filled);
		shapeRenderer.setColor(Color.ORANGE);
		shapeRenderer.circle(plastic6.getBoundingCircle().x,
				plastic6.getBoundingCircle().y,
				plastic6.getBoundingCircle().radius);
		shapeRenderer.end();

		shapeRenderer.begin(ShapeType.Filled);
		shapeRenderer.setColor(Color.BLUE);
		shapeRenderer.rect(oil1.getBoundingRect().x, oil1.getBoundingRect().y,
				oil1.getBoundingRect().width, oil1.getBoundingRect().height);
		shapeRenderer.end();
	}

}