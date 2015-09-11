package com.aerodash.monolith.entities;

import com.aerodash.monolith.core.GameObject;
import com.aerodash.monolith.main.Monolith;
import com.aerodash.monolith.utils.Assets;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Minion extends GameObject{

	public Minion(float x, float y) {
		super(x * Monolith.tileSize, y * Monolith.tileSize, Assets.minion.getRegionWidth(), Assets.minion.getRegionHeight(), Color.WHITE, true, true);
	}

	@Override
	public void update(float delta) {
		
	}

	@Override
	public void render(SpriteBatch sb) {
		sb.begin();
		sb.setColor(Color.WHITE);
		sb.draw(Assets.minion, x + Monolith.tileSize / 2 - Assets.minion.getRegionWidth() / 2, y + Monolith.tileSize / 2 - Assets.minion.getRegionHeight() / 2);
		sb.end();
	}

}
