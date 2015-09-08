package com.aerodash.monolith.tween;

import com.badlogic.gdx.graphics.Color;

import aurelienribon.tweenengine.TweenAccessor;

public class ColorAccessor implements TweenAccessor<Color>{
	
	public static final int ALPHA = 1;

	@Override
	public int getValues(Color target, int tweenType, float[] returnValues) {
		switch(tweenType){
		case ALPHA:
			returnValues[0] = target.a;
			return ALPHA;
		}
		
		return -1;
	}

	@Override
	public void setValues(Color target, int tweenType, float[] newValues) {
		switch(tweenType){
		case ALPHA:
			target.a = newValues[0];
		}
		
	}

}
