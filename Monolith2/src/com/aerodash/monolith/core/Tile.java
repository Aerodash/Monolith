package com.aerodash.monolith.core;

import com.aerodash.monolith.core.shapes.TetrisShape;
import com.aerodash.monolith.main.Monolith;
import com.aerodash.monolith.utils.Assets;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class Tile extends GameObject{
	
	private float size;
	public TetrisShape shape;
	public boolean hasResource = false;
	public boolean collided = false;
	public boolean outlined = false;

	public Tile(float x, float y, Color color, TetrisShape shape){
		super(x * Monolith.tileSize, y * Monolith.tileSize, Monolith.tileSize, Monolith.tileSize, color, true, true);
		this.size = Monolith.tileSize;
		this.shape = shape;
	}
	
	@Override
	public void update(float delta) {
	}	

	@Override
	public void render(SpriteBatch sb) {
		
		sb.begin();
		
		sb.setColor(color);
		if (collided)
			sb.setColor(new Color(1, .2f, .2f, .5f));
		
		if (outlined) 
			sb.draw(Assets.tileOutlined, x, y, width, height);
		else if (shape.getBuilding().selected)
			sb.draw(Assets.outline, x, y, width, height);
		else if (shape.getBuilding().selectedAfterBuilt){
			sb.draw(Assets.tile, x, y, width, height);
			sb.draw(Assets.outline, x - 3, y - 3, width + 6, height + 6);
		}else
			sb.draw(Assets.tile, x, y, width, height);
		
		sb.end();
		
	}
	
	public void renderForUI(Batch b, float scale) {
		
		b.setColor(color);
		if (collided)
			b.setColor(new Color(1, .2f, .2f, .5f));
		
		if (outlined) 
			b.draw(Assets.tileOutlined, x / Monolith.tileSize, y / Monolith.tileSize, 0, 0, width, height, scale, scale, 0);
		else
			b.draw(Assets.tile, x / Monolith.tileSize, y / Monolith.tileSize, 0, 0, width, height, scale, scale, 0);
		
	}
	
	@Override
	public void setX(float x) {
		this.x = x * size;
	}
	
	public void setY(float y){
		this.y = y * size;
	}
	
	@Override
	public void setPos(float x, float y) {
		setX(x);
		setY(y);
	}
	
	@Override
	public void setPos(Vector2 pos) {
		setX(pos.x);
		setY(pos.y);
	}
	
	public float getGridX(){
		return x / size;
	}
	
	public float getGridY(){
		return y / size;
	}
	
	public boolean collides(Tile tile){
		return getBounds().overlaps(tile.getBounds());
	}
	
	public boolean contains(float x, float y){
		return getBounds().contains(x, y);
	}
	
	public boolean contains(Vector2 pos){
		return contains(pos.x, pos.y);
	}
	
	public Array<Vector2> getSurrounding4TilesPoses(){
		Array<Vector2> poses = new Array<Vector2>();
		float x = getGridX();
		float y = getGridY();
		
		poses.add(new Vector2(x, y + 1));
		poses.add(new Vector2(x + 1, y));
		poses.add(new Vector2(x, y - 1));
		poses.add(new Vector2(x - 1, y));
		
		return poses;
	}
	
	public Array<Vector2> getSurrounding8TilesPoses(){
		Array<Vector2> poses = new Array<Vector2>();
		float x = getGridX();
		float y = getGridY();
		
		poses.add(new Vector2(x, y + 1));
		poses.add(new Vector2(x + 1, y + 1));
		poses.add(new Vector2(x + 1, y));
		poses.add(new Vector2(x + 1, y - 1));
		poses.add(new Vector2(x, y - 1));
		poses.add(new Vector2(x - 1, y - 1));
		poses.add(new Vector2(x - 1, y));
		poses.add(new Vector2(x - 1, y + 1));
		
		return poses;
	}

}
