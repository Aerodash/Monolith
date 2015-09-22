package com.aerodash.monolith.entities.buildings;

import java.util.ArrayList;

import com.aerodash.monolith.core.Cost;
import com.aerodash.monolith.core.shapes.TetrisShape.InitialState;
import com.aerodash.monolith.core.shapes.TetrisShape.Type;
import com.aerodash.monolith.core.shapes.TetrisShape.TypeState;
import com.aerodash.monolith.entities.Building;
import com.aerodash.monolith.entities.Resource;
import com.aerodash.monolith.main.Monolith;
import com.aerodash.monolith.utils.Assets;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Stock extends Building{
	
	ArrayList<Resource> resources;
	Resource.Type type;
	
	public Stock(float x, float y, Resource.Type type) {
		super(x, y, Color.GRAY, new TypeState(InitialState.ONE, Type.Tile), Cost.free);
		resources = new ArrayList<>();
		this.type = type;
	}
	
	@Override
	public void addResource(com.aerodash.monolith.entities.Resource.Type type) {
		if (this.type != type) throw new IllegalArgumentException("Type : '" + type.name() + "' fed into addResource is not compatible with stock type : '" + this.type.name() + "'");
		resources.add(new Resource(shape.tiles.get(0), type));
	}
	
	public void addResource(com.aerodash.monolith.entities.Resource.Type type, int count) {
		for (int i = 0; i < count; i++){
			addResource(type);
		}
	}
	
	public void render(SpriteBatch sb) {
		shape.render(sb);
		
		if (resources.size() > 0){
			resources.get(0).render(sb);
		}
		
		sb.begin();
		
		Assets.font.setColor(Color.BLACK);
		Assets.layout.setText(Assets.font, Integer.toString(resources.size()));
		Assets.font.draw(sb, Integer.toString(resources.size()), x + Monolith.tileSize / 2 - Assets.layout.width / 2, y + Monolith.tileSize / 2 + Assets.layout.height / 2);
		
		sb.end();
	}
	
}
