package com.aerodash.monolith.entities;

import java.util.ArrayList;

import com.aerodash.monolith.core.GameObject;
import com.aerodash.monolith.core.GameObjects;
import com.aerodash.monolith.core.Tile;
import com.aerodash.monolith.entities.Resource.Type;
import com.aerodash.monolith.main.Monolith;
import com.aerodash.monolith.screens.Play;
import com.aerodash.monolith.tween.MinionAccessor;
import com.aerodash.monolith.utils.Assets;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenManager;

public class Minion extends GameObject{
	
	private Tile currentTile;
	private Tile tileToGoTo;
	private TweenManager manager;
	private TweenCallback cb;
	private ArrayList<Tile> pattern;
	private float speed = .51f; // plus 0.01 sec of delay because tween take .5s
	private boolean isMoving = false;
	private Resource resourceToMove;
	private Job currentJob;
	
	public enum Job {
		IDLE,
		Engineering,
		Food,
		Construction,
		Research
	}
	
	public Minion(float x, float y) {
		super(x * Monolith.tileSize, y * Monolith.tileSize, Assets.minion.getRegionWidth(), Assets.minion.getRegionHeight(), Color.WHITE, true, true);
		setupTween();
		pattern = new ArrayList<>();
		setCurrentTile(x, y);
		currentJob = Job.IDLE;
	}
	
	@Override
	public void update(float delta) {
		manager.update(delta);
		setCurrentTile(getGridX(), getGridY());
		
		if (currentTile != null && currentTile.equals(tileToGoTo) && currentJob.equals(Job.Construction) && resourceToMove == null){
			System.out.println("Reached goal !");
			Resource r = currentTile.shape.getBuilding().res.pop();
			r.tile.hasResource = false;
			setResourceToMove(r.type);
		}
		
		if (resourceToMove != null)
			resourceToMove.update(delta);
	}

	@Override
	public void render(SpriteBatch sb) {
		sb.begin();
		sb.setColor(Color.WHITE);
		sb.draw(Assets.minion, x + Monolith.tileSize / 2 - Assets.minion.getRegionWidth() / 2, y + Monolith.tileSize / 2 - Assets.minion.getRegionHeight() / 2);
		sb.end();
		if (resourceToMove != null)
			resourceToMove.render(sb);
	}
	
	public void go(int nodeId){
		if (!isMoving)
			tweenToBuilding(nodeId);
	}
	
	private void setResourceToMove(Type type){
		resourceToMove = new Resource(currentTile, type);
		resourceToMove.minion = this;
	}
	
	public Job getCurrentJob() {
		return currentJob;
	}

	public void setCurrentJob(Job currentJob) {
		this.currentJob = currentJob;
	}

	private void setCurrentTile(float gridX, float gridY) {
		GameObject o = GameObjects.objectAt(gridX, gridY);
		if (o == null) return;
		if (o.getClass().equals(Tile.class))
			currentTile = (Tile) o;
		if (o.getClass().getSuperclass().equals(Building.class)){
			currentTile = ((Building)o).getTileAt(gridX, gridY);
		}
	}
	
	private void tweenToBuilding(int nodeId) {
		goToBuilding(nodeId);
		Timer t = new Timer();
		for (int i = 0; i < pattern.size(); i++){
			final Tile tl = pattern.get(i);
			
			Task task = new Task(){

				@Override
				public void run() {
					System.out.println("Going to " + tl);
					tweenToTile(tl);
				}
				
			};
			
			if (i == 0){
				task = new Task(){

					@Override
					public void run() {
						System.out.println("Going to " + tl);
						isMoving = true;
						tweenToTile(tl);
					}
					
				};
			}
			
			if (i == pattern.size() - 1){
				task = new Task(){

					@Override
					public void run() {
						System.out.println("Going to " + tl);
						tweenToTile(tl);
						isMoving = false;
					}
					
				};
			}
			
			t.scheduleTask(task, speed * i);
		}
	}

	private void tweenToTile(Tile t) {
		float x = t.getX() - currentTile.getX();
		float y = t.getY() - currentTile.getY();
		
		tweenMovement(MinionAccessor.X, x);
		tweenMovement(MinionAccessor.Y, y);
		
	}
	
	public void goToBuilding(int nodeId){
		pattern.clear();
		pattern.addAll(currentTile.getPathTo(nodeId));
		tileToGoTo = pattern.get(pattern.size() - 1);
		System.out.println("Tile to go to : " + tileToGoTo);
		System.out.println("Current tile ID : " + currentTile.nodeId);
		System.out.println(pattern.toString());
	}

	private void setupTween() {
		Tween.registerAccessor(Minion.class, new MinionAccessor());
		manager = new TweenManager();
		
		cb = new TweenCallback() {
			@Override
			public void onEvent(int type, BaseTween<?> source) {
				
			}
		};
	}
	
	public void tweenMovement(int type, float value){
		switch(type){
		case MinionAccessor.X:
			Tween.to(this, type, .5f).target(getX() + value).start(manager);
			break;
		case MinionAccessor.Y:
			Tween.to(this, type, .5f).target(getY() + value).start(manager);
			break;
		}
	}
}
