package com.obugames.babyshark.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.obugames.babyshark.BSGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "Baby Shark";
		config.width = 272;
        config.height = 408;
		new LwjglApplication(new BSGame(), config);
	}
}
