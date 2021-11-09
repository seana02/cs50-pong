package com.seana02.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.seana02.Main;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		Main main = new Main();
		config.title = main.getTitle();
		config.width = main.getWidth();
		config.height = main.getHeight();
		new LwjglApplication(main, config);
	}
}
