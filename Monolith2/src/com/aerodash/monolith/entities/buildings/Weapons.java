package com.aerodash.monolith.entities.buildings;

import com.aerodash.monolith.core.Cost;
import com.aerodash.monolith.core.shapes.TetrisShape.TypeState;
import com.aerodash.monolith.entities.Building;
import com.aerodash.monolith.utils.Colors;

public class Weapons extends Building{

	public Weapons(float x, float y, TypeState typeState) {
		super(x, y, Colors.weapons, typeState, Cost.weapons);
	}
}
