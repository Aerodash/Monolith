package com.aerodash.monolith.ui;

import com.aerodash.monolith.core.GameObjects;
import com.aerodash.monolith.core.shapes.LShape;
import com.aerodash.monolith.core.shapes.SShape;
import com.aerodash.monolith.core.shapes.TShape;
import com.aerodash.monolith.core.shapes.TetrisShape;
import com.aerodash.monolith.core.shapes.TetrisShape.InitialState;
import com.aerodash.monolith.core.shapes.TetrisShape.TypeState;
import com.aerodash.monolith.main.Monolith;
import com.aerodash.monolith.screens.Play;
import com.aerodash.monolith.utils.Assets;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class NextShapePanel extends Actor {
	
	private TetrisShape nextShape;
	private Color shapeColor;
	public static float scale = .6f;

	public NextShapePanel(float x, float y){
		setX(x);
		setY(y);
		setWidth(Monolith.width / 8 + 5);
		setHeight(80);
		shapeColor = new Color(1, 1, 1, .9f);
		nextShape();
	}
	
	public void nextShape() {
		TetrisShape.Type type = TetrisShape.random();
		if (nextShape != null){
			while (type == nextShape.typeState.type){
				type = TetrisShape.random();
			}
		}
		nextShape = TetrisShape.getShapeInstanceForUI(type, getX() + getWidth() / 2 - TetrisShape.getShapeWidthForUI(type) / 2, getY() + getHeight() / 2 - TetrisShape.getShapeHeightForUI(type) / 2, shapeColor, Monolith.tileSize * scale);
	
		if (nextShape.getClass().equals(LShape.class)){
			LShape l = (LShape) nextShape;
			if (l.state == LShape.InitialState.TWO){
				nextShape = new LShape(getX() + getWidth() / 2 - TetrisShape.getShapeWidthForUI(TetrisShape.Type.L) / 2 + Monolith.tileSize * scale, getY() + getHeight() / 2 - TetrisShape.getShapeHeightForUI(type) / 2, shapeColor, Monolith.tileSize * scale, InitialState.TWO);
			}
		}else if (nextShape.getClass().equals(SShape.class)){
			SShape l = (SShape) nextShape;
			if (l.state == SShape.InitialState.TWO){
				nextShape = new SShape(getX() + getWidth() / 2 - TetrisShape.getShapeWidthForUI(TetrisShape.Type.L) / 2 + Monolith.tileSize * scale, getY() + getHeight() / 2 - TetrisShape.getShapeHeightForUI(type) / 2, shapeColor, Monolith.tileSize * scale, InitialState.TWO);
			}
		}else if (nextShape.getClass().equals(TShape.class)){
			nextShape = new TShape(getX() + getWidth() / 2 - TetrisShape.getShapeWidthForUI(TetrisShape.Type.L) / 2, getY() + getHeight() / 2 - TetrisShape.getShapeHeightForUI(type) / 2 + Monolith.tileSize * scale, shapeColor, Monolith.tileSize * scale);
		}
		
		for (int i = 0; i < nextShape.tiles.size; i++){
			GameObjects.removeObject(nextShape.tiles.get(i).id);
		}
		
		nextShape.setOutlined(true);
	}

	@Override
	public void act(float delta) {
		if (Play.switchToNextShape){
			nextShape();
			Play.switchToNextShape = false;
		}
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha) {
		batch.setColor(0, 0, 0, 0.4f);
		batch.draw(Assets.tile, getX(), getY(), getWidth(), getHeight());
		
		nextShape.renderForUI(batch, scale);
		
		batch.setColor(0, 0, 0, 0.6f);
		batch.draw(Assets.tile, getX(), getY() + getHeight() - 20, 43, 20);
		
		Assets.font.setColor(Color.WHITE);
		Assets.font.draw(batch, "Next", getX() + 5, getY() + getHeight() - 5);
	
	}
	
	@Override
	public void drawDebug(ShapeRenderer sr) {
		sr.setColor(0, 0, 0, 1);
		sr.line(getX(), getY(), getX(), getY() + getHeight());
		sr.line(getX(), getY() + getHeight(), getX() + getWidth(), getY() + getHeight());
	
		//"Next" lines
		sr.line(getX(), getY() + getHeight() - 20, getX() + 43, getY() + getHeight() - 20);
		sr.line(getX() + 43, getY() + getHeight() - 20, getX() + 43, getY() + getHeight());
	}

	public TypeState getCurrentShapeTypeState(){
		return nextShape.typeState;
	}
}
