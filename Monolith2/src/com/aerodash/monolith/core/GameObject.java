package com.aerodash.monolith.core;

import com.aerodash.monolith.main.Monolith;
import com.aerodash.monolith.screens.Play;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public abstract class GameObject {

	public int id;
	protected float x, y;
	protected float width, height;
	protected Color color;
	protected boolean doRender, doUpdate;
	public boolean toBeRemoved = false;
	
	public abstract void update(float delta);
	public abstract void render(SpriteBatch sb);
	
	protected GameObject(float x, float y, float width, float height, Color color, boolean doRender, boolean doUpdate) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.color = color;
		this.doRender = doRender;
		this.doUpdate = doUpdate;
		if (doRender || doUpdate){
			GameObjects.addObject(this);
		}
	}
	
	public Rectangle getBounds(){
		return new Rectangle(x, y, width - 1, height - 1);
	}
	
	public boolean isJustClicked(){
		return getBounds().contains(Play.getMouse()) && Gdx.input.justTouched() && Gdx.input.isButtonPressed(Buttons.LEFT);
	}
	public boolean isClicked(){
		return getBounds().contains(Play.getMouse()) && Gdx.input.isTouched() && Gdx.input.isButtonPressed(Buttons.LEFT);
	}
	public boolean clickedOutside(){
		return !getBounds().contains(Play.getMouse()) && Gdx.input.isTouched();
	}
	public float getX() {
		return x;
	}
	public void setX(float x) {
		this.x = x;
	}
	public float getY() {
		return y;
	}
	public void setY(float y) {
		this.y = y;
	}
	public float getWidth() {
		return width;
	}
	public void setWidth(float width) {
		this.width = width;
	}
	public float getHeight() {
		return height;
	}
	public void setHeight(float height) {
		this.height = height;
	}
	
	public void setPos(Vector2 pos){
		x = pos.x;
		y = pos.y;
	}
	
	public void setPos(float x, float y){
		this.x = x;
		this.y = y;
	}
	
	public float getGridX(){
		return x / Monolith.tileSize;
	}
	public float getGridY(){
		return y / Monolith.tileSize;
	}
	
	public float getId(){
		return id;
	}
	
	public void dispose(){
		GameObjects.removeObject(id);
	}
	
}
