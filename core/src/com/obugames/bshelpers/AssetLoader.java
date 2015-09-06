package com.obugames.bshelpers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class AssetLoader {

	public static Texture texture, logoTexture;
	public static TextureRegion logo, bsLogo, bg, seaFloor, seaSurface,
			playButtonUp, playButtonDown, ready, gameOver, highScore,
			scoreboard, star, noStar, retry;;

	public static Animation sharkAnimation;
	public static TextureRegion shark, sharkDown, sharkUp;

	public static TextureRegion plastic, oil;

	public static Sound dead, flap, coin, fall;
	public static BitmapFont font, shadow, whiteFont;

	public static Preferences prefs;

	public static void load() {

		logoTexture = new Texture(Gdx.files.internal("data/logo.png"));
		logoTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);

		logo = new TextureRegion(logoTexture, 0, 0, 512, 114);

		texture = new Texture(Gdx.files.internal("data/texture.png"));
		texture.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);

		playButtonUp = new TextureRegion(texture, 0, 83, 29, 16);
		playButtonDown = new TextureRegion(texture, 29, 83, 29, 16);
		playButtonUp.flip(false, true);
		playButtonDown.flip(false, true);

		ready = new TextureRegion(texture, 201, 0, 34, 7);
		ready.flip(false, true);

		retry = new TextureRegion(texture, 201, 27, 33, 7);
		retry.flip(false, true);

		gameOver = new TextureRegion(texture, 201, 9, 46, 7);
		gameOver.flip(false, true);

		scoreboard = new TextureRegion(texture, 149, 60, 97, 37);
		scoreboard.flip(false, true);

		star = new TextureRegion(texture, 189, 44, 10, 10);
		noStar = new TextureRegion(texture, 202, 44, 10, 10);

		star.flip(false, true);
		noStar.flip(false, true);

		highScore = new TextureRegion(texture, 201, 18, 48, 7);
		highScore.flip(false, true);

		bsLogo = new TextureRegion(texture, 0, 55, 135, 24);
		bsLogo.flip(false, true);

		bg = new TextureRegion(texture, 0, 0, 136, 43);
		bg.flip(false, true);

		seaFloor = new TextureRegion(texture, 0, 43, 143, 11);
		seaFloor.flip(false, true);

		seaSurface = new TextureRegion(texture, 0, 106, 143, 12);
		seaSurface.flip(false, true);

		sharkDown = new TextureRegion(texture, 136, 0, 20, 15);
		sharkDown.flip(false, true);

		shark = new TextureRegion(texture, 156, 0, 20, 15);
		shark.flip(false, true);

		sharkUp = new TextureRegion(texture, 176, 0, 20, 15);
		sharkUp.flip(false, true);

		TextureRegion[] sharks = { sharkDown, shark, sharkUp };
		sharkAnimation = new Animation(0.24f, sharks);
		sharkAnimation.setPlayMode(Animation.PlayMode.LOOP_PINGPONG);

		plastic = new TextureRegion(texture, 136, 20, 24, 19);
		plastic.flip(false, true);

		oil = new TextureRegion(texture, 68, 84, 69, 12);
		plastic.flip(false, true);

		dead = Gdx.audio.newSound(Gdx.files.internal("data/dead.wav"));
		flap = Gdx.audio.newSound(Gdx.files.internal("data/flap.wav"));
		coin = Gdx.audio.newSound(Gdx.files.internal("data/coin.wav"));
		fall = Gdx.audio.newSound(Gdx.files.internal("data/fall.wav"));

		font = new BitmapFont(Gdx.files.internal("data/text.fnt"));
		font.getData().setScale(.25f, -.25f);

		whiteFont = new BitmapFont(Gdx.files.internal("data/whitetext.fnt"));
		whiteFont.getData().setScale(.1f, -.1f);

		shadow = new BitmapFont(Gdx.files.internal("data/shadow.fnt"));
		shadow.getData().setScale(.25f, -.25f);

		// Create (or retrieve existing) preferences file
		prefs = Gdx.app.getPreferences("BabyShark");

		// Provide default high score of 0
		if (!prefs.contains("highScore")) {
			prefs.putInteger("highScore", 0);
		}
	}

	public static void dispose() {
		// We must dispose of the texture when we are finished.
		texture.dispose();
		dead.dispose();
		flap.dispose();
		coin.dispose();
		font.dispose();
		shadow.dispose();
	}

	// Receives an integer and maps it to the String highScore in prefs
	public static void setHighScore(int val) {
		prefs.putInteger("highScore", val);
		prefs.flush();
	}

	// Retrieves the current high score
	public static int getHighScore() {
		return prefs.getInteger("highScore");
	}

}