package com.aerodash.monolith.core.shapes;

import com.aerodash.monolith.core.Tile;
import com.badlogic.gdx.graphics.Color;

public class TileShape extends TetrisShape {

	protected TileShape(float x, float y, Color color) {
		super(x, y, color, new TypeState(InitialState.ONE, Type.Tile));
		
		tiles.add(new Tile(x, y, color, this));
	}

	@Override
	public void rotate() {
		//do not rotate
	}

}
