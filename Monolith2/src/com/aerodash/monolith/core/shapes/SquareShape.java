package com.aerodash.monolith.core.shapes;

import com.aerodash.monolith.core.Tile;
import com.aerodash.monolith.core.shapes.TetrisShape.InitialState;
import com.aerodash.monolith.core.shapes.TetrisShape.Type;
import com.aerodash.monolith.core.shapes.TetrisShape.TypeState;
import com.aerodash.monolith.entities.Building;
import com.aerodash.monolith.main.Monolith;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class SquareShape extends TetrisShape{

	/*	#########
	 * 	#   #   #
	 *  #   #   #
	 *  #########
	 *  #   #   #
	 *  # X #   # Coordinate of the shape relative to that tile
	 *  #########
	 */
	public SquareShape(float x, float y, Color color) {
		super(x, y, color, new TypeState(InitialState.ONE, Type.Square));
		
		tiles.add(new Tile(x, y, color, this));
		tiles.add(new Tile(x + 1, y, color, this));
		tiles.add(new Tile(x, y + 1, color, this));
		tiles.add(new Tile(x + 1, y + 1, color, this));
		
	}
	
	public SquareShape(float x, float y, Color color, float tileSize) {
		super(x, y, color, new TypeState(InitialState.ONE, Type.Square));
		
		tiles.add(new Tile(x, y, color, this));
		tiles.add(new Tile(x + 1 * tileSize, y, color, this));
		tiles.add(new Tile(x, y + 1 * tileSize, color, this));
		tiles.add(new Tile(x + 1 * tileSize, y + 1 * tileSize, color, this));
		
	}

	@Override
	public void rotate() {
		//do not rotate
	}

}
