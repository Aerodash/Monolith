package com.aerodash.monolith.entities;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.aerodash.monolith.core.Cost;
import com.aerodash.monolith.core.GameObject;
import com.aerodash.monolith.core.GameObjects;
import com.aerodash.monolith.core.Tile;
import com.aerodash.monolith.core.shapes.TetrisShape;
import com.aerodash.monolith.core.shapes.TetrisShape.TypeState;
import com.aerodash.monolith.djikstra.Vertex;
import com.aerodash.monolith.entities.Resource.Type;
import com.aerodash.monolith.entities.buildings.Corridor;
import com.aerodash.monolith.exception.CostException;
import com.aerodash.monolith.main.Monolith;
import com.aerodash.monolith.screens.Play;
import com.aerodash.monolith.ui.ResourceBuildingBar;
import com.aerodash.monolith.utils.Assets;
import com.aerodash.monolith.utils.Colors;
import com.aerodash.monolith.utils.Utils;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public abstract class Building extends GameObject {

	public static Building selectedBuilding = null;
	public static Building selectedAfterBuiltBuilding = null;
	public static Building selectedBeforeBuiltBuilding = null;
	protected Array<Resource> res;// resource contained in the building
	public boolean isFull = false;//is full of resources

	protected TetrisShape shape;
	public boolean selectable = true; 
	public boolean built = false; //true if built
	public boolean waitingToBeBuilt = false; //waiting to be built
	public boolean selected = false; //following the cursor
	private boolean collided = false; //collided with another GameObject 
	public boolean selectedAfterBuilt = false; //selected after built
	public boolean selectedBeforeBuilt = false; //selected while waiting to be built
	@Deprecated private float oldX, oldY;
	public boolean showSurroundingTiles = false;//DEBUG FEATURE
	public boolean nextToCorridor = false;// building can only be placed next to
											// a corridor
	public boolean waitingTobeCollected = false;
	
	public Tile nodeTile = null;//the tile representing the building
	private Cost cost;
	
	//resource bars
	ResourceBuildingBar particleBar;
	ResourceBuildingBar energyBar;
	ResourceBuildingBar foodBar;

	public Building(float x, float y, Color color, TypeState typeState, Cost cost) {
		super(x * Monolith.tileSize, y * Monolith.tileSize, 0, 0, color, true, true);

		shape = TetrisShape.getShapeInstance(typeState, x, y, color);
		shape.setBuilding(this);

		this.cost = cost;
		res = new Array<Resource>(4);
		
	}

	private void setResourceBars(){
		float barWidth = Monolith.tileSize - 4, barHeight = Monolith.tileSize / 3 - 2;
		energyBar = new ResourceBuildingBar(nodeTile.getX() + 2, nodeTile.getY() + (2 * Monolith.tileSize) / 3 + 1, Colors.energy, cost.energy);
		particleBar = new ResourceBuildingBar(nodeTile.getX() + 2, energyBar.getY() - 2 - barHeight, Colors.particle, cost.particles);
		foodBar = new ResourceBuildingBar(nodeTile.getX() + 2, particleBar.getY() - 2 - barHeight, Colors.food, cost.food);
	}
	
	@Override
	public void update(float delta) {

		if (isClicked()) {
			if (selected && !collided) {

				ArrayList<Vector2> vecs = getSurroundingTiles();
				for (Vector2 v : vecs) {
					if (GameObjects.isObjectOfType(v.x, v.y, Corridor.class)) {
						nextToCorridor = true;
						break;
					} else {
						nextToCorridor = false;
					}
				}

				if (nextToCorridor) {
					// reduce the res by the cost of the building
					if (!Play.cheatMode) {
						try {
							Play.resRemaining.minus(cost);
						} catch (CostException e) {// if cost can't be
													// diminished
							Play.warn(e.getMessage(), 5);
							return;
						}
					}
					setSelected(false);// put building in position
					waitingToBeBuilt = true;
//					built = true;
					Play.switchToNextShape = true;
					if (!getClass().equals(Corridor.class)) // corridor are paths not nodes
						chooseNodeTile();//when built choose node TODO
					setResourceBars();//uses node tile must be after choosing node tile
				}
				// only one building can be selected at a time
				// if it is built it cannot be moved again
			} else if (selectedBuilding == null && !built && !waitingToBeBuilt) {

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
				// resetPos();
				// shape.update(delta);// update shape's position with old pos
				// setSelected(false);
			}

			collided = GameObjects.collides(this);
			for (Tile t : shape.tiles) {
				t.collided = collided;
			}

		}

		if (built && selectedBuilding == null && selectable) {
			if (isClicked())
				setSelectedAfterBuilt(true);
			if (clickedOutside())
				setSelectedAfterBuilt(false);
		}
		
		if (waitingToBeBuilt && selectedBuilding == null && selectable) {
			if (isClicked() && !built)
				setSelectedBeforeBuilt(true);
			if (clickedOutside())
				setSelectedBeforeBuilt(false);
		}
		
		if (res.size == 4) isFull = true;
		else isFull = false;
		
		if (waitingToBeBuilt){
			energyBar.update(delta);
			particleBar.update(delta);
			foodBar.update(delta);
		}
	}

	@Override
	public void render(SpriteBatch sb) {
		shape.render(sb);

		// render resources contained in building
		for (Resource r : res) {
			r.render(sb);
		}
		
		if (waitingToBeBuilt){
			energyBar.render(sb);
			particleBar.render(sb);
			foodBar.render(sb);
		}

		if (showSurroundingTiles) {
			sb.begin();
			ArrayList<Vector2> tab = getSurroundingTiles();
			Color transparentRed = Color.RED.cpy();
			transparentRed.a = .5f;
			sb.setColor(transparentRed);
			for (Vector2 v : tab) {
				sb.draw(Assets.tile, v.x * Monolith.tileSize, v.y * Monolith.tileSize, Monolith.tileSize,
						Monolith.tileSize);
			}
			sb.end();
		}
	}
	
	private void chooseNodeTile() {
		Array<Tile> tiles = new Array<Tile>();
		for (int i = 0; i < shape.tiles.size; i++) {
			Array<Vector2> poses = shape.tiles.get(i).getSurrounding4TilesPoses();
			for (Vector2 pos : poses) {
				if (GameObjects.isObjectOfType(pos.x, pos.y, Corridor.class)) {
					tiles.add(shape.tiles.get(i));
					break;
				}
			}
		}
		Tile t = tiles.get(MathUtils.random(tiles.size - 1));
		nodeTile = t;
		t.isNode = true;
		t.nodeId = Play.currentNodeId;
		Play.graph.addNode(new Vertex(Integer.toString(Play.currentNodeId), "NODE_" + Play.currentNodeId, t));
		Play.currentNodeId++;
	}

	public void dispose() {
		GameObjects.removeObject(id);
		if (selected) {
			selectedBuilding = null;
			selected = false;
		} else {
			selectedAfterBuiltBuilding = null;
			selectedAfterBuilt = false;
		}
		//remove the node from the graph
		List<Vertex> vs = Play.graph.getNodes();
		Iterator<Vertex> it = vs.iterator();
		while(it.hasNext()){
			Vertex v = it.next();
			if (v.getTile().equals(nodeTile)){
				it.remove();
			}
		}
	}

	@Deprecated
	private void setOldPos(float x, float y) {
		oldX = x;
		oldY = y;
	}

	@Deprecated
	public void resetPos() {
		x = oldX;
		y = oldY;
		shape.setPos(getGridX(), getGridY());
	}

	public boolean isClicked() {
		return Gdx.input.justTouched() && Gdx.input.isButtonPressed(Buttons.LEFT) && shape.contains(Play.getMouse());
	}

	public boolean clickedOutside() {
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

	public void setSelectedAfterBuilt(boolean b) {
		if (b) {
			selectedAfterBuilt = true;
		} else {
			selectedAfterBuilt = false;
		}
	}
	
	public void setSelectedBeforeBuilt(boolean b) {
		if (b) {
			selectedBeforeBuilt = true;
		} else {
			selectedBeforeBuilt = false;
		}
	}

	@SuppressWarnings("unchecked")
	public ArrayList<Vector2> getSurroundingTiles() {
		ArrayList<Vector2> res = new ArrayList<Vector2>();

		for (int i = 0; i < shape.tiles.size; i++) {
			Tile t = shape.tiles.get(i);
			Array<Vector2> poses = t.getSurrounding4TilesPoses();

			for (int j = 0; j < poses.size; j++) {
				res.add(poses.get(j));
			}
		}

		// remove the tiles that are part of the shape
		for (int i = 0; i < shape.tiles.size; i++) {
			for (int j = 0; j < res.size(); j++) {
				if (shape.tiles.get(i).getGridX() == res.get(j).x && shape.tiles.get(i).getGridY() == res.get(j).y) {
					res.remove(j);
					j--;
				}
			}

		}

		// remove duplicates
		return Utils.removeDuplicates(res);
	}

	public void rotate() {
		shape.rotate();
	}

	public TetrisShape getShape() {
		return shape;
	}

	public void addResource(Type type) {
		if (isFull) {
			return;
		}else{
			res.add(new Resource(emptyTile(), type));
			if (res.size > 0){
				waitingTobeCollected = true;
			}
		}
	}

	public Tile emptyTile() {
		for (int i = 0; i < shape.tiles.size; i++) {
			if (!shape.tiles.get(i).hasResource) {
				shape.tiles.get(i).hasResource = true;
				return shape.tiles.get(i);
			}
		}
		return null;
	}

	public boolean collides(Building b) {
		for (int i = 0; i < shape.tiles.size; i++) {
			for (int j = 0; j < b.shape.tiles.size; j++) {
				if (shape.tiles.get(i).collides(b.shape.tiles.get(j)))
					return true;
			}
		}
		return false;
	}

	public Building doUpdateAndRender(boolean doUpdate, boolean doRender) {
		this.doUpdate = doUpdate;
		this.doRender = doRender;
		return this;
	}

	public Cost getCost() {
		return cost;
	}

	public Tile getTileAt(float gridX, float gridY){
		for (int i = 0; i < shape.tiles.size; i++){
			if (shape.tiles.get(i).getGridX() == gridX && shape.tiles.get(i).getGridY() == gridY){
				return shape.tiles.get(i);
			}
		}
		return null;
	}
}
