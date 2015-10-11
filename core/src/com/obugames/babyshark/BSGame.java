package com.obugames.babyshark;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.obugames.bshelpers.AssetLoader;
import com.obugames.screens.GameScreen;
import com.obugames.screens.SplashScreen;

public class BSGame extends Game {
    private AdsController adsController;

    public BSGame(AdsController adsController){
        this.adsController = adsController;
    }

    @Override
    public void create() {
        AssetLoader.load();
        adsController.showBannerAd();
        setScreen(new SplashScreen(this));
    }

    @Override
    public void dispose() {
        super.dispose();
        AssetLoader.dispose();
    }
}
