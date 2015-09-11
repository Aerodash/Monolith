package com.aerodash.monolith.main;

import com.aerodash.monolith.screens.Play;
import com.aerodash.monolith.utils.Debug;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.graphics.GL20;

public class Monolith extends Game{
	
	public static final String title = "Monolith";
	public static final int height = 300;
	public static final int width = height * 16 / 9;
	public static final int tileSize = 20;
	
	@Override
	public void create() {
		Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 1);
		Gdx.gl.glClearColor(0.055f, 0.066f, 0.1f, 1);
		Debug.on();
		Debug.showText();
		setScreen(new Play());
	}
	
	@Override
	public void render() {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		super.render();
		Debug.render();
	}
	
	@Override
	public void dispose() {
		super.dispose();
		Debug.dispose();
	}

	public static void main(String[] args){
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		
		cfg.width = width;
		cfg.height = height;
		cfg.title = title;
		
		new LwjglApplication(new Monolith(), cfg);
	}
}
