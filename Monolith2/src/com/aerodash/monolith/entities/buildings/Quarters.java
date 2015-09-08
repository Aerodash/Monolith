package com.aerodash.monolith.entities.buildings;

import com.aerodash.monolith.core.Cost;
import com.aerodash.monolith.core.shapes.TetrisShape.TypeState;
import com.aerodash.monolith.entities.Building;
import com.aerodash.monolith.utils.Colors;

public class Quarters extends Building{

	public Quarters(float x, float y, TypeState typeState) {
		super(x, y, Colors.quarters, typeState, Cost.quarters);
	}
}
