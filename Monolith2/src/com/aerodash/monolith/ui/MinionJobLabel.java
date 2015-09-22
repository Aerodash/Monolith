package com.aerodash.monolith.ui;

import com.aerodash.monolith.entities.Minion;
import com.aerodash.monolith.main.Monolith;
import com.aerodash.monolith.utils.Assets;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;

public class MinionJobLabel extends Actor{

	public static final int height = (Monolith.height - 80) / 5;
	private String text;
	private LabelListener listener;
	private int updatesRemaining = 5;
	private Array<Minion> minions;
	
	public MinionJobLabel(float x, float y, String text){
		setPosition(x, y);
		setWidth(50);
		setHeight((Monolith.height - 80) / 5);
		this.text = text;
		minions = new Array<>();
	}
	
	@Override
	public void act(float delta) {
		if (isClicked() && listener != null){
			listener.onClick();
		}
		if (listener == null){
			listener = new LabelListener(){
				@Override
				public void onClick() {
				}
			};
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
		
		Assets.smallFont.setColor(Color.WHITE);
		Assets.layout.setText(Assets.smallFont, text);
		Assets.smallFont.draw(batch, text, getX() + getWidth() / 2 - Assets.layout.width / 2, getY() + getHeight() - 2);
	}
	
	@Override
	public void drawDebug(ShapeRenderer sr) {
		sr.setColor(0, 0, 0, 1);
		sr.line(getX(), getY(), getX() + getWidth(), getY());
		sr.line(getX(), getY(), getX(), getY() + getHeight());
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
	
	public void addMinion(Minion m){
		minions.add(m);
	}
	
	public void removeMinion(Minion m){
		minions.removeValue(m, true);
	}
}
