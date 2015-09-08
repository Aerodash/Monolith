package com.aerodash.monolith.utils;

import com.aerodash.monolith.core.GameObjects;
import com.aerodash.monolith.entities.Building;
import com.aerodash.monolith.main.Monolith;
import com.aerodash.monolith.screens.Play;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Matrix4;

public abstract class Debug {

	private static SpriteBatch sb;
	private static ShapeRenderer sr;
	private static boolean active = false;
	private static boolean gridShown = false;
	private static boolean textShown = false;
	private static float tileSize = 20;
	
	static{
		sb = new SpriteBatch();
		sr = new ShapeRenderer();
	}
	
	private Debug(){}
	
	public static void setProjectionMatrix(Matrix4 projection){
		sb.setProjectionMatrix(projection);
		sr.setProjectionMatrix(projection);
	}
	public static void render(){
		if (active){
			
			if (gridShown){
				sr.setColor(new Color(1, 1, 1, .2f));
				sr.begin(ShapeType.Line);
				
				for (int x = 0; x < Monolith.width; x += tileSize){
					sr.line(x, Monolith.height, x, 0);
				}
				
				for (int y = 0; y < Monolith.height; y += tileSize){
					sr.line(0, y, Monolith.width, y);
				}
				
				sr.end();
			}
			
			if (textShown){
				sb.begin();
				
				Assets.smallFont.setColor(Color.WHITE);
				Assets.smallFont.draw(sb, "FPS : " + Gdx.graphics.getFramesPerSecond(), 10, Monolith.height - 10);
				Assets.smallFont.draw(sb, "Mouse@ : [" + Play.getGridMouse().x + ", " + Play.getGridMouse().y +"] (" + Play.getMouse().x + ", " +  Play.getMouse().y + ")", 10, Monolith.height - 25);
				Assets.smallFont.draw(sb, "Nb Objects : " + GameObjects.getCount(), 10, Monolith.height - 40);
				Assets.smallFont.draw(sb, "Selected Building : " + Building.selectedAfterBuiltBuilding, 10, Monolith.height - 55);//15pixels offset on Y axis
				
				sb.end();
			}
		}
	}
	
	public static boolean isActive(){
		return active;
	}
	
	public static boolean isGridShown(){
		return gridShown;
	}
	
	public static boolean isTextShown(){
		return textShown;
	}
	
	public static void showText(){
		active = true;
		textShown = true;
	}
	
	public static void hideText(){
		textShown = false;
	}
	
	public static void showGrid(){
		active = true;
		gridShown = true;
	}
	
	public static void hideGrid(){
		gridShown = false;
	}
	
	public static void on(){
		active = true;
	}
	
	public static void off(){
		active = false;
	}
	
	public static void showAll(){
		gridShown = true;
		textShown = true;
	}

	public static void dispose(){
		sb.dispose();
		sr.dispose();
	}
}
