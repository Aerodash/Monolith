package com.aerodash.monolith.entities.buildings;

import com.aerodash.monolith.core.Cost;
import com.aerodash.monolith.core.shapes.TetrisShape.TypeState;
import com.aerodash.monolith.entities.Building;
import com.aerodash.monolith.utils.Colors;

public class Extractor extends Building {

	public Extractor(float x, float y, TypeState typeState) {
		super(x, y, Colors.extractor, typeState, Cost.extractor);
		// TODO Auto-generated constructor stub
	}

}
