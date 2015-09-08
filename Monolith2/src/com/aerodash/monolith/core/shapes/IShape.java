package com.aerodash.monolith.core.shapes;

import com.aerodash.monolith.core.GameObjects;
import com.aerodash.monolith.core.Tile;
import com.aerodash.monolith.core.shapes.TetrisShape.InitialState;
import com.aerodash.monolith.core.shapes.TetrisShape.Type;
import com.aerodash.monolith.core.shapes.TetrisShape.TypeState;
import com.aerodash.monolith.entities.Building;
import com.aerodash.monolith.main.Monolith;
import com.badlogic.gdx.graphics.Color;

public class IShape extends TetrisShape{

	/*
	 *  #####
	 *  #	#
	 *  #	#
	 *  #####
	 * 	#	#
	 *  #	#
	 *  #####
	 *  #	#
	 *  #	#
	 *  #####
	 *  #	#
	 *  #X  # Coordinate of the shape relative to that tile
	 *  #####
	 */
	private RotationState rot = RotationState.INITIAL;
	
	public IShape(float x, float y, Color color) {
		super(x, y, color, new TypeState(InitialState.ONE, Type.I));
		
		tiles.add(new Tile(x, y, color, this));
		tiles.add(new Tile(x, y + 1, color, this));
		tiles.add(new Tile(x, y + 2, color, this));
		tiles.add(new Tile(x, y + 3, color, this));
	}
	
	public IShape(float x, float y, Color color, float tileSize) {
		super(x, y, color, new TypeState(InitialState.ONE, Type.I));
		
		tiles.add(new Tile(x, y, color, this));
		tiles.add(new Tile(x, y + 1 * tileSize, color, this));
		tiles.add(new Tile(x, y + 2 * tileSize, color, this));
		tiles.add(new Tile(x, y + 3 * tileSize, color, this));
	}

	//initially spawns | and rotate to __
	@Override
	public void rotate() {
		float x = tiles.get(0).getGridX();
		float y = tiles.get(0).getGridY();
		
		if (rot == RotationState.INITIAL){
			tiles.get(1).setPos(x + 1, y);
			tiles.get(2).setPos(x + 2, y);
			tiles.get(3).setPos(x + 3, y);
			
			rot = RotationState.NINTY;
		}else{
			tiles.get(1).setPos(x, y + 1);
			tiles.get(2).setPos(x, y + 2);
			tiles.get(3).setPos(x, y + 3);
			
			rot = RotationState.INITIAL;
		}
		
	}
	
}
