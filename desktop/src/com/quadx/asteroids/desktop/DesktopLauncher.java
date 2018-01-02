package com.quadx.asteroids.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.math.Vector2;
import com.quadx.asteroids.tools.Game;

class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		Vector2 res=DesktopScreenAnalyzer.getScreenResolution();
		res.scl(1f/3f);
		config.width= (int) res.x;
		config.height= (int) res.y;
		config.fullscreen=false;
		new LwjglApplication(new Game(), config);

		com.quadx.asteroids.tools.Game.setScreenRes(res);
		com.quadx.asteroids.tools.Game.setMode(Game.Mode.DESKTOP);
	}
}
