package com.aerodash.monolith.entities.buildings;

import com.aerodash.monolith.core.Cost;
import com.aerodash.monolith.core.shapes.TetrisShape.InitialState;
import com.aerodash.monolith.core.shapes.TetrisShape.Type;
import com.aerodash.monolith.core.shapes.TetrisShape.TypeState;
import com.aerodash.monolith.entities.Building;
import com.badlogic.gdx.graphics.Color;

public class Stock extends Building{
	
	public Stock(float x, float y) {
		super(x, y, Color.GRAY, new TypeState(InitialState.ONE, Type.Tile), Cost.free);
	}
	
	@Override
	public void addResource(com.aerodash.monolith.entities.Resource.Type type) {
		
	}
}
