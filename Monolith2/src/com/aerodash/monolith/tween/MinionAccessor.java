package com.aerodash.monolith.tween;

import com.aerodash.monolith.entities.Minion;

import aurelienribon.tweenengine.TweenAccessor;

public class MinionAccessor implements TweenAccessor<Minion>{

	public static final int X = 1;
	public static final int Y = 2;

	@Override
	public int getValues(Minion target, int tweenType, float[] returnValues) {
		switch(tweenType){
		case X:
			returnValues[0] = target.getX();
			return X;
		case Y:
			returnValues[0] = target.getY();
			return Y;
		}
		
		return -1;
	}

	@Override
	public void setValues(Minion target, int tweenType, float[] newValues) {
		
		switch(tweenType){
		case X:
			target.setX(newValues[0]);
			break;
		case Y:
			target.setY(newValues[0]);
			break;
		}
	}

}
