package com.aerodash.monolith.entities;

import com.aerodash.monolith.core.GameObject;
import com.aerodash.monolith.entities.buildings.Extractor;
import com.aerodash.monolith.main.Monolith;
import com.aerodash.monolith.utils.Assets;
import com.aerodash.monolith.utils.Colors;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class ParticleMine extends GameObject {

	Array<Color> colors;
	Array<Vector2> randomPoses;
	private int remainingParticles = 10;
	private boolean depleted = false;
	private Extractor linkedExtractor = null;

	public ParticleMine(float x, float y) {
		super(x * Monolith.tileSize, y * Monolith.tileSize, 0, 0, null, true, true);
		randomPoses = new Array<>();
		colors = new Array<>();

		for (int i = 0; i < 20; i++) {
			Vector2 pos = new Vector2(this.x + MathUtils.random(3, Monolith.tileSize - 5),
					this.y + MathUtils.random(3, Monolith.tileSize - 6));
			randomPoses.add(pos);
			colors.add(Colors.randomShade(Colors.particle));
		}
	}
	
	public void setExtractor(Extractor linkedExtractor){
		this.linkedExtractor = linkedExtractor;
	}

	public void mine(int amount) {
		if (!depleted) {
			remainingParticles -= amount;
			if (remainingParticles <= 0) {
				depleted = true;
			}
		}
	}
	
	public boolean isDepleted(){
		return depleted;
	}

	@Override
	public void update(float delta) {
		if (isDepleted()){
			linkedExtractor.mineDepleted = true;
			dispose();
		}
	}

	@Override
	public void render(SpriteBatch sb) {
		sb.begin();

		for (int i = 0; i < randomPoses.size; i++) {
			sb.setColor(colors.get(i));
			sb.draw(Assets.tile, randomPoses.get(i).x, randomPoses.get(i).y, 5, 5);
		}
		
		Assets.font.setColor(Color.WHITE);
		Assets.layout.setText(Assets.font, Integer.toString(remainingParticles));
		Assets.font.draw(sb, Integer.toString(remainingParticles), x + Monolith.tileSize / 2 - Assets.layout.width / 2, y + Assets.layout.height + Assets.layout.height / 2);

		sb.end();
	}

}
