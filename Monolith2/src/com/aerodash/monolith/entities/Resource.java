package com.aerodash.monolith.entities;

import com.aerodash.monolith.core.GameObject;
import com.aerodash.monolith.core.Tile;
import com.aerodash.monolith.main.Monolith;
import com.aerodash.monolith.utils.Colors;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Resource extends GameObject{
	
	public TextureRegion tex;
	public Type type;
	public Tile tile;
	public Minion minion;
	
	public Resource(Tile tile, Type type){
		super(tile.getX() + Monolith.tileSize / 2 - 10 / 2, tile.getY() + Monolith.tileSize / 2 - 10 / 2, 10, 10, null, false, false);
		this.type = type;
		this.tile = tile;
		this.width = this.height = 10;
		setType();
	}
	
	public Resource(Minion minion, Type type){
		super(minion.getX() + Monolith.tileSize / 2 - 10 / 2, minion.getY() + Monolith.tileSize / 2 - 10 / 2, 3, 3, null, false, false);
		this.type = type;
		this.minion = minion;
		this.width = this.height = 10;
		setType();
	}
	
	private void setType(){
		switch(type){
		case Particle:
			this.color = Colors.particle;
			tex = new TextureRegion(new Texture(Gdx.files.internal("res/img/tile.png")));
			break;
		case Energy:
			this.color = Colors.energy;//draw rotate by 45�
			tex = new TextureRegion(new Texture(Gdx.files.internal("res/img/tile.png")));
			break;
		case Food:
			this.color = Colors.food;
			tex = new TextureRegion(new Texture(Gdx.files.internal("res/img/triangle.png")));
			break;
		case Sludge:
			this.color = Colors.sludge;
			tex = new TextureRegion(new Texture(Gdx.files.internal("res/img/headlessTriangle.png")));
			break;
		}
	}
	
	public enum Type{
		Particle,
		Energy,
		Food,
		Sludge
	}
	
	@Override
	public void update(float delta) {
		if (minion != null){
			x = minion.getX();
			y = minion.getY();
		}
	}
	
	@Override
	public void render(SpriteBatch sb) {
		sb.begin();
		
		sb.setColor(color);
		
		if (minion != null){
			if (type != Type.Energy)
				sb.draw(tex, x + Monolith.tileSize / 2 - 5, y + Monolith.tileSize / 2 - 5, 0, 0, width, height, .5f, .5f, 0, true);
			else
				sb.draw(tex, x, y, width / 2, height / 2, width, height, .5f, .5f, 45, true);
		}else{
			if (type != Type.Energy)
				sb.draw(tex, x, y, 0, 0, width, height, 1, 1, 0, true);
			else
				sb.draw(tex, x, y, width / 2, height / 2, width, height, 1, 1, 45, true);
		}
		
		sb.end();
	}
	
}
