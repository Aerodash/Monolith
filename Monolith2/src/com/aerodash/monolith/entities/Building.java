package com.aerodash.monolith.entities;

import java.util.ArrayList;

import com.aerodash.monolith.core.Cost;
import com.aerodash.monolith.core.GameObject;
import com.aerodash.monolith.core.GameObjects;
import com.aerodash.monolith.core.Tile;
import com.aerodash.monolith.core.shapes.TetrisShape;
import com.aerodash.monolith.core.shapes.TetrisShape.TypeState;
import com.aerodash.monolith.entities.Resource.Type;
import com.aerodash.monolith.entities.buildings.Corridor;
import com.aerodash.monolith.exception.CostException;
import com.aerodash.monolith.main.Monolith;
import com.aerodash.monolith.screens.Play;
import com.aerodash.monolith.utils.Assets;
import com.aerodash.monolith.utils.Utils;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public abstract class Building extends GameObject {

	public static Building selectedBuilding = null;
	public static Building selectedAfterBuiltBuilding = null;
	private Array<Resource> res;//resource contained in the building
	public boolean isFull = false;

	private TetrisShape shape;
	public boolean selectable = true;
	public boolean built = false;
	public boolean selected = false;
	private boolean collided = false;
	public boolean selectedAfterBuilt = false;
	private float oldX, oldY;
	public boolean showSurroundingTiles = false;
	public boolean nextToCorridor = false;//building can only be placed next to a corridor

	private Cost cost;

	public Building(float x, float y, Color color, TypeState typeState, Cost cost) {
		super(x * Monolith.tileSize, y * Monolith.tileSize, 0, 0, color, true, true);

		shape = TetrisShape.getShapeInstance(typeState, x, y, color);
		shape.setBuilding(this);
		
		this.cost = cost;
		res = new Array<Resource>(4);
	}

	@Override
	public void update(float delta) {
		
		if (isClicked()) {
			if (selected && !collided) {
				
				ArrayList<Vector2> vecs = getSurroundingTiles();
				for (Vector2 v : vecs){
					if (GameObjects.isObjectOfType(v.x, v.y, Corridor.class)){
						nextToCorridor = true;
						break;
					}else{
						nextToCorridor = false;
					}
				}
				
				if (nextToCorridor){
					//reduce the res by the cost of the building
					try {
						Play.resRemaining.minus(cost);
					} catch (CostException e) {//if cost can't be diminished
						Play.expLabel.warn(e.getMessage(), 5);
						return;
					}
					setSelected(false);// put building in position
					built = true;
					Play.switchToNextShape = true;
				}
				// only one building can be  selected at a time
				//if it is built it cannot be moved again
			} else if (selectedBuilding == null && !built) {
													
				setOldPos(x, y);
				setSelected(true);
			}
		}

		if (selected) {// if the building is selected update the shape's
						// position
			shape.setPos(Play.getGridMouse());
			shape.update(delta);

			x = shape.getX();
			y = shape.getY();

			// if you are selected
			if (Gdx.input.justTouched() && Gdx.input.isButtonPressed(Buttons.RIGHT)) {// if
																						// right
																						// click
				dispose();
//				resetPos();
//				shape.update(delta);// update shape's position with old pos
//				setSelected(false);
			}
			
			collided = GameObjects.collides(this);
			for (Tile t : shape.tiles){
				t.collided = collided;
			}	
			
		}
		
		if (built && selectedBuilding == null && selectable){
			if (isClicked()) setSelectedAfterBuilt(true);
			if (clickedOutside()) setSelectedAfterBuilt(false);
		}
		
	}
	
	private void dispose() {
		GameObjects.removeObject(id);
		selectedBuilding = null;
		selected = false;
	}

	@Override
	public void render(SpriteBatch sb) {
		shape.render(sb);
		
		//render resources contained in building
		for (Resource r : res){
			r.render(sb);
		}
		
		if (showSurroundingTiles){
			sb.begin();
			ArrayList<Vector2> tab =  getSurroundingTiles();
			sb.setColor(Color.RED);
			for (Vector2 v : tab){
				sb.draw(Assets.tile, v.x * Monolith.tileSize, v.y * Monolith.tileSize, Monolith.tileSize, Monolith.tileSize);
			}
			sb.end();
		}
	}

	private void setOldPos(float x, float y) {
		oldX = x;
		oldY = y;
	}

	public void resetPos() {
		x = oldX;
		y = oldY;
		shape.setPos(getGridX(), getGridY());
	}


	public boolean isClicked() {
		return Gdx.input.justTouched() && Gdx.input.isButtonPressed(Buttons.LEFT) && shape.contains(Play.getMouse());
	}
	
	public boolean clickedOutside(){
		return Gdx.input.justTouched() && !shape.contains(Play.getMouse());
	}

	public void setSelected(boolean b) {
		if (b) {
			selectedBuilding = this;
			selected = true;
		} else {
			selectedBuilding = null;
			selected = false;
		}
	}
	
	public void setSelectedAfterBuilt(boolean b){
		if (b){
			selectedAfterBuilt = true;
		}else{
			selectedAfterBuilt = false;
		}
	}

	public ArrayList<Vector2> getSurroundingTiles() {
		ArrayList<Vector2> res = new ArrayList<Vector2>();
		
		for (int i = 0; i < shape.tiles.size; i++){
			Tile t = shape.tiles.get(i);
			Array<Vector2> poses = t.getSurrounding4TilesPoses();
			
			for (int j = 0; j < poses.size; j++){
				res.add(poses.get(j));
			}
		}
		
		//remove the tiles that are part of the shape
		for (int i = 0; i < shape.tiles.size; i++){
			for (int j = 0; j < res.size(); j++){
				if (shape.tiles.get(i).getGridX() == res.get(j).x && shape.tiles.get(i).getGridY() == res.get(j).y){
					res.remove(j);
					j--;
				}
			}
			
		}
		
		//remove duplicates
		return Utils.removeDuplicates(res);
	}
	
	public void rotate(){
		shape.rotate();
	}

	public TetrisShape getShape() {
		return shape;
	}
	
	public void addResource(Type type){
		if (res.size == 4){ 
			isFull = true; 
			return;
		}
		res.add(new Resource(emptyTile(), type));
	}
	
	public Tile emptyTile(){
		for (int i = 0; i < shape.tiles.size; i++){
			if (!shape.tiles.get(i).hasResource){
				shape.tiles.get(i).hasResource = true;
				return shape.tiles.get(i);
			}
		}
		return null;
	}
	
	public boolean collides(Building b){
		for (int i = 0; i < shape.tiles.size; i++){
			for (int j = 0; j < b.shape.tiles.size; j++){
				if (shape.tiles.get(i).collides(b.shape.tiles.get(j))) return true;
			}
		}
		return false;
	}
	
	public Building doUpdateAndRender(boolean doUpdate, boolean doRender){
		this.doUpdate = doUpdate;
		this.doRender = doRender;
		return this;
	}
}
