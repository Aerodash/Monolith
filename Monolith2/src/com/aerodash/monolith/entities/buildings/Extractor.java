package com.aerodash.monolith.entities.buildings;

import java.util.ArrayList;

import com.aerodash.monolith.core.Cost;
import com.aerodash.monolith.core.GameObjects;
import com.aerodash.monolith.core.shapes.TetrisShape.TypeState;
import com.aerodash.monolith.entities.Building;
import com.aerodash.monolith.entities.ParticleMine;
import com.aerodash.monolith.entities.Resource.Type;
import com.aerodash.monolith.screens.Play;
import com.aerodash.monolith.utils.Colors;
import com.badlogic.gdx.math.Vector2;

public class Extractor extends Building {

	private ParticleMine linkedMine;
	private float miningInterval = .5f;
	private float miningAccum = 0;
	public boolean mineDepleted = false; 

	public Extractor(float x, float y, TypeState typeState) {
		super(x, y, Colors.extractor, typeState, Cost.extractor);
	}

	@Override
	public void update(float delta) {
		super.update(delta);

		if (linkedMine == null && !mineDepleted) {
			ArrayList<Vector2> vecs = getSurroundingTiles();
			for (Vector2 v : vecs) {
				if (GameObjects.isObjectOfType(v.x, v.y, ParticleMine.class)) {
					linkedMine = (ParticleMine) GameObjects.objectAt(v.x, v.y);
					linkedMine.setExtractor(this);
					break;
				}
			}
		}

		if (linkedMine != null && built && !linkedMine.isDepleted() && !isFull) {
			miningAccum += delta;
			if (miningAccum >= miningInterval) {//mine each 2 seconds
				miningAccum = 0;
				addResource(Type.Particle);
				if (!isFull){
					linkedMine.mine(1);
					Play.resRemaining.plus(1, 0, 0);
				}
			}
		}

	}

}
