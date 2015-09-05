package com.obugames.babyshark;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.obugames.bshelpers.AssetLoader;
import com.obugames.screens.GameScreen;
import com.obugames.screens.SplashScreen;

public class BSGame extends Game {
	@Override
	public void create() {
		AssetLoader.load();
		setScreen(new SplashScreen(this));
	}
	@Override
    public void dispose() {
        super.dispose();
        AssetLoader.dispose();
    }
}
