package com.quadx.asteroids;

import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.quadx.asteroids.tools.Game;

public class AndroidLauncher extends AndroidApplication {
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		Game g = new Game();
		initialize( g, config);
		g.setScreenRes(ScreenAnalyzer.getScreenResolution());
		g.setMode(Game.Mode.ANDROID);

	}
}
