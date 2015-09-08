package com.aerodash.monolith.core.shapes;

import com.aerodash.monolith.core.GameObject;
import com.aerodash.monolith.core.Tile;
import com.aerodash.monolith.entities.Building;
import com.aerodash.monolith.main.Monolith;
import com.aerodash.monolith.ui.NextShapePanel;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public abstract class TetrisShape extends GameObject {

	public Array<Tile> tiles;
	public boolean collided = false;
	Vector2 oldPos, offset;
	private Building building;
	@SuppressWarnings("unused") private boolean outlined = false;
	public TypeState typeState;

	public static class TypeState{
		public InitialState state;
		public Type type;
		public TypeState(InitialState state, Type type){
			this.state = state;
			this.type = type;
		}
		
	}
	
	public enum InitialState{
		ONE, TWO
	}
	
	public enum RotationState{
		INITIAL, NINTY, ONEEIGHTY, TWOSEVENTY 
	}
	
	public enum Type {
		I, L, Square, S, T, Tile
	}

	protected TetrisShape(float x, float y, Color color, TypeState type) {
		super(x * Monolith.tileSize, y * Monolith.tileSize, 0, 0, color, false, false);
		tiles = new Array<>(4);
		oldPos = offset = new Vector2();
		this.typeState = type;
	}

	@Override
	public void update(float delta) {
		
		for (int i = 1; i < tiles.size; i++) {//from 1 because 0 is the masterTile
			tiles.get(i).setPos(tiles.get(i).getGridX() + offset.x, tiles.get(i).getGridY() + offset.y);
		}
		
		x = getMasterTile().getX();
		y = getMasterTile().getY();
	}

	@Override
	public void render(SpriteBatch sb) {

		for (Tile t : tiles) {
			t.render(sb);
		}

	}
	
	public void renderForUI(Batch b, float scale){
		for (Tile t : tiles){
			t.renderForUI(b, scale);
		}
	}
	
	public Tile getMasterTile() {
		return tiles.get(0);
	}

	public boolean collides(TetrisShape shape) {
		for (Tile t : tiles) {
			for (Tile t1 : shape.tiles) {
				if (t.collides(t1)) {
					return true;
				}
			}
		}
		return false;
	}

	public static TetrisShape getShapeInstance(TypeState typeState, float x, float y, Color color) {
		TetrisShape res = null;
		
		switch (typeState.type) {
		case I:
			res = new IShape(x, y, color);
			break;
		case L:
			res = new LShape(x, y, color, 1, typeState.state);
			break;
		case Square:
			res = new SquareShape(x, y, color);
			break;
		case S:
			res = new SShape(x, y, color, 1, typeState.state);
			break;
		case T:
			res = new TShape(x, y, color);
			break;
		case Tile:
			res = new TileShape(x, y, color);
			break;
		}
		
		return res;
	}

	public static TetrisShape getShapeInstanceForUI(Type type, float x, float y, Color color, float tileSize){
		TetrisShape res = null;
		switch (type) {
		case I:
			res = new IShape(x, y, color, tileSize);
			break;
		case L:
			res = new LShape(x, y, color, tileSize);
			break;
		case Square:
			res = new SquareShape(x, y, color, tileSize);
			break;
		case S:
			res = new SShape(x, y, color, tileSize);
			break;
		case T:
			res = new TShape(x, y, color, tileSize);
			break;
		default:
			break;
		}
		
		return res;
	}
	
	public static Type random() {
		return Type.values()[MathUtils.random(4)];
	}

	public float getGridX() {
		return x / Monolith.tileSize;
	}

	public float getGridY() {
		return y / Monolith.tileSize;
	}
	
	@Override
	public void setPos(Vector2 pos) {
		oldPos = new Vector2(getMasterTile().getGridX(), getMasterTile().getGridY());
		getMasterTile().setPos(pos);
		offset = new Vector2(getMasterTile().getGridX() - oldPos.x,
				getMasterTile().getGridY() - oldPos.y);
	}
	
	@Override
	public void setPos(float x, float y) {
		setPos(new Vector2(x, y));
	}
	
	public boolean contains(float x, float y){
		for (int i = 0; i < tiles.size; i++){
			if (tiles.get(i).contains(x, y)) return true;
		}
		return false;
	}
	
	public boolean contains(Vector2 pos){
		return contains(pos.x, pos.y);
	}
	
	public boolean isMyTileAt(float gridX, float gridY){
		for (int i = 0; i < tiles.size; i++){
			if (tiles.get(i).getGridX() == gridX && tiles.get(i).getGridX() == gridY){
				return true;
			}
		}
		return false;
	}

	public Building getBuilding() {
		return building;
	}

	public void setBuilding(Building building) {
		this.building = building;
	}

	public abstract void rotate();
	
	public static float getShapeWidthForUI(Type type){
		switch (type) {
		case I:
			return Monolith.tileSize * NextShapePanel.scale;
		case L:
			return Monolith.tileSize * 2 * NextShapePanel.scale;
		case Square:
			return Monolith.tileSize * 2 * NextShapePanel.scale;
		case S:
			return Monolith.tileSize * 2 * NextShapePanel.scale;
		case T:
			return Monolith.tileSize * 2 * NextShapePanel.scale;
		default:
			break;
		}
		return -1;
	}
	
	public static float getShapeHeightForUI(Type type){
		switch (type) {
		case I:
			return Monolith.tileSize * 4 * NextShapePanel.scale;
		case L:
			return Monolith.tileSize * 3 * NextShapePanel.scale;
		case Square:
			return Monolith.tileSize * 2 * NextShapePanel.scale;
		case S:
			return Monolith.tileSize * 3 * NextShapePanel.scale;
		case T:
			return Monolith.tileSize * 3 * NextShapePanel.scale;
		default:
			break;
		}
		return -1;
	}
	
	public void setOutlined(boolean b){
		outlined = b;
		for (int i = 0; i < tiles.size; i++){
			tiles.get(i).outlined = b;
		}
	}
}
