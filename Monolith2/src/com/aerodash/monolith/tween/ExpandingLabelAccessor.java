package com.aerodash.monolith.tween;

import com.aerodash.monolith.ui.ExpandingLabel;

import aurelienribon.tweenengine.TweenAccessor;

public class ExpandingLabelAccessor implements TweenAccessor<ExpandingLabel>{

	public static final int WIDTH = 1;
	@Override
	public int getValues(ExpandingLabel target, int tweenType, float[] returnValues) {
		
		switch(tweenType){
		case WIDTH:
			returnValues[0] = target.getWidth();
			return WIDTH;
		}
		return -1;
	}

	@Override
	public void setValues(ExpandingLabel target, int tweenType, float[] newValues) {
		
		switch(tweenType){
		case WIDTH:
			target.setWidth(newValues[0]);
		}
	}


}
