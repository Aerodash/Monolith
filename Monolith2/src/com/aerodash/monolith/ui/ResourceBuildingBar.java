package com.aerodash.monolith.ui;

import com.aerodash.monolith.core.GameObject;
import com.aerodash.monolith.main.Monolith;
import com.aerodash.monolith.utils.Assets;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class ResourceBuildingBar extends GameObject {

	private int totalAmount;
	private int currentAmount;
	private Color lightColor;
	private boolean complete;

	public ResourceBuildingBar(float x, float y, Color color, int totalAmount) {
		super(x, y, Monolith.tileSize - 4, Monolith.tileSize / 3 - 2, color, false, false);
		this.totalAmount = totalAmount;
		complete = false;
		currentAmount = 0;
		lightColor = color.cpy();
		lightColor.a = .4f;
	}

	@Override
	public void update(float delta) {
		if (currentAmount >= totalAmount) {
			complete = true;
		}
		if (Gdx.input.isKeyJustPressed(Keys.A)) {
			incrementAmount();
		}
	}

	@Override
	public void render(SpriteBatch sb) {
		if (totalAmount > 0){

			sb.begin();
			// bg bar
			sb.setColor(lightColor);
			sb.draw(Assets.tile, x, y, width, height);
	
			// main bar
			sb.setColor(color);
			sb.draw(Assets.tile, x, y, (width * currentAmount) / totalAmount, height);
	
			sb.end();
		}
	}

	public boolean isComplete() {
		return complete;
	}

	public void incrementAmount() {
		if (complete) return;
		currentAmount++;
	}

}
