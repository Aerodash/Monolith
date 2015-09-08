package com.aerodash.monolith.core.shapes;

import com.aerodash.monolith.core.Tile;
import com.badlogic.gdx.graphics.Color;

public class TShape extends TetrisShape{
	
	/*
	 *  #####
	 *  #   #
	 *  #   #
	 *  #########
	 *  #X	#	# Coordinate of the shape relative to that tile
	 *  #   #   #
	 *  #########
	 *  # 	#
	 * 	#	# 
	 * 	#####
	 */
	private RotationState rot = RotationState.INITIAL;
	
	public TShape(float x, float y, Color color) {
		super(x, y, color, new TypeState(InitialState.ONE, Type.T));
		
		tiles.add(new Tile(x, y, color, this));
		tiles.add(new Tile(x, y + 1, color, this));
		tiles.add(new Tile(x, y - 1, color, this));
		tiles.add(new Tile(x + 1, y, color, this));
	}
	
	public TShape(float x, float y, Color color, float tileSize) {
		super(x, y, color, new TypeState(InitialState.ONE, Type.T));
		
		tiles.add(new Tile(x, y, color, this));
		tiles.add(new Tile(x, y + 1 * tileSize, color, this));
		tiles.add(new Tile(x, y - 1 * tileSize, color, this));
		tiles.add(new Tile(x + 1 * tileSize, y, color, this));
	}

	@Override
	public void rotate() {
		float x = tiles.get(0).getGridX();
		float y = tiles.get(0).getGridY();
		
		switch(rot){
		case INITIAL:
			tiles.get(1).setPos(x + 1, y);
			tiles.get(2).setPos(x - 1, y);
			tiles.get(3).setPos(x, y - 1);
			
			rot = RotationState.NINTY;
			break;
		case NINTY:
			tiles.get(1).setPos(x, y - 1);
			tiles.get(2).setPos(x, y + 1);
			tiles.get(3).setPos(x - 1, y);
			
			rot = RotationState.ONEEIGHTY;
			break;
		case ONEEIGHTY:
			tiles.get(1).setPos(x - 1, y);
			tiles.get(2).setPos(x + 1, y);
			tiles.get(3).setPos(x, y + 1);
			
			rot = RotationState.TWOSEVENTY;
			break;
		case TWOSEVENTY:
			tiles.get(1).setPos(x, y + 1);
			tiles.get(2).setPos(x, y - 1);
			tiles.get(3).setPos(x + 1, y);
			
			rot = RotationState.INITIAL;
			break;
		}
		
	}
	
}
