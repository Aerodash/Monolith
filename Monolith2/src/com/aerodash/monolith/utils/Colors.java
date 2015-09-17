package com.aerodash.monolith.utils;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;

public class Colors {
	
	public static final Color extractor = new Color(0.82f, 0.27f, 0.51f, 1);
	public static final Color particle = new Color(0.89f, 0.55f, 0.71f, 1);
	public static final Color garden = new Color(0.37f, 0.67f, 0.16f, 1);
	public static final Color sludge = new Color(0.61f, 0.86f, 0.42f, 1);
	public static final Color reactor = new Color(0.31f, 0.66f, 0.63f, 1);
	public static final Color energy = new Color(0.56f, 0.95f, 0.91f, 1);
	public static final Color kitchen = Color.ORANGE;
	public static final Color food = new Color(1f, 0.85f, 0.37f, 1);
	public static final Color weapons = new Color(0.17f, 0.45f, 0.7f, 1);
	public static final Color quarters = new Color(0.83f, 0.42f, 0.14f, 1);
	public static final Color corridor = new Color(0.51f, 0.40f, 0.36f, 1);
	
	//enemy wave timer bar
	public static final Color waveBarColor = new Color(0.91f, 0.21f, 0.21f, 1f);
	public static final Color waveBarBgColor = new Color(0.91f, 0.51f, 0.51f, 0.4f);
	
	public static Color randomShade(Color baseColor){
		float rand = MathUtils.random(0.3f, 0.75f);
		return new Color(baseColor.r * rand, baseColor.g * rand, baseColor.b * rand, baseColor.a);
	}
}
