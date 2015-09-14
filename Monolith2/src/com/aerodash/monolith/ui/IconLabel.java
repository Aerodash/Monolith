package com.aerodash.monolith.ui;

import com.aerodash.monolith.entities.Building;
import com.aerodash.monolith.main.Monolith;
import com.aerodash.monolith.utils.Assets;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class IconLabel extends Actor {

	private String text;
	private TextureRegion icon;
	private Color iconColor;
	private LabelListener listener;
	private float iconScale = .4f;

	public IconLabel(float x, float y, String text, TextureRegion icon, Color iconColor) {
		setX(x);
		setY(y);
		this.text = text;
		this.icon = icon;
		this.iconColor = iconColor;
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {

		if (Building.selectedAfterBuiltBuilding != null) {
			
			Assets.smallFont.setColor(iconColor);
			Assets.smallFont.draw(batch, text, getX() + icon.getRegionWidth() * iconScale + 5, getY());

			batch.setColor(iconColor);
			batch.draw(icon, getX(), getY() - icon.getRegionHeight() * iconScale, 0, 0, icon.getRegionWidth(),
					icon.getRegionHeight(), iconScale, iconScale, 0);

		}
	}

	@Override
	public void act(float delta) {//create a cancel button for building waiting to be built TODO
		if (isClicked() && listener != null && Building.selectedAfterBuiltBuilding != null) {
			listener.onClick();
		}
	}

	public void setListener(LabelListener listener) {
		this.listener = listener;
	}

	public Rectangle getBounds() {
		Assets.layout.setText(Assets.smallFont, text);
		float height = icon.getRegionHeight() > Assets.layout.height ? icon.getRegionHeight() : Assets.layout.height;
		return new Rectangle(getX() - 5, getY() - icon.getRegionHeight() * iconScale - height / 4,
				icon.getRegionWidth() + Assets.layout.width + 5, height);
	}

	public boolean isClicked() {
		return Gdx.input.justTouched() && getBounds().contains(Gdx.input.getX(), Monolith.height - Gdx.input.getY())
				&& Gdx.input.isButtonPressed(Buttons.LEFT);
	}

}
