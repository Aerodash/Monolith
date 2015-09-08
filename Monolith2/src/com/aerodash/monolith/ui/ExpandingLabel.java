package com.aerodash.monolith.ui;

import com.aerodash.monolith.tween.ColorAccessor;
import com.aerodash.monolith.tween.ExpandingLabelAccessor;
import com.aerodash.monolith.utils.Assets;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenManager;

public class ExpandingLabel extends Actor{
	
	public boolean visible = false;
	public boolean showText = false;
	public String text = "";
	public Color textColor = Color.WHITE;
	
	public static final int maxWidth = 440;
	private TweenManager manager;
	private TweenCallback cb, colorCb, backCb;
	public float duration;
	
	public ExpandingLabel(float x, float y){
		setX(x);
		setY(y);
		setHeight(22);
		setWidth(0);
		setupTween();
	}
	
	@Override
	public void act(float delta) {
//		if (getWidth() < maxWidth)
//		System.out.println(getWidth());
		if (visible){
			manager.update(delta);
			
			Tween.to(this, ExpandingLabelAccessor.WIDTH, .3f).target(maxWidth)
			.setCallback(cb).setCallbackTriggers(TweenCallback.COMPLETE)
					.start(manager);
		}else{
			setWidth(0);//tween back to 0
		}
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha) {
		if (visible){
			batch.setColor(0, 0, 0, 0.4f);
			batch.draw(Assets.tile, getX(), getY(), getWidth(), getHeight());
			
			if (showText){
					Tween.to(textColor, ColorAccessor.ALPHA, .5f).target(1)
						.setCallback(colorCb).setCallbackTriggers(TweenCallback.COMPLETE)
						.start(manager);
				Assets.font.setColor(textColor);
				Assets.layout.setText(Assets.font, text);
				Assets.font.draw(batch, text, getX() + getWidth() / 2 - Assets.layout.width / 2, getY() + getHeight() / 2 + Assets.layout.height / 2);
			}
		}
		
		System.out.println(textColor.a);
	}
	
	@Override
	public void drawDebug(ShapeRenderer sr) {
	}
	
	public void show(String text, float duration){
		show(text, Color.WHITE.cpy(), duration);
	}
	
	public void info(String text, float duration){
		show(text, Color.YELLOW.cpy(), duration);
	}
	
	public void warn(String text, float duration){
		show(text, Color.RED.cpy(), duration);
	}
	
	private void show(String text, Color color, float duration){
		if (!visible){
			visible = true;
			this.text = text;
			textColor = color;
			textColor.a = 0;
			this.duration = duration;
		}
	}
	
	public void hide(){
		visible = false;
	}
	
	private void setupTween() {
		Tween.registerAccessor(ExpandingLabel.class, new ExpandingLabelAccessor());
		Tween.registerAccessor(Color.class, new ColorAccessor());
		manager = new TweenManager();
		
		cb = new TweenCallback() {
			@Override
			public void onEvent(int type, BaseTween<?> source) {
				showText = true;
			}
		};
		
		colorCb = new TweenCallback() {
			@Override
			public void onEvent(int type, BaseTween<?> source) {
				//launch timer then set showText and visible to false
				Timer timer = new Timer();
				Task task = new Task(){
					@Override
					public void run() {
						//tween back
						Tween.to(ExpandingLabel.this, ExpandingLabelAccessor.WIDTH, .1f).target(0)
						.setCallback(backCb).setCallbackTriggers(TweenCallback.COMPLETE)
								.start(manager);
						Tween.to(textColor, ColorAccessor.ALPHA, .1f).target(0).start(manager);
					}
				};
				timer.scheduleTask(task, duration);
			}
		};
		
		backCb = new TweenCallback() {
			@Override
			public void onEvent(int type, BaseTween<?> source) {
				visible = false;
				showText = false;
			}
		};
		
	}
	
}
