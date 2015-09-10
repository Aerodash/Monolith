package com.aerodash.monolith.ui;

import com.aerodash.monolith.core.Cost;
import com.aerodash.monolith.entities.Building;
import com.aerodash.monolith.main.Monolith;
import com.aerodash.monolith.utils.Assets;
import com.aerodash.monolith.utils.Colors;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class BuildingLabel extends Actor{

	private GlyphLayout layout;
	private BitmapFont font;
	private String label;
	private Color color;
	private Cost cost;
	private LabelListener listener;
	
	private int updatesRemaining = 5;
	
	public BuildingLabel(float x, float y, String label, Color color, Cost cost){
		setPosition(x, y);
		setWidth(Monolith.width / 8);
		setHeight(40);
		this.label = label;
		this.color = color;
		this.cost = cost;
		font = Assets.font;
		layout = new GlyphLayout();
	}
	
	@Override
	public void act(float delta) {
		if (isClicked() && listener != null && Building.selectedBuilding == null){
			listener.onClick();
		}
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha) {
		
		if (isClicked() && listener != null){
			updatesRemaining = 5;
			batch.setColor(1, 1, 1, 0.1f);
		}else{
			batch.setColor(0, 0, 0, 0.4f);
		}
		if (updatesRemaining >= 0){
			updatesRemaining--;
			batch.setColor(1, 1, 1, 0.1f);
		}
		batch.draw(Assets.tile, getX(), getY(), getWidth(), getHeight());

		font.setColor(color);
		layout.setText(font, label);
		font.draw(batch, label, getX() + getWidth() / 2 - layout.width / 2, getY() + getHeight() - 5);
		
		batch.setColor(Colors.energy);
		batch.draw(Assets.tile, getX() + getWidth() / 6 - 6f, getY() + 11, 5, 5, 7, 7, 1, 1, 45, true);
		Assets.smallFont.setColor(Color.WHITE);
		Assets.smallFont.draw(batch, Integer.toString(cost.energy), getX() + getWidth() / 6 + 6, getY() + 17);
		
		batch.setColor(Colors.particle);
		batch.draw(Assets.tile, getX() + 3 * getWidth() / 6 - 6f, getY() + 10, 7, 7);
		Assets.smallFont.draw(batch, Integer.toString(cost.particles), getX() + 3 * getWidth() / 6 + 3, getY() + 17);
		
		batch.setColor(Colors.food);
		batch.draw(Assets.triangle, getX() + 5 * getWidth() / 6 - 8f, getY() + 10, 8, 8);
		Assets.smallFont.draw(batch, Integer.toString(cost.food), getX() + 5 * getWidth() / 6 + 1, getY() + 17);
		
	}
	
	@Override
	public void drawDebug(ShapeRenderer sr) {
		
		sr.setColor(0, 0, 0, 1);
		sr.line(getX() + getWidth(), getY(), getX() + getWidth(), getY() + getHeight());
		sr.line(getX(), getY() + getHeight(), getX() + getWidth(), getY() + getHeight());
		sr.line(getX(), getY(), getX() + getWidth(), getY());
	}
	
	public boolean isClicked(){
		return Gdx.input.justTouched() && getBounds().contains(Gdx.input.getX(), Monolith.height - Gdx.input.getY()) && Gdx.input.isButtonPressed(Buttons.LEFT);
	}
	
	public Rectangle getBounds(){
		return new Rectangle(getX(), getY(), getWidth() - 1, getHeight());
	}
	
	public void setListener(LabelListener listener){
		this.listener = listener;
	}
	
}
